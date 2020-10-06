package com.example.clockapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.clockapp.R;
import com.example.clockapp.viewmodel.ClockViewModel;
import com.example.clockapp.models.ClockDetails;
import com.example.clockapp.models.User;
import com.example.clockapp.recview.Divider;
import com.example.clockapp.recview.MyClockAdapter;
import com.example.clockapp.screen.MainScreen;
import com.example.clockapp.viewmodel.LocationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements MainScreen {

    private static final String TAG = "DetailsFragment";
    private LocationViewModel mViewModel;
    private final static int DATA_FETCHING_INTERVAL=5*1000; //5 seconds
    private int timer = 5;
    private RecyclerView recView;
    private MyClockAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private long mLastFetchedDataTimeStamp;

    private final Observer<User> userObserver = coinModels -> upDataUserData(coinModels);
    private final Observer<List<ClockDetails>> clockDetailObserver = clockModel -> upDateClockDetail(clockModel);
    public static DetailsFragment newInstance() { return new DetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        mView = view;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViews();
        mViewModel = ViewModelProviders.of(getActivity()).get(LocationViewModel.class);
        mViewModel.fetchUserClockData();
        mViewModel.getUserClockDetails().observe(this, clockDetailObserver);

    }

    private void bindViews() {
        // Toolbar toolbar = findViewById(R.id.toolbar);
        recView = mView.findViewById(R.id.recView);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                Log.d(TAG, "\tNot fetching from network because interval didn't reach");
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            mViewModel.fetchUserClockData();
        });
        mAdapter = new MyClockAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(lm);
        recView.setAdapter(mAdapter);
//        recView.addItemDecoration(new Divider(getActivity()));
        FloatingActionButton fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> recView.smoothScrollToPosition(0));

    }

    @Override
    public void upDateData(List<ClockDetails> data) {


    }

    @Override
    public void upDataUserData(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
    }

    @Override
    public void upDateClockDetail(List<ClockDetails> data) {
        mLastFetchedDataTimeStamp=System.currentTimeMillis();
        mAdapter.setItems(data);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void setError(String msg) {
        Toast.makeText(getActivity(), "Error:" + msg, Toast.LENGTH_LONG).show();
    }
}
