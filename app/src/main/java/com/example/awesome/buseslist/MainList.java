package com.example.awesome.buseslist;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainList extends ListFragment {

    // читай "Модификаторы доступа Java"
    //TODO fixed

    private Repository repository;
    private FileManager fileManager;
    private final String TAG = "myLogs";
    private Item items[];
    private SwipeRefreshLayout swipeRefresh;

    public interface MainListListener {
        void onSwipe();
    }

    private MainListListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MainListListener)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_list, null);

        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        repository = new Repository();
        fileManager = new FileManager();
        items = MainActivity.items;




        // ужасная практика так делать MainActivity.items, думай как не завязываться на MainActivity
        //TODO изменил


        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(items.length);
        Map<String, Object> map;
        for (int i = 0; i < items.length; i++) {
            map = new HashMap<String, Object>();
            map.put("id", items[i].id);
            map.put("fromCity", items[i].itemFrom.from_city_name);
            map.put("toCity", items[i].itemTo.to_city_name);
            map.put("depDate", items[i].from_date);
            map.put("depTime", items[i].from_time);
            map.put("depPlace", items[i].from_info);
            map.put("arrDate", items[i].to_date);
            map.put("arrTime", items[i].to_time);
            map.put("arrPlace", items[i].to_info);
            map.put("info", items[i].info);
            map.put("price", items[i].price);
            map.put("bus_id", items[i].bus_id);
            map.put("reservation", items[i].reservation_count);
            data.add(map);
        }
        String from[] = {"id", "fromCity", "toCity", "depDate", "depTime", "depPlace", "arrDate", "arrTime",
                "arrPlace", "info", "price", "bus_id", "reservation"};

        // называй вьюхи idTextView, coinImageView, или на крайняк idTv, coinIv
        //ты должен в коде понимать что за вюха
        //TODO fixed

        int to[] = {R.id.idTextView, R.id.fromCityTextView, R.id.toCityTextView, R.id.depTimeTextView2, R.id.depTimeTextView3,
                R.id.depPlaceTextView2, R.id.arrTimeTextView2, R.id.arrTimeTextView3, R.id.arrPlaceTextView2,
                R.id.routeTextView2, R.id.priceTextView2, R.id.busIDTextView2, R.id.reservationTextView2};

        final SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.item, from, to);
        setListAdapter(adapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);

                // ужасная практика так делать, ты жестко катишся на MainActivity, этот активити может быть null
                // читай interface java и передавай интерфейсы
                //TODO сделал через интерфейс
                repository.clearDatabase(getActivity());
                fileManager.clearFile();
                listener.onSwipe();
            }
        });
    }
}
