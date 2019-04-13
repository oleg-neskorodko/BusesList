package com.example.awesome.buseslist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "busesDb";
    public static final String TABLE_BUSES = "busesTable";

    public static final String keyID = "_id";

    public DBHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_BUSES + "(" + keyID + " integer primary key, " + MainActivity.keys[0] + " integer, "
                + MainActivity.keys[1] + " integer, " + MainActivity.keys[2] + " integer, " + MainActivity.keys[3] + " integer, " + MainActivity.keys[4] + " integer, "
                + MainActivity.keys[5] + " integer, " + MainActivity.keys[6] + " integer, " + MainActivity.keys[7] + " integer, "
                + MainActivity.keys[8] + " text, " + MainActivity.keys[9] + " text, " + MainActivity.keys[10] + " text, " + MainActivity.keys[11] + " text, "
                + MainActivity.keys[12] + " text, " + MainActivity.keys[13] + " text, " + MainActivity.keys[14] + " text, " + MainActivity.keys[15] + " text, "
                + MainActivity.keys[16] + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_BUSES);
        onCreate(db);
    }
}
