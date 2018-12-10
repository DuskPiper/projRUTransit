package com.mobileappeng.threegorgeous.projrutransit.api;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import com.mobileappeng.threegorgeous.projrutransit.data.constants.AppData;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusData;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusRoute;
import com.mobileappeng.threegorgeous.projrutransit.data.model.BusStop;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class NextBusAPI {

    private static final String TAG = NextBusAPI.class.getSimpleName();
    private static OkHttpClient okHttpClient;
    private static SAXParser saxParser;

    // Downloads data from a url and returns it as an input stream
    private static InputStream downloadUrl(String url) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().byteStream();
        } catch (IOException e) {
            Log.e(TAG, e.toString(), e);
        }

        return null;
    }

    // Setups the SAX parser and parses the XML from the url
    private static void parseXML(String url, DefaultHandler handler) {
        try {
            if (saxParser == null) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                saxParser = factory.newSAXParser();
            }
            InputStream inputStream = downloadUrl(url);
            // Log.d("SAX", "Fetched stream from url = " + url);
            if (inputStream == null) {
                throw new IOException("Can't connect to the Internet");
            } else {
                saxParser.parse(inputStream, handler);
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            Log.e(TAG, e.toString());
        }
    }

    // Saves the bus routes to the database
    public static void saveBusRoutes() {
        parseXML(AppData.ALL_ROUTES_URL, new XMLBusRouteHandler());
    }

    // Saves the bus stop times to the database
    public static void saveBusStopTimes(BusRoute route) {
        BusStop[] busStops = route.getBusStops();

        if (busStops != null) {
            // Create predictions link
            StringBuilder link = new StringBuilder(AppData.PREDICTIONS_URL);
            for (BusStop stop : busStops) {
                link.append("&stops=").append(route.getTag()).append("%7Cnull%7C").append(stop.getTag());
            }
            parseXML(link.toString(), new XMLBusTimesHandler(route));
        }
    }

    // Updates active routes
    public static void updateActiveRoutes() {
        parseXML(AppData.VEHICLE_LOCATIONS_URL, new XMLActiveRoutesHandler());
        Log.d("Next Bus API", "Updated active routes");
    }

    // Returns an array of the active routes
    public static ArrayList<BusRoute> getActiveRoutes() {
        updateActiveRoutes();
        return BusData.getActiveRoutes();
    }
}