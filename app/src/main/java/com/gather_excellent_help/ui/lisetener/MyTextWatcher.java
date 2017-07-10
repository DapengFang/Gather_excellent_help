package com.gather_excellent_help.ui.lisetener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by wuxin on 2017/7/10.
 */

public class MyTextWatcher implements TextWatcher{
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }
    @Override
    public void afterTextChanged(Editable edt) {
        String temp = edt.toString();
        if(edt.toString().getBytes().length != edt.length()){
            edt.delete(temp.length()-1, temp.length());
        }
//try {
//String temp = edt.toString();
//       String tem = temp.substring(temp.length()-1, temp.length());
//char[] temC = tem.toCharArray();
//int mid = temC[0];
//if(mid>=48&&mid<=57){//数字
//return;
//}
//if(mid>=65&&mid<=90){//大写字母
//return;
//}
//if(mid>=97&&mid<=122){//小写字母
//return;
//}
//edt.delete(temp.length()-1, temp.length());
//} catch (Exception e) {
//try {
//throw new Exception("登录页面监听密码输入框只能输入数字或者英文出错");
//} catch (Exception e1) {
//e1.printStackTrace();
//}
//}
    }
}
