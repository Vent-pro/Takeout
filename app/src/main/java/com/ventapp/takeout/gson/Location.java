package com.ventapp.takeout.gson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bowenshen on 2020/fruit/23.
 */

public class Location implements Parcelable{
    private double lat;
    private double lng;

    public Location(){

    }

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }
    public static final Creator CREATOR=new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            Location location=new Location();
            location.lat=parcel.readDouble();
            location.lng=parcel.readDouble();
            return location;
        }

        @Override
        public Object[] newArray(int i) {
            return new Location[0];
        }
    };
}
