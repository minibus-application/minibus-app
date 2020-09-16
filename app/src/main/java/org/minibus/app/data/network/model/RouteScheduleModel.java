package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.schedule.RouteScheduleResponse;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.data.network.pojo.user.UserResponse;

import java.util.List;

import io.reactivex.Single;

public class RouteScheduleModel extends BaseModel {

    public RouteScheduleModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<RouteScheduleResponse> doGetRouteScheduleData(String date, String routeId) {
        return getClient().getApiService().getRouteScheduleData(date, routeId);
    }

    public Single<UserResponse> doPostRouteTripData(String authToken, String depDate, String id, int seatsCount) {
        return getClient().getApiService().postRouteTripData("Bearer ".concat(authToken), depDate, id, seatsCount);
    }
}
