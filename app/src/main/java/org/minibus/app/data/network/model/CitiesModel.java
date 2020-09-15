package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.BaseResponse;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.city.CityResponse;

import java.util.List;

import io.reactivex.Single;

public class CitiesModel extends BaseModel {

    public CitiesModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<BaseResponse<List<City>>> doGetAllCitiesData() {
        return getClient().getApiService().getAllCitiesData();
    }

    public Single<BaseResponse<List<City>>> doGetFilteredCitiesData(String cityId) {
        return getClient().getApiService().getArrivalCitiesData(cityId);
    }
}
