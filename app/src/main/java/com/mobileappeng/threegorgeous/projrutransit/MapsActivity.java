package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.query.In;
import com.mobileappeng.threegorgeous.projrutransit.api.NextBusAPI;
import com.mobileappeng.threegorgeous.projrutransit.api.UpdateRoutesTask;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusData;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusPathSegment;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusVehicle;
import com.mobileappeng.threegorgeous.projrutransit.database.DatabaseHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private NavigationView navigation;
    private DrawerLayout drawer;
    private ArrayList<Marker> busStopMarkers;
    private ArrayList<Marker> activeBusMarkers;
    private BusPathSegment[] pathSegments;
    protected BusRoute route;
    private static DatabaseHelper databaseHelper;
    public ArrayList<SchoolBus> schoolBuses;
    private static BusData busData;
    private String showRoute;
    private ArrayList<String> activeRouteTags;
    private UpdateRoutesTask routeUpdater;
    private Timer timer;
    private long time;
    private Date date;
    private SimpleDateFormat format;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        time=System.currentTimeMillis();
        date=new Date(time);
        format=new SimpleDateFormat("E");
        // Fetch route data
        new UpdateRoutesTask().execute();

        // Initialize NavigationView and DrawerLayout
        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
            navigation.getMenu().getItem(i).setChecked(false);
        }
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        // Initialize UI
        activeBusMarkers = new ArrayList<>();
        busStopMarkers = new ArrayList<>();
        showRoute = "b";
        refreshActiveRouteTags();

        // Setup auto refresh
        timer = new Timer();
        TimerTask timedRouteRefresher = new TimerTask() {
            @Override
            public void run() {
                new UpdateRoutesTask().execute();
                refreshActiveRouteTags();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshShownRoute();
                    }
                });
            }
        };
        timer.schedule(timedRouteRefresher, 3000, 5000);
        
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
                        refreshShownRoute();
                        // Do nothing, stay in current activity
                        Log.d("Navigation", "Seleted Map");
                    return true;
                    case R.id.navigation_today:
                        // Go to activity: Today
                        Log.d("Navigation", "Seleted Today");
                        startActivity(new Intent(MapsActivity.this, TodaySummaryActivity.class));
                    return true;
                    case R.id.navigation_settings:
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted Settings");
                        startActivity(new Intent(MapsActivity.this, SettingsActivity.class));
                    return true;
                    case R.id.navigation_1:
                        showRoute="b";

                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            showRoute = "wknd1";
                            navigation.getMenu().findItem(R.id.navigation_1).setTitle("Weekend1");
                            navigation.getMenu().findItem(R.id.navigation_2).setTitle("Weekend2");
                            navigation.getMenu().findItem(R.id.navigation_3).setCheckable(false);
                            navigation.getMenu().findItem(R.id.navigation_4).setCheckable(false);
                            navigation.getMenu().findItem(R.id.navigation_5).setCheckable(false);
                            navigation.getMenu().findItem(R.id.navigation_6).setCheckable(false);
                            navigation.getMenu().findItem(R.id.navigation_7).setCheckable(false);
                            navigation.getMenu().findItem(R.id.navigation_8).setCheckable(false);
                            navigation.getMenu().findItem(R.id.navigation_9).setCheckable(false);
                            navigation.getMenu().findItem(R.id.navigation_10).setCheckable(false);
                            navigation.getMenu().findItem(R.id.navigation_3).setTitle("");
                            navigation.getMenu().findItem(R.id.navigation_4).setTitle("");
                            navigation.getMenu().findItem(R.id.navigation_5).setTitle("");
                            navigation.getMenu().findItem(R.id.navigation_6).setTitle("");
                            navigation.getMenu().findItem(R.id.navigation_7).setTitle("");
                            navigation.getMenu().findItem(R.id.navigation_8).setTitle("");
                            navigation.getMenu().findItem(R.id.navigation_9).setTitle("");
                            navigation.getMenu().findItem(R.id.navigation_10).setTitle("");
                        }
                        else
                        {
                            navigation.getMenu().findItem(R.id.navigation_1).setTitle("B");
                            navigation.getMenu().findItem(R.id.navigation_2).setTitle("EE");
                            navigation.getMenu().findItem(R.id.navigation_3).setTitle("F");
                            navigation.getMenu().findItem(R.id.navigation_4).setTitle("H");
                            navigation.getMenu().findItem(R.id.navigation_5).setTitle("LX");
                            navigation.getMenu().findItem(R.id.navigation_6).setTitle("REXB");
                            navigation.getMenu().findItem(R.id.navigation_7).setTitle("REXL");
                            navigation.getMenu().findItem(R.id.navigation_8).setTitle("RBHS");
                            navigation.getMenu().findItem(R.id.navigation_9).setTitle("A");
                            navigation.getMenu().findItem(R.id.navigation_10).setTitle("C");
                        }

                        route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get(showRoute);
                        drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted B Route");
                        return true;
                    case R.id.navigation_2:
                        showRoute="ee";

                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            showRoute = "wknd2";
                        }
                        route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get(showRoute);
                        drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted EE Route");
                        return true;
                    case R.id.navigation_3:
                        showRoute="f";
                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            navigation.getMenu().findItem(R.id.navigation_3).setCheckable(false);
                        }
                        else
                        {
                            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("f");
                            drawRoute();
                        // Go to activity: settings
                        Log.d("Navigation", "Seleted F Route");
                        }
                        return true;
                    case R.id.navigation_4:
                        showRoute="h";
                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            navigation.getMenu().findItem(R.id.navigation_4).setCheckable(false);
                        }
                        else {
                            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("h");
                            drawRoute();
                            // Go to activity: settings
                            Log.d("Navigation", "Seleted H Route");
                        }
                        return true;
                    case R.id.navigation_5:
                        showRoute="lx";
                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            navigation.getMenu().findItem(R.id.navigation_5).setCheckable(false);
                        }
                        else {
                            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("lx");
                            drawRoute();
                            // Go to activity: settings
                            Log.d("Navigation", "Seleted LX Route");
                        }
                        return true;
                    case R.id.navigation_6:
                        showRoute="rexb";
                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            navigation.getMenu().findItem(R.id.navigation_6).setCheckable(false);
                        }
                        else {
                            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("rexb");
                            drawRoute();
                            // Go to activity: settings
                            Log.d("Navigation", "Seleted REX B Route");
                        }
                        return true;
                    case R.id.navigation_7:
                        showRoute="rexl";
                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            navigation.getMenu().findItem(R.id.navigation_7).setCheckable(false);
                        }
                        else {
                            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("rexl");
                            drawRoute();
                            // Go to activity: settings
                            Log.d("Navigation", "Seleted REX L Route");
                        }
                        return true;


                    case R.id.navigation_8:
                        showRoute="rbhs";
                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            navigation.getMenu().findItem(R.id.navigation_8).setCheckable(false);
                        }
                        else {
                            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("rbhs");
                            drawRoute();
                            // Go to activity: settings
                            Log.d("Navigation", "Seleted RBHS Route");
                        }
                        return true;
                    case R.id.navigation_9:
                        showRoute="a";
                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            navigation.getMenu().findItem(R.id.navigation_9).setCheckable(false);
                        }
                        else {
                            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("a");
                            drawRoute();
                            // Go to activity: settings
                            Log.d("Navigation", "Seleted A Route");
                        }
                        return true;
                    case R.id.navigation_10:
                        showRoute="c";
                        if(format.format(date).equals("Sun") || format.format(date).equals("Sat"))
                        {
                            navigation.getMenu().findItem(R.id.navigation_10).setCheckable(false);
                        }
                        else {
                            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("c");
                            drawRoute();
                            // Go to activity: settings
                            Log.d("Navigation", "Seleted C Route");
                        }
                        return true;
                    default:
                        Log.e("Navigation", "Selected item not recognized");
                    return false;
                }
            }
        });

        navigation.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                new UpdateRoutesTask().execute();
                refreshActiveRouteTags();
                return true;
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng rutgersGate = new LatLng(40.498570, -74.445148);
        LatLng coreBuilding = new LatLng(40.5203528, -74.4604897);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coreBuilding, 14));

        refreshActiveRouteTags();
        refreshShownRoute();
    }

    private LatLng getLatLng(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    private void drawRoute() {
        mMap.clear();
        busStopMarkers.clear();
        activeBusMarkers.clear();
        // Draws the active bus locations
        new UpdateMarkers().execute();

        // Draws the bus route
        int polyLineColor = Color.BLUE;
        if (route != null) {
            pathSegments = route.getBusPathSegments();
        } else {
            pathSegments = new BusPathSegment[]{};
        }
        for (BusPathSegment pathSegment : pathSegments) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(polyLineColor);

            double[] latitudes = pathSegment.getLatitudes();
            double[] longitudes = pathSegment.getLongitudes();
            int size = latitudes.length;
            for (int j = 0; j < size; j++) {
                polylineOptions.add(getLatLng(latitudes[j], longitudes[j]));
            }

            mMap.addPolyline(polylineOptions);
        }
    }

    private void refreshActiveRouteTags() {
        Set<String> activeRouteTagSet = RUTransitApp.getBusData().getBusTagsToBusRoutes().keySet();
        activeRouteTags = new ArrayList<String>();
        activeRouteTags.addAll(activeRouteTagSet);
        Collections.sort(activeRouteTags);
    }

    private void refreshShownRoute() {
        route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get(showRoute);
        drawRoute();
    }

    private class UpdateMarkers extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            NextBusAPI.updateActiveRoutes();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            // Update active bus locations
            route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get(showRoute);
            Log.d("Update markers excecute", "Active routes:" + RUTransitApp.getBusData().getBusTagsToBusRoutes().keySet().toString());
            ArrayList<BusVehicle> activeBuses = route.getActiveBuses();
            if (activeBuses != null) {
                // Clear map of active bus markers
                for (int i = 0; i < activeBusMarkers.size(); i++) {
                    activeBusMarkers.get(i).remove();
                }
                activeBusMarkers.clear();

                // Add active bus markers
                Log.d("Map Marker", Integer.toString(activeBuses.size()) + " active buses");
                for (int i = 0; i < activeBuses.size(); i++) {
                    double[] location = activeBuses.get(i).getLocation();
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(getLatLng(location[0], location[1]))
                            .title("Vehicle ID: " + activeBuses.get(i).getVehicleId());
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus));
                    activeBusMarkers.add(mMap.addMarker(markerOptions));
                }
            }

            // Draw the bus stop markers
            BusStop[] busStops = route.getBusStops();
            if (busStops != null) {
                if (busStopMarkers.isEmpty()) { // Create the markers
                    Log.d("Map Marker", Integer.toString(busStops.length) + " stops");
                    for (BusStop stop : busStops) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(getLatLng(stop.getLatitude(), stop.getLongitude()))
                                .title(stop.getTitle());
                        if (!stop.isActive()) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        }
                        busStopMarkers.add(mMap.addMarker(markerOptions));
                    }
                } else { // Change the color if necessary
                    for (int i = 0; i < busStops.length; i++) {
                        if (!busStops[i].isActive()) {
                            busStopMarkers.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        } else {
                            busStopMarkers.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }


}
