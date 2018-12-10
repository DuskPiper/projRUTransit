package com.mobileappeng.threegorgeous.projrutransit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import github.vatsal.easyweather.retrofit.models.Sys;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = "Manage Favourite";
    private NavigationView navigation;
    private DrawerLayout drawer;
    private ListView favouriteListView;
    private Button addFavouriteButton;
    private List<Map<String, String>> favouriteBusData = new ArrayList<Map<String, String>>();
    private Timer timer;
    private SimpleAdapter favouriteDataAdapter;


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

        // Initialize ListView
        forceRefreshFavouriteListView();
        /*favouriteDataAdapter = new SimpleAdapter(
                SettingsActivity.this,
                favouriteBusData,
                R.layout.activity_settings_listview_peritem,
                new String[]{AppData.BUS_INFO_DESCRIPTION, AppData.BUS_INFO_APPROACHING_TIME},
                new int[]{R.id.manage_favourite_name,R.id.manage_favourite_approaching_time}
        );
        favouriteListView.setAdapter(favouriteDataAdapter);*/

        // Start Timed Data Fetching
        timer = new Timer();
        TimerTask timedRecentBusRefresher = new TimerTask() {
            @Override
            public void run() {
                new SettingsActivity.RefreshApproachingBuses().execute();
            }
        };
        timer.schedule(timedRecentBusRefresher, 0, 10000);

        // Initialize Long-press Options
        favouriteListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Options");
                menu.add(0, 0, 0, "Notify upon arrival");
                menu.add(0, 1, 1, "Delete");
            }
        });

        // Set Listeners
        addFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SettingsActivity.this, FavouriteActivity.class), AppData.REQUEST_ADD_NEW_FAVOURITE);
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
                        // Go to map activity
                        Log.d("Navigation", "Seleted Map");
                        startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                        return true;
                    case R.id.navigation_today:
                        // Go to activity: Today
                        Log.d("Navigation", "Seleted Today");
                        startActivity(new Intent(SettingsActivity.this, TodaySummaryActivity.class));
                        return true;
                    case R.id.navigation_settings:
                        // Do nothing, stay in current activity
                        Log.d("Navigation", "Seleted Settings");

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
            SharedPreferences sp = getSharedPreferences(AppData.SHAREDPREFERENCES_FAVOURITE_NAME, Activity.MODE_PRIVATE);
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
                item.put(AppData.BUS_INFO_DESCRIPTION, routeName + " @ " + stopName);
                item.put(AppData.BUS_INFO_APPROACHING_TIME, findRecentBusesOfRouteAtStop(routeTag, stopTag));
                item.put(AppData.ROUTE_NAME, routeName);
                item.put(AppData.ROUTE_TAG, routeTag);
                item.put(AppData.STOP_NAME, stopName);
                item.put(AppData.STOP_TAG, stopTag);
                favouriteBusData.add(item);
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String resultText) {
            Log.d(TAG, "Update data complete");
            //favouriteDataAdapter.notifyDataSetChanged();
            forceRefreshFavouriteListView();
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
                return "No approaching buses";
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
                return "No approaching buses";
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
                return "No approaching buses";
            } else {
                StringBuilder sb = new StringBuilder("Coming in ");
                for (int targetTime : targetTimes) {
                    sb.append(targetTime).append(" / ");
                }
                sb.setLength(sb.length() - 3);
                String result = sb.toString();
                Log.d("Bus time", route + " @ " + stop + " : " + result);
                return result+ " minutes";
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
        // Refresh ListView data
        // favouriteDataAdapter.notifyDataSetChanged();
        forceRefreshFavouriteListView();
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

    // Setup Jobs For Long-press Menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int favouriteItemId = (int)info.id;
        Log.d(TAG, "Selected item: " + Integer.toString(favouriteItemId));
        switch (item.getItemId()) {
            case 0:
                // Notify
                Map<String, String> favouriteBus = favouriteBusData.get(favouriteItemId);
                Toast.makeText(
                        SettingsActivity.this,
                        "Tracking " + favouriteBus.get(AppData.ROUTE_NAME) + " @ " + favouriteBus.get(AppData.STOP_NAME),
                        Toast.LENGTH_LONG
                        ).show();
                Intent serviceIntent = new Intent(this,BusArrivalNotify.class);
                serviceIntent.putExtra(AppData.ROUTE_NAME, favouriteBus.get(AppData.ROUTE_NAME));
                serviceIntent.putExtra(AppData.ROUTE_TAG, favouriteBus.get(AppData.ROUTE_TAG));
                serviceIntent.putExtra(AppData.STOP_NAME, favouriteBus.get(AppData.STOP_NAME));
                serviceIntent.putExtra(AppData.STOP_TAG, favouriteBus.get(AppData.STOP_TAG));
                startService(serviceIntent);
                return true;
            case 1:
                // Delete
                favouriteBusData.remove(favouriteItemId);
                saveFavouriteBusData();
                //favouriteDataAdapter.notifyDataSetChanged();
                forceRefreshFavouriteListView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void saveFavouriteBusData() {
        SharedPreferences sp = getSharedPreferences(AppData.SHAREDPREFERENCES_FAVOURITE_NAME, Activity.MODE_PRIVATE);
        sp.edit().clear().commit(); // Clear all
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(AppData.DATA_QUANTITY, favouriteBusData.size());
        for (int i = 0; i < favouriteBusData.size(); i ++) {
            String index = Integer.toString(i + 1);
            Map<String, String> favouriteBus = favouriteBusData.get(i);
            editor.putString(AppData.ROUTE_NAME + index, favouriteBus.get(AppData.ROUTE_NAME));
            editor.putString(AppData.ROUTE_TAG + index, favouriteBus.get(AppData.ROUTE_TAG));
            editor.putString(AppData.STOP_NAME + index, favouriteBus.get(AppData.STOP_NAME));
            editor.putString(AppData.STOP_TAG + index, favouriteBus.get(AppData.STOP_TAG));
        }
        editor.commit();
    }

    private void forceRefreshFavouriteListView() {
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
}
