package com.mobileappeng.threegorgeous.projrutransit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.FavouriteAdapter;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.AppData;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;

import java.util.ArrayList;
import java.util.List;


public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView favourite_route_recycleview;
    private RecyclerView favourite_bus_stop_recycleview;
    private FavouriteAdapter favourite_route_adapter;
    private FavouriteAdapter favourite_stop_adapter;
    private List<String> busRouteTagList;
    private List<String> busRouteNameList;
    private List<String> busStopTagList;
    private List<String> busStopNameList;
    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener;
    private String busRouteTagChoice = "";
    private String busRouteNameChoice = "";
    private String busStopTagChoice = "";
    private String busStopNameChoice = "";
    private int click_time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_favourite);
        click_time=0;

        busStopTagList = new ArrayList<>();
        busStopNameList = new ArrayList<>();
        busRouteTagList = new ArrayList<>();
        busRouteNameList = new ArrayList<>();
        initBusRoutes();
        onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

                click_time+=1;
                if(click_time==1)
                {
                    busRouteTagChoice = busRouteTagList.get(position);
                    busRouteNameChoice = busRouteNameList.get(position);
                }
                if(click_time==2)
                {
                    busStopTagChoice = busStopTagList.get(position);
                    busStopNameChoice = busStopNameList.get(position);
                    SharedPreferences sharedPreferences = getSharedPreferences("Favourite_Stop", Context.MODE_PRIVATE); //私有数据
                    int count=sharedPreferences.getInt("Number",0);
                    count+=1;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppData.ROUTE_TAG + count, busRouteTagChoice);
                    editor.putString(AppData.STOP_TAG + count, busStopTagChoice);
                    editor.putString(AppData.ROUTE_NAME + count, busRouteNameChoice);
                    editor.putString(AppData.STOP_NAME + count, busStopNameChoice);
                    editor.putInt("Number",count);
                    editor.commit();

                    // startActivity(new Intent(FavouriteActivity.this, TodaySummaryActivity.class));
                    FavouriteActivity.this.setResult(RESULT_OK);
                    FavouriteActivity.this.finish();
                }
                initBusStops(position);
                //Toast.makeText(getContext()," 点击了 "+position,Toast.LENGTH_SHORT).show();
                favourite_route_recycleview.setVisibility(RecyclerView.INVISIBLE);
                favourite_bus_stop_recycleview.setVisibility(RecyclerView.VISIBLE);

            }
        };


        favourite_route_recycleview =(RecyclerView) findViewById(R.id.favourite_bus_recycleview);
        favourite_route_adapter=new FavouriteAdapter(this,busRouteNameList,onRecyclerviewItemClickListener);
        favourite_route_recycleview.setLayoutManager(new LinearLayoutManager(this));
        favourite_route_recycleview.setAdapter(favourite_route_adapter);


        favourite_bus_stop_recycleview =(RecyclerView) findViewById(R.id.favourite_bustop_recycleview);
        favourite_stop_adapter=new FavouriteAdapter(this,busStopNameList,onRecyclerviewItemClickListener);
        favourite_bus_stop_recycleview.setLayoutManager(new LinearLayoutManager(this));
        favourite_bus_stop_recycleview.setAdapter(favourite_stop_adapter);
        favourite_bus_stop_recycleview.setVisibility(RecyclerView.INVISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(favourite_route_recycleview.getVisibility()==RecyclerView.INVISIBLE)
            {
                favourite_route_recycleview.setVisibility(RecyclerView.VISIBLE);
                favourite_bus_stop_recycleview.setVisibility(RecyclerView.INVISIBLE);
                click_time=0;
            }
            else{
            /*Intent intent = new Intent(this, TodaySummaryActivity.class);
            startActivity(intent);
            finish();*/
            FavouriteActivity.this.setResult(RESULT_CANCELED);
            FavouriteActivity.this.finish();
            }
        }
        return true;
    }
    private void initBusRoutes(){
        busRouteTagList.clear();
        ArrayList<BusRoute> busRoutes = RUTransitApp.getBusData().getBusRoutes();
        for (BusRoute busRoute : busRoutes) {
            busRouteTagList.add(busRoute.getTag());
            busRouteNameList.add(busRoute.getTitle()); // Displayed
        }
    }
    
    private void initBusStops(int position){
        ArrayList<BusRoute> busRoutes = RUTransitApp.getBusData().getBusRoutes();
        BusRoute selectedBusRoute = busRoutes.get(position);
        busStopTagList.clear();
        busStopNameList.clear();
        BusStop[] busStopsAtRoute = selectedBusRoute.getBusStops();
        for (BusStop busStop : busStopsAtRoute) {
            busStopTagList.add(busStop.getTag());
            busStopNameList.add(busStop.getTitle()); // Displayed
        }
        if(click_time!=2) {
            favourite_stop_adapter.notifyDataSetChanged();
        }
    }
}
