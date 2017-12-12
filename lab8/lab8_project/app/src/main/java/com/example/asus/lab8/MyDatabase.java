package com.example.asus.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ASUS on 2017/12/12.
 */

public class MyDatabase extends SQLiteOpenHelper {
    public static final String Database_name="Contacts.db";
    public static final String Table_name="Contacts";
    public static final int DB_VERSION=1;

    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MyDatabase(Context context, String name,int version){
        this(context,name,null,version);
    }
    public MyDatabase(Context context,String name){
        this(context,name,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + Table_name
                + " ("
                + "name text primary key, "
                + "birth text , "
                + "gift text);";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean insert(String name,String birth,String gift){ //传入参数，插入数据库
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        Cursor cursor=db.rawQuery("select * from "+MyDatabase.Table_name,null);
        if(cursor.moveToFirst()==false) {
            db.insert(Table_name,null,values);
            db.close();
            return true;
        }
        else {
            boolean flag=false;
            do{
                int name_index=cursor.getColumnIndex("name");
                String name_tmp=cursor.getString(name_index);
                if(name_tmp.equals(name)){ //存在相同name
                    flag=true;
                    break;
                }
            }while (cursor.moveToNext());
            if(!flag){
                db.insert(Table_name,null,values);
                db.close();
                return true;
            }
        }
        return false;
    }
    public void update(String name,String birth,String gift){
        SQLiteDatabase db=getWritableDatabase();
        String whereClause= "name=?";
        String[] whereArgs={name};
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.update(Table_name,values,whereClause,whereArgs);
    }
    public void delete(String name){
        SQLiteDatabase db=getWritableDatabase();
        String whereClause= "name=?";
        String[] whereArgs={name};
        db.delete(Table_name,whereClause,whereArgs);
    }
}
