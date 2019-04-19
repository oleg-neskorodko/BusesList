package com.example.awesome.buseslist;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {
    private final String TAG = "myLogs";

    private final String FILENAME = "json1.txt";

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
        Log.d(TAG, "file written");
    }


    public String readFile(Context context) {
        Log.d(TAG, "readFile starts");
        String str = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.openFileInput(FILENAME)));
            str = bufferedReader.readLine();
            Log.d(TAG, "file read 2 = " + str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException");
        }
        return str;
    }

    public void clearFile() {
        File f = new File("data/data/com.example.awesome.buseslist/files/" + FILENAME);
        Log.d(TAG, "file deleted");
    }
}
