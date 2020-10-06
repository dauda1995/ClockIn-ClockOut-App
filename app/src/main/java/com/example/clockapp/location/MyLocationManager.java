package com.example.clockapp.location;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.clockapp.models.User;
import com.example.clockapp.repository.ClockRepository;
import com.example.clockapp.tracking.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class MyLocationManager implements LifecycleObserver, LocationRepository {
    private static final String TAG = MyLocationManager.class.getSimpleName();



    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Tracker mTracker;
    private Context mCon;
    private LatLng checkPoint;
    private MutableLiveData<LatLng> mlocation = new MutableLiveData<>() ;


    public MyLocationManager(Tracker tracker, Context con) {

        mTracker = tracker;
        mCon = con;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mCon.getApplicationContext());
    }

    public static LocationRepository create(Context con, Application application){
        final Tracker mTracker = new Tracker(con);
        return new MyLocationManager(mTracker, con);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void init() {
        Log.d(TAG, "init() called");
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mCon.getApplicationContext());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void clean() {
        Log.d(TAG, "clean() called");
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        final LifecycleOwner lcOwner = (LifecycleOwner) mCon;
        mFusedLocationClient.removeLocationUpdates(mLocationCallbacks);
//        lcOwner.getLifecycle().removeObserver(this);
        mCon=null;
    }

    private LocationCallback mLocationCallbacks = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(TAG, "onLocationResult() called with: locationResult = [" + locationResult + "]");
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    checkPoint = mTracker.trackLocation(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "onLocationResult: " + checkPoint.latitude + " sdvdfsf");
                    mlocation.postValue(checkPoint);
                    mTracker.getLocation(location);
                }
            }
        }
    };

    public synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient() called");
        mGoogleApiClient = new GoogleApiClient.Builder(mCon)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG, "onConnected() called with: bundle = [" + bundle + "]");
                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(100);
                        mLocationRequest.setFastestInterval(100);
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        if (ContextCompat.checkSelfPermission(mCon,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED)
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallbacks, Looper.myLooper());


                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "onConnectionSuspended() called with: i = [" + i + "]");
                    }
                })
                .addOnConnectionFailedListener(connectionResult -> Log.d(TAG, "onConnectionFailed() called with: connectionResult = [" + connectionResult + "]"))
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public MutableLiveData<LatLng> getCurrentPosition(){
        return mlocation;
    }



}
