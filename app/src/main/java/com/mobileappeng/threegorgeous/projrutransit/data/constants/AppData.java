package com.mobileappeng.threegorgeous.projrutransit.data.constants;


public class AppData {
    // NextBus API links
    private static final String BASE_URL = "http://webservices.nextbus.com/service/publicXMLFeed?a=rutgers&command=";
    public static final String VEHICLE_LOCATIONS_URL = BASE_URL + "vehicleLocations";
    public static final String ALL_ROUTES_URL = BASE_URL + "routeConfig";
    public static final String PREDICTIONS_URL = BASE_URL + "predictionsForMultiStops";

    /*// Google Analytics
    public static final int ROUTE_NAME_DIMEN = 1;
    public static final int PAGE_CLICKED_FROM_DIMEN = 2;
    public static final int ORIGIN_DIMEN = 3;
    public static final int DESTINATION_DIMEN = 4;*/

    // Shared Preferences
    public static final String ROUTE_TAG = "bus_route_tag";
    public static final String ROUTE_NAME = "bus_route_name";
    public static final String STOP_TAG = "bus_stop_tag";
    public static final String STOP_NAME = "bus_stop_name";
}