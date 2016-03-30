package com.zk.application;

import com.zk.amin.CrashHandler;

import android.app.Application;

	/**
	 * application
	 * @author yanjiepeng
	 *
	 */
public class MyApplication extends Application {

	private String ip_electric;
	private String ip_air;
	private float hansi_len;
	private String id;
	public String getIp_electric() {
		return ip_electric;
	}

	public void setIp_electric(String ip_electric) {
		this.ip_electric = ip_electric;
	}

	public String getIp_air() {
		return ip_air;
	}

	public void setIp_air(String ip_air) {
		this.ip_air = ip_air;
	}
	
	public float getHansi_len() {
		return hansi_len;
	}

	public void setHansi_len(float hansi_len) {
		this.hansi_len = hansi_len;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "MyApplication [ip_electric=" + ip_electric + ", ip_air="
				+ ip_air + "]";
	}
	
	private static MyApplication instance;

	public static MyApplication getInstance() {

		return

		instance;

	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext());  
	}
	
	@SuppressWarnings("unused")
	private void clearInfo() {
		setIp_air(null);
		setIp_electric(null);
	}

}
