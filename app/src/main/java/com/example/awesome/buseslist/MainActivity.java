package com.example.awesome.buseslist;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    ProgressDialog pd;
    String link;
    String response;
    String jsonString;
    String jsonName;
    JsonParsing jsonParsing;
    MainList mainList;
    FragmentTransaction fragmentTransaction;
    JSONObject userJson = null;
    JSONArray jArray;
    JSONObject array1[];
    static Item items[];
    static int numberOfParameters;
    DBHelper dbHelper;
    final String FILENAME = "json1.txt";
    public static String keys[];
    public static boolean refreshingON;
    Bundle bundle1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        dbHelper = new DBHelper(this);
        numberOfParameters = 17;
        jsonName = "data";
        keys = new String[numberOfParameters];
        for (int i = 0; i < numberOfParameters; i++) {
            keys[i] = "key" + i;
        }
        bundle1 = savedInstanceState;
        link = "https://gist.githubusercontent.com/pavel-zlotarenchuk/2eefe88a5fbf5519e2cb98d1062d7104/raw/a833d16ba01cb740d0e029e8698ee611ffbfa172/testtesk";

        if(!readFromBase()) {
            if (!readFromFile()) {
                Log.d("myLogs", "both unsuccessful");
                jsonTaskStart();
            }
        }
    }

    public void jsonTaskStart() {
        new JsonTask().execute(link);
    }

    public boolean readFromBase() {
        boolean success = false;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_BUSES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            success = true;
            int columnIndexes[] = new int[numberOfParameters];
            for (int i = 0; i < columnIndexes.length; i++) {
                columnIndexes[i] = cursor.getColumnIndex(keys[i]);
            }
            items = new Item[cursor.getCount()];
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
            Log.d("myLogs", "database read");
            setFragment();
        } else Log.d("myLogs", "base is empty");
        cursor.close();
        dbHelper.close();
        return success;
    }

    public boolean readFromFile() {
        boolean success;
        jsonString = readFile();
        if (jsonString == "") {
            success = false;
        } else {
            success = true;
        try {
            userJson = new JSONObject(jsonString);
            jArray = userJson.getJSONArray(jsonName);
            array1 = new JSONObject[jArray.length()];
            for (int i = 0; i < jArray.length(); i++) {
                array1[i] = jArray.getJSONObject(i);
            }
            items = new Item[array1.length];
            jsonParsing = new JsonParsing();
            for (int i = 0; i < array1.length; i++) {
                items[i] = jsonParsing.Parse(array1[i]);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("myLogs", "file read");
        setFragment();
        }
        return success;
    }

    public void setFragment() {
        if (refreshingON) {
            mainList = new MainList();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, mainList);
            Log.d("myLogs", "fragment replaced");
            fragmentTransaction.commit();
        } else if (bundle1 == null) {
            mainList = new MainList();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayout, mainList);
            Log.d("myLogs", "fragment added");
            fragmentTransaction.commit();
        }
    }

    public void clearDatabase() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int deleted = database.delete(DBHelper.TABLE_BUSES, null, null);
        Log.d("myLogs", "database: deleted " + deleted + " rows");
        dbHelper.close();
    }

    public void clearFile() {
        File f = new File("data/data/com.example.awesome.buseslist/files/" + FILENAME);
        Log.d("myLogs", "file deleted");
    }

    public void writeFile(String str) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, false));
            bufferedWriter.write(str);
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile() {
        String str = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput(FILENAME)));
            str = bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage(getResources().getString(R.string.please_wait));
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            Log.d("myLogs", "doInBackground start");
            HttpsURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                response = buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            writeFile(response);
            try {
                userJson = new JSONObject(response);
                jArray = userJson.getJSONArray("data");
                array1 = new JSONObject[jArray.length()];
                for (int i = 0; i < jArray.length(); i++) {
                    array1[i] = jArray.getJSONObject(i);
                }
                items = new Item[array1.length];
                jsonParsing = new JsonParsing();
                for (int i = 0; i < array1.length; i++) {
                    items[i] = jsonParsing.Parse(array1[i]);
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

                    database.insert(DBHelper.TABLE_BUSES, null, contentValues);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dbHelper.close();
            Log.d("myLogs", "doInBackground end");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            setFragment();
            refreshingON = false;
        }
    }

    public class Item {

        int id;
        ItemFrom itemFrom = new ItemFrom();
        ItemTo itemTo = new ItemTo();
        String from_date;
        String from_time;
        String from_info;
        String to_date;
        String to_time;
        String to_info;
        String info;
        int price;
        int bus_id;
        int reservation_count;
    }

    public class ItemFrom {
        int from_city_highlight;
        int from_city_id;
        String from_city_name;
    }

    public class ItemTo {
        int to_city_highlight;
        int to_city_id;
        String to_city_name;
    }

    public class JsonParsing {

        public Item Parse(JSONObject input) throws JSONException {
            JSONObject object1;

            Item itemX = new Item();

            itemX.id = input.getInt("id");
            itemX.from_date = input.getString("from_date");
            itemX.from_time = input.getString("from_time");
            itemX.from_info = input.getString("from_info");
            itemX.to_date = input.getString("to_date");
            itemX.to_time = input.getString("to_time");
            itemX.to_info = input.getString("to_info");
            itemX.info = input.getString("info");
            itemX.price = input.getInt("price");
            itemX.bus_id = input.getInt("bus_id");
            itemX.reservation_count = input.getInt("reservation_count");

            object1 = input.getJSONObject("from_city");
            itemX.itemFrom.from_city_highlight = object1.getInt("highlight");
            itemX.itemFrom.from_city_id = object1.getInt("id");
            itemX.itemFrom.from_city_name = object1.getString("name");
            object1 = input.getJSONObject("to_city");
            itemX.itemTo.to_city_highlight = object1.getInt("highlight");
            itemX.itemTo.to_city_id = object1.getInt("id");
            itemX.itemTo.to_city_name = object1.getString("name");


            return itemX;
        }
    }
}
