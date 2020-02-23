package com.example.minibus.data.network.model;

import com.example.minibus.data.network.AppApiClient;
import com.example.minibus.data.network.pojo.city.CityResponse;

import java.util.List;

import io.reactivex.Single;

public class CitiesModel {

    public Single<List<CityResponse>> doGetCitiesData() {
        return AppApiClient.getApiService().getCitiesData();
    }
}
