package com.mobileappeng.threegorgeous.projrutransit;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.mobileappeng.threegorgeous.projrutransit.api.NextBusAPI;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.AppData;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusData;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStopTime;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusVehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BusArrivalNotify extends Service {
    public static final String TAG = "Bus Arrival Service";
    private String routeTag;
    private String routeName;
    private String stopTag;
    private String stopName;
    private int closestTime = Integer.MAX_VALUE;
    private Timer timer;
    private TimerTask timedRecentBusRefresher;
    private TimerTask timedNotifier;
    private int failedTrials = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Load Intent data
        routeName = intent.getStringExtra(AppData.ROUTE_NAME);
        routeTag = intent.getStringExtra(AppData.ROUTE_TAG);
        stopName = intent.getStringExtra(AppData.STOP_NAME);
        stopTag = intent.getStringExtra(AppData.STOP_TAG);
        if (routeName == null || routeTag == null || stopName == null || stopTag == null) {
            Log.e(TAG, "Data invalid");
            stopSelf();
        }
        Log.d(TAG, "Service started, checking route" + routeName + " @ " + stopName);
        // Initialize Loopers
        timer = new Timer();
        timedRecentBusRefresher = new TimerTask() {
            @Override
            public void run() {
                new FindRecentBuses().execute();
            }
        };
        timedNotifier = new TimerTask() {
            @Override
            public void run() {
                if (closestTime < 2) {
                    // Bus Approaching!
                    Log.i(TAG, "Bus " + routeName + " is approaching at " + stopName);
                    sendNotification();
                    stopSelf();
                }
            }
        };
        // Run Loopers
        timer.schedule(timedNotifier, 0, 2000);
        timer.schedule(timedRecentBusRefresher, 0, 10000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Log.d(TAG, "Service stopped, not checking route" + routeName + " @ " + stopName);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification() {
        // Notify
        Toast.makeText(
                getApplicationContext(),
                "Bus " + routeName + " approaching " + stopName,
                Toast.LENGTH_LONG).show();
        ////////////////////////// NOTIFY HERE //////////////////////////////
    }

    private class FindRecentBuses extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            return findRecentBusesOfRouteAtStop(routeTag, stopTag);
        }

        @Override
        protected void onPostExecute(Integer time) {
            closestTime = time;
            if (time > 9999) {
                failedTrials ++;
                if (failedTrials > 5) { // After 5 failed attempts
                    Log.e(TAG, "Bus data not available, auto stopped");
                    stopSelf();
                }
            }
        }

        private int findRecentBusesOfRouteAtStop (String route, String stop) {
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
                return Integer.MAX_VALUE;
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
                return Integer.MAX_VALUE;
            } else {
                for (BusStopTime stopTime : stopTimes) {
                    if (routeBusVehicleIds.contains(stopTime.getVehicleId())) {
                        targetTimes.add(stopTime.getMinutes());
                    }
                }
            }
            // toString
            if (targetTimes.size() == 0) {
                return Integer.MAX_VALUE;
            } else {
                return Collections.min(targetTimes);
            }
        }
    }

}
