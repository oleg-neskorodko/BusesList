package com.example.awesome.buseslist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class JsonTask extends AsyncTask<String, String, String> {

    public interface AsyncTaskListener{

        void updateResult(Item[] items);

    }

    private AsyncTaskListener listener;

    public JsonTask(Context context)
    {
        listener = (AsyncTaskListener)context;
        context1 = context;
    }

    private Context context1;
    private ProgressDialog pd;
    private final String TAG = "myLogs";
    private FileManager fileManager = new FileManager();
    private Repository repository = new Repository();
    private JsonParsing jsonParsing = new JsonParsing();
    public static Item items[];


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context1);
        pd.setMessage(context1.getResources().getString(R.string.please_wait));
        pd.setCancelable(true);
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {

        Log.d(TAG, "doInBackground start");
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        String result = null;


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
            result = buffer.toString();
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

        items = jsonParsing.parseString(context1, result);
        repository.writeIntoBase(context1, items);
        fileManager.writeFile(result);
        Log.d(TAG, "doInBackground end");
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute start");
        if (pd.isShowing()) {
            pd.dismiss();
        }
        listener.updateResult(items);
    }
}
