package org.minibus.app.ui.cities.arrival;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.pojo.BaseResponse;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.helpers.ApiErrorHelper;
import org.minibus.app.ui.cities.BaseCitiesPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ArrivalCitiesPresenter<V extends ArrivalCitiesContract.View> extends BaseCitiesPresenter<V>
        implements ArrivalCitiesContract.Presenter<V> {

    @Inject
    AppStorageManager storage;

    @Inject
    public ArrivalCitiesPresenter(CitiesModel citiesModel) {
        super(citiesModel);
    }

    @Override
    public void onStart() {
        addSubscription(getFilteredCitiesDataObservable(storage.getDepartureCity().getId())
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .subscribeWith(new DisposableSingleObserver<BaseResponse<List<City>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<City>> response) {
                        cities = response.getResult();
                        long prevSelectedCityId = storage.isRouteStored()
                                ? storage.getArrivalCity().getLongId()
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
