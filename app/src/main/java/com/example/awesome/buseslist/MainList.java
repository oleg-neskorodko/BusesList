package com.example.awesome.buseslist;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainList extends ListFragment {


    SwipeRefreshLayout swipeRefresh;

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

        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(MainActivity.items.length);
        Map<String, Object> map;
        for (int i = 0; i < MainActivity.items.length; i++) {
            map = new HashMap<String, Object>();
            map.put("id", MainActivity.items[i].id);
            map.put("fromCity", MainActivity.items[i].itemFrom.from_city_name);
            map.put("toCity", MainActivity.items[i].itemTo.to_city_name);
            map.put("depDate", MainActivity.items[i].from_date);
            map.put("depTime", MainActivity.items[i].from_time);
            map.put("depPlace", MainActivity.items[i].from_info);
            map.put("arrDate", MainActivity.items[i].to_date);
            map.put("arrTime", MainActivity.items[i].to_time);
            map.put("arrPlace", MainActivity.items[i].to_info);
            map.put("info", MainActivity.items[i].info);
            map.put("price", MainActivity.items[i].price);
            map.put("bus_id", MainActivity.items[i].bus_id);
            map.put("reservation", MainActivity.items[i].reservation_count);
            data.add(map);
        }
        String from[] = {"id", "fromCity", "toCity", "depDate", "depTime", "depPlace", "arrDate", "arrTime",
                "arrPlace", "info", "price", "bus_id", "reservation"};
        int to[] = {R.id.id, R.id.fromCity, R.id.toCity, R.id.depTime2, R.id.depTime3, R.id.depPlace2, R.id.arrTime2, R.id.arrTime3,
                R.id.arrPlace2, R.id.route2, R.id.price2, R.id.busID2, R.id.reservation2};

        final SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.item, from, to);
        setListAdapter(adapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                MainActivity.refreshingON = true;
                ((MainActivity) getActivity()).clearDatabase();
                ((MainActivity) getActivity()).clearFile();
                ((MainActivity) getActivity()).jsonTaskStart();
            }
        });
    }
}
