package com.mobileappeng.threegorgeous.projrutransit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

public class TodaySummaryActivity extends AppCompatActivity {
    private NavigationView navigation;
    private DrawerLayout drawer;
    private SimpleCursorAdapter mSimpleCursorAdapter;
    private DatabaseHelper DatabaseHelper;//declaration of dababasehelper
    public SQLiteDatabase dbW;
    public SQLiteDatabase dbR;
    ListView bus_timetable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_summary);

        bus_timetable=(ListView) findViewById(R.id.bus_timetable);

        mSimpleCursorAdapter=new SimpleCursorAdapter(TodaySummaryActivity.this,R.layout.activity_today_summary_list_item,
                null,new String[]{"title","description"},new int[]{R.id.bus_name,R.id.bus_time}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        bus_timetable.setAdapter(mSimpleCursorAdapter);

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

    public void refreshListView() {//refresh listviem
        Cursor mCursor = dbW.query("favourate_bus_timeable", null, null, null, null, null, null);
        mSimpleCursorAdapter.changeCursor(mCursor);
    }



}
