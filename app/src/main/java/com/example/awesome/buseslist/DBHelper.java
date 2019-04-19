package com.example.awesome.buseslist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // читай "Модификаторы доступа Java"
    // у тебя DATABASE_VERSION только тут используеться
    // она не должна быть public, а только private
    //TODO исправил
    private static final int DATABASE_VERSION = 1;
    private String keys[];
    private int numberOfParameters = 17;
    public static final String DATABASE_NAME = "busesDb";
    public static final String TABLE_BUSES = "busesTable";

    private static final String keyID = "_id";

    public DBHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        keys = new String[numberOfParameters];
        for (int i = 0; i < numberOfParameters; i++) {
            keys[i] = "key" + i;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // уже писал, никогда не завязывайся на активити
        //TODO исправил
        db.execSQL("create table " + TABLE_BUSES + "(" + keyID + " integer primary key, " + keys[0] + " integer, "
                + keys[1] + " integer, " + keys[2] + " integer, " + keys[3] + " integer, " + keys[4] + " integer, "
                + keys[5] + " integer, " + keys[6] + " integer, " + keys[7] + " integer, "
                + keys[8] + " text, " + keys[9] + " text, " + keys[10] + " text, " + keys[11] + " text, "
                + keys[12] + " text, " + keys[13] + " text, " + keys[14] + " text, " + keys[15] + " text, "
                + keys[16] + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_BUSES);
        onCreate(db);
    }
}
