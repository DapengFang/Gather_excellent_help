package com.gather_excellent_help.utils.downweb;

import com.gather_excellent_help.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ${} on 2017/7/19.
 */

public class HtmlRequest {
    /**
     * 通过网站域名URL获取该网站的源码
     * @param url
     * @return String
     * @throws Exception
     */
    public static String getURLSource(URL url) throws Exception    {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream =  conn.getInputStream();  //通过输入流获取html二进制数据
        byte[] data = readInputStream(inStream);        //把二进制数据转化为byte字节数据
        String htmlSource = new String(data);
        LogUtil.e(htmlSource);
        return htmlSource;
    }

    /**
     * 把二进制流转化为byte字节数组
     * @param instream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream instream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]  buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1){
            outStream.write(buffer,0,len);
            LogUtil.e("len ===" +len);
        }
        instream.close();
        return outStream.toByteArray();
    }

}
