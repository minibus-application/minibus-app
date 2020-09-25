package org.minibus.app.ui.cities;

import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseCitiesPresenter<V extends BaseCitiesContract.View> extends BasePresenter<V>
        implements BaseCitiesContract.Presenter<V> {

    @Inject
    AppStorageManager storage;

    protected CitiesModel citiesModel;
    protected List<City> cities;

    @Inject
    public BaseCitiesPresenter(CitiesModel citiesModel) {
        this.citiesModel = citiesModel;
    }

    @Override
    public void onCitySelect(City city) {
        getView().ifAlive(v -> v.changeCity(city));
        getView().ifAlive(V::close);
    }

    @Override
    public void onCloseButtonClick() {
        getView().ifAlive(V::close);
    }

    protected Single<List<City>> getCitiesDataObservable() {
        return citiesModel.doGetAllCitiesData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected Single<List<City>> getFilteredCitiesDataObservable(String cityId) {
        return citiesModel.doGetFilteredCitiesData(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
