package com.example.awesome.buseslist;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParsing {

    String jsonName;
    JSONArray jArray;
    JSONObject array1[];
    Item items1[];
    JSONObject inputJson = null;
    private final String TAG = "myLogs";
    public static int numberOfParameters;



    public Item parseArray(JSONObject input) throws JSONException {
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

    public Item[] parseString(Context context, String input) {
        jsonName = context.getResources().getString(R.string.json_name);


        try {
            inputJson = new JSONObject(input);
            jArray = inputJson.getJSONArray(jsonName);
            array1 = new JSONObject[jArray.length()];
            for (int i = 0; i < jArray.length(); i++) {
                array1[i] = jArray.getJSONObject(i);
            }


            items1 = new Item[array1.length];
            for (int i = 0; i < array1.length; i++) {
                items1[i] = parseArray(array1[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //TODO определение количества параметров_______________________


        int exteriorParams, interiorParams, deduction;
        interiorParams = 0;
        deduction = 0;
        exteriorParams = array1[0].length();
        JSONArray names = array1[0].names();

        for (int i = 0; i < names.length(); i++) {
            try {
                interiorParams += (array1[0].getJSONObject(names.getString(i)).length());
                deduction++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        numberOfParameters = exteriorParams + interiorParams - deduction;


        //TODO __________________________________________________________________



            Log.d(TAG, "parseString");
            return items1;
        }
    }
