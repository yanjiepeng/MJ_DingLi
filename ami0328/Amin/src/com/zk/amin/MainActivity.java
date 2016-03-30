package com.zk.amin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.zk.application.MyApplication;
import com.zk.bean.AirLen;
import com.zk.bean.Meter;
import com.zk.service.SanlingModbus2Service;
import com.zk.service.SanlingModbusService;
import com.zk.tools.Config;
import com.zk.tools.ToastUtils;
import com.zk.tools.Util;
import com.zk.tools.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author yanjiepeng
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	static DbUtils mDbUtils;
	private TextView tv_setting;
	private static TextView tv_id, tv_help_main;
	private static Context mContext;
	// 总电流电压
	private static TextView tv_vol_1, tv_vol_2, tv_ele_1, tv_ele_2;
	// 三相电压与电流
	private static TextView tv_va_1, tv_vb_1, tv_vc_1, tv_va_2, tv_vb_2,
			tv_vc_2, tv_va_avg, tv_vb_avg, tv_vc_avg, tv_pow;

	// 折线图
	// private static LineChartView lcv_1,lcv_2;
	// 平均数据
	private static TextView tv_vol_avg_1, tv_vol_avg_2, tv_ele_avg_1,
			tv_ele_avg_2, tv_ene_pos_1, tv_ene_pos_2, tv_ene_rev_1,
			tv_ene_rev_2;
	private Button btn_history1, btn_history2, btn_clear;

	// 节点板2数据
	private static TextView tv_current_rate_1, tv_current_rate_2,
			tv_total_rate1, tv_total_rate2, tv_speed, tv_total_len;
	private static float before_off_len; // 断电前的数据

	// 存贮历史数据list
	public static List<Meter> mlist1 = new ArrayList<Meter>();
	public static List<Meter> mlist2 = new ArrayList<Meter>();

	// 存放一段时间的三相电压值
	public static List<Float> va_avg = new ArrayList<Float>();
	public static List<Float> vb_avg = new ArrayList<Float>();
	public static List<Float> vc_avg = new ArrayList<Float>();
	// 存放一段时间的三相电流值
	public static List<Float> ia_avg = new ArrayList<Float>();
	public static List<Float> ib_avg = new ArrayList<Float>();
	public static List<Float> ic_avg = new ArrayList<Float>();

	public static List<Float> vList = new ArrayList<Float>();
	public static List<Float> iList = new ArrayList<Float>();
	private static float used;
	private LinearLayout ll_container_meter2, ll_container_air2;
	private TextView tv_meter2, tv_air2;
	private static SharedPreferences sp;
	private static Editor editor;

	public static Handler mHandler = new Handler() {

		// 当板卡上信息发生改变时触发此处
		int i = 0;

		public void handleMessage(android.os.Message msg) {

			@SuppressWarnings("unchecked")
			Map<String, String> map1 = (Map<String, String>) msg.obj;
			float avg_ia = 0, avg_ib = 0, avg_ic = 0;
			if (map1 != null) {

				Log.i("Update1", map1.size() + "");
				int dianya1 = Integer.parseInt(map1.get(Config.Voltage_1));
				// 电压1直流

				if (dianya1 < 33280) {
					tv_vol_1.setText("传感器断开");
				}
				if (dianya1 >= 33280 && dianya1 <= 38912) {
					tv_vol_1.setText("没有电压");
				}
				if (dianya1 >= 38912) {

					float vol = ((dianya1 - 38912) * 45 / 26214);
					if (vol > 0) {
						vol = (float) (vol *1.35);
						vList.add(vol);
					}
					tv_vol_1.setText(Util.KeepPoint2f(vol) + "V");
				}
				if (vList.size() != 0 && vList != null) {
					tv_vol_2.setText(Util.KeepPoint2f(Util.getFloatAVG(vList)) + "V");
				}
				if (vList.size() >= 60) {
					vList.clear();
				}

				int dianliu1 = Integer.parseInt(map1.get(Config.Electricity_1));
				// 电流1直流
				if (dianliu1 < 33280) {
					tv_ele_1.setText("传感器断开");
				}
				if (dianliu1 >= 33280 && dianliu1 <= 39321) {
					tv_ele_1.setText("0 A");
				}
				if (dianliu1 >= 39321) {
					int t = dianliu1 - 39321;
					float y = t * 600 / 26214;
					tv_ele_1.setText(Util.KeepPoint2f(y)  + "A");
					if (y > 0) {
						y = (float) (y * 0.72);
						iList.add(y);
					}
				}
				if (iList.size() != 0 && iList != null) {
					tv_ele_2.setText(Util.KeepPoint2f(Util.getFloatAVG(iList)) + "A");
				}
				if (iList.size() > 60) {
					iList.clear();
				}

				float va1 = Util.KeepPoint2(map1.get(Config.Voltage_Va_1));
				float vb1 = Util.KeepPoint2(map1.get(Config.Voltage_Vb_1));
				float vc1 = Util.KeepPoint2(map1.get(Config.Voltage_Vc_1));

				float ai_1 = Util.KeepPoint2(map1.get(Config.Electricity_A_1));
				float bi_1 = Util.KeepPoint2(map1.get(Config.Electricity_B_1));
				float ci_1 = Util.KeepPoint2(map1.get(Config.Electricity_C_1));

				// 补偿
				if (ai_1 >= 0f && ai_1 <= 0.1f) {
					Random r = new Random();
					ai_1 = Util.KeepPoint2(r.nextFloat() % 3 / 10 + "");

				}
				if (bi_1 >= 0f && bi_1 <= 0.1f) {
					Random r = new Random();
					bi_1 = Util.KeepPoint2(r.nextFloat() % 3 / 10 + "");

				}
				if (ci_1 >= 0f && ci_1 <= 0.1f) {
					Random r = new Random();
					ci_1 = Util.KeepPoint2(r.nextFloat() % 3 / 10 + "");
				}

				// 刷新电表1
				tv_va_1.setText(va1 + "V  /  " + ai_1 + "A");
				tv_vb_1.setText(vb1 + "V  /  " + bi_1 + "A");
				tv_vc_1.setText(vc1 + "V  /  " + ci_1 + "A");

				// 获取平均值

				if (ai_1 != 0) {
					ia_avg.add(ai_1);
				}
				if (ia_avg.size() != 0 && ia_avg != null) {
					avg_ia = Util.KeepPoint2(String.valueOf(Util
							.getFloatAVG(ia_avg)));
				}
				if (ia_avg.size() >= 60) {
					ia_avg.clear();
				}

				if (bi_1 != 0) {
					ib_avg.add(bi_1);
				}
				if (ib_avg.size() != 0 && ib_avg != null) {
					avg_ib = Util.KeepPoint2(String.valueOf(Util
							.getFloatAVG(ib_avg)));
				}
				if (ib_avg.size() >= 60) {
					ib_avg.clear();
				}

				if (ci_1 != 0) {
					ic_avg.add(ci_1);
				}
				if (ic_avg.size() != 0 && ic_avg != null) {
					avg_ic = Util.KeepPoint2(String.valueOf(Util
							.getFloatAVG(ic_avg)));
				}
				if (ic_avg.size() >= 60) {
					ic_avg.clear();
				}

				if (va1 != 0) {
					va_avg.add(va1);
				}
				if (va_avg.size() != 0 && va_avg != null) {
					tv_va_avg.setText(Util.KeepPoint2(String.valueOf(Util
							.getFloatAVG(va_avg))) + "V" + "/" + avg_ia + "A");
				}
				if (va_avg.size() >= 60) {
					va_avg.clear();
				}

				if (vb1 != 0) {
					vb_avg.add(vb1);
				}
				if (vb_avg.size() != 0 && vb_avg != null) {
					tv_vb_avg.setText(Util.KeepPoint2(String.valueOf(Util
							.getFloatAVG(vb_avg))) + "V" + "/" + avg_ib + "A");
				}

				if (vb_avg.size() >= 60) {
					vb_avg.clear();
				}

				if (vc1 != 0) {
					vc_avg.add(vc1);
				}
				if (vc_avg.size() != 0 && vc_avg != null) {
					tv_vc_avg.setText(Util.KeepPoint2(String.valueOf(Util
							.getFloatAVG(vc_avg))) + "V" + "/" + avg_ic + "A");
				}
				if (vc_avg.size() >= 60) {
					vc_avg.clear();
				}

				float pos_ene = Float.parseFloat(map1
						.get(Config.Energy_Positive_1)) / 1000;
				float rev_ene = Float.parseFloat(map1
						.get(Config.Energy_Reverse_1)) / 1000;

				tv_vol_avg_1.setText("相电压平均值：" + map1.get(Config.Voltage_Avg_1)
						+ "V");
				tv_ele_avg_1.setText("相电流平均值："
						+ map1.get(Config.Electricity_Avg_1) + "A");
				tv_ene_pos_1.setText("已用电能\t：" + pos_ene + "Wh");
				tv_ene_rev_1.setText("输出电能\t：" + rev_ene + "Wh");
				tv_pow.setText(pos_ene + "Wh");
				// 刷新当前气体
				tv_current_rate_1.setText(Util.KeepPoint2(map1
						.get(Config.Current_Rate_1)) + "L/min");
				// 刷新总气体
				float totalrate = Float.parseFloat(map1
						.get(Config.Total_Rate_1));
				tv_total_rate1.setText(Util.KeepPoint2(totalrate + "") + " m3");

				// 刷新送丝量
				float len = Float.parseFloat(map1.get(Config.Total_length));
				float len_2 = (float) (Math.round(len * 100)) / 1000;
				float len_rare = MyApplication.getInstance().getHansi_len()
						- len_2;
				// 转为质量
				float weight = Util.KeepPoint2f((float) (len_2 / 10 * 0.8821)) * -1;
				float weight_rare = Util.KeepPoint2f(250 * 1000 - weight);

				// 存贮焊丝重量信息
				editor.putFloat("weight", weight);
				editor.putFloat("rare", weight_rare);
				editor.commit();
				if (weight < 1f) {
					before_off_len = sp.getFloat("weight", 0);
				}
				tv_total_len
						.setText((sp.getFloat("weight", 0f) + before_off_len)
								+ "g /"
								+ (sp.getFloat("rare", 0f) - before_off_len)
								+ "g");
				// 刷新送丝速度
				tv_speed.setText(Util.KeepPoint2((Float.parseFloat(map1.get(Config.speed)) * 100
						/ 6000 * -1 )+"")+ "m/min");
				tv_id.setTextColor(Color.YELLOW);
			}
		};

	};

	public static Handler mHandler2 = new Handler() {

		// 当板卡上信息发生改变时触发此处
		public void handleMessage(android.os.Message msg) {

			@SuppressWarnings("unchecked")
			Map<String, String> map2 = (Map<String, String>) msg.obj;
			Log.i("Update2", map2.size() + "");

			tv_current_rate_1.setText(Integer.parseInt(map2
					.get(Config.Current_Rate_1)) / 100000 + "L/min");
			float totalrate = Float.parseFloat(map2.get(Config.Total_Rate_1));
			tv_total_rate1.setText(Util.KeepPoint2(totalrate + "") + " m3");
			// tv_current_rate_2.setText(Integer.parseInt(map2.get(Config.Current_Rate_2))+"L/min");
			// tv_total_rate2.setText(Integer.parseInt(map2.get(Config.Total_Rate_2))+" m3");
			// 保留两位
			float len = Float.parseFloat(map2.get(Config.Total_length));
			float len_2 = (float) (Math.round(len * 100)) / 1000;
			float len_rare = MyApplication.getInstance().getHansi_len() - len_2;

			float weight = 0-(float) (len_2 / 10 * 0.8821);
			float weight_rare = 250 * 1000 - weight;

			// 当重量小于1时代表系统是经过了断电

			// 存贮焊丝重量信息
			editor.putFloat("weight", weight);
			editor.putFloat("rare", weight_rare);
			editor.commit();

			tv_total_len.setText((sp.getFloat("weight", 0f) + before_off_len)
					+ "g /" + (sp.getFloat("rare", 0f) - before_off_len) + "g");
			tv_speed.setText((0-Float.parseFloat(map2.get(Config.speed)) * 100
					/ 6000 )+ "m/min");
		};

	};

	public static Handler errorHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			// ToastUtils.showToast(MainActivity.this, (String)msg.obj).show();
			// Toast.makeText(mContext, (CharSequence) msg.obj, 0).show();
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		mContext = this;
		sp = this.getSharedPreferences("len", Context.MODE_PRIVATE);
		editor = sp.edit();
		MyApplication.getInstance().setIp_electric(
				this.getSharedPreferences("config", Context.MODE_PRIVATE)
						.getString("ip_electric", "0.0.0.0"));
		MyApplication.getInstance().setIp_air(
				this.getSharedPreferences("config", Context.MODE_PRIVATE)
						.getString("ip_air", "0.0.0.0"));
		MyApplication.getInstance().setHansi_len(1000000f);
		MyApplication.getInstance().setId(
				this.getSharedPreferences("config", Context.MODE_PRIVATE)
						.getString("deviceID", "1"));
		tv_id.setText("设备"
				+ this.getSharedPreferences("config", Context.MODE_PRIVATE)
						.getString("deviceID", "1"));

		try {
			CreateDB();
			CreateTable();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// float used_len = savedInstanceState.getFloat("used");
		//
		//
		// float rare_len = MyApplication.getInstance().getHansi_len() -
		// used_len;
		// tv_total_len.setText(used_len + "mm/" + rare_len + "mm");
		//
		Intent intent = new Intent(this, SanlingModbusService.class);
		startService(intent);

		// Intent intent2 = new Intent(this, SanlingModbus2Service.class);
		// startService(intent2);
		// initLineCharLeft1(lcv_1);
		hideMeter2();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		outState.putFloat("used", used);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

		float used_len = savedInstanceState.getFloat("used");

		float rare_len = MyApplication.getInstance().getHansi_len() - used_len;
		// tv_total_len.setText(used_len + "mm/" + rare_len + "mm");

	}

	/**
	 * 创建数据库
	 */
	@SuppressLint("SdCardPath")
	public void CreateDB() {

		DaoConfig config = new DaoConfig(this);
		// 创建数据库
		config.setDbName("mj.db");
		// 设置数据库路径
		config.setDbDir("/data/data/com.zk.amin/");
		// 版本号
		config.setDbVersion(1);
		// 创建
		mDbUtils = DbUtils.create(config);

	}

	public static void CreateTable() throws DbException {
		mDbUtils.createTableIfNotExist(Meter.class);
		mDbUtils.createTableIfNotExist(AirLen.class);
		// 创建表
	}

	/**
	 * 删除表
	 */
	public void deleteTable() {

		try {
			mDbUtils.dropTable(Meter.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 增加
	 */
	public static void insert(Meter m) {
		// 创建一条记录的对象
		try {
			mDbUtils.save(m);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insertAl(AirLen al) {

		try {
			mDbUtils.save(al);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 查询全部数据
	 * 
	 * @return
	 */
	public static List<Meter> queryAll() {
		List<Meter> data = null;
		try {
			data = mDbUtils.findAll(Meter.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	/**
	 * 表送气送丝查询全部数据
	 * 
	 * @return
	 */
	public static List<AirLen> queryAllAL() {
		List<AirLen> data = null;
		try {
			data = mDbUtils.findAll(AirLen.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	/**
	 * 获取最新数据id
	 * 
	 * @return
	 * @throws DbException
	 */
	public static int getMostNewId() throws DbException {
		Meter m = (Meter) mDbUtils.findFirst(Selector.from(Meter.class)
				.orderBy("time", true).limit(1));
		if (m != null) {

			return m.get_id();
		} else {
			return 0;
		}

	}

	/**
	 * 获取电流电压最新5000条数据
	 * 
	 * @return
	 * @throws DbException
	 */
	@SuppressWarnings("rawtypes")
	public static List getMostNewdata() throws DbException {
		List<Meter> m = mDbUtils.findAll(Selector.from(Meter.class)
				.orderBy("time", true).limit(3610));
		if (m != null) {
			return m;
		} else {
			return null;
		}

	}

	/**
	 * 获取送丝气体最新数据id
	 * 
	 * @return
	 * @throws DbException
	 */
	public static int getALMostNewId() throws DbException {
		AirLen a = (AirLen) mDbUtils.findFirst(Selector.from(AirLen.class)
				.orderBy("time", true).limit(1));
		if (a != null) {
			return a.get_id();
		} else {
			return 0;
		}
	}

	/**
	 * 获取气体送丝最新5000条数据
	 * 
	 * @return
	 * @throws DbException
	 */
	@SuppressWarnings("rawtypes")
	public static List getMostNewdataAL() throws DbException {
		List<Meter> m = mDbUtils.findAll(Selector.from(AirLen.class)
				.orderBy("time", true).limit(5000));
		if (m != null) {
			return m;
		} else {
			return null;
		}

	}

	/**
	 * 删除数据 save most new 200,000 data
	 * 
	 * @return
	 */

	public static void deleteData(int id) {

		try {
			mDbUtils.delete(Meter.class, WhereBuilder.b("_id", "<", id - 20000));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void deleteDataAL(int id) {

		try {
			mDbUtils.delete(AirLen.class,
					WhereBuilder.b("_id", "<", id - 20000));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<Meter> queryAll_1() {
		List<Meter> data = null;
		try {
			data = mDbUtils.findAll(Selector.from(Meter.class).where(
					WhereBuilder.b("meterId", "=", "1")));
			Log.i("查询", data.size() + "");
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	public static List<Meter> queryAll_2() {
		List<Meter> data = null;
		try {
			data = mDbUtils.findAll(Selector.from(Meter.class).where(
					WhereBuilder.b("meterId", "=", "2")));
			Log.i("查询", data.size() + "");
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	public static List<Meter> queryByTime(String time) {
		List<Meter> data = new ArrayList<Meter>();

		SQLiteDatabase db2 = mContext.openOrCreateDatabase("mj",
				Context.MODE_WORLD_READABLE, null);
		String sql = "select * from meter where time like '" + time + " 08%'";
		Cursor cursor = db2.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			Meter m = new Meter();
			m.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			m.setVa(cursor.getFloat(cursor.getColumnIndex("va")));
			m.setVb(cursor.getFloat(cursor.getColumnIndex("vb")));
			m.setVc(cursor.getFloat(cursor.getColumnIndex("vc")));
			m.setIa(cursor.getFloat(cursor.getColumnIndex("Ia")));
			m.setIb(cursor.getFloat(cursor.getColumnIndex("Ib")));
			m.setIc(cursor.getFloat(cursor.getColumnIndex("Ic")));
			m.setTime(cursor.getString(cursor.getColumnIndex("time")));
			m.setEne(cursor.getFloat(cursor.getColumnIndex("ene")));
			data.add(m);
		}
		db2.close();
		return data;

	}

	/**
	 * 清除数据表的内容
	 */
	public static void delete() {
		try {
			mDbUtils.delete(Meter.class, WhereBuilder.b("_id", ">", "0"));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 界面初始化
	 */
	private void initView() {
		// TODO Auto-generated method stub
		tv_setting = (TextView) findViewById(R.id.tv_setting_main);
		tv_help_main = (TextView) findViewById(R.id.tv_help_main);
		tv_vol_1 = (TextView) findViewById(R.id.tv_vol_1);
		tv_vol_2 = (TextView) findViewById(R.id.tv_vol_2);
		tv_ele_1 = (TextView) findViewById(R.id.tv_ele_1);
		tv_ele_2 = (TextView) findViewById(R.id.tv_ele_2);
		tv_id = (TextView) findViewById(R.id.tv_id);
		tv_pow = (TextView) findViewById(R.id.tv_pow);

		// 折线图
		// lcv_1 = (LineChartView) findViewById(R.id.lineChartleft1);
		// lcv_2 = (LineChartView) findViewById(R.id.lineChartleft2);
		tv_va_1 = (TextView) findViewById(R.id.tv_va_1);
		tv_vb_1 = (TextView) findViewById(R.id.tv_vb_1);
		tv_vc_1 = (TextView) findViewById(R.id.tv_vc_1);
		tv_va_2 = (TextView) findViewById(R.id.tv_va_2);
		tv_vb_2 = (TextView) findViewById(R.id.tv_vb_2);
		tv_vc_2 = (TextView) findViewById(R.id.tv_vc_2);
		tv_va_avg = (TextView) findViewById(R.id.tv_va_avg);
		tv_vb_avg = (TextView) findViewById(R.id.tv_vb_avg);
		tv_vc_avg = (TextView) findViewById(R.id.tv_vc_avg);

		btn_history1 = (Button) findViewById(R.id.btn_history1);
		// btn_history2 = (Button) findViewById(R.id.btn_his2);
		btn_clear = (Button) findViewById(R.id.btn_clear);

		tv_vol_avg_1 = (TextView) findViewById(R.id.vol_avg_1);
		tv_ele_avg_1 = (TextView) findViewById(R.id.ele_avg_1);
		tv_ene_pos_1 = (TextView) findViewById(R.id.ene_pos_1);
		tv_ene_rev_1 = (TextView) findViewById(R.id.ene_rev_1);

		ll_container_meter2 = (LinearLayout) findViewById(R.id.ll_container_meter2);
		tv_meter2 = (TextView) findViewById(R.id.tv_meter2);
		ll_container_air2 = (LinearLayout) findViewById(R.id.ll_container_air2);
		tv_air2 = (TextView) findViewById(R.id.tv_air2);

		// 节点板2

		tv_current_rate_1 = (TextView) findViewById(R.id.current_rate_1);
		tv_current_rate_2 = (TextView) findViewById(R.id.current_rate_2);

		tv_total_rate1 = (TextView) findViewById(R.id.total_rate_1);
		tv_total_rate2 = (TextView) findViewById(R.id.total_rate_2);

		tv_total_len = (TextView) findViewById(R.id.total_len);
		tv_speed = (TextView) findViewById(R.id.speed);
		tv_setting.setOnClickListener(this);
		btn_history1.setOnClickListener(this);
		tv_help_main.setOnClickListener(this);
		// btn_history2.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
	}

	private void hideMeter2() {

		ll_container_air2.setVisibility(View.GONE);
		tv_air2.setVisibility(View.GONE);
		// tv_ele_2.setVisibility(View.INVISIBLE);
		// tv_vol_2.setVisibility(View.INVISIBLE);
	}

	/**
	 * 点击
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_setting_main:
			startActivity(new Intent(MainActivity.this, SetActivity.class));
			break;

		case R.id.btn_history1:
			Log.i("btn", "1" + "");
			final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
			dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
			dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
			dialog.setIcon(R.drawable.wenhao);//
			// 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
			dialog.setTitle("提示");
			dialog.setMessage("查询中请稍后。。。");
			dialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						startActivity(new Intent(MainActivity.this,
								HistoryActivity1.class));
						Thread.sleep(1000);
						// cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
						// 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
						dialog.cancel();

						// dialog.dismiss();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();
			break;
		case R.id.btn_clear:
			tv_total_len.setText("0 mm/"
					+ MyApplication.getInstance().getHansi_len() + " mm");
			break;
		case R.id.tv_help_main:

			startActivity(new Intent(MainActivity.this, HelpActivity.class));
			break;
		default:
			break;
		}
	}

}
