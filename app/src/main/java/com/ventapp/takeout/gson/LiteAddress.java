package com.ventapp.takeout.gson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bowenshen on 2020/fruit/23.
 */

public class LiteAddress implements Parcelable{
    protected String name;
    protected Location location;
    protected String address;
    protected String province;
    protected String city;
    protected int distance;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(location,0);
        parcel.writeString(address);
        parcel.writeString(province);
        parcel.writeString(city);
        parcel.writeInt(distance);
    }
    public static final Creator<LiteAddress> CREATOR=new Creator<LiteAddress>() {
        @Override
        public LiteAddress createFromParcel(Parcel parcel) {
            LiteAddress liteAddress=new LiteAddress();
            liteAddress.name=parcel.readString();
            liteAddress.location=parcel.readParcelable(Location.class.getClassLoader());
            liteAddress.address=parcel.readString();
            liteAddress.province=parcel.readString();
            liteAddress.city=parcel.readString();
            liteAddress.distance=parcel.readInt();
            return liteAddress;
        }

        @Override
        public LiteAddress[] newArray(int i) {
            return new LiteAddress[0];
        }
    };

    public static LiteAddress parseLiteAddress(DetailedAddress detailedAddress){
        LiteAddress liteAddress=detailedAddress;
        liteAddress.setDistance(detailedAddress.getDetail_info().getDistance());
        return liteAddress;
    }
}
