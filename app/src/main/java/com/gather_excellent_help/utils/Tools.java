package com.gather_excellent_help.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.gather_excellent_help.SplashActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by wuxin on 2017/7/11.
 */

public class Tools {

    /**
     * 用户是否登录
     * @param context
     * @return
     */
    public static boolean isLogin(Context context){
        return CacheUtils.getBoolean(context,CacheUtils.LOGIN_STATE,false);
    }

    /**
     * 用户是否绑定支付宝
     * @param context
     * @return
     */
    public static boolean isBindAlipay(Context context){
        return CacheUtils.getBoolean(context,CacheUtils.PAY_STATE,false);
    }


    /**
     * 用户是否登录
     * @param context
     * @return
     */
    public static boolean isBindTao(Context context){
        return CacheUtils.getBoolean(context,CacheUtils.BIND_STATE,false);
    }

    public static boolean isToggleShow(Context context){
        return CacheUtils.getBoolean(context, CacheUtils.TOGGLE_SHOW, false);
    }

    /**
     * 用户登录后的标识
     * @param context
     * @return
     */
    public static String getUserLogin(Context context){
        return CacheUtils.getString(context,CacheUtils.LOGIN_VALUE,"");
    }
    /**
     * 用户登录后的广告位id
     * @param context
     * @return
     */
    public static String getAdverId(Context context){
        return CacheUtils.getString(context,CacheUtils.ADVER_ID,"");
    }

    public static String getSampleUser(Context context){
        return CacheUtils.getString(context,CacheUtils.SAMPLE_VALUE,"");
    }
    /**
     * 用户登录后的佣金比率
     * @param context
     * @return
     */
    public static String getUserRate(Context context){
        return CacheUtils.getString(context,CacheUtils.USER_RATE,"");
    }

    /**
     * 获取支付状态
     * @param context
     * @return
     */
    public static int getPayState(Context context){
        return CacheUtils.getInteger(context,CacheUtils.PAY_STATUS,-1);
    }
    /**
     * 获取申请状态
     * @param context
     * @return
     */
    public static int getApplyState(Context context){
        return CacheUtils.getInteger(context,CacheUtils.APPLAY_STATUS,-1);
    }
    /**
     * 用户登录后的手机号
     * @param context
     * @return
     */
    public static String getUserPhone(Context context){
        return CacheUtils.getString(context,CacheUtils.LOGIN_PHONE,"");
    }



    /**
     * 用户登录后的类型
     * @param context
     * @return
     */
    public static int getGroupId(Context context){
        return CacheUtils.getInteger(context,CacheUtils.GROUP_TYPE,-1);
    }
    /**
     * 用户登录后的类型
     * @param context
     * @return
     */
    public static int getShopType(Context context){
        return CacheUtils.getInteger(context,CacheUtils.SHOP_TYPE,-1);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //讲Bitmap转为Base64的方法。
    @SuppressLint("NewApi")
    public static String BitmapToBase64(Bitmap bitmap) {
        String result = "";
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 对图片大小进行压缩
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * 设置部分字体颜色为红色(后面)
     */
    public static void setPartTextColor(TextView tv,String str,String s) {
        int end = str.indexOf(s);
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.RED),end, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(style);
    }
    /**
     * 设置部分字体颜色为红色(前面)
     */
    public static void setPartTextColor2(TextView tv,String str,String s) {
        if(str!=null && str.length()>0 && s!=null && s.length()>0) {
            int end = str.indexOf(s);
            SpannableStringBuilder style = new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(Color.RED),0, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv.setText(style);
        }
    }

    //获取当前版本号。
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void downloadApk(String apkUrl, File apkFile,
                                   ProgressDialog pd, Handler handler) throws Exception {
        // 得到连接对象
        URL url = new URL(apkUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(10000);
        connection.setDoInput(true);
        // 连接
        connection.connect();

        if (connection.getResponseCode() == 200) {
            //设置最大进度
            pd.setMax(connection.getContentLength());

            LogUtil.e(apkFile.getAbsolutePath());
            //数据读取并保存到文件
            FileOutputStream fos = new FileOutputStream(apkFile);
            InputStream is = connection.getInputStream();
            byte[] buffer = new byte[2048];
            int len = -1;
            while((len=is.read(buffer))>0) {
                fos.write(buffer, 0, len);
                pd.incrementProgressBy(len);//更新进度
            }
            is.close();
            fos.close();
            connection.disconnect();
            handler.sendEmptyMessage(SplashActivity.REQUEST_DOWNLOAD_SUCCESS);
        } else {
            throw new RuntimeException("没能正常得到数据!");
        }
    }

    public static void closeBoard(Context mcontext) {
        InputMethodManager imm = (InputMethodManager) mcontext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        if (imm.isActive())  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static void hideSystemKeyBoard(Context mcontext,View v) {
        InputMethodManager imm = (InputMethodManager) mcontext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    public static String getDeviceId(Activity activity){
        final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();

        return uniqueId;
    }
    //保存阿里云热修复冷启动的状态
    public static void saveHotfixStute(Context context,boolean bool){
        CacheUtils.putBoolean(context,CacheUtils.HOT_FIX_STATUS,bool);
    }
    //得到阿里云热修复冷启动的状态
    public static boolean getHotfixStute(Context context){
        return CacheUtils.getBoolean(context,CacheUtils.HOT_FIX_STATUS,false);
    }
}
