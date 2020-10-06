package com.example.clockapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.clockapp.models.CheckPoints;
import com.example.clockapp.models.ClockDetails;
import com.example.clockapp.models.User;
import com.example.clockapp.mapper.ClockDetailMapper;
import com.example.clockapp.repository.dataSource.RemoteDataSource;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClockRepositoryImpl implements ClockRepository {

    private static final String TAG = "ClockRepositoryImpl";

    private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
    private final RemoteDataSource mRemoteDataSource;
    private final ClockDetailMapper mMapper;
    MediatorLiveData<List<ClockDetails>> mDataMerger = new MediatorLiveData<>();
//    MediatorLiveData<User> mDataUser = new MediatorLiveData<>();
//    MediatorLiveData<String> mErrorMerger = new MediatorLiveData<>();
    MutableLiveData<ClockDetails> mMsg = new MutableLiveData<>();



    public ClockRepositoryImpl(RemoteDataSource dataSource, ClockDetailMapper mappers) {
        mRemoteDataSource = dataSource;
        this.mMapper = mappers;
//        mDataUser.addSource(mRemoteDataSource.getUserDataStream(), entities ->
//                mExecutor.execute(() -> mDataUser.postValue(entities)));

    }


    public static ClockRepository create(Context mAppContext) {

        final ClockDetailMapper mapper = new ClockDetailMapper();
        final RemoteDataSource remoteDataSource = new RemoteDataSource(mAppContext, mapper);
        return new ClockRepositoryImpl(remoteDataSource, mapper);
    }

    @Override
    public LiveData<User> getUserData() { return mRemoteDataSource.getUserDataStream(); }

    @Override
    public LiveData<List<ClockDetails>> getUserClockDetails() {
        return mRemoteDataSource.getUserClockDataStream();
    }


    @Override
    public LiveData<String> callBack() {
        return mRemoteDataSource.getCallBack();
    }

    @Override
    public LiveData<String> getErrorStream() { return null; }

    @Override
    public LiveData<List<CheckPoints>> getTotalCheckPoints() {
        return mRemoteDataSource.getCheckPoints();
    }

    @Override
    public void fetchData() { mRemoteDataSource.fetchUserData(); }

    @Override
    public void fetchCheckPointData() {
        mRemoteDataSource.fetchCheckPointData();
    }

    @Override
    public void fetchClockDetails() {
        mRemoteDataSource.fetchClockDetails();

    }

    @Override
    public void submitClockDetails(ClockDetails clockDetails) {
        mRemoteDataSource.SubmitClockDataIn(clockDetails);
    }

    @Override
    public void submitClockDetailsOut(ClockDetails clockDetails) {
        mRemoteDataSource.SubmitClockDataOut(clockDetails);
    }
}
