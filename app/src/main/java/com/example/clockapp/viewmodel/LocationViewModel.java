package com.example.clockapp.viewmodel;

import android.Manifest;
import android.app.Application;
import android.content.Context;
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
import com.example.clockapp.location.MyLocationManager;
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

public class LocationViewModel extends AndroidViewModel {

    private ClockRepository mClockRepository;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        mClockRepository = ClockRepositoryImpl.create(application);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void fetchUserClockData(){
        mClockRepository.fetchClockDetails();
    }

    public LiveData<List<ClockDetails>> getUserClockDetails(){
        return mClockRepository.getUserClockDetails();
    }
}
