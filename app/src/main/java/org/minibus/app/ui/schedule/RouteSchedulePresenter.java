package org.minibus.app.ui.schedule;

import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.RouteScheduleModel;
import org.minibus.app.data.network.model.RoutesModel;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.schedule.RouteScheduleResponse;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BasePresenter;
import org.minibus.app.helpers.ApiErrorHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RouteSchedulePresenter<V extends RouteScheduleContract.View> extends BasePresenter<V>
        implements RouteScheduleContract.Presenter<V> {

    @Inject
    AppStorageManager storage;

    private RouteScheduleModel routeScheduleModel;
    private RoutesModel routesModel;

    @Inject
    public RouteSchedulePresenter(RouteScheduleModel routeScheduleModel, RoutesModel routesModel) {
        this.routeScheduleModel = routeScheduleModel;
        this.routesModel = routesModel;
    }

    @Override
    public void onSortByClick(RouteScheduleAdapter.SortingOption selectedSortingOption) {
        getView().ifAlive(v -> v.openSortingOptions(selectedSortingOption));
    }

    @Override
    public void onDepartureCityFieldClick() {
        getView().ifAlive(V::openDepartureCities);
    }

    @Override
    public void onArrivalCityFieldClick() {
        if (storage.isDepartureCityStored()) {
            getView().ifAlive(V::openArrivalCities);
        } else {
            getView().ifAlive(v -> v.showError(R.string.error_departure_first));
        }
    }

    @Override
    public void onRouteDirectionFabClick() {
        getView().ifAlive(V::toggleRouteDirection);
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
    public void onRouteTripBooked(LocalDate depDate) {
        onRefresh(depDate);

        getView().ifAlive(v -> v.showAction(R.string.success_booking_title,
                R.string.success_booking_message,
                R.string.profile_title,
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    getView().ifAlive(V::openProfile);
                }));
    }

    @Override
    public void onRefresh(LocalDate depDate) {
        if (storage.isRouteStored()) {
            addSubscription(getRouteScheduleObservable(depDate)
                    .doFinally(() -> getView().ifAlive(V::hideRefresh))
                    .subscribeWith(getRouteScheduleObserver()));
        } else {
            getView().ifAlive(V::hideRefresh);
            getView().ifAlive(v -> v.showError(R.string.error_complete_route));
        }
    }

    @Override
    public void onStart(LocalDate depDate) {
        if (storage.isRouteStored()) {
            Route storedRoute = storage.getRoute();

            getView().ifAlive(v -> v.setRouteDirection(storedRoute.getDepartureCity().getFullName(),
                    storedRoute.getArrivalCity().getFullName()));

            addSubscription(getRouteScheduleObservable(depDate)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .subscribeWith(getRouteScheduleObserver()));
        } else {
            addSubscription(getAllRoutesDataObservable()
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showLoadingDataDialog))
                    .doFinally(() -> getView().ifAlive(V::hideLoadingDataDialog))
                    .flatMap(response -> {
                        Route defaultRoute = response.get(0);
                        City depCity = defaultRoute.getDepartureCity();
                        City arrCity = defaultRoute.getArrivalCity();

                        storage.setRoute(defaultRoute);

                        getView().ifAlive(v -> v.setRouteDirection(depCity.getFullName(), arrCity.getFullName()));

                        return getRouteScheduleObservable(depDate);
                    })
                    .subscribeWith(getRouteScheduleObserver()));
        }
    }

    @Override
    public void onCreatedOptionsMenu() {
        getView().ifAlive(v -> v.setOptionsMenu(storage.isAuthorised()));
    }

    @Override
    public void onUserAuthorized() {
        getView().ifAlive(v -> v.setOptionsMenu(true));
    }

    @Override
    public void onProfileMenuItemClick() {
        getView().ifAlive(V::openProfile);
    }

    @Override
    public void onLoginMenuItemClick() {
        getView().ifAlive(V::openLogin);
    }

    @Override
    public void onAboutMenuItemClick() {
        getView().ifAlive(V::openAbout);
    }

    @Override
    public void onLogoutMenuItemClick() {
        getView().ifAlive(v -> v.showQuestion(R.string.warning_logout_message, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            storage.clearUserSession();
            getView().ifAlive(view -> view.setOptionsMenu(false));
        }));
    }

    @Override
    public void onSwapRouteDirectionButtonClick(LocalDate depDate) {
        if (storage.isRouteStored()) {
            City newArrivalCity = storage.getRoute().getDepartureCity();
            City newDepartureCity = storage.getRoute().getArrivalCity();

            addSubscription(getCompleteRouteScheduleObserver(newDepartureCity.getId(), newArrivalCity.getId(), depDate));
        } else {
            getView().ifAlive(v -> v.showError(R.string.error_complete_route));
        }
    }

    @Override
    public void onCalendarDateClick(LocalDate depDate) {
        if (storage.isRouteStored()) {
            addSubscription(getRouteScheduleObservable(depDate)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .doFinally(() -> getView().ifAlive(V::jumpTop))
                    .subscribeWith(getRouteScheduleObserver()));
        } else {
            getView().ifAlive(v -> v.showError(R.string.error_complete_route));
        }
    }

    @Override
    public void onDepartureCityChanged(City depCity, LocalDate depDate) {
        if (storage.isArrivalCityStored()) {
            addSubscription(getCompleteRouteScheduleObserver(depCity.getId(), storage.getArrivalCity().getId(), depDate));
        }
    }

    @Override
    public void onArrivalCityChanged(City arrCity, LocalDate depDate) {
        addSubscription(getCompleteRouteScheduleObserver(storage.getDepartureCity().getId(), arrCity.getId(), depDate));
    }

    @Override
    public void onRouteDirectionCollapsed() {
        if (storage.isRouteStored()) {
            getView().ifAlive(v -> v.setRouteDirectionDescription(storage.getRoute().getDescription()));
        } else {
            onRouteDirectionExpanded();
        }
    }

    @Override
    public void onRouteDirectionExpanded() {
        getView().ifAlive(v -> v.setRouteDirectionDescription(R.string.route_schedule_route_title));
    }

    @Override
    public void onRouteTripSelectButtonClick(LocalDate depDate, String tripId) {
        if (storage.isAuthorised()) {
            addSubscription(getRouteScheduleObservable(depDate)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showRouteTripLoading))
                    .doFinally(() -> getView().ifAlive(V::hideRouteTripLoading))
                    .subscribeWith(new DisposableSingleObserver<RouteScheduleResponse>() {
                        @Override
                        public void onSuccess(RouteScheduleResponse response) {
                            getView().ifAlive(v -> v.setRouteScheduleData(response.getRouteTrips(), response.getRoute()));

                            Optional<RouteTrip> optional = response.getRouteTrips().stream()
                                    .filter(t -> t.getId().equals(tripId)).findFirst();

                            if (optional.isPresent()) {
                                getView().ifAlive(v -> v.openRouteTripSummary(optional.get(), storage.getRoute(), depDate));
                            } else {
                                onError(new UnsupportedOperationException("Time's up, and the bus is probably already too"));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            onRefresh(depDate);
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

    private DisposableSingleObserver<RouteScheduleResponse> getRouteScheduleObserver() {
        return new DisposableSingleObserver<RouteScheduleResponse>() {
            @Override
            public void onSuccess(RouteScheduleResponse response) {
                getView().ifAlive(v -> v.setRouteScheduleData(response.getRouteTrips(), response.getRoute()));
            }

            @Override
            public void onError(Throwable throwable) {
                getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                getView().ifAlive(V::showEmptyView);
            }
        };
    }

    private DisposableSingleObserver<RouteScheduleResponse> getCompleteRouteScheduleObserver(String depCityId, String arrCityId, LocalDate depDate) {
        return getRouteDataObservable(depCityId, arrCityId)
                .doOnSubscribe(disposable -> {
                    getView().ifAlive(V::disableRouteDirection);
                    getView().ifAlive(V::showProgress);
                })
                .doFinally(() -> {
                    getView().ifAlive(V::enableRouteDirection);
                    getView().ifAlive(V::jumpTop);
                })
                .flatMap(response -> {
                    Route stored = storage.getRoute();
                    if (!stored.getDepartureCity().getId().equals(depCityId) || !stored.getArrivalCity().getId().equals(arrCityId)) {
                        storage.setRoute(response);
                        getView().ifAlive(v -> v.setRouteDirection(response.getDepartureCity().getFullName(),
                                response.getArrivalCity().getFullName()));
                    }

                    return getRouteScheduleObservable(depDate);
                })
                .subscribeWith(getRouteScheduleObserver());
    }

    private Single<RouteScheduleResponse> getRouteScheduleObservable(LocalDate date) {
        Route storedRoute = storage.getRoute();

        // have to leave it here because of necessity to update operational days even on error
        getView().ifAlive(v -> v.setOperationalDays(storedRoute.getOperationalDays()));

        return routeScheduleModel.doGetRouteScheduleData(date, storedRoute.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<Route> getRouteDataObservable(String depCityId, String arrCityId) {
        return routesModel.doGetRouteData(depCityId, arrCityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<List<Route>> getAllRoutesDataObservable() {
        return routesModel.doGetAllRoutesData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
