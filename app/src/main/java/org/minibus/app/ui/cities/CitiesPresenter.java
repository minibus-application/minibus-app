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

    private CitiesModel citiesModel;
    private List<City> cities;

    @Inject
    AppStorageManager storage;

    @Inject
    public CitiesPresenter(CitiesModel citiesModel) {
        this.citiesModel = citiesModel;
    }

    @Override
    public void onStart() {
        addSubscription(getCitiesDataObservable()
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .subscribeWith(new DisposableSingleObserver<CityResponse>() {
                    @Override
                    public void onSuccess(CityResponse response) {
                        cities = response.getCities();
                        long prevSelectedCityId = storage.isDirectionStored()
                                ? storage.getDepartureCity().getId()
                                : AppConstants.DEFAULT_SELECTED_CITY_ID;

                        // add selected id setting
                        if (!response.isEmpty()) getView().ifAlive(v -> v.setCitiesData(cities));
                        else getView().ifAlive(V::showEmptyView);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                        getView().ifAlive(V::showEmptyView);
                    }
                }));
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

    private Single<CityResponse> getCitiesDataObservable() {
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
