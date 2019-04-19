package com.example.awesome.buseslist;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements JsonTask.AsyncTaskListener, MainList.MainListListener {

    // читай "Модификаторы доступа Java"
    //TODO исправил



    private final String TAG = "myLogs";
    private MainList mainList;
    private FragmentTransaction fragmentTransaction;

    private boolean refreshingON;
    private String response;
    private FileManager fileManager;
    private Repository repository;
    private JsonParsing jsonParsing;
    public static Item items[];

    private Bundle bundle1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        fileManager = new FileManager();
        repository = new Repository();
        jsonParsing = new JsonParsing();



        // Откуда ты знаешь сколько будет параметров? А если -+1?
        //TODO сделал расчет кол-ва параметров в классе JsonParsing

        int numberOfParameters;
        numberOfParameters = 17;



        //TODO Читай про onSaveInstanceState, onRestoreInstanceState
        bundle1 = savedInstanceState;



        //TODO читай onResume
        if((items = repository.readFromBase(this)) != null) {
            setFragment();
        } else
        if ((response = fileManager.readFile(this)) != "") {
            items = jsonParsing.parseString(this, response);
            Log.d(TAG, "file read");
            setFragment();
        } else {
            Log.d(TAG, "both unsuccessful");
            jsonTaskStart();
        }
    }

    @Override
    public void onSwipe() {
        jsonTaskStart();
        refreshingON = true;
    }

    public void refreshFragment(Item[] items1) {

        items = items1;
        mainList = new MainList();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (refreshingON) {
            fragmentTransaction.replace(R.id.frameLayout, mainList);
            Log.d(TAG, "fragment replaced");
        } else {
            fragmentTransaction.add(R.id.frameLayout, mainList);
            Log.d(TAG, "fragment added");
        }
        fragmentTransaction.commit();
        refreshingON = false;
    }

    @Override
    public void updateResult(Item[] items) {
        refreshFragment(items);
    }

    public void jsonTaskStart() {

        // это должно быть вынесено в константы, в идиале специальный класс ApiManager
        //TODO сделано
        new JsonTask(this).execute(ApiManager.link1);
    }

    // это в отдельный класс Repository например, как и всю остальную работу с БД
    // это в отдельный класс FileManager
    // Отдельный класс, подумай как вынести но что б асинхронность сохранилась
    //TODO разнес в отдельные классы


    public void setFragment() {

        if (bundle1 == null) {
            // если уже делаешь fragmentTransaction = getFragmentManager().beginTransaction();
            // то чего не делаешь его выше, перед if?
            //TODO сейчас по-другому

            mainList = new MainList();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayout, mainList);
            Log.d(TAG, "fragment added");
            fragmentTransaction.commit();
        }
    }
}
