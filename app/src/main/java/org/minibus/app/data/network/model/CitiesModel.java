package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.city.CityResponse;

import java.util.List;

import io.reactivex.Single;

public class CitiesModel extends BaseModel {

    public CitiesModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<CityResponse> doGetCitiesData() {
        return getClient().getApiService().getCitiesData();
    }
}
