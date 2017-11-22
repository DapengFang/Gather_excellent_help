package com.gather_excellent_help.db.suning;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dapeng Fang on 2017/11/15.
 */

public class GoodscartOpenHelper extends SQLiteOpenHelper {

    private static final String name = "goods_cart";//数据库名
    private static final int version = 1;//版本

    public GoodscartOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists goods_cart( "
                + "id integer primary key autoincrement,"
                + "product_id text,"
                + "product_title text,"
                + "product_num text,"
                + "product_spec text,"
                + "product_sprice text,"
                + "product_mprice text,"
                + "product_pic text,"
                + "product_spec_id text,"
                + "product_check text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists goods_cart");
        onCreate(db);
    }
}
