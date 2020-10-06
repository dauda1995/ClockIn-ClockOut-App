package com.example.clockapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.clockapp.dashboard.DashBoardActivity;
import com.example.clockapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends TrackingActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static final String TAG = "LoginActivity";

    EditText usernameEditText;
    EditText passwordEditText;
    EditText staffEditText;
    Button loginButton;
    Button signUpButton;
    ProgressBar loadingProgressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in_login);


        mAuth = FirebaseAuth.getInstance();


        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        staffEditText  =findViewById(R.id.staffId);

        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.sign_up);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });




    }

    private boolean validateForm(String email, String password , String staffid) {
        if (TextUtils.isEmpty(email)) {
            usernameEditText.setError(getString(R.string.invalid_username));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.invalid_password));
            return false;
        } else if(TextUtils.isEmpty(staffid)){
            staffEditText.setError(getString(R.string.invalid_staffid));
            return false;
        }
        else {
            usernameEditText.setError(null);
            passwordEditText.setError(null);
            staffEditText.setError(null);
            return true;
        }
    }

    private void onAuthSuccess(FirebaseUser firebaseUser) {
        Log.d(TAG, "LoginActivity: " + firebaseUser.getUid());

        startActivity(new Intent(this, DashBoardActivity.class));
//        startActivity(new Intent(this, FaultLogActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }

    }
    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void signIn(){

        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String staffId = staffEditText.getText().toString().trim();

        if(validateForm(email, staffId, password)){
            hideButtons();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        onAuthSuccess(task.getResult().getUser());
                    } else {
                        showButtons();
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void hideButtons(){
        loginButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
    }

    private void showButtons(){
        loginButton.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.VISIBLE);
    }

    private void signUp(){


        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String staffId = staffEditText.getText().toString().trim();

        if(validateForm(email, staffId, password)) {
            hideButtons();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       String email = task.getResult().getUser().getEmail();
                       String username = email;
                       String staffId = staffEditText.getText().toString().trim();
                       String timeIn = "";
                       String timeOut = "";
                       if (email != null && email.contains("@")) {
                           username = email.split("@")[0];
                       }
                       User user = new User(username, staffId);
                       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                       mDatabase.child("users").child(task.getResult().getUser().getUid()).setValue(user);
                       onAuthSuccess((task.getResult().getUser()));


                   } else {
                       showButtons();
                       Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.login:
                signIn();
                break;
            case R.id.sign_up:
                signUp();
                break;
        }
    }
}
