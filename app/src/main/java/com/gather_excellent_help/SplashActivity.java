package com.gather_excellent_help;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gather_excellent_help.ui.base.BaseFullScreenActivity;
import com.gather_excellent_help.ui.widget.SplashView;
import com.gather_excellent_help.utils.CacheUtils;
import com.gather_excellent_help.utils.Tools;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseFullScreenActivity {

    private static final int REQUEST_TIME = 1;
    @Bind(R.id.iv_splash)
    ImageView  ivSplash;
    @Bind(R.id.rl_splash)
    RelativeLayout rlSplash;

    private int time=2;

    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_TIME :
                    goToMain();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        startLoadingData();
    }

    /**
     * start splash animation
     */
    private void startLoadingData(){
        // finish "loading data" in a random time between 1 and 3 seconds
//        Random random = new Random();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onLoadingDataEnded();
//            }
//        },1000 + random.nextInt(2000));
        ivSplash.setBackgroundResource(R.drawable.splash_img);
        mHandler.sendEmptyMessageDelayed(REQUEST_TIME,2000);
    }



//    private void onLoadingDataEnded(){
//        // start the splash animation
//        splashView.splashAndDisappear(new SplashView.ISplashListener(){
//            @Override
//            public void onStart(){
//                // log the animation start event
//                if(BuildConfig.DEBUG){
//
//                }
//            }
//
//            @Override
//            public void onUpdate(float completionFraction){
//                // log animation update events
//                if(BuildConfig.DEBUG){
//
//                }
//            }
//
//            @Override
//            public void onEnd(){
//                // log the animation end event
//                if(BuildConfig.DEBUG){
//
//                }
//                // free the view so that it turns into garbage
//                splashView = null;
//                goToMain();
//            }
//        });
//    }

    public void goToMain() {
        finish();
        boolean isFirst = CacheUtils.getBoolean(this, CacheUtils.FIRST_STATE, false);
        if(!isFirst) {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            CacheUtils.putBoolean(this,CacheUtils.FIRST_STATE,true);
        }else{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
