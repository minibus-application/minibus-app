package org.minibus.app.ui.cities.departure;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.pojo.BaseResponse;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.city.CityResponse;
import org.minibus.app.helpers.ApiErrorHelper;
import org.minibus.app.ui.cities.BaseCitiesPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public class DepartureCitiesPresenter<V extends DepartureCitiesContract.View> extends BaseCitiesPresenter<V>
        implements DepartureCitiesContract.Presenter<V> {

    @Inject
    AppStorageManager storage;

    @Inject
    public DepartureCitiesPresenter(CitiesModel citiesModel) {
        super(citiesModel);
    }

    @Override
    public void onStart() {
        Single<BaseResponse<List<City>>> observable;

        if (storage.isRouteStored()) observable = getFilteredCitiesDataObservable(storage.getArrivalCity().getId());
        else observable = getCitiesDataObservable();

        addSubscription(observable
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .subscribeWith(new DisposableSingleObserver<BaseResponse<List<City>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<City>> response) {
                        cities = response.getResult();
                        long prevSelectedCityId = storage.isRouteStored()
                                ? storage.getDepartureCity().getLongId()
                                : AppConstants.DEFAULT_SELECTED_CITY_ID;

                        if (!response.getResult().isEmpty()) getView().ifAlive(v -> v.setCitiesData(cities, prevSelectedCityId));
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
