package org.minibus.app.ui.cities.departure;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.pojo.city.CityResponse;
import org.minibus.app.helpers.ApiErrorHelper;
import org.minibus.app.ui.cities.CitiesContract;
import org.minibus.app.ui.cities.CitiesPresenter;

import javax.inject.Inject;

import io.reactivex.observers.DisposableSingleObserver;

public class DepartureCitiesPresenter<V extends DepartureCitiesContract.View> extends CitiesPresenter<V>
        implements DepartureCitiesContract.Presenter<V> {

    @Inject
    AppStorageManager storage;

    @Inject
    public DepartureCitiesPresenter(CitiesModel citiesModel) {
        super(citiesModel);
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

                        if (!response.isEmpty()) getView().ifAlive(v -> v.setCitiesData(cities, prevSelectedCityId));
                        else getView().ifAlive(V::showEmptyView);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                        getView().ifAlive(V::showEmptyView);
                    }
                }));
    }
}
