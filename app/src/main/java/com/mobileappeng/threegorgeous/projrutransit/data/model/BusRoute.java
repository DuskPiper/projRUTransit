package com.mobileappeng.threegorgeous.projrutransit.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class BusRoute implements Parcelable, Serializable, Comparable<BusRoute> {

    private static final long serialVersionUID = 1632593783571404823L;
    private String tag;
    private String title;
    private boolean starred;
    private BusStop[] busStops;
    private BusPathSegment[] busPathSegments;
    private transient ArrayList<BusVehicle> activeBuses;
    private transient long lastUpdatedTime;

    public BusRoute(String tag, String title) {
        this.tag = tag;
        this.title = title;
        this.starred = false;
        this.busStops = null;
        this.busPathSegments = null;
        this.activeBuses = null;
    }

    public BusRoute(String title) {
        this.title = title;
        this.tag = null;
        this.starred = false;
        this.busStops = null;
        this.busPathSegments = null;
        this.activeBuses = null;
    }

    @SuppressWarnings("unchecked")
    private BusRoute(Parcel in) {
        tag = in.readString();
        title = in.readString();
        starred = in.readByte() != 0;
        busStops = in.createTypedArray(BusStop.CREATOR);
        busPathSegments = in.createTypedArray(BusPathSegment.CREATOR);
        activeBuses = in.createTypedArrayList(BusVehicle.CREATOR);
    }

    public BusRoute() {
        // Needed for ormlite
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public BusStop[] getBusStops() {
        return busStops;
    }

    public void setBusStops(BusStop[] busStops) {
        this.busStops = busStops;
    }

    public BusPathSegment[] getBusPathSegments() {
        return busPathSegments;
    }

    public void setBusPathSegments(BusPathSegment[] busPathSegments) {
        this.busPathSegments = busPathSegments;
    }

    public ArrayList<BusVehicle> getActiveBuses() {
        return activeBuses;
    }

    public void setActiveBuses(ArrayList<BusVehicle> activeBusLocations) {
        this.activeBuses = activeBusLocations;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(tag);
        out.writeString(title);
        out.writeByte((byte) (starred ? 1 : 0));
        out.writeTypedArray(busStops, 0);
        out.writeTypedArray(busPathSegments, 0);
        out.writeTypedList(activeBuses);
    }

    public static Parcelable.Creator<BusRoute> CREATOR = new Parcelable.Creator<BusRoute>() {
        public BusRoute createFromParcel(Parcel in) {
            return new BusRoute(in);
        }

        public BusRoute[] newArray(int size) {
            return new BusRoute[size];
        }
    };

    @Override
    public int compareTo(@NonNull BusRoute other) {
        if (this == other) {
            return 0;
        } else if (isStarred() && !other.isStarred()) {
            return -1;
        } else if (!isStarred() && other.isStarred()) {
            return 1;
        } else {
            return title.compareTo(other.getTitle());
        }
    }
}