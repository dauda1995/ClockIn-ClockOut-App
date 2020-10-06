package com.example.clockapp.splash;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.clockapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import static com.example.clockapp.utils.Constants.USERS;
import static com.example.clockapp.utils.HelperClass.logErrorMessage;

@SuppressWarnings("ConstantConditions")
class SplashRepository {
    private static final String TAG = "SplashRepository";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private User user = new User();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersRef = rootRef.child(USERS);

    MutableLiveData<User> checkIfUserIsAuthenticatedInFirebase() {
        MutableLiveData<User> isUserAuthenticateInFirebaseMutableLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            user.isAuthenticated = false;
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user);
            Log.d(TAG, "checkIfUserIsAuthenticatedInFirebase: user is not present "  + user.uid);
        } else {
            user.uid = firebaseUser.getUid();
            user.isAuthenticated = true;
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user);
            Log.d(TAG, "checkIfUserIsAuthenticatedInFirebase: user is present " + user.uid);
        }
        return isUserAuthenticateInFirebaseMutableLiveData;
    }

    MutableLiveData<User> addUserToLiveData(String uid) {
        Log.d(TAG, "addUserToLiveData: About to get User info from database");
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        usersRef.child(uid).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if(b){
                    if(dataSnapshot.exists()){
                        User user = dataSnapshot.getValue(User.class);
                        userMutableLiveData.setValue(user);

                    }
                } else {
                    logErrorMessage(databaseError.getMessage());
                }


            }
        });
        return userMutableLiveData;
    }
}