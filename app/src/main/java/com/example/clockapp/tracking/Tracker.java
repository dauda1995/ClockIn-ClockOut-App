package com.example.clockapp.tracking;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

public class Tracker implements LifecycleObserver {

    private final RequestQueue mQueue;
    private final String mOsVersion;
    private Context mCon;
    private static final String TAG = "Tracker";

    public Tracker(Context con) {
        mCon = con;
        mOsVersion = Build.VERSION.RELEASE;
        mQueue = Volley.newRequestQueue(con.getApplicationContext());
        ((AppCompatActivity) con).getLifecycle().addObserver(this);
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void trackOnCreate() {
        Log.d(TAG, "trackOnCreate() called create");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void trackOnDestroy() {
        Log.d(TAG, "trackOnDestroy() called");
        ((AppCompatActivity)mCon).getLifecycle().removeObserver(this);

        Lifecycle.State currentState=((AppCompatActivity)mCon).getLifecycle().getCurrentState();
        mCon=null;

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void trackOnStart() {
        Log.d(TAG, "trackOnStart() called start");


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void trackOnResume() {
        Log.d(TAG, "trackOnResume() called resume");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void trackOnPause() {
        Log.d(TAG, "trackOnPause() called resume pause");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void trackOnStop() {
        Log.d(TAG, "trackOnStop() called stop");


    }

    public LatLng trackLocation(double lat, double lng) {
        return new LatLng(lat, lng);

    }

    public Location getLocation(Location location){ return location; }

    public float distanceToLocation(Location checkPoint, Location current){
        return checkPoint.distanceTo(current);
    }


}
