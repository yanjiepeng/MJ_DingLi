package com.zk.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.exception.DbException;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.zk.amin.MainActivity;
import com.zk.application.MyApplication;
import com.zk.bean.Meter;
import com.zk.tools.Config;
import com.zk.tools.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by huaqiang on 2015/9/11.
 */
public class SanlingModbusService extends Service {
	private boolean mConnected = false;
	private ModbusMaster master;
	private final static int SLAVE_ID = 24;
	private int PORT = 502;
	private int TIME_OUT = 5000;
	private int RETRY_TIME = 5;
	private String str = "";
	private Thread mConnectThread;
	public static ArrayList<String> line1_V = new ArrayList<String>(10);
	public static ArrayList<String> line2_V = new ArrayList<String>(10);
	public static ArrayList<String> line3_V = new ArrayList<String>(10);

	public static ArrayList<String> line1_A = new ArrayList<String>(10);
	public static ArrayList<String> line2_A = new ArrayList<String>(10);
	public static ArrayList<String> line3_A = new ArrayList<String>(10);

	/**
	 * 板卡读线程
	 */

	public Runnable readTask = new Runnable() {
		@SuppressLint("SimpleDateFormat")
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			while (mConnected) {
				try {
					String msg = Utils.imitateData(master, SLAVE_ID).toString();
					Log.i("msg", msg);
					Map<String, String> map = Utils.formatResult(msg);
					// Date now = new Date();
					// Long time = now.getTime();
					// if (time % (1000 * 60 * 60) == 0) {
					// // 整点清空一次数据表
					// MainActivity.delete();
					// }

					int id = MainActivity.getMostNewId();
					Log.i("id", id + "");
					if (id > 20000) {
						MainActivity.deleteData(id);
					}

					if (!map.isEmpty()
							&& (!str.equals(msg) || Utils.getServiceOnStart())) {
						// EventBus.getDefault().post(new EventAA(map,
						// EventAA.ACTION_SEND_MSG));
						// 内容发生变化时触发
						HashMap<String, String> data = new HashMap<String, String>();

						// 电压1 直流
						data.put(Config.Voltage_1, map.get(Config.Voltage_1));
						// data.put(Config.Voltage_2,
						// map.get(Config.Voltage_2));
						// 电流1 直流
						data.put(Config.Electricity_1,
								map.get(Config.Electricity_1));
						// data.put(Config.Electricity_2,map.get(Config.Electricity_2));

						// 电表1 三相电压
						data.put(Config.Voltage_Va_1,
								map.get(Config.Voltage_Va_1));
						data.put(Config.Voltage_Vb_1,
								map.get(Config.Voltage_Vb_1));
						data.put(Config.Voltage_Vc_1,
								map.get(Config.Voltage_Vc_1));
						// 电表1 相平均电压
						data.put(Config.Voltage_Avg_1,
								map.get(Config.Voltage_Avg_1));
						// 电表1 三相电流
						data.put(Config.Electricity_A_1,
								map.get(Config.Electricity_A_1));
						data.put(Config.Electricity_B_1,
								map.get(Config.Electricity_B_1));
						data.put(Config.Electricity_C_1,
								map.get(Config.Electricity_C_1));
						// 电表1 三相电流评均值
						data.put(Config.Electricity_Avg_1,
								map.get(Config.Electricity_Avg_1));
						// 电表1 正向时功电能
						data.put(Config.Energy_Positive_1,
								map.get(Config.Energy_Positive_1));
						// 电表1 反向时供i电能
						data.put(Config.Energy_Reverse_1,
								map.get(Config.Energy_Reverse_1));
						// 气体流量 瞬时流量
						data.put(Config.Current_Rate_1, (Float.parseFloat(map
								.get(Config.Current_Rate_1)) / 1000 + ""));
						//气体流量 总流量
						data.put(Config.Total_Rate_1,
								Float.parseFloat(map.get(Config.Total_Rate_1))
										/ 1000 + "");
						
						//送丝量 总量
						data.put(Config.Total_length,
								map.get(Config.Total_length) + "");
						//送丝量 速度
						data.put(Config.speed, map.get(Config.speed) + "");

					

						Log.i("data1:", data.size() + "");

						// 存到数据库
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
						String str = formatter.format(curDate);
						Meter m = new Meter(
								1,
								Float.parseFloat(data.get(Config.Voltage_Va_1)),
								Float.parseFloat(data.get(Config.Voltage_Vb_1)),
								Float.parseFloat(data.get(Config.Voltage_Vc_1)),
								Float.parseFloat(data
										.get(Config.Electricity_A_1)),
								Float.parseFloat(data
										.get(Config.Electricity_B_1)),
								Float.parseFloat(data
										.get(Config.Electricity_C_1)),
								str,
								Float.parseFloat(data
										.get(Config.Energy_Positive_1)) / 1000,
								Integer.parseInt(data.get(Config.Voltage_1)),
								Integer.parseInt(data.get(Config.Electricity_1)),
								Float.parseFloat(data.get(Config.Current_Rate_1)),
								Float.parseFloat(data.get(Config.Total_Rate_1)),
								Float.parseFloat(data.get(Config.speed)));


						Message msg1 = Message.obtain();
						msg1.obj = data;
						MainActivity.mHandler.sendMessage(msg1);

						MainActivity.insert(m);
						Utils.setServiceOnStart(false);
					}
					str = msg;
					Thread.currentThread().sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		connectIpThread();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Utils.setServiceOnStart(true);

		if (mConnectThread != null && !mConnectThread.isAlive()) {
			connectIpThread();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void connectIpThread() {
		if (!mConnected) {
			mConnectThread = new Thread() {
				@Override
				public void run() {
					connectIP();
				}
			};
			mConnectThread.start();
		}
	}

	private void connectIP() {
		try {
			IpParameters tcpParameters = new IpParameters();
			tcpParameters.setHost(MyApplication.getInstance().getIp_electric());
			tcpParameters.setPort(PORT);
			ModbusFactory modbusFactory = new ModbusFactory();

			master = modbusFactory.createTcpMaster(tcpParameters, true);
			master.setTimeout(TIME_OUT);
			master.setRetries(0);
			master.init();
			if (master.isInitialized()) {
				mConnected = true;
				new Thread(readTask).start();
			}
		} catch (Exception e) {
			master.destroy();
			e.printStackTrace();
			// L.e(getClass().getSimpleName(), e.getMessage());

			try {
				Thread.sleep(10 * 1000);
			} catch (Exception ex) {
			}
			connectIP();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		master.destroy();
		try {
			master.destroy();
		} catch (Exception e) {
		}
	}
}
