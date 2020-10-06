//package com.example.clockapp.db;
//
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import com.example.clockapp.entities.ClockEntity;
//
//
///**
// * Created by omrierez on 30.12.17.
// */
//@Database(entities = {ClockEntity.class}, version = 1)
//public abstract class RoomDb extends RoomDatabase {
//
//    static final String DATABASE_NAME = "market_data";
//    private static RoomDb INSTANCE;
//    public abstract ClockDao clockDao();
//    public static RoomDb getDatabase(Context context) {
//        if (INSTANCE == null) {
//            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
//                    RoomDb.class, DATABASE_NAME).build();
//        }
//        return INSTANCE;
//    }
//
//}
