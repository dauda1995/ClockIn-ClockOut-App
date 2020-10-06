package com.example.clockapp.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.clockapp.models.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.clockapp.utils.Constants.USERS;
import static com.example.clockapp.utils.HelperClass.logErrorMessage;

@SuppressWarnings("ConstantConditions")
class AuthRepository {
    private static final String TAG = "AuthRepository";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersRef = rootRef.child(USERS);

    MutableLiveData<User> firebaseSignInWithGoogle(User user) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    String staffemail = firebaseUser.getEmail();
                    User userDetails = new User(uid, staffemail, false);
                    userDetails.isNew = isNewUser;
                    authenticatedUserMutableLiveData.setValue(userDetails);
                }
            } else {
                logErrorMessage(authTask.getException().getMessage());

            }
        });
        return authenticatedUserMutableLiveData;
    }



    MutableLiveData<User> firebaseSignUpWithEmailandPassword(User userAuth){
        MutableLiveData<User> signUpUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(userAuth.getEmail(), userAuth.getPassword()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                User user = new User();
                user.uid = task.getResult().getUser().getUid();
                user.staffId = task.getResult().getUser().getEmail();
                user.isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                user.isCreated = true;
                signUpUserMutableLiveData.setValue(user);
//                    Map<String, Object> usermap = user.toMap();
//                    usersRef.child(user.uid).setValue(usermap);
                Log.d(TAG, "onComplete: data has been set");
            }
        });
        return signUpUserMutableLiveData;
    }

    MutableLiveData<User> createUserInFirestoreIfNotExists(User authenticatedUser) {
        MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();
        DatabaseReference uidRef = usersRef.child(authenticatedUser.uid);
//        uidRef.runTransaction(new Transaction.Handler() {
//            @NonNull
//            @Override
//            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                if (!mutableData.hasChildren()) {
//                    authenticatedUser.isCreated = true;
//                    newUserMutableLiveData.setValue(authenticatedUser);
//                    mutableData.setValue(authenticatedUser);
//                }else{
//                    newUserMutableLiveData.setValue(authenticatedUser);
//                }
//                return Transaction.success(mutableData);
//            }
//            @Override
//            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                if(!b){
//                    logErrorMessage(databaseError.getMessage());
//                }
//                Log.d(TAG, "onComplete: user is successfully signed up");
//            }
//        });
        Log.d(TAG, "createUserInFirestoreIfNotExists: about to load contents to detabaset " + authenticatedUser.uid);
        uidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    authenticatedUser.isCreated =true;
                    newUserMutableLiveData.setValue(authenticatedUser);
                    uidRef.setValue(authenticatedUser);
                    Log.d(TAG, "onDataChange: uploaded");
                }else{
                    User details = dataSnapshot.getValue(User.class);
                    newUserMutableLiveData.setValue(details);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return newUserMutableLiveData;
    }
}