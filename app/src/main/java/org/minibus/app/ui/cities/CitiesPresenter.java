package org.minibus.app.ui.cities;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.pojo.city.CityResponse;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.helpers.ApiErrorHelper;
import org.minibus.app.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CitiesPresenter<V extends CitiesContract.View> extends BasePresenter<V>
        implements CitiesContract.Presenter<V> {

    protected CitiesModel citiesModel;
    protected List<City> cities;
    protected long prevSelectedCityId;

    @Inject
    AppStorageManager storage;

    @Inject
    public CitiesPresenter(CitiesModel citiesModel) {
        this.citiesModel = citiesModel;
    }

    @Override
    public void onCitySelect(City city) {
//        if (!storage.isDirectionStored() || !storage.isStoredArrivalCityMatches(selectedCity.getArrivalCityName())) {
//            getView().ifAlive(v -> v.changeArrivalBusStop(getArrivalCityFinalBusStop(selectedCity),
//                    getDepartureCityStartBusStop(selectedCity)));
//        }
        getView().ifAlive(v -> v.changeCity(city));
        getView().ifAlive(V::close);
    }

    @Override
    public void onCloseButtonClick() {
        getView().ifAlive(V::close);
    }

    protected Single<CityResponse> getCitiesDataObservable() {
        return citiesModel.doGetCitiesData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // have to do those hacks due to the lack of city types
//    private City getArrivalCityFinalBusStop(City departureCity) {
//        return cities.stream()
//                .filter(city -> !city.getCities().contains(departureCity))
//                .findFirst()
//                .get()
//                .getStartBusStop();
//    }
//
//    private City getDepartureCityStartBusStop(City departureCity) {
//        return cities.stream()
//                .filter(city -> city.getCities().contains(departureCity))
//                .findFirst()
//                .get()
//                .getStartBusStop();
//    }
}
