package com.mobileappeng.threegorgeous.projrutransit;


import github.vatsal.easyweather.Helper.ForecastCallback;
import github.vatsal.easyweather.Helper.TempUnitConverter;
import github.vatsal.easyweather.Helper.WeatherCallback;
import github.vatsal.easyweather.WeatherMap;
import github.vatsal.easyweather.retrofit.models.ForecastResponseModel;
import github.vatsal.easyweather.retrofit.models.Weather;
import github.vatsal.easyweather.retrofit.models.WeatherResponseModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ccy.miuiweatherline.MiuiWeatherView;
import com.example.ccy.miuiweatherline.WeatherBean;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.DataBean;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.GalleryAdapter;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.RecyclerAdapter;
import com.mobileappeng.threegorgeous.projrutransit.api.NextBusAPI;
import com.mobileappeng.threegorgeous.projrutransit.api.UpdateRoutesTask;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.AppData;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusData;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStopTime;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusVehicle;
import com.squareup.picasso.Picasso;
//import com.mobileappeng.threegorgeous.projrutransit.gson.Weather;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TodaySummaryActivity extends AppCompatActivity {
    private NavigationView navigation;
    private DrawerLayout drawer;
    private SimpleAdapter bus_CursorAdapter;
    private ListView bus_timetable;
    private RecyclerView hourly_weather;
    private GalleryAdapter hourly_weather_adapter;
    private TextView city_text_view;
    private TextView all_the_day_textview;
    private List<String> mDatas;
    private List<String> titles;
    private List<String> wendu;
    private List<Map<String, Object>> favouriteBusData = new ArrayList<Map<String, Object>>();
    private MiuiWeatherView weatherView;
    private RecyclerView history_today;
    private String city = "Piscataway";
    private List<DataBean> dataBeanList;
    private DataBean dataBean;
    private RecyclerAdapter mAdapter;
    private ImageView all_the_day_weather_imageview;
    private TextView wendu_textview;
    private TextView shidu_textview;
    private String[] url_list;
    private TextView qiya_textview;
    private TextView fengsu_textview;
    private Button btn_click_plus_bus;
    private ScrollView scrollView;
    private Timer timer;
    private TimerTask timedRecentBusRefresher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_summary);
        url_list=new String[5];
        url_list[0]="";
        url_list[1]="";
        url_list[2]="";
        url_list[3]="";
        url_list[4]="";

        favouriteBusData = new ArrayList<Map<String, Object>>();
        new FindRecentBuses().execute();




        bus_timetable=(ListView) findViewById(R.id.bus_timetable);
        hourly_weather=(RecyclerView) findViewById(R.id.hourly_weather);
        scrollView=(ScrollView)findViewById(R.id.scrollView);
        btn_click_plus_bus=(Button) findViewById(R.id.btn_click_plus_bus);
        qiya_textview =(TextView) findViewById(R.id.qiya_textView);
        fengsu_textview =(TextView) findViewById(R.id.fengsu_textView);

        city_text_view =(TextView) findViewById(R.id.city_textView);
        city_text_view.setText("Piscataway");

        all_the_day_textview =(TextView) findViewById(R.id.all_the_day_weather_textview);
        all_the_day_textview.setText("Sunny");
        all_the_day_weather_imageview=(ImageView) findViewById(R.id.all_the_day_weather_imageview);
        wendu_textview=(TextView)findViewById(R.id.wendu_textView);
        shidu_textview=(TextView)findViewById(R.id.shidu_textView);

        history_today=(RecyclerView) findViewById(R.id.history_today);
        initData();
        history_today.setAdapter(mAdapter);

        loadWeather(city);
        weatherView = (MiuiWeatherView) findViewById(R.id.Miui_weather_view);
        List<WeatherBean> data = new ArrayList<>();
        WeatherBean b1 = new WeatherBean(WeatherBean.SUN,20,"05:00");
        WeatherBean b2 = new WeatherBean(WeatherBean.RAIN,22,"日出","05:30");
        data.add(b1);
        data.add(b2);
        weatherView.setData(data);


        bus_CursorAdapter=new SimpleAdapter(TodaySummaryActivity.this,
                /*loadFavouriteBusData(),*/
                favouriteBusData,
                R.layout.activity_today_summary_list_item,
                new String[]{"bus_name","bus_time"},
                new int[]{R.id.bus_name,R.id.bus_time}
                );
        bus_timetable.setAdapter(bus_CursorAdapter);

        bus_timetable.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences share=getSharedPreferences("Favourite_Stop",Activity.MODE_PRIVATE);
                int count=share.getInt("Number",0);
                for (int i = position+1; i <=count; i++) {
                    Map<String, Object> item = new HashMap<String, Object>();

                    String route=share.getString("Bus_Route"+i,"No_data");
                    String stop=share.getString("Bus_Stop"+i,"No_data");
                    int next=i+1;
                    String route_next=share.getString("Bus_Route"+next,"No_data");
                    String stop_next=share.getString("Bus_Stop"+next,"No_data");
                    if(route_next=="No_data")
                    {
                        route_next="";
                        stop_next="";
                    }
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("Bus_Route"+i, route_next);
                    editor.putString("Bus_Stop"+i, stop_next);
                    editor.putInt("Number",count);
                    editor.commit();

                }
                SharedPreferences.Editor editor = share.edit();
                editor.remove("Bus_Route"+count);
                editor.remove("Bus_Stop"+count);
                count-=1;

                editor.putInt("Number",count);
                editor.commit();

                return false;
            }
        });



        bus_timetable.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // 当手指触摸listview时，让父控件焦点,不能滚动
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        // 当手指松开时，让父控件重新获取焦点
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });



        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
            navigation.getMenu().getItem(i).setChecked(false);
        }
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        initWeatherData();
        hourly_weather_adapter=new GalleryAdapter(this,mDatas,titles,wendu);
        LinearLayoutManager ms= new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourly_weather.setLayoutManager(ms);
        hourly_weather.setAdapter(hourly_weather_adapter);
        hourly_weather.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        hourly_weather.setHasFixedSize(true);


        btn_click_plus_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                startActivityForResult(new Intent(TodaySummaryActivity.this, FavouriteActivity.class),1);
                //Toast.makeText(TodaySummaryActivity.this,"Button点击事件1",Toast.LENGTH_LONG).show();
            }
        });



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
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
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

    private void initWeatherData()
    {
        mDatas = new ArrayList<>(Arrays.asList(
                url_list[0],
        url_list[1],
        url_list[2],
        url_list[3],
        url_list[4]
                ));
        titles = new ArrayList<>(Arrays.asList("1:00","23:00","1:00","23:00","1:00"));
        wendu = new ArrayList<>(Arrays.asList("","","","",""));
    }

    private List<Map<String, Object>> loadFavouriteBusData()
    {
        SharedPreferences share=getSharedPreferences("Favourite_Stop",Activity.MODE_PRIVATE);
        int count = share.getInt("Number",0);
        for (int i = 1; i <= count; i++) {

            Map<String, Object> item = new HashMap<String, Object>();

            String route = share.getString("Bus_Route" + i,"No_data");
            String stop = share.getString("Bus_Stop" + i,"No_data");

            item.put("bus_name", route);
            item.put("bus_time", stop);
            favouriteBusData.add(item);
    }
        return favouriteBusData;
    }

    private void initData(){
        dataBeanList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
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
    
    private void loadWeather(String city){
        WeatherMap weatherMap = new WeatherMap(this, "27314559f4adc16163087a6e7314f6e4");
        weatherMap.getCityWeather(city, new WeatherCallback() {
            @Override
            public void success(WeatherResponseModel response) {
                Weather weather[] = response.getWeather();
                Double temperature = TempUnitConverter.convertToCelsius(response.getMain().getTemp());
                String location = response.getName();
                String humidity= response.getMain().getHumidity();
                String pressure = response.getMain().getPressure();
                String windSpeed = response.getWind().getSpeed();
                String des1= weather[0].getDescription();
                String link = weather[0].getIconLink();
                Picasso.with(getApplicationContext()).load(link).into(all_the_day_weather_imageview);
                System.out.println("Test getBase:"+response.getBase());
                System.out.println("Test getCod:"+response.getCod());
                System.out.println("Test getMessage:"+response.getSys().getMessage());
                System.out.println("Test getDt:"+response.getDt());
                all_the_day_textview.setText(des1);
                city_text_view.setText(location);
                wendu_textview.setText(""+Math.round(temperature)+"\u2103");
                shidu_textview.setText(humidity+"%");
                qiya_textview.setText(pressure);
                fengsu_textview.setText(windSpeed);
            }
            @Override
            public void failure(String message) {

            }
        });

        weatherMap.getCityForecast(city, new ForecastCallback() {
            @Override
            public void success(ForecastResponseModel response) {
                //ForecastResponseModel responseModel = response;
                Weather weather1[] = response.getList()[0].getWeather();

                String des1= weather1[0].getDescription();
                String Icon1=weather1[0].getIconLink();
                Weather weather2[] = response.getList()[8].getWeather();
                String des2= weather2[0].getDescription();
                String Icon2=weather2[0].getIconLink();
                Weather weather3[] = response.getList()[18].getWeather();
                String des3= weather3[0].getDescription();
                String Icon3=weather3[0].getIconLink();
                Weather weather4[] = response.getList()[28].getWeather();
                String des4= weather4[0].getDescription();
                String Icon4=weather4[0].getIconLink();
                Weather weather5[] = response.getList()[37].getWeather();
                String des5= weather5[0].getDescription();
                String Icon5=weather5[0].getIconLink();

                url_list[0]=Icon1;
                url_list[1]=Icon2;
                url_list[2]=Icon3;
                url_list[3]=Icon4;
                url_list[4]=Icon5;

                mDatas.clear();
                mDatas.add(url_list[0]);
                mDatas.add(url_list[1]);
                mDatas.add(url_list[2]);
                mDatas.add(url_list[3]);
                mDatas.add(url_list[4]);

                titles.clear();
                titles.add(des1);
                titles.add(des2);
                titles.add(des3);
                titles.add(des4);
                titles.add(des5);

                hourly_weather_adapter.notifyDataSetChanged();
                //  }
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        new FindRecentBuses().execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Setup auto refresh
        timer = new Timer();
        timedRecentBusRefresher = new TimerTask() {
            @Override
            public void run() {
                new FindRecentBuses().execute();
            }
        };
        timer.schedule(timedRecentBusRefresher, 10000, 10000);
    }

    private class FindRecentBuses extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sp = getSharedPreferences("Favourite_Stop",Activity.MODE_PRIVATE);
            int count = sp.getInt("Number",0);
            favouriteBusData = new ArrayList<Map<String, Object>>();
            for (int i = 1; i <= count; i++) {
                // Load from shared preference
                Map<String, Object> item = new HashMap<String, Object>();
                String routeTag = sp.getString(AppData.ROUTE_TAG + i,"");
                String stopTag = sp.getString(AppData.STOP_TAG + i,"");
                String routeName = sp.getString(AppData.ROUTE_NAME + i, "N/A");
                String stopName = sp.getString(AppData.STOP_NAME + i, "N/A");
                // Query for data and update to in-memory storage
                item.put("bus_name", routeName + " @ " + stopName);
                item.put("bus_time", findRecentBusesOfRouteAtStop(routeTag, stopTag));
                favouriteBusData.add(item);
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String resultText) {
            if (resultText.equals("OK")) {
                bus_CursorAdapter.notifyDataSetChanged();
            }
        }

        private String findRecentBusesOfRouteAtStop (String route, String stop) {
            // Init data
            ArrayList<String> routeBusVehicleIds = new ArrayList<>();
            ArrayList<BusStopTime> stopTimes = new ArrayList<>();
            List<BusStop> allBusStops = new ArrayList<>();
            ArrayList<Integer> targetTimes = new ArrayList<>();
            // Get all bus ids in queried route
            ArrayList<BusVehicle> routeBusVehicles =
                    RUTransitApp.getBusData().getBusTagsToBusRoutes().get(route).getActiveBuses();
            for (BusVehicle bus : routeBusVehicles) {
                routeBusVehicleIds.add(bus.getVehicleId());
            }
            // Get all BusStopTime at queried bus stop
            allBusStops = Arrays.asList(RUTransitApp.getBusData().getAllBusStops());
            for (BusRoute findRoute : BusData.getActiveRoutes()) {
                if (findRoute.getTag().equals(route)) {
                    allBusStops = Arrays.asList(findRoute.getBusStops());
                    NextBusAPI.saveBusStopTimes(findRoute);
                }
            }
            if (allBusStops == null) {
                return "";
            } else {
                for (BusStop checkStop : allBusStops) {
                    if (checkStop.getTag().equals(stop)) {
                        stopTimes = checkStop.getTimes();
                        break;
                    }
                }
            }
            // Filter BusStopTime and find out time for needed route
            if (stopTimes == null) {
                return "";
            } else {
                for (BusStopTime stopTime : stopTimes) {
                    if (routeBusVehicleIds.contains(stopTime.getVehicleId())) {
                        targetTimes.add(stopTime.getMinutes());
                    }
                }
            }
            // toString
            Collections.sort(targetTimes);
            if (targetTimes.size() == 0) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder("in ");
                for (int targetTime : targetTimes) {
                    sb.append(targetTime).append(" / ");
                }
                sb.setLength(sb.length() - 3);
                String result = sb.toString();
                Log.d("Bus time", route + " @ " + stop + " : " + result);
                return result;
            }
        }
    }
}
