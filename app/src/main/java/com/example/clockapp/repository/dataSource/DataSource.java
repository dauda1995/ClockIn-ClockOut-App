package com.example.clockapp.repository.dataSource;

import androidx.lifecycle.LiveData;

import com.example.clockapp.models.CheckPoints;
import com.example.clockapp.models.ClockDetails;
import com.example.clockapp.models.User;

import java.util.List;

public interface DataSource<T> {

    LiveData<User> getUserDataStream();
    LiveData<T> getUserClockDataStream();
    LiveData<String> getErrorStream();
    LiveData<List<CheckPoints>> getCheckPoints();
    LiveData<String> getCallBack();


}
