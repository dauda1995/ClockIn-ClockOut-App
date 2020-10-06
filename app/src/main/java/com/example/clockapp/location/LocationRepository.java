package com.example.clockapp.location;

import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;

public interface LocationRepository {
    LiveData<LatLng> getCurrentPosition();
}

