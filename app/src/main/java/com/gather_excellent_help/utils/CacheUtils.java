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
	//是否相同用户
	public static final String SAMPLE_VALUE = "sample_value";
	//登录状态。
	public static final String LOGIN_STATE = "Login_State";
	//绑定淘宝状态
	public static final String BIND_STATE = "bind_state";
	//绑定支付宝状态
	public static final String PAY_STATE = "pay_state";
	//第一次打开app
	public static final String FIRST_STATE = "first_state";
	//保存用户类型
	public static final String GROUP_TYPE = "group_type";
    //保存推广赚开关状态
	public static final String TOGGLE_SHOW = "toggle_show";
	//保存会员类型
	public static final String SHOP_TYPE = "shop_type";

	private static final String NAME = "login";
	//保存广告位id
	public static final String ADVER_ID = "adver_id";
	public static final String TAOBAO_NICK = "taobao_nick";
	public static final String ALIPAY_ACCOUNT = "alipay_account";
	public static final String ALIPAY_USERNAME = "alipay_username";
	public static final String USER_RATE = "user_rate";
	public static final String LOGIN_PHONE = "login_phone";

	public static final String APPLAY_STATUS = "applay_status";
	public static final String PAY_STATUS = "pay_status";

	//保存冷启动修复状态
	public static final String HOT_FIX_STATUS = "hot_fix_status";

	//保存是否开启红包状态
	public static final String IS_OPEN_RED = "is_open_red";

	//保存热更新加载的提示次数
	public static final String HOT_FIX_SHOW_TOAST = "hot_fix_show_toast";

	//保存购物车勾选状态
	public static final String NETCART_CHECK_DATA = "netcart_check_data";

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
	public static Integer getInteger(Context context,String key,Integer defult){
		if(sp == null){
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		return sp.getInt(key,defult);
	}

	public static void putInteger(Context context,String key,Integer value){
		if(sp == null){
			sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).apply();
	}

}