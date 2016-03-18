package com.zk.amin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

import com.zk.application.MyApplication;
import com.zk.service.SanlingModbus2Service;
import com.zk.service.SanlingModbusService;
import com.zk.tools.Config;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

	/**
	 * 
	 * @author yanjiepeng
	 *
	 */
public class MainActivity extends Activity implements OnClickListener {

	private TextView tv_setting;
	//总电流电压
	private static TextView tv_vol_1,tv_vol_2,tv_ele_1,tv_ele_2;
    //三相电压与电流
	private static TextView tv_va_1,tv_vb_1,tv_vc_1,tv_va_2,tv_vb_2,tv_vc_2;
	
	//折线图
	private static LineChartView lcv_1,lcv_2;
	//平均数据
	private static TextView tv_vol_avg_1,tv_vol_avg_2,
							tv_ele_avg_1,tv_ele_avg_2,
							tv_ene_pos_1,tv_ene_pos_2,
							tv_ene_rev_1,tv_ene_rev_2
							;
	
	
	//节点板2数据
	private static TextView tv_current_rate_1,tv_current_rate_2,tv_total_rate1,tv_total_rate2,tv_speed,tv_total_len;
	public static Handler mHandler = new Handler() {

		// 当板卡上信息发生改变时触发此处
		public void handleMessage(android.os.Message msg) {

			
			@SuppressWarnings("unchecked")
			Map<String,String > map1 = (Map<String, String>) msg.obj;
			Log.i("Update1", map1.size()+"");
			int dianya1 = Integer.parseInt(map1.get(Config.Voltage_1));
			//电压1
			if (dianya1 >= 30796 && dianya1 <= 33024) {
				tv_vol_1.setText("传感器断开");
			}
			if (dianya1 >= 36864 && dianya1 <= 39321) {
				tv_vol_1.setText("没有电压");
			}
			if (dianya1 >39321) {
				tv_vol_1.setText((dianya1 - 39321) * 45 /26214 +"V" );
			}
			
			int dianya2 = Integer.parseInt(map1.get(Config.Voltage_2));
			//电压2
			if (dianya2 >= 30796 && dianya2 <= 33024) {
				tv_vol_2.setText("传感器断开");
			}
			if (dianya2 >= 36864 && dianya2 <= 39321) {
				tv_vol_2.setText("没有电压");
			}
			if (dianya2 >39321) {
				tv_vol_2.setText((dianya2 - 39321) * 45 /26214 +"V" );
			}
			
			int dianliu1 = Integer.parseInt(map1.get(Config.Electricity_1));
			//电流1
			if (dianliu1 >= 30796 && dianliu1 <= 33024) {
				tv_ele_1.setText("传感器断开");
			}
			if (dianliu1 >= 36864 && dianliu1 <= 39321) {
				tv_ele_1.setText("没有电流");
			}
			if (dianliu1 >39321) {
				tv_ele_1.setText((dianliu1 - 39321) * 600 /26214 +"A" );
			}
			
			
			int dianliu2 = Integer.parseInt(map1.get(Config.Electricity_2));
			
			if (dianliu2 >= 30796 && dianliu2 <= 33024) {
				tv_ele_2.setText("传感器断开");
			}
			if (dianliu2 >= 36864 && dianliu2 <= 39321) {
				tv_ele_2.setText("没有电流");
			}
			if (dianliu2 >39321) {
				tv_ele_2.setText((dianliu2 - 39321) * 600 /26214 +"A" );
			}
			
			
			
//			tv_vol_1.setText((Integer.parseInt(map1.get(Config.Voltage_1))) +"V");
//			tv_vol_2.setText((Integer.parseInt(map1.get(Config.Voltage_2))) +"V");
//			tv_ele_1.setText((Integer.parseInt(map1.get(Config.Electricity_1)))+"A");
//			tv_ele_2.setText((Integer.parseInt(map1.get(Config.Electricity_2)))+"A");
			
			//刷新电表1
//			tv_va_1.setText(map1.get(Config.Voltage_Va_1)+"V/"+map1.get(Config.Electricity_A_1)+"A");
//			tv_vb_1.setText(map1.get(Config.Voltage_Vb_1)+"V/"+map1.get(Config.Electricity_B_1)+"A");
//			tv_vc_1.setText(map1.get(Config.Voltage_Vc_1)+"V/"+map1.get(Config.Electricity_C_1)+"A");
			if (SanlingModbusService.line1_V.size()==10) {
				SanlingModbusService.line1_V.remove(0);
			}
			if (SanlingModbusService.line2_V.size()==10) {
				SanlingModbusService.line2_V.remove(0);
			}
			if (SanlingModbusService.line3_V.size()==10) {
				SanlingModbusService.line3_V.remove(0);
			}
			if (SanlingModbusService.line1_A.size()==10) {
				SanlingModbusService.line1_A.remove(0);
			}
			if (SanlingModbusService.line2_A.size()==10) {
				SanlingModbusService.line2_A.remove(0);
			}
			if (SanlingModbusService.line3_A.size()==10) {
				SanlingModbusService.line3_A.remove(0);
			}
			SanlingModbusService.line1_V.add(map1.get(Config.Voltage_Va_1));
			SanlingModbusService.line2_V.add(map1.get(Config.Voltage_Vb_1));
			SanlingModbusService.line3_V.add(map1.get(Config.Voltage_Vc_1));

			SanlingModbusService.line1_A.add(map1.get(Config.Electricity_A_1));
			SanlingModbusService.line2_A.add(map1.get(Config.Electricity_B_1));
			SanlingModbusService.line3_A.add(map1.get(Config.Electricity_C_1));
			
			lcv_1.invalidate();
			
			
			
			tv_vol_avg_1.setText("相电压平均值："+map1.get(Config.Voltage_Avg_1)+"V");
			tv_ele_avg_1.setText("相电流平均值："+map1.get(Config.Electricity_Avg_1)+"A");
			tv_ene_pos_1.setText("正向实功电能："+map1.get(Config.Energy_Positive_1)+"Wh");
			tv_ene_rev_1.setText("反向实功电能："+map1.get(Config.Energy_Reverse_1)+"Wh");
			
			//刷新电表2
			tv_va_2.setText(map1.get(Config.Voltage_Va_2)+"V/"+map1.get(Config.Electricity_A_2)+"A");
			tv_vb_2.setText(map1.get(Config.Voltage_Vb_2)+"V/"+map1.get(Config.Electricity_B_2)+"A");
			tv_vc_2.setText(map1.get(Config.Voltage_Vc_2)+"V/"+map1.get(Config.Electricity_C_2)+"A");
		
			tv_vol_avg_2.setText("相电压平均值："+map1.get(Config.Voltage_Avg_2)+"V");
			tv_ele_avg_2.setText("相电流平均值："+map1.get(Config.Electricity_Avg_2)+"A");
			
//			BigDecimal bd = new BigDecimal(map1.get(Config.Energy_Positive_2));
//			String str = bd.toPlainString();
			float temp = Float.parseFloat(map1.get(Config.Energy_Positive_2));
			if (temp<0.00001) {
				tv_ene_pos_2.setText("正向实功电能：0.0Wh");
			}else {
 			tv_ene_pos_2.setText("正向实功电能："+temp+"Wh");
			}
			tv_ene_rev_2.setText("反向实功电能："+map1.get(Config.Energy_Reverse_2)+"Wh");
		};

	};
	
	public static Handler mHandler2 = new Handler() {

		// 当板卡上信息发生改变时触发此处
		public void handleMessage(android.os.Message msg) {
			
			@SuppressWarnings("unchecked")
			Map<String,String > map2 = (Map<String, String>) msg.obj;
			Log.i("Update2", map2.size()+"");
			
			tv_current_rate_1.setText(Integer.parseInt(map2.get(Config.Current_Rate_1))+"L/min");
			tv_total_rate1.setText(Integer.parseInt(map2.get(Config.Total_Rate_1))+" m3");
			tv_current_rate_2.setText(Integer.parseInt(map2.get(Config.Current_Rate_2))+"L/min");
			tv_total_rate2.setText(Integer.parseInt(map2.get(Config.Total_Rate_2))+" m3");
			//保留两位
			float len = Float.parseFloat(map2.get(Config.Total_length));
			float len_2 = (float)(Math.round(len*100))/100;
			tv_total_len.setText(len_2+"cm");
			tv_speed.setText(map2.get(Config.speed)+"cm/s");
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		MyApplication.getInstance().setIp_electric(
				this.getSharedPreferences("config", Context.MODE_PRIVATE)
						.getString("ip_electric", "0.0.0.0"));
		MyApplication.getInstance().setIp_air(
				this.getSharedPreferences("config", Context.MODE_PRIVATE)
						.getString("ip_air", "0.0.0.0"));
		Intent intent = new Intent(this, SanlingModbusService.class);
		startService(intent);

		Intent intent2 = new Intent(this, SanlingModbus2Service.class);
		startService(intent2);
		
		for (int i = 0; i < 10; i++) {
			SanlingModbusService.line1_A.add(i+"");
		}
		initLineCharLeft1(lcv_1);
	}

	/**
	 * 节面
	 */
	private void initView() {
		// TODO Auto-generated method stub
		tv_setting = (TextView) findViewById(R.id.tv_setting_main);
		
		tv_vol_1 = (TextView) findViewById(R.id.tv_vol_1);
		tv_vol_2 = (TextView) findViewById(R.id.tv_vol_2);
		tv_ele_1 = (TextView) findViewById(R.id.tv_ele_1);
		tv_ele_2 = (TextView) findViewById(R.id.tv_ele_2);
		
		//折线图
		lcv_1 = (LineChartView) findViewById(R.id.lineChartleft1);
//		lcv_2 = (LineChartView) findViewById(R.id.lineChartleft2);
//		tv_va_1 = (TextView) findViewById(R.id.tv_va_1);
//		tv_vb_1 = (TextView) findViewById(R.id.tv_vb_1);
//		tv_vc_1 = (TextView) findViewById(R.id.tv_vc_1);
		tv_va_2 = (TextView) findViewById(R.id.tv_va_2);
		tv_vb_2 = (TextView) findViewById(R.id.tv_vb_2);
		tv_vc_2 = (TextView) findViewById(R.id.tv_vc_2);
		
		tv_vol_avg_1 = (TextView) findViewById(R.id.vol_avg_1);
		tv_vol_avg_2 = (TextView) findViewById(R.id.vol_avg_2);
		tv_ele_avg_1 = (TextView) findViewById(R.id.ele_avg_1);
		tv_ele_avg_2 = (TextView) findViewById(R.id.ele_avg_2);
		tv_ene_pos_1 = (TextView) findViewById(R.id.ene_pos_1);
		tv_ene_pos_2 = (TextView) findViewById(R.id.ene_pos_2);
		tv_ene_rev_1 = (TextView) findViewById(R.id.ene_rev_1);
		tv_ene_rev_2 = (TextView) findViewById(R.id.ene_rev_2);
		
		
		//节点板2
		
		tv_current_rate_1 = (TextView) findViewById(R.id.current_rate_1);
		tv_current_rate_2 = (TextView) findViewById(R.id.current_rate_2);
		
		tv_total_rate1 = (TextView) findViewById(R.id.total_rate_1);
		tv_total_rate2 = (TextView) findViewById(R.id.total_rate_2);
		
		tv_total_len = (TextView) findViewById(R.id.total_len);
		tv_speed = (TextView) findViewById(R.id.speed);
		tv_setting.setOnClickListener(this);
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

		default:
			break;
		}
	}
	
	private  void initLineCharLeft1(LineChartView linechar) {
		// TODO Auto-generated method stub
		//电压1
		List<PointValue> mPointValues1 = new ArrayList<PointValue>();
		List<AxisValue> mAxisValues1 = new ArrayList<AxisValue>();
		for (int i = 0; i < SanlingModbusService.line1_V.size(); i++) {
			mPointValues1.add(new PointValue(i, Float.parseFloat(SanlingModbusService.line1_V.get(i))));
			mAxisValues1.add(new AxisValue(i).setLabel(i + "次")); // 为每个对应的i设置相应的label(显示在X轴)
		}
		Line line1 = new Line(mPointValues1).setColor(
				getResources().getColor(R.color.red)).setCubic(false);
		
		
		//电压2
		List<PointValue> mPointValues2 = new ArrayList<PointValue>();
		for (int i = 0; i < SanlingModbusService.line2_V.size(); i++) {
			mPointValues2.add(new PointValue(i, Float.parseFloat(SanlingModbusService.line2_V.get(i))));
		}
		Line line2 = new Line(mPointValues2).setColor(
				getResources().getColor(R.color.yellow)).setCubic(false);
		
		//电压3
		List<PointValue> mPointValues3 = new ArrayList<PointValue>();
		for (int i = 0; i < SanlingModbusService.line3_V.size(); i++) {
			mPointValues3.add(new PointValue(i, Float.parseFloat(SanlingModbusService.line3_V.get(i))));
		}
		Line line3 = new Line(mPointValues3).setColor(
				getResources().getColor(R.color.blue)).setCubic(false);
		
		//电流1
		
		List<PointValue> mPointValues4 = new ArrayList<PointValue>();
		for (int i = 0; i < SanlingModbusService.line1_A.size(); i++) {
			mPointValues4.add(new PointValue(i, Float.parseFloat(SanlingModbusService.line1_A.get(i))));
		}
		Line line4 = new Line(mPointValues4).setColor(
				getResources().getColor(R.color.pink)).setCubic(false);

		//电流2
		
		List<PointValue> mPointValues5 = new ArrayList<PointValue>();
		for (int i = 0; i < SanlingModbusService.line2_A.size(); i++) {
			mPointValues5.add(new PointValue(i, Float.parseFloat(SanlingModbusService.line2_A.get(i))));
		}
		Line line5 = new Line(mPointValues5).setColor(
				getResources().getColor(R.color.green)).setCubic(false);
		
		//电流3
		

		//电流1
		
		List<PointValue> mPointValues6 = new ArrayList<PointValue>();
		for (int i = 0; i < SanlingModbusService.line3_A.size(); i++) {
			mPointValues6.add(new PointValue(i, Float.parseFloat(SanlingModbusService.line3_A.get(i))));
		}
		Line line6 = new Line(mPointValues6).setColor(
				getResources().getColor(R.color.black)).setCubic(false);
		
		List<Line> lines = new ArrayList<Line>();
		lines.add(line1);
		lines.add(line2);
		lines.add(line3);
		lines.add(line4);
		lines.add(line5);
		lines.add(line6);
		LineChartData data = new LineChartData();
		data.setLines(lines);

		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(Color.BLUE);;
		axisX.setName("采集次数");
		axisX.setTextColor(Color.BLACK);
		axisX.setMaxLabelChars(10);
		axisX.setValues(mAxisValues1);
		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y轴
		axisY.setMaxLabelChars(7);
		axisY.setTextColor(Color.BLACK);// 默认是3，只能看最后三个数字
		data.setAxisYLeft(axisY);

		// 设置行为属性，支持缩放、滑动以及平移
		linechar.setInteractive(true);
		//linechar.setZoomType(ZoomType.HORIZONTAL);
		//linechar.setContainerScrollEnabled(true,	ContainerScrollType.HORIZONTAL);
		linechar.setLineChartData(data);
		linechar.setVisibility(View.VISIBLE);
		linechar.startDataAnimation(1000);
	}
	
	
  

}
