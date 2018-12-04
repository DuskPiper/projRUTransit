package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ccy.miuiweatherline.MiuiWeatherView;
import com.example.ccy.miuiweatherline.WeatherBean;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.DataBean;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.GalleryAdapter;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TextView city_text_view;
    private TextView all_the_day_textview;
    private List<Integer> mDatas;
    private List<String> titles;
    private List<String> wendu;
    private List<Map<String, Object>> bus_data = new ArrayList<Map<String, Object>>();
    private MiuiWeatherView weatherView;
    private RecyclerView history_today;

    private List<DataBean> dataBeanList;
    private DataBean dataBean;
    private RecyclerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_summary);

        bus_timetable=(ListView) findViewById(R.id.bus_timetable);
        hourly_weather=(RecyclerView) findViewById(R.id.hourly_weather);

        city_text_view =(TextView) findViewById(R.id.city_textView);
        city_text_view.setText("Piscataway");

        all_the_day_textview =(TextView) findViewById(R.id.all_the_day_weather_textview);
        all_the_day_textview.setText("Sunny");

        history_today=(RecyclerView) findViewById(R.id.history_today);
        initData();
        history_today.setAdapter(mAdapter);


        weatherView = (MiuiWeatherView) findViewById(R.id.Miui_weather_view);
        List<WeatherBean> data = new ArrayList<>();
        WeatherBean b1 = new WeatherBean(WeatherBean.SUN,20,"05:00");
        WeatherBean b2 = new WeatherBean(WeatherBean.RAIN,22,"日出","05:30");
        data.add(b1);
        data.add(b2);
        weatherView.setData(data);




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
        hourly_weather.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        hourly_weather_adapter.notifyDataSetChanged();


        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
            navigation.getMenu().getItem(i).setChecked(false);
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
                        startActivity(new Intent(TodaySummaryActivity.this, TodaySummaryActivity.class));
                        return true;
                    case R.id.navigation_1:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted B Route");

                        return true;
                    case R.id.navigation_2:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted EE Route");

                        return true;
                    case R.id.navigation_3:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted F Route");

                        return true;
                    case R.id.navigation_4:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted H Route");

                        return true;
                    case R.id.navigation_5:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted LX Route");

                        return true;
                    case R.id.navigation_6:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted REX B Route");

                        return true;
                    case R.id.navigation_7:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted REX L Route");
                        return true;
                    case R.id.navigation_8:
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted REX L Route");

                        return true;
                    default:
                        Log.e("Navigation", "Selected item not recognized");
                        return false;
                }
            }
        });

    }



    private void initWeather_Datas()
    {
        mDatas = new ArrayList<>(Arrays.asList(
                R.drawable.sunny,
                R.drawable.sunandcloud,
                R.drawable.cloudy,
                R.drawable.bigsnowy,
                R.drawable.bigrain
                ));
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

    private void initData(){
        dataBeanList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dataBean = new DataBean();
            dataBean.setID(i+"");
            dataBean.setType(0);
            dataBean.setParentLeftTxt("父--"+i);
            dataBean.setParentRightTxt("父内容--"+i);
            dataBean.setChildLeftTxt("子--"+i);
            dataBean.setChildRightTxt("子内容--"+i);
            dataBean.setChildBean(dataBean);
            dataBeanList.add(dataBean);
        }
        setData();
    }

    private void setData(){
        history_today.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter(this,dataBeanList);
        history_today.setAdapter(mAdapter);
        //滚动监听
        mAdapter.setOnScrollListener(new RecyclerAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                history_today.scrollToPosition(pos);
            }
        });
    }

}
