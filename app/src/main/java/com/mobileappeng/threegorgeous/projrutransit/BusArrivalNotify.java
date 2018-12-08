package com.mobileappeng.threegorgeous.projrutransit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BusArrivalNotify extends Service {
    public static final String TAG = "Bus Arrival Serive";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
