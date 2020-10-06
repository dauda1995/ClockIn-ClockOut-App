package com.example.clockapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Serializable {

    public String uid;
    public String staffId;
    public String timeIn = "";
    public String timeOut = "";
    public String staffSuperiorEmail;
    public String checkPointLocation;
    public Boolean status = true;
    public Double latitude, longitude;
    @Exclude
    public boolean isAuthenticated =false;
    @Exclude
    public boolean isNew, isCreated;
    @Exclude
    private String password;
    @Exclude
    private String email;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String uid, String staffId, boolean isAuth) {
        this.uid = uid;
        this.staffId = staffId;
        this.isAuthenticated = isAuth;
    }





    public User(String uid, String staffId, String timeIn, String timeOut, String staffSuperiorEmail, Boolean status, String checkPointLocation) {
        this.uid = uid;
        this.staffId = staffId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.staffSuperiorEmail = staffSuperiorEmail;
        this.status = status;
        this.checkPointLocation = checkPointLocation;
    }


    public String getUid() {
        return uid;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public String getStaffSuperiorEmail() {
        return staffSuperiorEmail;
    }

    public String getCheckPointLocation() {
        return checkPointLocation;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public void setStaffSuperiorEmail(String staffSuperiorEmail) {
        this.staffSuperiorEmail = staffSuperiorEmail;
    }

    public void setCheckPointLocation(String checkPointLocation) {
        this.checkPointLocation = checkPointLocation;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> details = new HashMap<>();
        details.put("uid", uid);
        details.put("staffId", staffId);
        details.put("timeIn", timeIn);
        details.put("timeOut", timeOut);
        details.put("staffSuperiorEmail", staffSuperiorEmail);
        details.put("status", status);
        return details;
    }


}
