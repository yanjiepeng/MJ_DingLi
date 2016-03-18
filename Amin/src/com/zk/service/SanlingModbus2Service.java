package com.zk.service;


import android.R.integer;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.zk.amin.MainActivity;
import com.zk.application.MyApplication;
import com.zk.tools.Config;
import com.zk.tools.Utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by huaqiang
 * on 2015/9/11.
 */
public class SanlingModbus2Service extends Service {
    private boolean mConnected = false;
    private ModbusMaster master;
    private final static int SLAVE_ID = 24;
    private int PORT = 502;
    private int TIME_OUT = 5000;
    private int RETRY_TIME = 5;
    private String str = "";
    private Thread mConnectThread;

    /**
     * 板卡读线程
     */

    public Runnable readTask = new Runnable() {
        @SuppressWarnings("static-access")
		@Override
        public void run() {
            while (mConnected) {
                try {
                    String msg = Utils.imitateData2(master, SLAVE_ID).toString();
                    
                    
                    Log.i("msg", msg);
                    Map<String, String> map = Utils.formatResult(msg);
                    if (!map.isEmpty() && (!str.equals(msg) || Utils.getServiceOnStart2())) {
//                        EventBus.getDefault().post(new EventAA(map, EventAA.ACTION_SEND_MSG));
                    	//内容发生变化时触发
                    	HashMap<String, String> data = new HashMap<String, String>();
                    	
                    	data.put(Config.Current_Rate_1,(Integer.parseInt(map.get(Config.Current_Rate_1))/1000+"" ));
                    	
                    	data.put(Config.Total_Rate_1,  (Integer.parseInt(map.get(Config.Total_Rate_1_H))*65536 + Integer.parseInt(map.get(Config.Total_Rate_1_M)) +( Integer.parseInt(map.get(Config.Total_Rate_1_L))/1000))+"");
                    	
                        data.put(Config.Current_Rate_2,(Integer.parseInt(map.get(Config.Current_Rate_2))/1000 +"" ));
                    	
                    	data.put(Config.Total_Rate_2,  (Integer.parseInt(map.get(Config.Total_Rate_2_H))*65536 + Integer.parseInt(map.get(Config.Total_Rate_2_M)) +( Integer.parseInt(map.get(Config.Total_Rate_2_L))/1000))+"");
                    	
                    	data.put(Config.Total_length, map.get(Config.Total_length)+"");
                    	data.put(Config.speed, map.get(Config.speed)+"");
                    	Log.i("data2:", data.size()+"");
                    	Message msgA = Message.obtain();
                    	msgA.obj = data;
                    	MainActivity.mHandler2.sendMessage(msgA);
                    	Utils.setServiceOnStart2(false);
                    	
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
        Utils.setServiceOnStart2(true);
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
            tcpParameters.setHost(MyApplication.getInstance().getIp_air().trim());
            tcpParameters.setPort(PORT);
            ModbusFactory modbusFactory = new ModbusFactory();

            master = modbusFactory.createTcpMaster(tcpParameters, true);
            master.setTimeout(TIME_OUT);
            master.setRetries(0);
            master.init();
            if (master.isInitialized()) {
                mConnected = true;
                Log.i("ta", "success2");
                new Thread(readTask).start();
            }
        } catch (Exception e) {
            master.destroy();
            e.printStackTrace();
//            L.e(getClass().getSimpleName(), e.getMessage());

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
