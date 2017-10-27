package com.gather_excellent_help.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gather_excellent_help.R;
import com.gather_excellent_help.ui.activity.credits.InviteFriendsActivity;
import com.gather_excellent_help.ui.base.BaseActivity;
import com.gather_excellent_help.utils.LogUtil;
import com.gather_excellent_help.utils.qrcode.QRCodeUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class QRcodeActivity extends BaseActivity {

    private ImageView iv_qrcode;
    private TextView tv_qrcode_tip;
    private RelativeLayout rl_exit;
    private TextView tv_top_title_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        tv_qrcode_tip = (TextView) findViewById(R.id.tv_qrcode_tip);
        rl_exit = (RelativeLayout)findViewById(R.id.rl_exit);
        tv_top_title_name = (TextView)findViewById(R.id.tv_top_title_name);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_top_title_name.setText("邀请好友二维码");
        Intent intent = getIntent();
        final String share_url = intent.getStringExtra("share_url");
        final String filePath = getFileRoot(QRcodeActivity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";

        LogUtil.e("filePath = " + filePath);

        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(share_url, 800, 800,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.juyoubangs),
                        filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                            final String share_content = "我邀请你一起来聚优帮耍，快点打开" + share_url + "和我一起玩转聚优帮吧。";
                            final UMImage image = new UMImage(QRcodeActivity.this, bitmap);//bitmap文件
                            iv_qrcode.setImageBitmap(bitmap);
                            tv_qrcode_tip.setVisibility(View.VISIBLE);
                            iv_qrcode.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    new ShareAction(QRcodeActivity.this)
                                            .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                                            .withText("hello")//分享内容
                                            .withMedia(image)
                                            .setCallback(shareListener)//回调监听器
                                            .share();
                                    return true;
                                }
                            });
                            iv_qrcode.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //用广播通知相册进行更新相册
                                    saveImage();
                                }
                            });
                        }
                    });
                }
            }
        }).start();

        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(QRcodeActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(QRcodeActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(QRcodeActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };


    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }


    private void saveImage(){
        iv_qrcode.buildDrawingCache();
        Bitmap bitmap = iv_qrcode.getDrawingCache();
        //将Bitmap 转换成二进制，写入本地
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG , 100 , stream);
        byte[] byteArray = stream.toByteArray();
        File dir=new File(Environment.getExternalStorageDirectory ().getAbsolutePath()+"/qrcode" );
        if(!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, File.separator
                + "qr_" + System.currentTimeMillis() + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArray, 0, byteArray.length);
            fos.flush();
            //用广播通知相册进行更新相册
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            QRcodeActivity.this.sendBroadcast(intent);
            Toast.makeText(QRcodeActivity.this,"保存相册成功~",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

}
