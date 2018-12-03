package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private NavigationView navigation;
    private DrawerLayout drawer;

    public ArrayList<SchoolBus> schoolBuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize NavigationView and DrawerLayout
        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
            navigation.getMenu().getItem(i).setChecked(false);
        }
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);


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

    private void refreshSchoolBuses() {
        // ToDo refresh schoolBuses using API
    }
}
