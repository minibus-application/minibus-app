package org.minibus.app.data.network.pojo.city;

import org.minibus.app.data.network.pojo.BaseResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CityResponse extends BaseResponse<ArrayList<City>> {

    public CityResponse(ArrayList<City> cities) {
        super(cities);
    }

    public ArrayList<City> getCities() {
        return getResult();
    }

    public boolean isEmpty() {
        return getCitiesCount() == 0 || !isSucceeded();
    }

    public int getCitiesCount() {
        return getCities() == null ? 0 : getCities().size();
    }

    public List<Long> getCitiesIds() {
        return getCities().stream().map(City::getLongId).collect(Collectors.toList());
    }
}
