package org.minibus.app.ui.cities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.R;

import java.util.List;

public class ArrivalCitiesFragment extends BaseCitiesFragment {

    public static final int REQ_CODE = AppConstants.ARR_CITIES_FRAGMENT_REQ_CODE;
    public static final String CITY_KEY = "key_arrival_city";

    public interface OnCitySelectListener {
        void onArrivalCitySelected(City city);
    }

    private OnCitySelectListener callback;

    public static ArrivalCitiesFragment newInstance() {
        return new ArrivalCitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        callback = (OnCitySelectListener) getTargetFragment();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void changeCity(City city) {
        callback.onArrivalCitySelected(city);
    }

    @Override
    public void setCitiesData(List<City> cities) {
//        long prevSelectedCityId =
//        super.setData(cities, prevSelectedCityId);
    }

    @Override
    protected String getTitle() {
        return getMainActivity().getResources().getString(R.string.arrival_cities_title);
    }
}
