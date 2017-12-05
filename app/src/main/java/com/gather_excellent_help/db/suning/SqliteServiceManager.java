package com.gather_excellent_help.db.suning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gather_excellent_help.bean.suning.SuningGoodscartBean;
import com.gather_excellent_help.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dapeng Fang on 2017/11/15.
 */

public class SqliteServiceManager implements GoodscartService {

    private GoodscartOpenHelper goodscartOpenHelper;
    private SQLiteDatabase db;

    public SqliteServiceManager(Context context) {
        this.goodscartOpenHelper = new GoodscartOpenHelper(context);
    }

    @Override
    public void addGoods(Object[] params) {
        try {
            db = goodscartOpenHelper.getWritableDatabase();
            String sql = "insert into goods_cart(product_id,product_title,product_num,product_spec,product_sprice,product_mprice,product_pic,product_spec_id,product_check,product_goodsid,product_spec_limit) values(?,?,?,?,?,?,?,?,?,?,?)";
            db.execSQL(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }

    @Override
    public void deleteGoods(Object[] params) {
        try {
            db = goodscartOpenHelper.getWritableDatabase();
            String sql = "delete from goods_cart where id=?";
            db.execSQL(sql,params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void deleteAllGoods() {
        try {
            db = goodscartOpenHelper.getWritableDatabase();
            String sql = "DROP TABLE if exists goods_cart";
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void updateGoods(Object[] params) {
        try {
            db = goodscartOpenHelper.getWritableDatabase();
            String sql = "update goods_cart set product_num = ? where id=?";
            db.execSQL(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void updateGoodsCheck(Object[] params) {
        try {
            db = goodscartOpenHelper.getWritableDatabase();
            String sql = "update goods_cart set product_check = ? where id=?";
            db.execSQL(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public Map<String, String> selectGoods(String[] selectionArgs) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            db = goodscartOpenHelper.getWritableDatabase();
            String sql = "select * from goods_cart where id=?";
            Cursor cursor = db.rawQuery(sql, selectionArgs);
            int colums = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return map;
    }

    @Override
    public Map<String, String> selectGoodsId(String[] selectionArgs) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            db = goodscartOpenHelper.getWritableDatabase();
            String sql = "select * from goods_cart where product_id=?";
            Cursor cursor = db.rawQuery(sql, selectionArgs);
            int colums = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return map;
    }

    @Override
    public List<Map<String, String>> selectAllGoods(String[] selectionArgs) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String sql = "select * from goods_cart";
        try {
            db = goodscartOpenHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, selectionArgs);
            int colums = cursor.getColumnCount();
            LogUtil.e("colums = " + colums);
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                for (int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return list;
    }
}
