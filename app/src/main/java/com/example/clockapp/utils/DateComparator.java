package com.example.clockapp.utils;


import com.example.clockapp.models.CheckPoints;
import java.util.Comparator;

public class DateComparator implements Comparator<CheckPoints> {
    public int compare(CheckPoints item1, CheckPoints item2) {
//        Long l = Long.valueOf(item2.getCreateDate())-Long.valueOf(item1.getCreateDate());
//        return l.intValue();
        return 0;
    }
}