package com.mobileappeng.threegorgeous.projrutransit;


import github.vatsal.easyweather.Helper.ForecastCallback;
import github.vatsal.easyweather.Helper.TempUnitConverter;
import github.vatsal.easyweather.Helper.WeatherCallback;
import github.vatsal.easyweather.WeatherMap;
import github.vatsal.easyweather.retrofit.models.ForecastResponseModel;
import github.vatsal.easyweather.retrofit.models.Weather;
import github.vatsal.easyweather.retrofit.models.WeatherResponseModel;
import retrofit2.http.HEAD;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Vibrator;

public class TodaySummaryActivity extends AppCompatActivity {
    private NavigationView navigation;
    private DrawerLayout drawer;
    private SimpleAdapter busCursorAdapter;
    private ListView busTimeTable;
    private RecyclerView hourlyWeather;
    private GalleryAdapter hourlyWeatherAdapter;
    private TextView cityTextView;
    private TextView allDayTextView;
    private List<String> weatherData;
    private List<String> titles;
    private List<String> wendu;
    private List<Map<String, String>> favouriteBusData;
    private RecyclerView todayInHistoryView;
    private String city = "Piscataway";
    private List<DataBean> dataBeanList;
    private DataBean dataBean;
    private RecyclerAdapter mAdapter;
    private ImageView allDatWeatherView;
    private TextView temperatureView;
    private TextView humidityView;
    private String[] urlList;
    private TextView atmPressureView;
    private TextView windSpeedView;
    private Button plusButton;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_summary);

        urlList = new String[5];
        urlList[0] = "";
        urlList[1] = "";
        urlList[2] = "";
        urlList[3] = "";
        urlList[4] = "";

        // Load Bus Data
        favouriteBusData = new ArrayList<Map<String, String>>();
        new FindRecentBuses().execute();

        // Initialize Widgets
        busTimeTable = (ListView) findViewById(R.id.bus_timetable);
        hourlyWeather = (RecyclerView) findViewById(R.id.hourly_weather);
        plusButton = (Button) findViewById(R.id.btn_click_plus_bus);
        plusButton.setClickable(false);
        plusButton.setVisibility(View.INVISIBLE);
        atmPressureView = (TextView) findViewById(R.id.qiya_textView);
        windSpeedView = (TextView) findViewById(R.id.fengsu_textView);
        cityTextView = (TextView) findViewById(R.id.city_textView);
        cityTextView.setText("Piscataway");
        allDayTextView = (TextView) findViewById(R.id.all_the_day_weather_textview);
        allDayTextView.setText("Sunny");
        allDatWeatherView = (ImageView) findViewById(R.id.all_the_day_weather_imageview);
        temperatureView = (TextView)findViewById(R.id.wendu_textView);
        humidityView = (TextView)findViewById(R.id.shidu_textView);
        todayInHistoryView = (RecyclerView) findViewById(R.id.history_today);
        navigation = (NavigationView)findViewById(R.id.navigation);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);


        // Initialize Adapters
        todayInHistoryView.setAdapter(mAdapter);

        busCursorAdapter = new SimpleAdapter(TodaySummaryActivity.this,
                favouriteBusData,
                R.layout.activity_today_summary_list_item,
                new String[]{"bus_name", "bus_time"},
                new int[]{R.id.bus_name,R.id.bus_time}
        );
        busTimeTable.setAdapter(busCursorAdapter);

        // Initialize Joke and Weather
        initJokeDate();
        loadWeather(city);
        initWeatherData();


        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
            navigation.getMenu().getItem(i).setChecked(false);
        }

        hourlyWeatherAdapter = new GalleryAdapter(this,weatherData,titles,wendu);
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourlyWeather.setLayoutManager(ms);
        hourlyWeather.setAdapter(hourlyWeatherAdapter);
        hourlyWeather.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        hourlyWeather.setHasFixedSize(true);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(TodaySummaryActivity.this, FavouriteActivity.class),1);
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
                        startActivity(new Intent(TodaySummaryActivity.this, SettingsActivity.class));
                        return true;
                    case R.id.navigation_1:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        Log.d("Navigation", "Seleted B Route");
                        return true;
                    case R.id.navigation_2:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        Log.d("Navigation", "Seleted EE Route");
                        return true;
                    case R.id.navigation_3:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        Log.d("Navigation", "Seleted F Route");
                        return true;
                    case R.id.navigation_4:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        Log.d("Navigation", "Seleted H Route");
                        return true;
                    case R.id.navigation_5:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        Log.d("Navigation", "Seleted LX Route");
                        return true;
                    case R.id.navigation_6:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        Log.d("Navigation", "Seleted REX B Route");
                        return true;
                    case R.id.navigation_7:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        Log.d("Navigation", "Seleted REX L Route");
                        return true;
                    case R.id.navigation_8:
                        startActivity(new Intent(TodaySummaryActivity.this, MapsActivity.class));
                        Log.d("Navigation", "Seleted REX L Route");
                        return true;
                    default:
                        Log.e("Navigation", "Selected item not recognized");
                        return false;
                }
            }
        });
    }

    private void startNotifyService(String routeName, String routeTag, String stopName, String stopTag) {
        Intent serviceIntent = new Intent(this,BusArrivalNotify.class);
        serviceIntent.putExtra(AppData.ROUTE_NAME, routeName);
        serviceIntent.putExtra(AppData.ROUTE_TAG, routeTag);
        serviceIntent.putExtra(AppData.STOP_NAME, stopName);
        serviceIntent.putExtra(AppData.STOP_TAG, stopTag);
        startService(serviceIntent);
    }

    private void initWeatherData() {
        weatherData = new ArrayList<>(Arrays.asList(
                urlList[0],
        urlList[1],
        urlList[2],
        urlList[3],
        urlList[4]
                ));
        titles = new ArrayList<>(Arrays.asList("1:00","23:00","1:00","23:00","1:00"));
        wendu = new ArrayList<>(Arrays.asList("","","","",""));
    }

    private void setJokeData(){
        todayInHistoryView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter(this,dataBeanList);
        todayInHistoryView.setAdapter(mAdapter);
        mAdapter.setOnScrollListener(new RecyclerAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                todayInHistoryView.scrollToPosition(pos);
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
                Picasso.with(getApplicationContext()).load(link).into(allDatWeatherView);
                allDayTextView.setText(des1);
                cityTextView.setText(location);
                temperatureView.setText(""+Math.round(temperature)+"\u2103");
                humidityView.setText(humidity+"%");
                atmPressureView.setText(pressure);
                windSpeedView.setText(windSpeed);
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

                String des1 = weather1[0].getDescription();
                String Icon1 = weather1[0].getIconLink();
                Weather weather2[] = response.getList()[8].getWeather();
                String des2 = weather2[0].getDescription();
                String Icon2 = weather2[0].getIconLink();
                Weather weather3[] = response.getList()[18].getWeather();
                String des3 = weather3[0].getDescription();
                String Icon3 = weather3[0].getIconLink();
                Weather weather4[] = response.getList()[28].getWeather();
                String des4 = weather4[0].getDescription();
                String Icon4 = weather4[0].getIconLink();
                Weather weather5[] = response.getList()[37].getWeather();
                String des5 = weather5[0].getDescription();
                String Icon5 = weather5[0].getIconLink();

                urlList[0] = Icon1;
                urlList[1] = Icon2;
                urlList[2] = Icon3;
                urlList[3] = Icon4;
                urlList[4] = Icon5;

                weatherData.clear();
                weatherData.add(urlList[0]);
                weatherData.add(urlList[1]);
                weatherData.add(urlList[2]);
                weatherData.add(urlList[3]);
                weatherData.add(urlList[4]);

                titles.clear();
                titles.add(des1);
                titles.add(des2);
                titles.add(des3);
                titles.add(des4);
                titles.add(des5);

                hourlyWeatherAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(String message) { }
        });
    }

    private void initJokeDate(){
        dataBeanList = new ArrayList<>();
        {
            dataBean = new DataBean();
            dataBean.setType(0);
            dataBean.setID("0");
            dataBean.setParentLeftTxt("What do you call dangerous precipitation?");
            dataBean.setChildLeftTxt("A rain of terror");
            dataBean.setChildBean(dataBean);
            dataBeanList.add(dataBean);
        }
        {
            dataBean = new DataBean();
            dataBean.setType(0);
            dataBean.setID("1");
            dataBean.setParentLeftTxt("What do you call a month's worth of rain?");
            dataBean.setChildLeftTxt("England");
            dataBean.setChildBean(dataBean);
            dataBeanList.add(dataBean);
        }
        {
            dataBean = new DataBean();
            dataBean.setType(0);
            dataBean.setID("2");
            dataBean.setParentLeftTxt("What do snowmen call their offspring?");
            dataBean.setChildLeftTxt("Chill-dren");
            dataBean.setChildBean(dataBean);
            dataBeanList.add(dataBean);
        }
        {
            dataBean = new DataBean();
            dataBean.setType(0);
            dataBean.setID("3");
            dataBean.setParentLeftTxt("How does a snowman get to work?");
            dataBean.setChildLeftTxt("By icicle");
            dataBean.setChildBean(dataBean);
            dataBeanList.add(dataBean);
        }
        setJokeData();
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
        TimerTask timedRecentBusRefresher = new TimerTask() {
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
            SharedPreferences sp = getSharedPreferences(AppData.SHAREDPREFERENCES_FAVOURITE_NAME,Activity.MODE_PRIVATE);
            int count = sp.getInt(AppData.DATA_QUANTITY,0);
            favouriteBusData = new ArrayList<Map<String, String>>();
            for (int i = 1; i <= count; i++) {
                // Load from shared preference
                Map<String, String> item = new HashMap<String, String>();
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
            busCursorAdapter = new SimpleAdapter(TodaySummaryActivity.this,
                    favouriteBusData,
                    R.layout.activity_today_summary_list_item,
                    new String[]{"bus_name", "bus_time"},
                    new int[]{R.id.bus_name, R.id.bus_time}
            );
            busTimeTable.setAdapter(busCursorAdapter);
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
