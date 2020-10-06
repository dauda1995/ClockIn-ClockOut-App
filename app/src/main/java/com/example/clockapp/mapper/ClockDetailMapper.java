package com.example.clockapp.mapper;

import androidx.annotation.NonNull;

import com.example.clockapp.models.ClockDetails;
import com.example.clockapp.entities.ClockEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ClockDetailMapper extends ObjectMapper {

    public ArrayList<ClockEntity> mapJSONToEntity(String jsonStr) {
        ArrayList<ClockEntity> data = null;

        try {
            data = readValue(jsonStr, new TypeReference<ArrayList<ClockEntity>>() {
            });
        } catch (Exception e) {
        }
        return data;
    }

//    @NonNull
//    public List<ClockDetails> mapEntityToModel(List<ClockEntity> datum) {
//        final ArrayList<ClockDetails> listData = new ArrayList<>();
//        ClockEntity entity;
//        for (int i = 0; i < datum.size(); i++) {
//            entity = datum.get(i);
//            listData.add(new ClockDetails(entity.getId(), entity.getName(),
//                    entity.isUserId(), entity.getLat(), entity.getLng(),
//                    entity.getTimeIn(), entity.getTimeOut(), entity.isStatus()));
//        }
//
//        return listData;
//    }

    public String mapEntitiesToString(List<ClockEntity> data)
    {
        try {
            return writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
