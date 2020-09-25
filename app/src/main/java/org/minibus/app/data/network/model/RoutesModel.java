package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.route.Route;

import java.util.List;

import io.reactivex.Single;

public class RoutesModel extends Model {

    public RoutesModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<Route> doGetRouteData(String depCityId, String arrCityId) {
        return getClient().getApiService().getRouteData(depCityId, arrCityId);
    }

    public Single<List<Route>> doGetAllRoutesData() {
        return getClient().getApiService().getAllRoutesData();
    }

}
