package com.example.clockapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ClockDetails {

    public String uid;
    public String staffId;
    public Double latitude;
    public Double longitude;
    public String timeIn;
    public String timeOut;
    public Boolean status;
    public Double latitudeOut;
    public Double longitudeOut;



    public ClockDetails(){}

    public ClockDetails(String uid) {
        this.uid = uid;
    }

    public ClockDetails(String uid, String staffId, Double latitude, Double longitude, String timeIn, String timeOut, Boolean status) {
        this.uid = uid;
        this.staffId = staffId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = status;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }


    public String getStaffId() {
        return staffId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public Double getLatitudeOut() {
        return latitudeOut;
    }

    public void setLatitudeOut(Double latitudeOut) {
        this.latitudeOut = latitudeOut;
    }

    public Double getLongitudeOut() {
        return longitudeOut;
    }

    public void setLongitudeOut(Double longitudeOut) {
        this.longitudeOut = longitudeOut;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("staffId", staffId);
        result.put("mLatitude", latitude);
        result.put("mLongitude", longitude);
        result.put("timeIn", timeIn);
        result.put("timeOut", timeOut);
        result.put("status", status);
        return result;
    }
}
