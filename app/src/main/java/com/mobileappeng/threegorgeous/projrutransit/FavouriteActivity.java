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
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp.getContext;


public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView favourite_route_recycleview;
    private RecyclerView favourite_bus_stop_recycleview;
    private FavouriteAdapter favourite_route_adapter;
    private FavouriteAdapter favourite_stop_adapter;
    private List<String> bus_list;
    private List<String> stop_list;
    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener;
    private String bus_judge;
    private String stop_judge;
    private int click_time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_favourite);
        click_time=0;

        initBusdate();
        onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

                click_time+=1;
                if(click_time==1)
                {
                    bus_judge=bus_list.get(position);
                }
                if(click_time==2)
                {
                    stop_judge=stop_list.get(position);
                    SharedPreferences sharedPreferences = getSharedPreferences("Favourite_Stop", Context.MODE_PRIVATE); //私有数据
                    int count=sharedPreferences.getInt("Number",0);
                    count+=1;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Bus_Route"+count, bus_judge);
                    editor.putString("Bus_Stop"+count, stop_judge);
                    editor.putInt("Number",count);
                    editor.commit();

                    // startActivity(new Intent(FavouriteActivity.this, TodaySummaryActivity.class));
                    FavouriteActivity.this.setResult(RESULT_OK);
                    FavouriteActivity.this.finish();
                }
                initeBut_stop(position);
                //Toast.makeText(getContext()," 点击了 "+position,Toast.LENGTH_SHORT).show();
                favourite_route_recycleview.setVisibility(RecyclerView.INVISIBLE);
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
private void initBusdate(){
    bus_list = new ArrayList<>(Arrays.asList(
            "ee", "penn"));
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
    for (BusStop busStop : busStopsAtRoute) {
        stop_list.add(busStop.getTitle()); //// show this
    }
    if(click_time!=2) {
        favourite_stop_adapter.notifyDataSetChanged();
    }
}

}
