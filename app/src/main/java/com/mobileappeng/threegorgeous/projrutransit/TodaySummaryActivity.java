package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class TodaySummaryActivity extends AppCompatActivity {
    private NavigationView navigation;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_summary);


        // Initialize NavigationView and DrawerLayout
        navigation = (NavigationView)findViewById(R.id.navigation);
        int size = navigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigation.getMenu().getItem(i).setCheckable(true);
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
                        startActivity(new Intent(TodaySummaryActivity.this, SettingsActivity.class));
                        return true;
                    default:
                        Log.e("Navigation", "Selected item not recognized");
                        return false;
                }
            }
        });

    }
}
