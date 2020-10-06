package com.example.clockapp.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.clockapp.dashboard.DashBoardActivity;
import com.example.clockapp.R;
import com.example.clockapp.models.User;
import com.example.clockapp.auth.AuthActivity;

import static com.example.clockapp.utils.Constants.USER;

public class SplashActivity extends AppCompatActivity {
    SplashViewModel splashViewModel;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        initSplashViewModel();
        checkIfUserIsAuthenticated();
    }

    private void initSplashViewModel() {
        splashViewModel =ViewModelProviders.of(this).get(SplashViewModel.class);
    }

    private void checkIfUserIsAuthenticated() {
        splashViewModel.checkIfUserIsAuthenticated();
        splashViewModel.isUserAuthenticatedLiveData.observe(this, user -> {
            if (!user.isAuthenticated) {
                goToAuthInActivity();
                finish();
            } else {
                getUserFromDatabase(user.uid);
            }
        });
    }

    private void goToAuthInActivity() {
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    private void getUserFromDatabase(String uid) {
        splashViewModel.setUid(uid);
        splashViewModel.userLiveData.observe(this, user -> {
            Log.d(TAG, "getUserFromDatabase: " + user.checkPointLocation);
            goToMainActivity(user);
            finish();
        });
    }

    private void goToMainActivity(User user) {
        Log.d(TAG, "goToMainActivity: you are about to be logged in " + user.uid);
        Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
    }
}