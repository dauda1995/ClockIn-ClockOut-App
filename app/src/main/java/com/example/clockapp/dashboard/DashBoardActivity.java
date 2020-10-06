package com.example.clockapp.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.clockapp.R;

import com.example.clockapp.models.User;
import com.example.clockapp.repository.dao.MyFragmentListenerImpl;
import com.example.clockapp.utils.Utility;
import com.example.clockapp.view.SectionsPagerAdapter;
import com.example.clockapp.viewmodel.ClockViewModel;
import com.google.android.material.tabs.TabLayout;

import static com.example.clockapp.utils.Constants.USER;

public class DashBoardActivity extends AppCompatActivity implements MyFragmentListenerImpl {

    private ClockViewModel mViewModel;
    private static final String TAG = "DashBoardActivity";

    private String username, checkpoint;
    private User mUser;
    private String mUseStaffId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mUser = (User) getIntent().getSerializableExtra(USER);
        username = mUser.getEmail();
        mUseStaffId = mUser.staffId;
        checkpoint = mUser.getCheckPointLocation();
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("ClockIn");
      //  mViewModel= ViewModelProviders.of(this).get(ClockViewModel.class);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }


    @Override
    public void onFabButtonClicked() {

    }

    public String getUserName() {
        return username;
    }

    public String getCheckPoint(){return checkpoint;}

    public String getUseStaffId() {
        return mUseStaffId;
    }

    public User getUser() {
        return mUser;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }

}