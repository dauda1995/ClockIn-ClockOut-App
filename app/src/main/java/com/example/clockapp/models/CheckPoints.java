package com.example.clockapp.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class CheckPoints {

    public String checkName;
    public Double mLatitude;
    public Double mLongitude;
    public LatLng mLatLng;



//    public CheckPoints(String checkName, LatLng latLng) {
//        this.checkName = checkName;
//        mLatLng = latLng;
//        mLatitude = mLatLng.latitude;
//        mLongitude = mLatLng.longitude;
//
//    }

    public CheckPoints(){

    }

    public CheckPoints(String checkName, Double latitude, Double longitude){
        this.checkName = checkName;
        this.mLatitude = Double.valueOf(latitude);
        this.mLongitude =Double.valueOf(longitude);
        this.mLatLng = new LatLng(mLatitude, mLongitude);

    }

    public String grtCheckPointName(){
        return checkName;
    }

    public Location getCheckPointLocation(){
        Location checkLocation = new Location("");
        if(mLatitude == null && mLongitude == null){
            checkLocation.setLatitude(0.00);
            checkLocation.setLongitude(0.00);
            return checkLocation;
        }
        checkLocation.setLatitude(mLatitude);
        checkLocation.setLongitude(mLongitude);
        return checkLocation;
    }

    public LatLng getLatLng(){
        return mLatLng;
    }
}
