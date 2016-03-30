package com.zk.amin;

import com.zk.application.MyApplication;
import com.zk.tools.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

	
	/**
	 * 
	 * @author yanjiepeng
	 *
	 */
public class SetActivity extends Activity implements OnClickListener {

	private EditText et_ip_electric, et_ip_air,et_length,et_device;;
	private TextView tv_back;
	private Button btn_confirm_set,btn_confirm_songsi,btn_device;
	public static String ip_air, ip_electric;
	SharedPreferences sp;
	Editor editor ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		sp = SetActivity.this.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		editor = sp.edit();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

		et_ip_air = (EditText) findViewById(R.id.et_ip_air);
		et_ip_electric = (EditText) findViewById(R.id.et_ip_electric);
		et_length = (EditText) findViewById(R.id.et_length);
		et_device = (EditText) findViewById(R.id.et_device);
		
		ip_air = sp.getString("ip_air", "0.0.0.0");
		ip_electric = sp.getString("ip_electric", "0.0.0.0");
		float l = sp.getFloat("length",  10000.0f);
		et_ip_air.setHint(ip_air);
		et_ip_electric.setHint(ip_electric);
		et_length.setHint("当前设置焊丝长度:"+l+"");
		
		
		btn_confirm_set = (Button) findViewById(R.id.btn_confirm);
		btn_confirm_songsi = (Button) findViewById(R.id.btn_hansi_len);
		btn_device = (Button) findViewById(R.id.btn_device);
		btn_confirm_set.setOnClickListener(this);
		btn_confirm_songsi.setOnClickListener(this);
		btn_device.setOnClickListener(this);
		tv_back = (TextView) findViewById(R.id.btn_back);
		tv_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_confirm:


			if (et_ip_air.getText().toString().length()==0 || et_ip_electric.getText().toString().length() == 0) {
				ToastUtils.showToast(SetActivity.this, "请确认修改的板卡地址不能为空");
			}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示ʾ"); // 标题
			builder.setMessage("确认修改?"); // 提示文本
			builder.setIcon(R.drawable.wenhao
					);// image
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() { 
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); // 取消dialog
							
							

							ip_air = et_ip_air.getText().toString().trim();
							ip_electric = et_ip_electric.getText().toString().trim();

							editor.putString("ip_air", ip_air);
							editor.putString("ip_electric", ip_electric);
							MyApplication.getInstance().setIp_electric(ip_electric);
							MyApplication.getInstance().setIp_air(ip_air);
							editor.commit();

							et_ip_air.setHint(ip_air);
							et_ip_electric.setHint(ip_electric);
							ToastUtils.showToast(SetActivity.this, "修改成功");
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() { // 监听点击
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			}
			break;

		case R.id.btn_back:
			
			startActivity(new Intent(SetActivity.this, MainActivity.class));
			break;
		case R.id.btn_hansi_len:
	
			if (et_length.length()==0) {
				ToastUtils.showToast(SetActivity.this, "请确认焊丝长度不能为空！");
			}
			float len = Float.parseFloat(et_length.getText().toString().trim());
			editor.putFloat("length", len);
			editor.commit();
			MyApplication.getInstance().setHansi_len(len);
			ToastUtils.showToast(SetActivity.this, "修改成功");
			break;
			
		case R.id.btn_device:
			
			if (et_device.length()==0) {
				ToastUtils.showToast(SetActivity.this, "请确认设备编号不能为空！");
			}
			//设备id
			String id = et_device.getText().toString().trim();
			MyApplication.getInstance().setId("设备2");
			editor.putString("deviceID", id);
			editor.commit();
			ToastUtils.showToast(SetActivity.this, "修改成功");
		default:
			break;
		}
	}
}
