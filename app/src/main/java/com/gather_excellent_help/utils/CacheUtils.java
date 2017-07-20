package com.gather_excellent_help.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//SP保存工具类。
public class CacheUtils {
	//登录标识
	public static final String LOGIN_VALUE = "login_value";
	//登录状态。
	public static final String LOGIN_STATE = "Login_State";
	//绑定淘宝状态
	public static final String BIND_STATE = "bind_state";
	//绑定支付宝状态
	public static final String PAY_STATE = "pay_state";

	private static final String NAME = "login";
	public static SharedPreferences sp;

	public static void putBoolean(Context context,String key,boolean value){
		if(sp == null){
			 sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).apply();
	}
	
	public static boolean getBoolean(Context context,String key,boolean defult){
		if(sp == null){
			 sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defult);
	}

	public static String getString(Context context,String key,String defult){
		if(sp == null){
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defult);
	}

	public static void putString(Context context,String key,String value){
		if(sp == null){
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).apply();
	}

	public static Long getLong(Context context,String key,long defult){
		if(sp == null){
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		return sp.getLong(key, defult);
	}

	public static void putLong(Context context,String key,long value){
		if(sp == null){
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putLong(key, value).apply();
	}
	public static Long getInteger(Context context,String key,Integer defult){
		if(sp == null){
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		return sp.getLong(key, defult);
	}

	public static void putInteger(Context context,String key,Integer value){
		if(sp == null){
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putLong(key, value).apply();
	}

}