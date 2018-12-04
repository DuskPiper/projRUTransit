package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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
import com.mobileappeng.threegorgeous.projrutransit.api.NextBusAPI;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusData;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusPathSegment;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusVehicle;
import com.mobileappeng.threegorgeous.projrutransit.database.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private NavigationView navigation;
    private DrawerLayout drawer;
    private ArrayList<Marker> busStopMarkers;
    private ArrayList<Marker> activeBusMarkers;
    private BusPathSegment[] pathSegments;
    private BusRoute route;
    private static DatabaseHelper databaseHelper;
    public ArrayList<SchoolBus> schoolBuses;
    private static Context mContext;
    private static BusData busData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mContext = getApplicationContext();

        // Initialize NavigationView and DrawerLayout
        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
            navigation.getMenu().getItem(i).setChecked(false);
        }
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        
        // Initialize route
        new UpdateRoutesTask().execute();
        // route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
        activeBusMarkers = new ArrayList<>();
        busStopMarkers = new ArrayList<>();

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

                        route = RUTransitApp.getBusData().getBusTagsToBusRoutes().get("b");
                        drawRoute();

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
                    default:
                        Log.e("Navigation", "Selected item not recognized");
                    return false;
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng rutgersGate = new LatLng(40.498570, -74.445148);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rutgersGate, 14));


    }

    private LatLng getLatLng(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    private void drawRoute() {
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

    private class UpdateMarkers extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            NextBusAPI.updateActiveRoutes();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            // Update active bus locations
            ArrayList<BusVehicle> activeBuses = route.getActiveBuses();
            if (activeBuses != null) {
                // Clear map of active bus markers
                for (int i = 0; i < activeBusMarkers.size(); i++) {
                    activeBusMarkers.get(i).remove();
                }
                activeBusMarkers.clear();

                // Add active bus markers
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

    public static DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public static BusData getBusData() {
        if (busData == null) {
            try {
                // Query database first
                List<BusData> data = getDatabaseHelper().getDao().queryForAll();
                if (data.size() > 0) {
                    busData = data.get(0);
                }
                else {
                    // Create a new object in the database if it is nonexistent
                    busData = new BusData();
                    getDatabaseHelper().getDao().create(busData);
                }
            } catch (SQLException e) {
                Log.e("MapsActivity", e.toString(), e);
            }
        }
        return busData;
    }

    private class UpdateRoutesTask extends AsyncTask<Void, Void, ArrayList<BusRoute>> {

        protected ArrayList<BusRoute> doInBackground(Void... voids) {
            // Update the bus routes if more than a day has passed since it has been updated
            long busDataDate = RUTransitApp.getBusData().getDateInMillis();
            if (busDataDate == 0 || System.currentTimeMillis() - busDataDate >= 86400000/*MILLIS_IN_DAY*/) {
                NextBusAPI.saveBusRoutes();
                RUTransitApp.getBusData().setDateInMillis(System.currentTimeMillis());
            }
            return NextBusAPI.getActiveRoutes();
        }
    }

}
