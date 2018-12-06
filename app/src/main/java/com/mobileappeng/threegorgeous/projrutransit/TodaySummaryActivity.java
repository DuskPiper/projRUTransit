package com.mobileappeng.threegorgeous.projrutransit;


import github.vatsal.easyweather.Helper.ForecastCallback;
import github.vatsal.easyweather.Helper.TempUnitConverter;
import github.vatsal.easyweather.Helper.WeatherCallback;
import github.vatsal.easyweather.WeatherMap;
import github.vatsal.easyweather.retrofit.models.ForecastResponseModel;
import github.vatsal.easyweather.retrofit.models.Weather;
import github.vatsal.easyweather.retrofit.models.WeatherResponseModel;

import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ccy.miuiweatherline.MiuiWeatherView;
import com.example.ccy.miuiweatherline.WeatherBean;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.DataBean;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.GalleryAdapter;
import com.mobileappeng.threegorgeous.projrutransit.RecycleView_RU_Transit.RecyclerAdapter;
import com.squareup.picasso.Picasso;
//import com.mobileappeng.threegorgeous.projrutransit.gson.Weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Map<String, Object>> bus_data = new ArrayList<Map<String, Object>>();
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

        bus_timetable=(ListView) findViewById(R.id.bus_timetable);
        hourly_weather=(RecyclerView) findViewById(R.id.hourly_weather);

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
                get_bus_data(),
                R.layout.activity_today_summary_list_item,
                new String[]{"bus_name","bus_time"},
                new int[]{R.id.bus_name,R.id.bus_time}
                );
        bus_timetable.setAdapter(bus_CursorAdapter);





        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
            navigation.getMenu().getItem(i).setChecked(false);
        }
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        initWeather_Datas();
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
                Toast.makeText(TodaySummaryActivity.this,"Button点击事件1",Toast.LENGTH_LONG).show();
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



    private void initWeather_Datas()
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
}
