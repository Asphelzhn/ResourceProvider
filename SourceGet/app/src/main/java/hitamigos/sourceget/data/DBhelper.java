/*
 * Copyright (c) 2017.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.sourceget.data;

/**
 * Created by kuangmeng on 2017/1/2.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    public DBhelper(Context  context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {// 覆写onCreate方法，当数据库创建时就用SQL命令创建一个表
        db.execSQL("create table if not exists download(id integer primary key autoincrement,title text, link text)");
        System.out.println("数据表生成");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
  //      db.execSQL("drop table if exists download");
    //    db.execSQL("create table download(id integer primary key autoincrement,title text, link text)");
    }

}