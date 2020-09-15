package org.minibus.app.ui.schedule;

import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.BusScheduleModel;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.model.RoutesModel;
import org.minibus.app.data.network.pojo.BaseResponse;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.schedule.BusScheduleResponse;
import org.minibus.app.data.network.pojo.schedule.BusTrip;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BasePresenter;
import org.minibus.app.helpers.ApiErrorHelper;
import org.minibus.app.helpers.AppDatesHelper;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class BusSchedulePresenter<V extends BusScheduleContract.View> extends BasePresenter<V>
        implements BusScheduleContract.Presenter<V> {

    private BusScheduleModel busScheduleModel;
    private CitiesModel citiesModel;
    private RoutesModel routesModel;

    @Inject
    AppStorageManager storage;

    @Inject
    public BusSchedulePresenter(BusScheduleModel busScheduleModel, CitiesModel citiesModel, RoutesModel routesModel) {
        this.busScheduleModel = busScheduleModel;
        this.citiesModel = citiesModel;
        this.routesModel = routesModel;
    }

    @Override
    public void onDepartureCityClick() {
        getView().ifAlive(V::openDepartureCities);
    }

    @Override
    public void onArrivalCityClick() {
        if (storage.isDepartureCityStored()) {
            getView().ifAlive(V::openArrivalCities);
        } else {
            getView().ifAlive(v -> v.showError(R.string.error_departure_first));
        }
    }

    @Override
    public void onProfileIconClick() {
        if (storage.isAuthorised()) getView().ifAlive(V::openProfile);
        else getView().ifAlive(V::openLogin);
    }

    @Override
    public void onFilterFabClick() {
        getView().ifAlive(V::toggleFilter);
    }

    @Override
    public void onJumpTopFabClick() {
        getView().ifAlive(V::jumpTop);
    }

    @Override
    public void onBackPressed() {
        getView().ifAlive(V::finish);
    }

    @Override
    public void onUserBookedBusTrip(String departureDate) {
        onRefresh(departureDate);

        getView().ifAlive(V::updateProfileBadge);
        getView().ifAlive(v -> v.showAction(R.string.success_booking_title,
                R.string.success_booking_message,
                R.string.profile_title,
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    getView().ifAlive(V::openProfile);
                }));
    }

    @Override
    public void onUserLoggedIn() {
        getView().ifAlive(V::updateProfileBadge);
    }

    @Override
    public void onUserLoggedOut() {
        getView().ifAlive(V::updateProfileBadge);
    }

    @Override
    public void onUserBookingsUpdate() {
        getView().ifAlive(V::updateProfileBadge);
    }

    @Override
    public void onCreateProfileBadge() {
        if (storage.isAuthorised()) {
            final int value = storage.getUserBookingsCount();
            getView().ifAlive(v -> v.setProfileBadge(value));

            Timber.d("Set profile badge value to %d", value);
        }
    }

    @Override
    public void onRefresh(String depDate) {
        if (storage.isRouteStored()) {
            addSubscription(getBusScheduleObservable(depDate, storage.getRoute().getId())
                    .doFinally(() -> getView().ifAlive(V::hideRefresh))
                    .subscribeWith(getBusScheduleObserver()));
        } else {
            getView().ifAlive(V::hideRefresh);
            getView().ifAlive(v -> v.showError(R.string.error_complete_route));
        }
    }

    @Override
    public void onStart(String depDate) {
        if (storage.isRouteStored()) {
            Route storedRoute = storage.getRoute();
            getView().ifAlive(v -> v.setDirection(storedRoute.getDepartureCity().getFullName(),
                    storedRoute.getArrivalCity().getFullName()));

            addSubscription(getBusScheduleObservable(depDate, storedRoute.getId())
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .subscribeWith(getBusScheduleObserver()));
        } else {
            addSubscription(getAllRoutesDataObservable()
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showLoadingDataDialog))
                    .doFinally(() -> getView().ifAlive(V::hideLoadingDataDialog))
                    .flatMap(response -> {
                        if (response.getResult() != null && !response.getResult().isEmpty()) {
                            Route defaultRoute = response.getResult().get(0);
                            City depCity = defaultRoute.getDepartureCity();
                            City arrCity = defaultRoute.getArrivalCity();

                            storage.setRoute(defaultRoute);

                            getView().ifAlive(v -> v.setDirection(depCity.getFullName(), arrCity.getFullName()));

                            return getBusScheduleObservable(depDate, defaultRoute.getId());
                        } else {
                            getView().ifAlive(V::showEmptyView);
                            throw new Exception("Oops something went wrong!");
                        }
                    })
                    .subscribeWith(getBusScheduleObserver()));
        }
    }

    @Override
    public void onDirectionSwapButtonClick(String depDate) {
        if (storage.isRouteStored()) {
            City newArrivalCity = storage.getRoute().getDepartureCity();
            City newDepartureCity = storage.getRoute().getArrivalCity();

            storage.setDepartureCity(newDepartureCity);
            storage.setArrivalCity(newArrivalCity);

            getView().ifAlive(V::showSwapDirectionAnimation);
            getView().ifAlive(v -> v.setDirection(newDepartureCity.getFullName(), newArrivalCity.getFullName()));

            addSubscription(getCompleteBusScheduleObserver(newDepartureCity.getId(), newArrivalCity.getId(), depDate));
        } else {
            getView().ifAlive(v -> v.showError(R.string.error_complete_route));
        }
    }

    @Override
    public void onDateClick(String depDate) {
        if (storage.isRouteStored()) {
            addSubscription(getBusScheduleObservable(depDate, storage.getRoute().getId())
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .doFinally(() -> getView().ifAlive(V::jumpTop))
                    .subscribeWith(getBusScheduleObserver()));
        } else {
            getView().ifAlive(v -> v.showError(R.string.error_complete_route));
        }
    }

    @Override
    public void onDepartureCityChange(City depCity, String depDate) {
        storage.setDepartureCity(depCity);
        getView().ifAlive(v -> v.setDepartureCity(depCity.getFullName()));

        if (storage.isArrivalCityStored()) {
            addSubscription(getCompleteBusScheduleObserver(depCity.getId(), storage.getArrivalCity().getId(), depDate));
        }
    }

    @Override
    public void onArrivalCityChange(City arrCity, String depDate) {
        storage.setArrivalCity(arrCity);
        getView().ifAlive(v -> v.setArrivalCity(arrCity.getFullName()));

        addSubscription(getCompleteBusScheduleObserver(storage.getDepartureCity().getId(), arrCity.getId(), depDate));
    }

    @Override
    public void onFilterCollapsed() {
        if (storage.isRouteStored()) {
            getView().ifAlive(v -> v.setDirectionDescription(storage.getRoute().getDescription()));
        } else {
            onFilterExpanded();
        }
    }

    @Override
    public void onFilterExpanded() {
        getView().ifAlive(v -> v.setDirectionDescription(R.string.bus_schedule_filter_title));
    }

    @Override
    public void onBusTripSelectButtonClick(String depDate, long id, int pos, String routeId) {
        if (storage.isAuthorised()) {
            addSubscription(getBusScheduleObservable(depDate, routeId)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showBusTripLoading))
                    .doFinally(() -> getView().ifAlive(V::hideBusTripLoading))
                    .subscribeWith(new DisposableSingleObserver<BusScheduleResponse>() {
                        @Override
                        public void onSuccess(BusScheduleResponse response) {
                            getView().ifAlive(v -> v.setBusScheduleData(response.getBusTrips(), response.getRoute()));

                            Optional<BusTrip> optBusTrip = response.getBusTripById(id);

                            if (optBusTrip.isPresent()) {
                                String date = AppDatesHelper.formatDate(depDate,
                                        AppDatesHelper.DatePattern.API_SCHEDULE_REQUEST,
                                        AppDatesHelper.DatePattern.SUMMARY);

                                getView().ifAlive(v -> v.openBusTripSummary(optBusTrip.get(), storage.getRoute(), date));
                            } else {
                                getView().ifAlive(v -> v.showError(R.string.error_trip_not_available));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                        }
                    }));
        } else {
            getView().ifAlive(v -> v.showAction(R.string.warning_unauthorized_message,
                    R.string.login_title,
                    ((dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        getView().ifAlive(V::openLogin);
                    })));
        }
    }

    private DisposableSingleObserver<BusScheduleResponse> getBusScheduleObserver() {
        return new DisposableSingleObserver<BusScheduleResponse>() {
            @Override
            public void onSuccess(BusScheduleResponse response) {
                getView().ifAlive(v -> v.setBusScheduleData(response.getBusTrips(), response.getRoute()));
            }

            @Override
            public void onError(Throwable throwable) {
                getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                getView().ifAlive(V::showEmptyView);
            }
        };
    }

    private DisposableSingleObserver<BusScheduleResponse> getCompleteBusScheduleObserver(String depCityId, String arrCityId, String depDate) {
        return getRoutesDataObservable(depCityId, arrCityId)
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .doFinally(() -> getView().ifAlive(V::jumpTop))
                .flatMap(routesResponse -> {
                    Route route = routesResponse.getResult();
                    storage.setRoute(route);
                    return getBusScheduleObservable(depDate, routesResponse.getResult().getId());
                })
                .subscribeWith(getBusScheduleObserver());
    }

    private Single<BusScheduleResponse> getBusScheduleObservable(String date, String routeId) {
        return busScheduleModel.doGetBusScheduleData(date, routeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<BaseResponse<Route>> getRoutesDataObservable(String depCityId, String arrCityId) {
        return routesModel.doGetRoutesData(depCityId, arrCityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<BaseResponse<List<Route>>> getAllRoutesDataObservable() {
        return routesModel.doGetAllRoutesData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
