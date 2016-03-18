package com.zk.tools;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

	/**
	 * 提醒封装工具
	 * @param ctx
	 * @param id
	 * @param str
	 */
	public static void showToast(Context ctx, int id, String str) {
        if (str == null) {
            return;
        }

        Toast toast = Toast.makeText(ctx, ctx.getString(id) + str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(Context ctx, String errInfo) {
        if (errInfo == null) {
            return;
        }

        Toast toast = Toast.makeText(ctx, errInfo, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
