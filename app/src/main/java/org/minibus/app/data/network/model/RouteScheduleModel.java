package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.schedule.RouteScheduleResponse;
import org.minibus.app.data.network.pojo.user.UserResponse;
import org.minibus.app.helpers.AppDatesHelper;

import java.time.LocalDate;

import io.reactivex.Single;

public class RouteScheduleModel extends BaseModel {

    public RouteScheduleModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<RouteScheduleResponse> doGetRouteScheduleData(LocalDate date, String routeId) {
        String formattedDate = AppDatesHelper.formatDate(date, AppDatesHelper.DatePattern.ISO);
        return getClient().getApiService().getRouteScheduleData(formattedDate, routeId);
    }

    public Single<UserResponse> doPostRouteTripData(String authToken, LocalDate depDate, String routeId, String tripId, int seatsCount) {
        String formattedDate = AppDatesHelper.formatDate(depDate, AppDatesHelper.DatePattern.ISO);
        return getClient().getApiService().postRouteTripData("Bearer ".concat(authToken), formattedDate, routeId, tripId, seatsCount);
    }
}
