package com.example.clockapp.fragments;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clockapp.R;
import com.example.clockapp.dashboard.DashBoardActivity;
import com.example.clockapp.models.CheckPoints;
import com.example.clockapp.models.ClockDetails;
import com.example.clockapp.models.User;
import com.example.clockapp.repository.dao.MyFragmentListenerImpl;
import com.example.clockapp.tracking.Tracker;
import com.example.clockapp.viewmodel.ClockViewModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.clockapp.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ClockFragments extends Fragment implements OnMapReadyCallback {

    private ClockViewModel mViewModel;


    private static final String TAG = "ChildTrackingFragment";
    private MyFragmentListenerImpl mFragmentCallback;
    private SupportMapFragment mapFragment;
    private DashBoardActivity mDashBoardActivity;
    private GoogleMap mMap;
    private View root;
    private List<LatLng> latLngs;
    private TextView txt_position, txt_distanceToPoint;
    private FloatingActionButton mFloatingActionButton;
    private int counter = 0;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    private HashMap<String, CheckPoints> userClockDetails = new HashMap<String, CheckPoints>();

    private Tracker mTracker;
    private final static int PERMISSION_REQUEST_LOCATION = 1234;

    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LatLng mCurrentLiveLocation;
    private CheckPoints mCheckPointLocation = null;
    private String mUserChck;

    private final Observer<String> callBackObserver = clockModel -> displayCallBackStatus(clockModel);
    private User mUser = new User();


    public static ClockFragments newInstance() {
        return new ClockFragments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.activity_clock_in, container, false);
        initializingView();
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mDashBoardActivity = (DashBoardActivity) context;

        try {
            mFragmentCallback = (MyFragmentListenerImpl) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ClockViewModel.class);
        mViewModel.fetchData();
        mViewModel.getUserDetails().observe(this, user -> {

            if(user!=null) {
                setUser(user);
                setUpDataAndMapView();
            }
        });
        mTracker = new Tracker(getActivity());
       checkLocationPermission();
        mViewModel.fetchCheckpoints();




        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) { mMap = googleMap; }

    private void refreshMap() {
        mMap.clear();
        mapFragment.onResume();
    }

    private void initializingView(){
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        txt_position = root.findViewById(R.id.position);
        txt_distanceToPoint = root.findViewById(R.id.distancetoPoint);
        mFloatingActionButton = root.findViewById(R.id.fab_clock1);
        mFloatingActionButton.setOnClickListener(v -> {

                submitDetails(mCurrentLiveLocation, mUser.getStatus());

            mFragmentCallback.onFabButtonClicked();
        });

    }

    private void setUser(User user){

        mUser= user;

        Log.d(TAG, "setUser: " + mUser.getCheckPointLocation() + " 1");
    }

    public void  setLocationAndDistance(Location checkPoint, LatLng currentLocation){
                if (currentLocation != null) {
                    Location location = new Location("");
                    location.setLatitude(currentLocation.latitude);
                    location.setLongitude(currentLocation.longitude);
                    float distance = checkPoint.distanceTo(location);
                    txt_distanceToPoint.setText("distance to clockPoint: " + distance);
                    txt_position.setText("Longitude:" + currentLocation.latitude+ " Latitude: " + currentLocation.longitude);

                }else {
                    txt_distanceToPoint.setText("");
                    txt_position.setText("No Locations Available");
                }
    }



    public void setUpDataAndMapView(){
        if(mViewModel != null){
//            Log.d(TAG, "setUpDataAndMapView: " + mUser.getCheckPointLocation() + " 2");
            LiveData<List<CheckPoints>> liveData = mViewModel.getCheckPointListLiveData();
            liveData.observe(getActivity(), (List<CheckPoints> user) ->{
                if (user != null && !user.isEmpty()) {
                    counter = 0;
                    markCheckPointsOnMapView(user);
                    getUserCheckPoint(user);
                }else {
                    refreshMap();
                    user.clear();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getUserCheckPoint(List<CheckPoints> userCheckPoints){

        mUserChck = mUser.getCheckPointLocation();
        Log.d(TAG, "getUserCheckPoint: " + mUserChck);
        List<CheckPoints> checkPointlist = getUserCheckPointFromList(userCheckPoints, mUserChck);
        int size = checkPointlist.size();
        Log.d(TAG, "getUserCheckPoint: "+ size);

        if(size > 0){
            for(CheckPoints chk : checkPointlist){
                if(chk.grtCheckPointName().equals(mUserChck)){
                    mCheckPointLocation = chk;
                    Log.d(TAG, "getUserCheckPoint: " + chk.getCheckPointLocation().getLatitude() + chk.getCheckPointLocation().getLongitude());
                    setMarkerProperties(chk.getLatLng(), chk.checkName);
                }
            }
        }
    }

    private List<CheckPoints> getUserCheckPointFromList(List<CheckPoints> userDetails, String userCheckPoint){
       List<CheckPoints> userClockDetails = new ArrayList<>();
        for(CheckPoints u : userDetails){
            if(u.grtCheckPointName().equals(userCheckPoint))
            userClockDetails.add(u);
        }
        return userClockDetails;
    }

    private Set<String> getAllCheckPointFromList(List<CheckPoints> userDetails){
        userClockDetails = new HashMap<>();
        for(CheckPoints u : userDetails){
            userClockDetails.put(u.grtCheckPointName(), u);
        }
        return userClockDetails.keySet();
    }

    public void markCheckPointsOnMapView(List<CheckPoints> checkPoint){
        refreshMap();
        Set<String> checkList = getAllCheckPointFromList(checkPoint);
        counter = 0;
        latLngs = new ArrayList<LatLng>();
        for(String chkP : checkList){
            displayDataOnMap(chkP, checkList.size());
        }
    }

    public void displayDataOnMap(String checkPointName, Integer userListSize){

        if(mViewModel != null){
            LiveData<List<CheckPoints>> liveData = mViewModel.getCheckPointListLiveData();
            liveData.observe(getActivity(), (List<CheckPoints> mEntities) ->{
                counter++;
                if(mEntities.size() !=0) {
                  //  Collections.sort(mEntities, new DateComparator());
                    for (CheckPoints c : mEntities) {
                        LatLng position = new LatLng(c.getCheckPointLocation().getLatitude(), c.getCheckPointLocation().getLongitude());
                        latLngs.add(position);
                        setMarkerProperties(position, c.checkName);
                    }
                }
                if (counter == userListSize) cameraUpdateCall(latLngs);
            });
        }
    }

    public void setMarkerProperties(LatLng latLng, String checkName){
        int color = new Random().nextInt(360);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(checkName)
                .icon(BitmapDescriptorFactory.defaultMarker(color))

        );
        marker.showInfoWindow();


    }

    public void cameraUpdateCall(List<LatLng> latLngs) {
        if (latLngs.size() > 0) {
            builder = new LatLngBounds.Builder();
            for (LatLng position : latLngs)
                builder.include(position);
            if (areBoundsTooSmall(builder.build(), 100)) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(builder.build().getCenter(), 14));
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 5));
            }
        }
    }

    private boolean areBoundsTooSmall(LatLngBounds bounds, int minDistanceInMeter) {
        float[] result = new float[1];
        Location.distanceBetween(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude, result);
        return result[0] < minDistanceInMeter;
    }

    private void checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission() called");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getActivity(), "Please give me location permissions", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);
            }
        }
        else
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
          buildGoogleApiClient();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                       buildGoogleApiClient();
                    }
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
                return;
            }
        }
    }

    private LocationCallback mLocationCallbacks = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    mCurrentLiveLocation = mTracker.trackLocation(location.getLatitude(), location.getLongitude());
                    mTracker.getLocation(location);
                    if(mCheckPointLocation != null){
                        setLocationAndDistance(mCheckPointLocation.getCheckPointLocation(), mCurrentLiveLocation);
                    }
                }
            }
        }
    };

    public synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient() called");
        mGoogleApiClient = new GoogleApiClient.Builder(root.getContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG, "onConnected() called with: bundle = [" + bundle + "]");
                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(100);
                        mLocationRequest.setFastestInterval(100);
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        if (ContextCompat.checkSelfPermission(root.getContext(),
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

    public void submitDetails(LatLng latLng, Boolean status){
        Log.d(TAG, "submitDetails: ");
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setConfirmText("Yes")
                .setConfirmClickListener(sDialog ->
                {
                    if(!mUser.getStatus()) {
                        LiveData callback = mViewModel.submitClockIn(setDetails(latLng, status));
                        callback.observe(this, callBackObserver);
                        sDialog.dismissWithAnimation();
                    }else{
                        LiveData callback = mViewModel.submitClockOut(setDetails(latLng, status));
                        callback.observe(this, callBackObserver);
                        sDialog.dismissWithAnimation();
                    }

                })
                .setCancelButton("Cancel", sDialog -> sDialog.dismissWithAnimation())
                .show();
    }

    public ClockDetails setDetails(LatLng latLng, Boolean status){
        ClockDetails clockDetails = new ClockDetails();
        clockDetails.setUid(mUser.uid);
        clockDetails.setStaffId(mUser.staffId);
        if(!status) {
            clockDetails.setTimeIn(getTime());
            clockDetails.setLatitude(latLng.latitude);
            clockDetails.setLongitude(latLng.longitude);
            clockDetails.setTimeOut("");
            clockDetails.setLatitudeOut(0.0);
            clockDetails.setLongitudeOut(0.0);
        }else{
            clockDetails.setTimeIn(mUser.timeIn);
            clockDetails.setLatitude(mUser.latitude);
            clockDetails.setLongitude(mUser.longitude);
            clockDetails.setTimeOut(getTime());
            clockDetails.setLatitudeOut(latLng.latitude);
            clockDetails.setLongitudeOut(latLng.longitude);
        }

        clockDetails.setStatus(!status);

        return clockDetails;
    }

    private void displayCallBackStatus(String clockModel) {
        if(clockModel.equals(Constants.CALLBACK_SUCCESSFUL)){
            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Submission Successful")
                    .show();
        }
        else if(clockModel.equals(Constants.CALLBACK_FAILED)){
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Submission Failed")
                    .show();

        }

    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(new Date());
        return time;
    }
}
