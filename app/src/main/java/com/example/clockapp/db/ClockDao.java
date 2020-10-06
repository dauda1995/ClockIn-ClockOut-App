package com.example.clockapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.clockapp.entities.ClockEntity;

import java.util.List;

@Dao
public interface ClockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoins(List<ClockEntity> details);

    @Query("SELECT * FROM clockData")
    LiveData<List<ClockEntity>> getAllDataLive();

    @Query("SELECT * FROM clockData")
    List<ClockEntity> getAllData();

    @Query("SELECT * FROM clockData LIMIT :limit")
    LiveData<List<ClockEntity>>getData(int limit);

    @Query("SELECT * FROM clockData WHERE timeIn=:symbol")
    LiveData<ClockEntity>getDatum(String symbol);

}
