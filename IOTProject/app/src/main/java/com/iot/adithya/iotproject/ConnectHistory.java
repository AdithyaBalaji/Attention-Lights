package com.iot.adithya.iotproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Adithya on 21-Jun-16.
 */
public class ConnectHistory extends SQLiteOpenHelper{
    private static final String DBNAME= "History";
    private static final String TABLE_NAME="History";
    private static final String IP="IP";
    private static final String PORT="PORT";
    private static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ("+IP+" VARCHAR(30), "+PORT+" VARCHAR(10))";
    private static final int DATABASE_VERSION = 1;


    public ConnectHistory(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void insertData(String ip,String port){
        //this.getWritableDatabase().execSQL("INSERT INTO " + TABLE_NAME + " VALUES (" + ip + "," + port + ")");
        ContentValues cv = new ContentValues();
        cv.put(IP,ip);
        cv.put(PORT,port);
        this.getWritableDatabase().insert(TABLE_NAME,"NULL",cv);

    }

    public ArrayList<String> getData(){
        Cursor cu= this.getReadableDatabase().rawQuery("SELECT * FROM "+ TABLE_NAME,null);
        ArrayList<String> list = new ArrayList<>();

        while(cu.moveToNext()){
             list.add(cu.getString(0)+":"+cu.getString(1));
        }
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void clearTable(){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME );
    }
}
