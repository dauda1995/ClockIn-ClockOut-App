package com.example.clockapp.repository.dataSource;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.clockapp.models.CheckPoints;
import com.example.clockapp.models.ClockDetails;
import com.example.clockapp.models.User;
import com.example.clockapp.mapper.ClockDetailMapper;
import com.example.clockapp.utils.Utility;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.clockapp.utils.Constants.CALLBACK_FAILED;
import static com.example.clockapp.utils.Constants.CALLBACK_SUCCESSFUL;
import static com.example.clockapp.utils.Constants.CLOCKDETAILS;
import static com.example.clockapp.utils.Constants.USER;
import static com.example.clockapp.utils.Constants.USERS;

public class RemoteDataSource implements DataSource<List<ClockDetails>> {

    private static final String TAG = "RemoteDataSource";
    private DatabaseReference mDb;
    private FirebaseAuth mAuth;
    private final ClockDetailMapper mObjMapper;
    private List<CheckPoints> mList = new ArrayList<>();
    public MutableLiveData<List<CheckPoints>> mCheckPointData = new MutableLiveData<>();
    private final MutableLiveData<String> mError=new MutableLiveData<>();
    private final MutableLiveData<User> mUserData=new MutableLiveData<>();
    private final MutableLiveData<List<ClockDetails>> mUserClockData=new MutableLiveData<>();
    private final MutableLiveData<String> resultCallBack = new MutableLiveData<>();
    private String mUserId;
    private User mUser = new User();
    private List<ClockDetails>  mList1 = new ArrayList<>();

    public RemoteDataSource(Context context, ClockDetailMapper objMapper){
        mAuth = FirebaseAuth.getInstance();
        mUserId = mAuth.getUid();
        mDb = FirebaseDatabase.getInstance().getReference();
        mObjMapper = objMapper;


    }

    public void fetchUserData(){
        Log.d(TAG, "fetchUserData: " + mUserId);
        DatabaseReference mdatabase = mDb.child(USERS).child(mUserId);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
//                Log.d(TAG, "onDataChange: " + data.uid);
                mUserData.setValue(mUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void fetchClockDetails(){
        mList1.clear();
//        mUserClockData.setValue(null);
        String userId = mAuth.getUid();
        mDb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDatabase = mDb.child(CLOCKDETAILS).child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ClockDetails details = snapshot.getValue(ClockDetails.class);
                    mList1.add(details);

                }
                Log.d(TAG, "onDataChange: gotten clock data" + mList1.size());
                mUserClockData.postValue(mList1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fetchCheckPointData(){
        String checkRef = Utility.CHECKPOINT_PATH;
        mDb =   FirebaseDatabase.getInstance().getReference().child("checkpoints");
        mDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    DataSnapshot snapshot = iterator.next();
                    Iterator<DataSnapshot> iterator2 = snapshot.getChildren().iterator();
                    String name = snapshot.child("checkName").getValue().toString();
                    Double latitude = Double.parseDouble(snapshot.child("latitude").getValue().toString());
                    Double longitude = Double.parseDouble(snapshot.child("longitude").getValue().toString());
                    CheckPoints checkPoints = new CheckPoints(name, latitude, longitude);
                    mList.add(checkPoints);
                    mList.add(checkPoints);
                }
                mCheckPointData.postValue(mList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        FirebaseQueryLiveData mLiveData = new FirebaseQueryLiveData(mDb);
//        mCheckPointData =
//                Transformations.map(mLiveData, new DeserializerCheckPoint());
//

    }

    private class DeserializerCheckPoint implements Function<DataSnapshot, List<CheckPoints>> {

        @Override
        public List<CheckPoints> apply(DataSnapshot input) {
            mList.clear();
            for(DataSnapshot snap : input.getChildren()){
                CheckPoints checkPoints = snap.getValue(CheckPoints.class);
                Log.d(TAG, "apply: " + checkPoints.checkName);
                mList.add(checkPoints);
            }
            return mList;
        }
    }

    public void SubmitClockDataIn(ClockDetails clockData){
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> userData = new HashMap<>();
        userData.put("status", clockData.status);
        userData.put("timeIn", clockData.getTimeIn());
        userData.put("latitude", clockData.latitude);
        userData.put("longitude", clockData.longitude);
        data.put(clockData.getTimeIn(), clockData);
        DatabaseReference mDatabase = mDb.child(CLOCKDETAILS).child(mUserId);
        DatabaseReference ref = mDb.child(USERS).child(mUserId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User mUser1 = dataSnapshot.getValue(User.class);

                if (!dataSnapshot.child("status").getValue(Boolean.class)) {
                    try {
                        mDatabase.updateChildren(data).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                ref.updateChildren(userData);
                                resultCallBack.postValue(CALLBACK_SUCCESSFUL);
                            } else {
                                resultCallBack.postValue(CALLBACK_FAILED);
                            }

                        });
                    } catch (DatabaseException e) {
                        Log.d(TAG, "onDataChange: " + e.getMessage());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void SubmitClockDataOut(ClockDetails clockData){
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> userData = new HashMap<>();
        userData.put("status", clockData.status);
        userData.put("timeOut", clockData.getTimeOut());
        data.put(clockData.getTimeIn(), clockData);
        DatabaseReference mDatabase = mDb.child(CLOCKDETAILS).child(mUserId);
        DatabaseReference ref = mDb.child(USERS).child(mUserId);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                if(user.getStatus()){
        try {


            mDatabase.updateChildren(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ref.updateChildren(userData);
                    resultCallBack.postValue(CALLBACK_SUCCESSFUL);
                } else {
                    resultCallBack.postValue(CALLBACK_FAILED);
                }
            });
        }catch (DatabaseException e){
            Log.d(TAG, "SubmitClockDataOut: " + e.getMessage());
        }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public LiveData<User> getUserDataStream() {
        return mUserData;
    }

    @Override
    public LiveData<List<ClockDetails>> getUserClockDataStream() {
        return mUserClockData;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    @Override
    public LiveData<List<CheckPoints>> getCheckPoints() {
        return mCheckPointData;
    }

    @Override
    public LiveData<String> getCallBack() {
        return resultCallBack;
    }
}
