package com.example.clockapp.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

@Entity(tableName = "clockData",
        indices = {@Index("id"),
                   })

public class ClockEntity {

    @JsonProperty("id")

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String id;

    @JsonProperty("name")
    @ColumnInfo(name = "n")
    private String name;

    @JsonProperty("staffid")
    @ColumnInfo(name = "staffid")
    private String staffid;

    @JsonProperty("lat")
    @ColumnInfo(name = "lat")
    private Double lat;

    @JsonProperty("lng")
    @ColumnInfo(name = "lng")
    private Double lng;

    @JsonProperty("timeIn")
    @ColumnInfo(name = "timeIn")
    private String timeIn;

    @JsonProperty("timeOut")
    @ColumnInfo(name = "timeOut")
    private String timeOut;

    @JsonProperty("status")
    @ColumnInfo(name = "status")
    private boolean status;

    @JsonProperty("userId")
    @ColumnInfo(name = "UserId")
    private String userId;

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getUserId() {
        return userId;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("staffid")
    public String getstaffid() {
        return staffid;
    }

    @JsonProperty("staffid")
    public void setstaffid(String staffid) {
        this.staffid = staffid;
    }

    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @JsonProperty("lng")
    public Double getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(Double lng) {
        this.lng = lng;
    }

    @JsonProperty("timeIn")
    public String getTimeIn() {
        return timeIn;
    }

    @JsonProperty("timeIn")
    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    @JsonProperty("timeOut")
    public String getTimeOut() {
        return timeOut;
    }

    @JsonProperty("timeOut")
    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    @JsonProperty("status")
    public boolean isStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(boolean status) {
        this.status = status;
    }

    @JsonProperty("userId")
    public String isUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
