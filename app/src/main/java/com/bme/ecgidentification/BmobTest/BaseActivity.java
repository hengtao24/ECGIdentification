package com.bme.ecgidentification.BmobTest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.bme.ecgidentification.BmobUtil;

import cn.bmob.v3.Bmob;

/**
 * @ClassName: BaseActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-20 9:55:34
 */
public class BaseActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, BmobUtil.ApplicationId);
	}

	Toast toast;

	public void ShowToast(String text) {
		if (!TextUtils.isEmpty(text)) {
			if (toast == null) {
				toast = Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT);
			} else {
				toast.setText(text);
			}
			toast.show();
		}
	}
}
