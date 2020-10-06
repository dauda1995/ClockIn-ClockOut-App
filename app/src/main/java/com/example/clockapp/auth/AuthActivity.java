package com.example.clockapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.clockapp.dashboard.DashBoardActivity;
import com.example.clockapp.R;
import com.example.clockapp.models.User;

import static com.example.clockapp.utils.Constants.USER;


public class AuthActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    EditText mEmail;
    EditText mPassword;
    private static final String TAG = "AuthActivity";
  //  private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in_login);
        initSignInButton();
        initAuthViewModel();

    }

    private void initSignInButton() {
        mEmail = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        Button googleSignInButton = findViewById(R.id.login);
        Button emailSignUpButton = findViewById(R.id.sign_up);



        emailSignUpButton.setOnClickListener(v -> {
            String emailValue = mEmail.getText().toString().trim();
            String passwordValue = mPassword.getText().toString().trim();
            if(validateForm(emailValue, passwordValue)){
                User user = new User(emailValue, passwordValue);
              signUpWithEmailCredentials(user);
            }

        });
        googleSignInButton.setOnClickListener(v -> {
            String emailValue = mEmail.getText().toString().trim();
            String passwordValue = mPassword.getText().toString().trim();
            if(validateForm(emailValue, passwordValue)){
                User user =new User(emailValue, passwordValue);
                Log.d(TAG, "initSignInButton: "+ user.getEmail() + ", " + user.getPassword());
                signInWithGoogleAuthCredential(user);
            }
        });
    }

    private void signUpWithEmailCredentials(User user){
        authViewModel.signUpWithEmail(user);
        authViewModel.signUpUserLiveData.observe(this, userCredientials -> {
            Log.d(TAG, "signUpWithEmailCredentials: xxxxxxx");
            if(!userCredientials.isCreated){
                toastMessage(userCredientials.getStaffId());
            }else {
                Log.d(TAG, "signUpWithEmailCredentials: yyyyyy");
                createNewUser(userCredientials);
            }
        });
    }

    private void initAuthViewModel() {
        authViewModel =  ViewModelProviders.of(this).get(AuthViewModel.class);
    }


    private boolean validateForm(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.invalid_username));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.invalid_password));
            return false;
        }
        else {
            mEmail.setError(null);
            mPassword.setError(null);

            return true;
        }
    }


    private void signInWithGoogleAuthCredential(User user) {
        authViewModel.signInWithGoogle(user);
        authViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser.isNew) {
                Log.d(TAG, "signInWithGoogleAuthCredential: user is new " + authenticatedUser.isNew);
                createNewUser(user);
            } else {
                Log.d(TAG, "signInWithGoogleAuthCredential: user is not new " + authenticatedUser.isNew);
                goToMainActivity(authenticatedUser);
            }
        });
    }

    private void createNewUser(User mUser) {
        authViewModel.createUser(mUser);
        authViewModel.createdUserLiveData.observe(this, user -> {
            if (!user.isCreated) {
                toastMessage(user.getStaffId());
            }
            goToMainActivity(user);
        });
    }

    private void toastMessage(String name) {
        Toast.makeText(this, "Hi " + name + "!\n" + "Your account was successfully created.", Toast.LENGTH_LONG).show();
    }

    private void goToMainActivity(User user) {
        Log.d(TAG, "goToMainActivity: you are about to be logged in " + user.uid);
        Intent intent = new Intent(AuthActivity.this, DashBoardActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();
    }
}