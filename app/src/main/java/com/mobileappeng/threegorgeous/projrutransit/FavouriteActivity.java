package com.mobileappeng.threegorgeous.projrutransit;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.FavouriteAdapter;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp.getContext;


public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView favourite_route_recycleview;
    private RecyclerView favourite_bus_stop_recycleview;
    private FavouriteAdapter favourite_route_adapter;
    private FavouriteAdapter favourite_stop_adapter;
    private List<String> bus_list;
    private List<String> stop_list;
    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_favourite);

        initBusdate();
        onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                //这里的view就是我们点击的view  position就是点击的position
                initeBut_stop(position);
                Toast.makeText(getContext()," 点击了 "+position,Toast.LENGTH_SHORT).show();
                favourite_route_recycleview.setVisibility(RecyclerView.GONE);
                favourite_bus_stop_recycleview.setVisibility(RecyclerView.VISIBLE);

            }
        };


        favourite_route_recycleview =(RecyclerView) findViewById(R.id.favourite_bus_recycleview);
        favourite_route_adapter=new FavouriteAdapter(this,bus_list,onRecyclerviewItemClickListener);
        favourite_route_recycleview.setLayoutManager(new LinearLayoutManager(this));
        favourite_route_recycleview.setAdapter(favourite_route_adapter);


        favourite_bus_stop_recycleview =(RecyclerView) findViewById(R.id.favourite_bustop_recycleview);
        favourite_stop_adapter=new FavouriteAdapter(this,stop_list,onRecyclerviewItemClickListener);
        favourite_bus_stop_recycleview.setLayoutManager(new LinearLayoutManager(this));
        favourite_bus_stop_recycleview.setAdapter(favourite_stop_adapter);
        favourite_bus_stop_recycleview.setVisibility(RecyclerView.INVISIBLE);


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击完返回键，执行的动作
            Intent intent = new Intent(this, TodaySummaryActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
private void initBusdate(){



    bus_list = new ArrayList<>(Arrays.asList(
            "ee", "penn", "pennexpr", "a", "b", "c", "housing", "f", "h", "rexb", "mdntpenn", "lx", "rbhs", "rexl", "s", "kearney", "wknd2", "wknd1", "w1", "w2", "ccexp", "connect"
    ));
    bus_list.clear();
    ArrayList<BusRoute> busRoutes = RUTransitApp.getBusData().getBusRoutes();
    ArrayList<String> busRoutesList = new ArrayList<>();
    for (BusRoute busRoute : busRoutes) {
        bus_list.add(busRoute.getTitle());
        busRoutesList.add(busRoute.getTitle()); //// show this
    }
    stop_list = new ArrayList<>(Arrays.asList(
            "test6",
            "test7",
            "test8",
            "test9",
            "test10"
    ));
}

private void initeBut_stop(int position){
    ArrayList<BusRoute> busRoutes = RUTransitApp.getBusData().getBusRoutes();
    BusRoute selectedBusRoute = busRoutes.get(position);
    stop_list.clear();
    BusStop[] busStopsAtRoute = selectedBusRoute.getBusStops();
    ArrayList<String> busStopList = new ArrayList<>();
    for (BusStop busStop : busStopsAtRoute) {
        stop_list.add(busStop.getTitle()); //// show this
    }
    favourite_stop_adapter.notifyDataSetChanged();
}




}
