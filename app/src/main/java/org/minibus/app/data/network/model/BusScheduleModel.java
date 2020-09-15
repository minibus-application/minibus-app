package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.schedule.BusScheduleResponse;

import io.reactivex.Single;

public class BusScheduleModel extends BaseModel {

    public BusScheduleModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<BusScheduleResponse> doGetBusScheduleData(String date, String routeId) {
        return getClient().getApiService().getScheduleData(date, routeId);
    }
}
