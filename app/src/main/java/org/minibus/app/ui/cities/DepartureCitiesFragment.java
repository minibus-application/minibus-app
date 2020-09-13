package org.minibus.app.ui.cities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.R;

public class DepartureCitiesFragment extends BaseCitiesFragment {

    public static final int REQ_CODE = AppConstants.DEP_CITIES_FRAGMENT_REQ_CODE;
    public static final String CITY_KEY = "key_departure_city";

    public interface onCitySelectListener {
        void onDepartureCitySelected(City city);
    }

    private onCitySelectListener callback;

    public static DepartureCitiesFragment newInstance() {
        return new DepartureCitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        callback = (onCitySelectListener) getTargetFragment();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void changeCity(City city) {
        callback.onDepartureCitySelected(city);
    }

    @Override
    protected String getTitle() {
        return getMainActivity().getResources().getString(R.string.departure_cities_title);
    }
}
