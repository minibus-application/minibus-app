package org.minibus.app.ui.cities.arrival;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.minibus.app.AppConstants;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.R;
import org.minibus.app.ui.cities.BaseCitiesFragment;

import java.util.List;

import javax.inject.Inject;

public class ArrivalCitiesFragment extends BaseCitiesFragment implements ArrivalCitiesContract.View {

    public static final int REQ_CODE = AppConstants.ARR_CITIES_FRAGMENT_REQ_CODE;
    public static final String CITY_KEY = "key_arrival_city";

    @Inject
    ArrivalCitiesPresenter<ArrivalCitiesContract.View> presenter;

    public interface OnCityClickListener {
        void onArrivalCityClicked(City city);
    }

    private OnCityClickListener listener;

    public static ArrivalCitiesFragment newInstance() {
        return new ArrivalCitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listener = (OnCityClickListener) getTargetFragment();
        getActivityComponent().inject(this);
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void changeCity(City city) {
        listener.onArrivalCityClicked(city);
    }

    @Override
    public void setCitiesData(List<City> cities, long prevSelectedCityId) {
        super.setData(cities, prevSelectedCityId);
    }

    @Override
    protected String getTitle() {
        return getMainActivity().getResources().getString(R.string.arrival_cities_title);
    }
}
