package com.example.awesome.buseslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Repository {

    private DBHelper dbHelper;
    private Item items[];
    private int numberOfParameters = 17;
    private String keys[];
    private final String TAG = "myLogs";


    public Item[] readFromBase(Context context) {
        keys = new String[numberOfParameters];
        for (int i = 0; i < numberOfParameters; i++) {
            keys[i] = "key" + i;
        }
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_BUSES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndexes[] = new int[numberOfParameters];
            for (int i = 0; i < columnIndexes.length; i++) {
                columnIndexes[i] = cursor.getColumnIndex(keys[i]);
            }
            items = new Item[cursor.getCount()];
            Log.d(TAG, "database contains " + cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                Item itemX = new Item();
                items[i] = itemX;
                items[i].id = cursor.getInt(columnIndexes[0]);
                items[i].itemFrom.from_city_highlight = cursor.getInt(columnIndexes[1]);
                items[i].itemFrom.from_city_id = cursor.getInt(columnIndexes[2]);
                items[i].itemTo.to_city_highlight = cursor.getInt(columnIndexes[3]);
                items[i].itemTo.to_city_id = cursor.getInt(columnIndexes[4]);
                items[i].price = cursor.getInt(columnIndexes[5]);
                items[i].bus_id = cursor.getInt(columnIndexes[6]);
                items[i].reservation_count = cursor.getInt(columnIndexes[7]);
                items[i].itemFrom.from_city_name = cursor.getString(columnIndexes[8]);
                items[i].itemTo.to_city_name = cursor.getString(columnIndexes[9]);
                items[i].from_date = cursor.getString(columnIndexes[10]);
                items[i].from_time = cursor.getString(columnIndexes[11]);
                items[i].from_info = cursor.getString(columnIndexes[12]);
                items[i].to_date = cursor.getString(columnIndexes[13]);
                items[i].to_time = cursor.getString(columnIndexes[14]);
                items[i].to_info = cursor.getString(columnIndexes[15]);
                items[i].info = cursor.getString(columnIndexes[16]);
                cursor.moveToNext();
            }
            Log.d(TAG, "database read");
        } else {
            Log.d(TAG, "base is empty");
            items = null;
        }
        cursor.close();
        dbHelper.close();
        return items;
    }


    public void clearDatabase(Context context) {
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int deleted = database.delete(DBHelper.TABLE_BUSES, null, null);
        Log.d(TAG, "database: deleted " + deleted + " rows");
        dbHelper.close();
    }

    public void writeIntoBase(Context context, Item[] input) {
        long inserted = 0;
        items = input;
        keys = new String[numberOfParameters];
        for (int i = 0; i < numberOfParameters; i++) {
            keys[i] = "key" + i;
        }
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < items.length; i++) {
            contentValues.put(keys[0], items[i].id);
            contentValues.put(keys[1], items[i].itemFrom.from_city_highlight);
            contentValues.put(keys[2], items[i].itemFrom.from_city_id);
            contentValues.put(keys[3], items[i].itemTo.to_city_highlight);
            contentValues.put(keys[4], items[i].itemTo.to_city_id);
            contentValues.put(keys[5], items[i].price);
            contentValues.put(keys[6], items[i].bus_id);
            contentValues.put(keys[7], items[i].reservation_count);
            contentValues.put(keys[8], items[i].itemFrom.from_city_name);
            contentValues.put(keys[9], items[i].itemTo.to_city_name);
            contentValues.put(keys[10], items[i].from_date);
            contentValues.put(keys[11], items[i].from_time);
            contentValues.put(keys[12], items[i].from_info);
            contentValues.put(keys[13], items[i].to_date);
            contentValues.put(keys[14], items[i].to_time);
            contentValues.put(keys[15], items[i].to_info);
            contentValues.put(keys[16], items[i].info);

            inserted = database.insert(DBHelper.TABLE_BUSES, null, contentValues);

            // ты даже не представляешь на сколько запись в БД будет быстрее, если делать ее
            // ОДНИМ большым запросом
            //TODO fixed
        }
        database.setTransactionSuccessful();
        database.endTransaction();

        Log.d(TAG, "database: inserted " + inserted + " rows");
        dbHelper.close();
    }

}

