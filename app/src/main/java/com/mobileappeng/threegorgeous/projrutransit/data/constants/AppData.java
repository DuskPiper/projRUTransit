package com.mobileappeng.threegorgeous.projrutransit.data.constants;


public class AppData {
    // NextBus API links
    private static final String BASE_URL = "http://webservices.nextbus.com/service/publicXMLFeed?a=rutgers&command=";
    public static final String VEHICLE_LOCATIONS_URL = BASE_URL + "vehicleLocations";
    public static final String ALL_ROUTES_URL = BASE_URL + "routeConfig";
    public static final String PREDICTIONS_URL = BASE_URL + "predictionsForMultiStops";

    // Request Codes
    public static final int REQUEST_ADD_NEW_FAVOURITE = 1000;

    // Shared Preferences
    public static final String ROUTE_TAG = "bus_route_tag";
    public static final String ROUTE_NAME = "bus_route_name";
    public static final String STOP_TAG = "bus_stop_tag";
    public static final String STOP_NAME = "bus_stop_name";

    // Favourite Buses ListView Adapter
    public static final String BUS_INFO_DESCRIPTION = "route_and_stop_info_description";
    public static final String BUS_INFO_APPROACHING_TIME = "route_and_stop_info_approaching_time";
}