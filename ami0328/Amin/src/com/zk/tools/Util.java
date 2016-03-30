package com.zk.tools;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class Util {

	
	
	
	
	
	
	public static int HexToDecimal(String s ) {
		
		
		
		int i = Integer.parseInt(s, 16);
		
		return i;
		
		
	}
	public static long HexToDecimalLong(String s) {
		
		
		long i = Long.parseLong(s,16);
		
		return i ;
	}
	
	//保留两位小数 float
	public static float KeepPoint2(String s) {
		float a = Float.parseFloat(s);
		float b = (float)(Math.round(a*100))/100;
		return b;		
	}
	
	//保留两位小数 float
		public static float KeepPoint2f(float s) {
			String a = String.valueOf(s);
			float b = KeepPoint2(a);
			return b;		
		}
	
	public static float getFloatAVG(List<Float> list ) {
		float res = 0 ;
		for (int i = 0; i < list.size(); i++) {
			res += list.get(i);
		}
		res = res / list.size();
		return res;
	}
	
	
	
	public static int getIntAVG(List<Integer> list ) {
		int res = 0 ;
		for (int i = 0; i < list.size(); i++) {
			res += list.get(i);
		}
		res = res / list.size();
		return res;
	}
	
	
}
