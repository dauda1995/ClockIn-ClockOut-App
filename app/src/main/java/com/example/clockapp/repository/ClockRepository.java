package com.example.clockapp.repository;

import android.location.Location;

import androidx.lifecycle.LiveData;

import com.example.clockapp.models.CheckPoints;
import com.example.clockapp.models.User;
import com.example.clockapp.models.ClockDetails;

import java.util.List;

public interface ClockRepository {

    LiveData<User> getUserData();
    LiveData<List<ClockDetails>> getUserClockDetails();
    LiveData<String> getErrorStream();
    LiveData<List<CheckPoints>> getTotalCheckPoints();
    LiveData<String> callBack();
    void fetchData();
    void fetchCheckPointData();
    void fetchClockDetails();
    void submitClockDetails(ClockDetails clockDetails);
    void submitClockDetailsOut(ClockDetails clockDetails);






}
