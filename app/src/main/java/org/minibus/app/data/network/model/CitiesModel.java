package org.minibus.app.data.network.model;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.pojo.city.City;

import java.util.List;

import io.reactivex.Single;

public class CitiesModel extends Model {

    public CitiesModel(AppApiClient appApiClient) {
        super(appApiClient);
    }

    public Single<List<City>> doGetAllCitiesData() {
        return getClient().getApiService().getAllCitiesData();
    }

    public Single<List<City>> doGetFilteredCitiesData(String cityId) {
        return getClient().getApiService().getFilteredCitiesData(cityId);
    }
}
