package com.mobileappeng.threegorgeous.projrutransit;

import com.google.android.gms.maps.model.LatLng;

public class SchoolBus {
    public String line;
    public LatLng location;

    SchoolBus(String line, LatLng location) {
        this.line = line;
        this.location = location;
    }
}
