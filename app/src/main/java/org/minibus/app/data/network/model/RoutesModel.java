package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.BaseResponse;
import org.minibus.app.data.network.pojo.route.Route;

import java.util.List;

import io.reactivex.Single;

public class RoutesModel extends BaseModel {

    public RoutesModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<BaseResponse<Route>> doGetRoutesData(String depCityId, String arrCityId) {
        return getClient().getApiService().getRoutesData(depCityId, arrCityId);
    }

    public Single<BaseResponse<List<Route>>> doGetAllRoutesData() {
        return getClient().getApiService().getAllRoutesData();
    }
}
