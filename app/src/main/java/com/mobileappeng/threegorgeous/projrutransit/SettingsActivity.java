package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private NavigationView navigation;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize NavigationView and DrawerLayout
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
                    default:
                        Log.e("Navigation", "Selected item not recognized");
                        return false;
                }
            }
        });

    }
}
