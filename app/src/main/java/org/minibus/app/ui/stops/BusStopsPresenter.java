package org.minibus.app.ui.stops;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.pojo.city.CityResponse;
import org.minibus.app.data.network.pojo.city.BusStop;
import org.minibus.app.helpers.ApiErrorHelper;
import org.minibus.app.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BusStopsPresenter<V extends BusStopsContract.View> extends BasePresenter<V>
        implements BusStopsContract.Presenter<V> {

    private CitiesModel citiesModel;
    private List<CityResponse> cities;

    @Inject
    AppStorageManager storage;

    @Inject
    public BusStopsPresenter(CitiesModel citiesModel) {
        this.citiesModel = citiesModel;
    }

    @Override
    public void onStart() {
        addSubscription(getCitiesDataObservable()
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .subscribeWith(new DisposableSingleObserver<List<CityResponse>>() {
                    @Override
                    public void onSuccess(List<CityResponse> citiesResponse) {
                        cities = citiesResponse;
                        int prevSelectedBusStopId = storage.isDirectionStored()
                                ? storage.getDepartureBusStop().getId()
                                : AppConstants.DEFAULT_SELECTED_BUS_STOP_ID;

                        boolean isEmpty = citiesResponse.stream().allMatch(CityResponse::isBusStopsListEmpty);

                        if (!isEmpty) getView().ifAlive(v -> v.setCitiesData(citiesResponse, prevSelectedBusStopId));
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
    public void onDepartureBusStopSelected(BusStop selectedBusStop) {
        if (!storage.isDirectionStored() || !storage.isStoredArrivalCityMatches(selectedBusStop.getArrivalCityName())) {
            getView().ifAlive(v -> v.changeArrivalBusStop(getArrivalCityFinalBusStop(selectedBusStop),
                    getDepartureCityStartBusStop(selectedBusStop)));
        }

        getView().ifAlive(v -> v.changeDepartureBusStop(selectedBusStop));
        getView().ifAlive(V::close);
    }

    @Override
    public void onRefreshButtonClick() {
        onStart();
    }

    @Override
    public void onCloseButtonClick() {
        getView().ifAlive(V::close);
    }

    private Single<List<CityResponse>> getCitiesDataObservable() {
        return citiesModel.doGetCitiesData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // have to do those hacks due to the lack of city types
    private BusStop getArrivalCityFinalBusStop(BusStop departureBusStop) {
        return cities.stream()
                .filter(city -> !city.getBusStops().contains(departureBusStop))
                .findFirst()
                .get()
                .getStartBusStop();
    }

    private BusStop getDepartureCityStartBusStop(BusStop departureBusStop) {
        return cities.stream()
                .filter(city -> city.getBusStops().contains(departureBusStop))
                .findFirst()
                .get()
                .getStartBusStop();
    }
}
