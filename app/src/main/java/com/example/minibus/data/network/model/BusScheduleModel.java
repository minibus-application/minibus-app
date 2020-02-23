package com.example.minibus.data.network.model;

import com.example.minibus.data.network.AppApiClient;
import com.example.minibus.data.network.pojo.schedule.BusScheduleResponse;

import io.reactivex.Single;

public class BusScheduleModel {

    public Single<BusScheduleResponse> doGetBusScheduleData(int busStopId, String date) {
        return AppApiClient.getApiService().getScheduleData(String.valueOf(busStopId), date);
    }
}
