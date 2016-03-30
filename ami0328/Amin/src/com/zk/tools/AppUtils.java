 	package com.zk.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
	
/**
 * app工具
 * @author yanjiepeng
 *
 */
public class AppUtils {
	
	
	private AppUtils()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");  
  
    }  
  
    /** 
     * ��ȡӦ�ó������ 
     */  
    public static String getAppName(Context context)  
    {  
        try  
        {  
            PackageManager packageManager = context.getPackageManager();  
            PackageInfo packageInfo = packageManager.getPackageInfo(  
                    context.getPackageName(), 0);  
            int labelRes = packageInfo.applicationInfo.labelRes;  
            return context.getResources().getString(labelRes);  
        } catch (NameNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * [��ȡӦ�ó���汾�����Ϣ] 
     *  
     * @param context 
     * @return ��ǰӦ�õİ汾��� 
     */  
    public static String getVersionName(Context context)  
    {  
        try  
        {  
            PackageManager packageManager = context.getPackageManager();  
            PackageInfo packageInfo = packageManager.getPackageInfo(  
                    context.getPackageName(), 0);  
            return packageInfo.versionName;  
  
        } catch (NameNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  

}
