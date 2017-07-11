package com.gather_excellent_help.utils.imageutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by wuxin on 2017/7/4.
 * 联网下载图片的工具类
 */

public class DownloadImageUtil {


    /**
     * 下载图片保存到指定的文件
     * @param urls
     * @param file
     * @return
     */
    public static boolean downloadImageByUrl(String urls , File file){
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buf = new byte[512];
            int len = 0;
            while((len = is.read(buf)) !=-1) {
               fos.write(buf,0,len);
            }
            fos.flush();
           return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
                try {
                    if(fos!=null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(is!=null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return false;
    }

    /**
     * 下载图片并且转化为Bitmap对象
     * @param urls
     * @param imageView
     * @return
     */
    public static Bitmap downloadImageByUrl(String urls , ImageView imageView){
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            ImageSizeUtil.ImageSize imageViewSize = ImageSizeUtil.getImageViewSize(imageView);
            options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,imageViewSize.width,imageViewSize.height);
            options.inJustDecodeBounds = false;
            is.reset();
            bitmap = BitmapFactory.decodeStream(is, null, options);
            conn.disconnect();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(fos!=null) {
                    fos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                if(is!=null) {
                    is.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

}
