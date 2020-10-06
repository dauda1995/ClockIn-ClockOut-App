package com.example.clockapp.auth;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.clockapp.models.User;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    LiveData<User> authenticatedUserLiveData;
    LiveData<User> createdUserLiveData;
    LiveData<User> signUpUserLiveData;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    void signInWithGoogle(User user) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(user);
    }

    void createUser(User userAuthenticated) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(userAuthenticated);
    }

    void signUpWithEmail(User user){
        signUpUserLiveData = authRepository.firebaseSignUpWithEmailandPassword(user);
    }
}