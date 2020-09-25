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

    @Inject
    ArrivalCitiesPresenter<ArrivalCitiesContract.View> presenter;

    public static final int REQ_CODE = AppConstants.ARR_CITIES_FRAGMENT_REQ_CODE;
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
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected String getTitle() {
        return getMainActivity().getResources().getString(R.string.arrival_cities_title);
    }

    /**
     * View contract methods
     */

    @Override
    public void changeCity(City city) {
        listener.onArrivalCityItemClicked(city);
    }

    @Override
    public void setCitiesData(List<City> cities, String prevSelectedCityId) {
        super.setData(cities, prevSelectedCityId);
    }

    public interface OnCityClickListener {
        void onArrivalCityItemClicked(City city);
    }
}
