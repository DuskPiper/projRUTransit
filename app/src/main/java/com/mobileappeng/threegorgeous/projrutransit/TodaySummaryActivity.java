package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

public class TodaySummaryActivity extends AppCompatActivity {
    private NavigationView navigation;
    private DrawerLayout drawer;
    private SimpleAdapter bus_CursorAdapter;
    private DatabaseHelper DatabaseHelper;//declaration of dababasehelper
    public SQLiteDatabase dbW;
    public SQLiteDatabase dbR;
    private ListView bus_timetable;
    private RecyclerView hourly_weather;
    private GalleryAdapter hourly_weather_adapter;
    private List<Integer> mDatas;
    private List<String> titles;
    private List<String> wendu;
    private List<Map<String, Object>> bus_data = new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_summary);

        bus_timetable=(ListView) findViewById(R.id.bus_timetable);
        hourly_weather=(RecyclerView) findViewById(R.id.hourly_weather);


        bus_CursorAdapter=new SimpleAdapter(TodaySummaryActivity.this,
                get_bus_data(),
                R.layout.activity_today_summary_list_item,
                new String[]{"bus_name","bus_time"},
                new int[]{R.id.bus_name,R.id.bus_time}
                );
        bus_timetable.setAdapter(bus_CursorAdapter);

        initWeather_Datas();



        hourly_weather_adapter=new GalleryAdapter(this,mDatas,titles,wendu);
        LinearLayoutManager ms= new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourly_weather.setLayoutManager(ms);

        hourly_weather.setAdapter(hourly_weather_adapter);
        hourly_weather_adapter.notifyDataSetChanged();


        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
        }
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);



        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int size = navigation.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigation.getMenu().getItem(i).setChecked(false);
                }
                drawer.closeDrawers();
                menuItem.setChecked(true);
                Log.d("Navigation", "Checked item id = " + Integer.toString(menuItem.getItemId()));
                switch(menuItem.getItemId()) {
                    case R.id.navigation_map:
                        // Go to activity: maps
                        Log.d("Navigation", "Seleted Map");
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        return true;
                    case R.id.navigation_today:
                        // Do nothing, stay in current activity
                        Log.d("Navigation", "Seleted Today");
                        return true;
                    case R.id.navigation_settings:
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted Settings");
                        startActivity(new Intent(TodaySummaryActivity.this, SettingsActivity.class));
                        return true;
                    default:
                        Log.e("Navigation", "Selected item not recognized");
                        return false;
                }
            }
        });

    }

    /*public void refreshListView() {//refresh listviem
        Cursor mCursor = dbW.query("favourate_bus_timeable", null, null, null, null, null, null);
        bus_CursorAdapter.changeCursor(mCursor);
    }*/

    private void initWeather_Datas()
    {
        mDatas = new ArrayList<>(Arrays.asList(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher));
        titles = new ArrayList<>(Arrays.asList("1:00","23:00","1:00","23:00","1:00"));
        wendu = new ArrayList<>(Arrays.asList("0","10","20","30","24"));
    }
    private List<Map<String, Object>> get_bus_data()
    {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("bus_name", "bus"+i);
            item.put("bus_time", "time" + i);
            bus_data.add(item);
        }
        return bus_data;

    }
}
