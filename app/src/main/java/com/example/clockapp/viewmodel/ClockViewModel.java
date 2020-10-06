package com.example.clockapp.viewmodel;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.clockapp.location.LocationRepository;
import com.example.clockapp.models.CheckPoints;
import com.example.clockapp.models.User;
import com.example.clockapp.models.ClockDetails;
import com.example.clockapp.repository.ClockRepository;
import com.example.clockapp.repository.ClockRepositoryImpl;
import com.example.clockapp.tracking.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ClockViewModel extends AndroidViewModel{

    private static final String TAG = "ClockViewModel";
    private ClockRepository mClockRepository;

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }


    public ClockViewModel(@NonNull Application application) {
        super(application);
        mClockRepository = ClockRepositoryImpl.create(application);
    }

    public void fetchData(){
        mClockRepository.fetchData();

    }
    public void fetchCheckpoints(){
        mClockRepository.fetchCheckPointData();
    }

    public void fetchUserClockData(){
        mClockRepository.fetchClockDetails();
    }
    public LiveData<List<ClockDetails>> getUserClockDetails(){
        return mClockRepository.getUserClockDetails();
    }



    public LiveData<User> getUserDetails(){
        return mClockRepository.getUserData();
    }

    public LiveData<List<CheckPoints>> getCheckPointListLiveData(){
        return mClockRepository.getTotalCheckPoints();

    }

    public LiveData<String> submitClockIn(ClockDetails clockDetails){
        mClockRepository.submitClockDetails(clockDetails);
        return mClockRepository.callBack();
    }

    public LiveData<String> submitClockOut(ClockDetails clockDetails){
        mClockRepository.submitClockDetailsOut(clockDetails);
        return mClockRepository.callBack();
    }


}
