//package com.example.clockapp.repository.dataSource;
//
//
//import android.content.Context;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.clockapp.models.CheckPoints;
//import com.example.clockapp.models.User;
//import com.example.clockapp.db.RoomDb;
//import com.example.clockapp.entities.ClockEntity;
//
//import java.util.List;
//
///**
// * Created by omrierez on 28.12.17.
// */
//
//public class LocalDataSource implements DataSource<List<ClockEntity>>{
//    private final RoomDb mDb;
//    private final MutableLiveData<String> mError=new MutableLiveData<>();
//    public LocalDataSource(Context mAppContext) {
//        mDb= RoomDb.getDatabase(mAppContext);
//    }
//
//
//
//    @Override
//    public LiveData<User> getUserDataStream() {
//        return null;
//    }
//
//    @Override
//    public LiveData<List<ClockEntity>> getUserClockDataStream() {
//        return mDb.clockDao().getAllDataLive();
//    }
//
//    @Override
//    public LiveData<String> getErrorStream() {
//        return mError;
//    }
//
//    @Override
//    public LiveData<List<CheckPoints>> getCheckPoints() {
//        return null;
//    }
//
//
//    public void writeData(List<ClockEntity> data) {
//        try {
//            mDb.clockDao().insertCoins(data);
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//            mError.postValue(e.getMessage());
//        }
//    }
//
//    public List<ClockEntity> getALlCoins() {
//        return mDb.clockDao().getAllData();
//    }
//}
