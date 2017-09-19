package push.jerry.cn.scan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import push.jerry.cn.scan.camera.CameraManager;
import push.jerry.cn.scan.common.FlashLightManager;
import push.jerry.cn.scan.config.Config;
import push.jerry.cn.scan.config.ConstValue;
import push.jerry.cn.scan.decode.CaptureActivityHandler;
import push.jerry.cn.scan.permisson.Acp;
import push.jerry.cn.scan.permisson.AcpListener;
import push.jerry.cn.scan.permisson.AcpOptions;
import push.jerry.cn.scan.permisson.ToastUtil;
import push.jerry.cn.scan.view.ViewfinderView;


/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * <p>
 * 此Activity所做的事： 1.开启camera，在后台独立线程中完成扫描任务；
 * 2.绘制了一
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public class CaptureActivity extends AppCompatActivity
        implements SurfaceHolder.Callback {
    public static final int OPEN_TYPE_COUPON_CODE = 1;//打开类型 扫描印书码
    public static final int OPEN_TYPE_PV_CODE = 2;//打开类型 扫描商家优惠码

    private static final int ANIMATION_DURATION = 800;
    private static final int ANIMATION_STARTOFFSET = 500;
    private static final String TAG = CaptureActivity.class.getSimpleName();

    private static final int PARSE_BARCODE_FAIL = 300;
    private static final int PARSE_BARCODE_SUC = 200;
    FrameLayout flMain;
    /**
     * 是否有预览
     */
    private boolean hasSurface;
    /**
     * 活动监控器。如果手机没有连接电源线，那么当相机开启后如果一直处于不被使用状态则该服务会将当前activity关闭。
     * 活动监控器全程监控扫描活跃状态，与CaptureActivity生命周期相同.每一次扫描过后都会重置该监控，即重新倒计时。
     */
    private InactivityTimer inactivityTimer;
    /**
     * 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
     */
    private BeepManager beepManager;
    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    private AmbientLightManager ambientLightManager;
    private CameraManager cameraManager;
    /**
     * 扫描区域
     */
    private ViewfinderView viewfinderView;
    private CaptureActivityHandler handler;
    private Result lastResult;
    private boolean isFlashlightOpen;
    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 编码类型，该参数告诉扫描器采用何种编码方式解码，即EAN-13，QR
     * Code等等 对应于DecodeHintType.POSSIBLE_FORMATS类型
     * 参考DecodeThread构造函数中如下代码：hints.put(DecodeHintType.POSSIBLE_FORMATS,
     * decodeFormats);
     */
    private Collection<BarcodeFormat> decodeFormats;
    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 该参数最终会传入MultiFormatReader，
     * 上面的decodeFormats和characterSet最终会先加入到decodeHints中 最终被设置到MultiFormatReader中
     * 参考DecodeHandler构造器中如下代码：multiFormatReader.setHints(hints);
     */
    private Map<DecodeHintType, ?> decodeHints;
    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 字符集，告诉扫描器该以何种字符集进行解码
     * 对应于DecodeHintType.CHARACTER_SET类型
     * 参考DecodeThread构造器如下代码：hints.put(DecodeHintType.CHARACTER_SET,
     * characterSet);
     */
    private String characterSet;
    private Result savedResultToShow;
    private IntentSource source;
    private RelativeLayout rlOpenUp, rlOpenDown;
    private SurfaceHolder surfaceHolder;
    private ImageView lightView;
    private static OnScanResultListener onScanResultListener;

    public static void open(Context context, OnScanResultListener listener) {
        onScanResultListener = listener;
        context.startActivity(new Intent(context, CaptureActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        flMain = (FrameLayout) findViewById(R.id.capture_frame);
        lightView = (ImageView) findViewById(R.id.btnLight);
        lightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(CaptureActivity.this);
                boolean lightOpen = sharedPrefs.getBoolean(Config.KEY_LIGHT_OPEN, false);
                if (lightOpen) {
                    sharedPrefs.edit().putBoolean(Config.KEY_LIGHT_OPEN, false).commit();
                    FlashLightManager.turnLightOff(cameraManager.getCamera());
                    lightView.setImageDrawable(getResources().getDrawable(R.drawable.qrcode_scan_btn_flash_nor));
                } else {
                    sharedPrefs.edit().putBoolean(Config.KEY_LIGHT_OPEN, true).commit();
                    FlashLightManager.turnLightOn(cameraManager.getCamera());
                    lightView.setImageDrawable(getResources().getDrawable(R.drawable.qrcode_scan_btn_flash_down));
                }
            }
        });

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);
    }

    private void requestPermission(final SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        if (Build.VERSION.SDK_INT >= 23){
            Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA).build(),
                    new AcpListener() {
                        @Override
                        public void onGranted() {
                            initCamera(surfaceHolder);
                        }
                        @Override
                        public void onDenied(List<String> permissions) {
                            ToastUtil.show(CaptureActivity.this,"权限拒绝");
                            finish();
                        }
                    });
        }else {
            initCamera(surfaceHolder);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initCamera(this.surfaceHolder);
        }
    }

    protected int getScreenHeight() {
        WindowManager wm = this.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.

        // 相机初始化的动作需要开启相机并测量屏幕大小，这些操作
        // 不建议放到onCreate中，因为如果在onCreate中加上首次启动展示帮助信息的代码的 话，
        // 会导致扫描窗口的尺寸计算有误的bug
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.capture_viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        viewfinderView.setTipView(findViewById(R.id.scan_tip));
        viewfinderView.setErrorView(findViewById(R.id.error_tip));

        rlOpenUp = (RelativeLayout) findViewById(R.id.rl_capture_open_up);
        rlOpenDown = (RelativeLayout) findViewById(R.id.rl_capture_open_down);

        int screenHeightHalf = getScreenHeight();
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -screenHeightHalf);
        animation.setRepeatCount(0);//设置重复次数
        animation.setFillAfter(true);// 停在最后一帧
        animation.setDuration(ANIMATION_DURATION);//设置动画持续时间
        animation.setStartOffset(ANIMATION_STARTOFFSET);
        rlOpenUp.startAnimation(animation);

        TranslateAnimation animation2 = new TranslateAnimation(0, 0, 0, screenHeightHalf);
        animation2.setRepeatCount(0);//设置重复次数
        animation2.setFillAfter(true);// 停在最后一帧
        animation2.setDuration(ANIMATION_DURATION);//设置动画持续时间
        animation2.setStartOffset(ANIMATION_STARTOFFSET);
        rlOpenDown.startAnimation(animation2);

//        ObjectAnimator animUp = ObjectAnimator.ofFloat(rlOpenUp, "translationY", 0, -screenHeightHalf);
//        ObjectAnimator animDown = ObjectAnimator.ofFloat(rlOpenDown, "translationY", 0, screenHeightHalf);
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.setDuration(10000);
//        animSet.setInterpolator(new LinearInterpolator());
//        animSet.playTogether(animUp, animDown);
//        animSet.start();

        handler = null;
        lastResult = null;

        // 摄像头预览功能必须借助SurfaceView，因此也需要在一开始对其进行初始化
        // 如果需要了解SurfaceView的原理
        // 参考:http://blog.csdn.net/luoshengyang/article/details/8661317
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view); // 预览
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
//            initCamera(surfaceHolder);
            requestPermission(surfaceHolder);
        } else {
            // 防止sdk8的设备初始化预览异常
            //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }

        // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        beepManager.updatePrefs();

        // 启动闪光灯调节器
        ambientLightManager.start(cameraManager);

        // 恢复活动监控器
        inactivityTimer.onResume();

        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();

        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //if ((source == IntentSource.NONE) && lastResult != null)
                //{ // 重新进行扫描
                //    restartPreviewAfterDelay(0L);
                //    return true;
                //}
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.zoomIn();
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.zoomOut();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            //initCamera(holder);
            requestPermission(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        hasSurface = false;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and
     * show
     * the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode,
                             float scaleFactor) {
        // 重新计时
        inactivityTimer.onActivity();
        lastResult = rawResult;

        // 把图片画到扫描框
        //viewfinderView.drawResultBitmap(barcode);

        //播放声音
//        beepManager.playBeepSoundAndVibrate();


        doAnalysisResult(ResultParser.parseResult(rawResult).toString());
    }

    public void clickHow(View view) {
        // TODO: 2017/4/27 show howe
        // ScanGuideActivity.open(CaptureActivity.this);
    }

    protected void doAnalysisResult(String result) {
        if (onScanResultListener != null) {
            onScanResultListener.onResult(result);
        }
        finish();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(ConstValue.scan_restart_preview, delayMS);
        }
        resetStatusView();
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }

        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 向CaptureActivityHandler中发送消息，并展示扫描到的图像
     */
    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, ConstValue.scan_decode_succeeded,
                        savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.scan_msg_camera_framework_bug));
        builder.setPositiveButton(R.string.scan_dialog_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void Close(View view) {
        this.finish();
    }

    public interface OnScanResultListener {
        void onResult(String result);
    }
}
