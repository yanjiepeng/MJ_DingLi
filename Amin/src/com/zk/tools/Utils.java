package com.zk.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.zk.amin.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huaqiang on 2015/9/11.
 */
public class Utils {
	private static boolean mIsOnStart = false;
	private static boolean mIsOnStart2 = false;

	public static String getIndexResult(String message, int index) {
		String[] msg = message.substring(1, message.length() - 1).split(",");
		return msg[index];
	}

	public static BatchResults<String> imitateData(ModbusMaster master,
			int slaveID) {
		BatchResults<String> results = null;
		BatchRead<String> batchRead = new BatchRead<String>();

		//读int部分
		batchRead.addLocator(Config.Voltage_1, BaseLocator.holdingRegister(
				slaveID, 0000, DataType.TWO_BYTE_INT_UNSIGNED));
		batchRead.addLocator(Config.Voltage_2, BaseLocator.holdingRegister(
				slaveID, 0002, DataType.TWO_BYTE_INT_UNSIGNED));

		batchRead
				.addLocator(Config.Electricity_1, BaseLocator
						.holdingRegister(slaveID, 0004,
								DataType.TWO_BYTE_INT_UNSIGNED));
		batchRead
				.addLocator(Config.Electricity_2, BaseLocator
						.holdingRegister(slaveID, 0006,
								DataType.TWO_BYTE_INT_UNSIGNED));
		
		//读float部分 电表1
		batchRead.addLocator(Config.Voltage_Va_1, BaseLocator.holdingRegister(
				slaveID, 8, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		
		batchRead.addLocator(Config.Voltage_Vb_1, BaseLocator.holdingRegister(
				slaveID, 10, DataType.FOUR_BYTE_FLOAT_SWAPPED));
	
		batchRead.addLocator(Config.Voltage_Vc_1, BaseLocator
				.holdingRegister(slaveID, 12, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Voltage_Avg_1, BaseLocator
				.holdingRegister(slaveID, 14, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Electricity_A_1, BaseLocator
				.holdingRegister(slaveID, 16, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Electricity_B_1, BaseLocator
				.holdingRegister(slaveID, 18, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Electricity_C_1, BaseLocator
				.holdingRegister(slaveID, 20, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Electricity_Avg_1, BaseLocator
				.holdingRegister(slaveID, 22, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		
		batchRead.addLocator(Config.Energy_Positive_1, BaseLocator
				.holdingRegister(slaveID, 24, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		
		batchRead.addLocator(Config.Energy_Reverse_1, BaseLocator
				.holdingRegister(slaveID, 26, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		//读电表2 float部分
		batchRead.addLocator(Config.Voltage_Va_2, BaseLocator
				.holdingRegister(slaveID, 28, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Voltage_Vb_2, BaseLocator
				.holdingRegister(slaveID, 30, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		
		batchRead.addLocator(Config.Voltage_Vc_2, BaseLocator
				.holdingRegister(slaveID, 32, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		
		batchRead.addLocator(Config.Voltage_Avg_2, BaseLocator
				.holdingRegister(slaveID, 34, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Electricity_A_2, BaseLocator
				.holdingRegister(slaveID, 36, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		
		batchRead.addLocator(Config.Electricity_B_2, BaseLocator
				.holdingRegister(slaveID, 38, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		
		batchRead.addLocator(Config.Electricity_C_2, BaseLocator
				.holdingRegister(slaveID, 40, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		
		
		batchRead.addLocator(Config.Electricity_Avg_2, BaseLocator
				.holdingRegister(slaveID, 42, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Energy_Positive_2, BaseLocator
				.holdingRegister(slaveID, 44, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		batchRead.addLocator(Config.Energy_Reverse_2, BaseLocator
				.holdingRegister(slaveID, 46, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		try {
			results = master.send(batchRead);
		} catch (ModbusTransportException e) {
			e.printStackTrace();
		} catch (ErrorResponseException e) {
			e.printStackTrace();
		}
		return results;
	}

	public static BatchResults<String> imitateData2(ModbusMaster master,
			int slaveID) {
		BatchResults<String> results = null;
		BatchRead<String> x = new BatchRead<String>();
		// 气体流量器1
		x.addLocator(Config.Current_Rate_1, BaseLocator.holdingRegister(
				slaveID, 0, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED));
		
		x.addLocator(Config.Total_Rate_1_H, BaseLocator.holdingRegister(
				slaveID, 2, DataType.TWO_BYTE_INT_UNSIGNED));
		x.addLocator(Config.Total_Rate_1_M, BaseLocator.holdingRegister(
				slaveID, 3, DataType.TWO_BYTE_INT_UNSIGNED));
		x.addLocator(Config.Total_Rate_1_L, BaseLocator.holdingRegister(
				slaveID, 4, DataType.TWO_BYTE_INT_UNSIGNED));

		// 气体流量器2
		x.addLocator(Config.Current_Rate_2, BaseLocator.holdingRegister(
				slaveID, 5, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED));
		x.addLocator(Config.Total_Rate_2_H, BaseLocator.holdingRegister(
				slaveID, 7, DataType.TWO_BYTE_INT_UNSIGNED));
		x.addLocator(Config.Total_Rate_2_M, BaseLocator.holdingRegister(
				slaveID, 8, DataType.TWO_BYTE_INT_UNSIGNED));
		x.addLocator(Config.Total_Rate_2_L, BaseLocator.holdingRegister(
				slaveID, 9, DataType.TWO_BYTE_INT_UNSIGNED));

		// 送丝
		x.addLocator(Config.Total_length, BaseLocator.holdingRegister(slaveID, 8192, DataType.FOUR_BYTE_FLOAT_SWAPPED));
		x.addLocator(Config.speed, BaseLocator.holdingRegister(slaveID, 8194, DataType.FOUR_BYTE_FLOAT_SWAPPED));

		try {
			results = master.send(x);
			Log.i("tag", "tag");
		}  catch (Exception e) {
			e.printStackTrace();
			Log.i( "damn", e.toString());
			MainActivity.mHandler.sendEmptyMessage(0);
		}
		return results;
	}
	
	
	
	
	
	

	public static Map<String, String> formatResult(String result) {
		Map<String, String> map = new HashMap();
		String s = result.substring(1, result.length() - 1).trim();
		String[] arr = s.split(",");
		for (int i = 0, len = arr.length; i < len; i++) {
			String[] string = arr[i].trim().split("=");
			map.put(string[0], string[1]);
		}
		return map;
	}

	// 获取或转换信息
	public static String getProduceData() {
		String produceData = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		produceData = format.format(curDate);
		return produceData;
	}

	public static String getOperaStatus(Context context, String mOperaStatus,
			ArrayList<Integer> operaStatusList) {
		String operaStatus = "";
		if (mOperaStatus != null) {
			operaStatus = context.getResources().getString(
					operaStatusList.get(Integer.parseInt(mOperaStatus)));
		}
		return operaStatus;
	}

	/*
	 * public static String getAlarmStatus(Context context, String mAlarmStatus,
	 * ArrayList<Integer> alarmStatusList) { String alarmStatus = "";
	 * 
	 * if (mAlarmStatus != null) { if (mAlarmStatus.equals(Config.STR_ZERO))
	 * alarmStatus =
	 * context.getResources().getString(alarmStatusList.get(Integer
	 * .parseInt(mAlarmStatus))); else alarmStatus =
	 * context.getResources().getString
	 * (alarmStatusList.get(Integer.parseInt(Config.STR_ONE))); }
	 * 
	 * return alarmStatus; }
	 */

	public static String getOperaMode(Context context, String mOperaMode,
			ArrayList<Integer> operaModeList) {
		String operaMode = "";

		if (mOperaMode != null)
			operaMode = context.getResources().getString(
					operaModeList.get(Integer.parseInt(mOperaMode)));

		return operaMode;
	}

	public static String getAbsolute(String mAbsolute) {
		String absolute = "";

		if (mAbsolute != null)
			absolute = String.valueOf(Float.parseFloat(mAbsolute) / 1000);

		return absolute;
	}

	public static String getPartName(Context context, String mPartName,
			ArrayList<Integer> partNameList) {
		String partName = "";
		if (mPartName != null) {
			if (Integer.parseInt(mPartName) <= 2) // for test
				partName = context.getResources().getString(
						partNameList.get(Integer.parseInt(mPartName)));
			else
				partName = "A";
		}
		return partName;
	}

	public static String getSpeed(String mSpeed) {
		String speed = "";

		if (mSpeed != null)
			speed = String.valueOf(Integer.parseInt(mSpeed) / 10);// mm/min转为cm/min

		return speed;
	}

	public static String getFormatTime(String min, String msec, boolean isH) {
		int hour = 0;
		int minute = 0;
		float minNum = 0;

		if (min == null && msec != null)
			minNum = Float.parseFloat(msec) / 60 * 1000;
		else if (min != null && msec == null)
			minNum = Float.parseFloat(min);
		else if (min != null && msec != null)
			minNum = Float.parseFloat(min) + Float.parseFloat(msec)
					/ (60 * 1000);

		hour = (int) minNum / 60;
		minute = (int) minNum % 60;

		if (isH)
			return String.valueOf(hour); // 返回小时h
		else
			return String.valueOf(minute); // 返回分钟m
	}

	public static void setServiceOnStart(boolean isOnStart) {
		mIsOnStart = isOnStart;
	}

	public static boolean getServiceOnStart2() {
		return mIsOnStart2;
	}

	public static void setServiceOnStart2(boolean mIsOnStart2) {
		Utils.mIsOnStart2 = mIsOnStart2;
	}

	public static boolean getServiceOnStart() {
		return mIsOnStart;
	}

	/*
	 * public static void initTabHost(Context context, FragmentTabHost tabHost,
	 * String tag, String title, Class<?> fragment) { TabHost.TabSpec
	 * tabSpecInvest = tabHost.newTabSpec(tag);
	 * tabSpecInvest.setIndicator(getIndicatorView(context, title)); Bundle
	 * investextras = new Bundle(); investextras.putString("msg", title);
	 * tabHost.addTab(tabSpecInvest, fragment, investextras); }
	 */

	/*
	 * public static View getIndicatorView(Context context, String text) { View
	 * view = LayoutInflater.from(context).inflate(R.layout.view_tab_item,
	 * null); TextView tvIndicator = (TextView)
	 * view.findViewById(R.id.tab_item_text); tvIndicator.setText(text); return
	 * view; }
	 */

	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					packageName, 0);

			if (null == info)
				return false;
			return true;

		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
}
