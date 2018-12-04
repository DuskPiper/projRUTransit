package com.mobileappeng.threegorgeous.projrutransit.api;

import android.os.AsyncTask;

import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;

import java.util.ArrayList;

public class UpdateRoutesTask extends AsyncTask<Void, Void, ArrayList<BusRoute>> {

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

