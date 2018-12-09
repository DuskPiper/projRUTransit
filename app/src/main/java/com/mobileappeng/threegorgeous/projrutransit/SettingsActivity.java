package com.mobileappeng.threegorgeous.projrutransit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mobileappeng.threegorgeous.projrutransit.api.NextBusAPI;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.AppData;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusData;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStopTime;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusVehicle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = "Manage Favourite";
    private NavigationView navigation;
    private DrawerLayout drawer;
    private ListView favouriteListView;
    private Button addFavouriteButton;
    private List<Map<String, Object>> favouriteBusData = new ArrayList<Map<String, Object>>();
    // private SimpleAdapter favouriteListViewAdapter;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.e("Settings ACT", "activity started");

        // Initialize NavigationView and DrawerLayout
        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
            navigation.getMenu().getItem(i).setChecked(false);
        }
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        favouriteListView = (ListView)findViewById(R.id.manage_favourite_listview);
        addFavouriteButton = (Button)findViewById(R.id.manage_favourite_add_button);

        // Start Timed Data Fetching
        timer = new Timer();
        TimerTask timedRecentBusRefresher = new TimerTask() {
            @Override
            public void run() {
                new SettingsActivity.RefreshApproachingBuses().execute();
            }
        };
        timer.schedule(timedRecentBusRefresher, 0, 10000);

        // Set Listeners
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
                        // Go to map activity
                        Log.d("Navigation", "Seleted Map");
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                        return true;
                    case R.id.navigation_today:
                        // Go to activity: Today
                        Log.d("Navigation", "Seleted Today");
                        startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                        return true;
                    case R.id.navigation_settings:
                        // Do nothing, stay in current activity
                        Log.d("Navigation", "Seleted Settings");


                        JSONObject dataJson= null;
                        try {
                            dataJson = new JSONObject("你的Json数据");
                            JSONObject response=dataJson.getJSONObject("response");
                            JSONArray data=response.getJSONArray("data");
                            JSONObject info=data.getJSONObject(0);
                            String province=info.getString("province");
                            String city=info.getString("city");
                            String district=info.getString("district");
                            String address=info.getString("address");
                            System.out.println(province+city+district+address);}
                            catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return true;
                    case R.id.navigation_1:
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted B Route");

                        return true;
                    case R.id.navigation_2:
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted EE Route");

                        return true;
                    case R.id.navigation_3:
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted F Route");

                        return true;
                    case R.id.navigation_4:
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted H Route");

                        return true;
                    case R.id.navigation_5:
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted LX Route");

                        return true;
                    case R.id.navigation_6:
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                        //route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        //drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted REX B Route");

                        return true;
                    case R.id.navigation_7:
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
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

    private class RefreshApproachingBuses extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "Update data started");
            SharedPreferences sp = getSharedPreferences("Favourite_Stop", Activity.MODE_PRIVATE);
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
                item.put(AppData.BUS_INFO_DESCRIPTION, routeName + " @ " + stopName);
                item.put(AppData.BUS_INFO_APPROACHING_TIME, findRecentBusesOfRouteAtStop(routeTag, stopTag));
                favouriteBusData.add(item);
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String resultText) {
            Log.d(TAG, "Update data complete");
            favouriteListView.setAdapter(
                    new SimpleAdapter(
                            SettingsActivity.this,
                            favouriteBusData,
                            R.layout.activity_settings_listview_peritem,
                            new String[]{AppData.BUS_INFO_DESCRIPTION, AppData.BUS_INFO_APPROACHING_TIME},
                            new int[]{R.id.manage_favourite_name,R.id.manage_favourite_approaching_time}
                    )
            );
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
                new SettingsActivity.RefreshApproachingBuses().execute();
            }
        };
        timer.schedule(timedRecentBusRefresher, 0, 10000);
    }
}
