package com.zk.application;

import android.app.Application;

	/**
	 * application
	 * @author yanjiepeng
	 *
	 */
public class MyApplication extends Application {

	private String ip_electric;
	private String ip_air;

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
	}
	
	@SuppressWarnings("unused")
	private void clearInfo() {
		setIp_air(null);
		setIp_electric(null);
	}

}
