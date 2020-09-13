package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.schedule.BusScheduleResponse;

import io.reactivex.Single;

public class BusScheduleModel {

    public Single<BusScheduleResponse> doGetBusScheduleData(long cityId, String date) {
        return AppApiClient.getApiService().getScheduleData(String.valueOf(cityId), date);
    }
}
