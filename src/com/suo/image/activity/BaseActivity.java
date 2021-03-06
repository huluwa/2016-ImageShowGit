package com.suo.image.activity;

import cn.bmob.v3.Bmob;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

@SuppressLint("CommitPrefEdits")
public class BaseActivity extends Activity implements OnClickListener{

	public SharedPreferences mShared;
	public SharedPreferences.Editor mEditor;
	public String yoyoShare;
	public static String APPID = "ac16820f5b075884d3a341dea39b705b";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		mShared = this.getSharedPreferences(yoyoShare, 0);
		mEditor = mShared.edit();
		Bmob.initialize(this, APPID);
//		AppConnect.getInstance("0df9931685622b433b8e42ee34524104","default",this);
	}

//	public boolean putString(final String entry, String value, boolean commit) {
//        if(mEditor == null){
//            return false;
//        }
//        mEditor.putString(entry.toString(), value);
//        if(commit){
//            return mEditor.commit();
//        }
//        return true;
//    }
	
	public String getString(final String entry, String defaultValue) {
        if(mEditor == null){
            return defaultValue;
        }
        try{
            return mShared.getString(entry.toString(), defaultValue);
        }
        catch(Exception e){
            e.printStackTrace();
            return defaultValue;
        }
    }
	
	public boolean putInt(final String entry, int value, boolean commit) {
        if(mEditor == null){
            return false;
        }
        mEditor.putInt(entry.toString(), value);
        if(commit){
            return mEditor.commit();
        }
        return true;
    }
	
	public int getInt(final String entry, int defaultValue) {
        if(mEditor == null){
            return defaultValue;
        }
        try{
            return mShared.getInt(entry.toString(), defaultValue);
        }
        catch(Exception e){
            e.printStackTrace();
            return defaultValue;
        }
    }
	
	public void showToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	public void showToast(int msg)
	{
		Toast.makeText(this, getString(msg), Toast.LENGTH_SHORT).show();
	}
	
	public void showLog(String msg)
	{
		Log.e("suo", "" + msg);
	}

	@Override
	public void onClick(View v) {
		
	}
	
	public void launch(Class<? extends Activity> clazz) {
		startActivity(new Intent(this, clazz));
	}

	public void launch(Intent intent) {
		startActivity(intent);
	}
}
