package com.zk.tools;

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
	
	
	
	
	
}
