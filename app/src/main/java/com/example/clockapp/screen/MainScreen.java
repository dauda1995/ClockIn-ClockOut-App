package com.example.clockapp.screen;

import com.example.clockapp.models.User;
import com.example.clockapp.models.ClockDetails;

import java.util.List;

public interface MainScreen {

    void upDateData(List<ClockDetails> data);

    void upDataUserData(User user);

    void upDateClockDetail(List<ClockDetails> data);

    void setError(String msg);
}


