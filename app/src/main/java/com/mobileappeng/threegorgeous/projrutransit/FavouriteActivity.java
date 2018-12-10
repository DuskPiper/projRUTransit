package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.FavouriteAdapter;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.AppData;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;

import java.util.ArrayList;
import java.util.List;


public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView favouriteRouteView;
    private RecyclerView favouriteStopView;
    private FavouriteAdapter favouriteRouteAdapter;
    private FavouriteAdapter FavouriteStopAdapter;
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
        click_time = 0;

        busStopTagList = new ArrayList<>();
        busStopNameList = new ArrayList<>();
        busRouteTagList = new ArrayList<>();
        busRouteNameList = new ArrayList<>();
        initBusRoutes();
        onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                click_time += 1;
                if(click_time == 1) {
                    busRouteTagChoice = busRouteTagList.get(position);
                    busRouteNameChoice = busRouteNameList.get(position);
                    initBusStops(position);
                } else if(click_time == 2) {
                    busStopTagChoice = busStopTagList.get(position);
                    busStopNameChoice = busStopNameList.get(position);
                    // Save in shared preferences
                    SharedPreferences sharedPreferences = getSharedPreferences(AppData.SHAREDPREFERENCES_FAVOURITE_NAME, Context.MODE_PRIVATE); //私有数据
                    int count = sharedPreferences.getInt(AppData.DATA_QUANTITY, 0);
                    count ++;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppData.ROUTE_TAG + count, busRouteTagChoice);
                    editor.putString(AppData.STOP_TAG + count, busStopTagChoice);
                    editor.putString(AppData.ROUTE_NAME + count, busRouteNameChoice);
                    editor.putString(AppData.STOP_NAME + count, busStopNameChoice);
                    editor.putInt(AppData.DATA_QUANTITY, count);
                    editor.apply();

                    // startActivity(new Intent(FavouriteActivity.this, TodaySummaryActivity.class));
                    FavouriteActivity.this.setResult(RESULT_OK);
                    FavouriteActivity.this.finish();
                } else {
                    FavouriteActivity.this.setResult(RESULT_CANCELED);
                    FavouriteActivity.this.finish();
                }

                //Toast.makeText(getContext()," 点击了 "+position,Toast.LENGTH_SHORT).show();
                favouriteRouteView.setVisibility(RecyclerView.INVISIBLE);
                favouriteStopView.setVisibility(RecyclerView.VISIBLE);

            }
        };


        favouriteRouteView =(RecyclerView) findViewById(R.id.favourite_bus_recycleview);
        favouriteRouteAdapter = new FavouriteAdapter(this,busRouteNameList, onRecyclerviewItemClickListener);
        favouriteRouteView.setLayoutManager(new LinearLayoutManager(this));
        favouriteRouteView.setAdapter(favouriteRouteAdapter);


        favouriteStopView =(RecyclerView) findViewById(R.id.favourite_bustop_recycleview);
        FavouriteStopAdapter = new FavouriteAdapter(this,busStopNameList, onRecyclerviewItemClickListener);
        favouriteStopView.setLayoutManager(new LinearLayoutManager(this));
        favouriteStopView.setAdapter(FavouriteStopAdapter);
        favouriteStopView.setVisibility(RecyclerView.INVISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(favouriteRouteView.getVisibility() == RecyclerView.INVISIBLE)
            {
                favouriteRouteView.setVisibility(RecyclerView.VISIBLE);
                favouriteStopView.setVisibility(RecyclerView.INVISIBLE);
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
            FavouriteStopAdapter.notifyDataSetChanged();
        }
    }
}
