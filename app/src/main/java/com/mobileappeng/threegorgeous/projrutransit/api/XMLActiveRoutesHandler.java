package com.mobileappeng.threegorgeous.projrutransit.api;

import android.util.Log;

import com.mobileappeng.threegorgeous.projrutransit.MapsActivity;
import com.mobileappeng.threegorgeous.projrutransit.data.constants.RUTransitApp;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusData;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusVehicle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class XMLActiveRoutesHandler extends DefaultHandler {

    private static final String TAG = XMLActiveRoutesHandler.class.getSimpleName();
    private BusData busData;
    private HashMap<String, BusRoute> busTagsToBusRoutes;
    private TreeSet<BusRoute> activeRoutes;

    public void startDocument() throws SAXException {
        //busData = RUTransitApp.getBusData();
        busData = MapsActivity.getBusData();
        busTagsToBusRoutes = busData.getBusTagsToBusRoutes();
        if (busTagsToBusRoutes == null) {
            busTagsToBusRoutes = new HashMap<>();
        }

        for (BusRoute route : busTagsToBusRoutes.values()) {
            route.setActiveBuses(new ArrayList<BusVehicle>());
        }

        activeRoutes = new TreeSet<>();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        if (qName.equalsIgnoreCase("vehicle")) {
            // Add to active routes
            String busTag = atts.getValue("routeTag");
            //busTag = "b";
            BusRoute route = busTagsToBusRoutes.get(busTag);
            if (route == null) {
                route = new BusRoute(busTag, busTag);
            }
            activeRoutes.add(route);

            // Add active bus location
            ArrayList<BusVehicle> activeBuses = route.getActiveBuses();

            if (activeBuses == null) {
                activeBuses = new ArrayList<BusVehicle>();
            }

            BusVehicle vehicle = new BusVehicle();
            vehicle.setLocation(Double.parseDouble(atts.getValue("lat")), Double.parseDouble(atts.getValue("lon")));
            vehicle.setVehicleId(atts.getValue("id"));
            activeBuses.add(vehicle);
            Log.d("Routes Handler", "Added one bus for line " + busTag);
            route.setActiveBuses(activeBuses); }
    }

    public void endDocument() throws SAXException {
        // Update active routes
        BusData.setActiveRoutes(new ArrayList<>(activeRoutes));
        // Update bus data
        try {
            RUTransitApp.getDatabaseHelper().getDao().createOrUpdate(busData);
            Log.d("Routes Handler", "Saved active routes");
        } catch (SQLException e) {
            Log.e(TAG, e.toString(), e);
            Log.e("Routes Handler", "Active routes not saved");
        }
    }
}