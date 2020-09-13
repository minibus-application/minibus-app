package org.minibus.app.ui.schedule;

import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.BusScheduleModel;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.schedule.BusScheduleResponse;
import org.minibus.app.data.network.pojo.schedule.BusTrip;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BasePresenter;
import org.minibus.app.helpers.ApiErrorHelper;
import org.minibus.app.helpers.AppDatesHelper;

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

    @Inject
    AppStorageManager storage;

    @Inject
    public BusSchedulePresenter(BusScheduleModel busScheduleModel, CitiesModel citiesModel) {
        this.busScheduleModel = busScheduleModel;
        this.citiesModel = citiesModel;
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
        if (storage.isUserLoggedIn()) getView().ifAlive(V::openProfile);
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
    public void onRedirectToLogin() {
        getView().ifAlive(v -> v.showAction(R.string.warning_unauthorized_message,
                R.string.login_title,
                ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    getView().ifAlive(V::openLogin);
                })));
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
        if (storage.isUserLoggedIn()) {
            final int value = storage.getUserBookingsCount();
            getView().ifAlive(v -> v.setProfileBadge(value));

            Timber.d("Set profile badge value to %d", value);
        }
    }

    @Override
    public void onRefresh(String departureDate) {
        if (storage.isDirectionStored()) {
            addSubscription(getBusScheduleObservable(storage.getDepartureCity(), departureDate)
                    .doFinally(() -> getView().ifAlive(V::hideRefresh))
                    .subscribeWith(getBusScheduleObserver()));
        } else {
            getView().ifAlive(V::hideRefresh);
        }
    }

    @Override
    public void onStart(String departureDate) {
//        if (storage.isDirectionStored()) {
//            getView().ifAlive(v -> v.setDirection(storage.getDepartureBusStop().getFullName(),
//                    storage.getArrivalBusStop().getFullName()));
//
//            addSubscription(getBusScheduleObservable(storage.getDepartureBusStop(), departureDate)
//                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
//                    .subscribeWith(getBusScheduleObserver()));
//        } else {
//            addSubscription(getCitiesDataObservable()
//                    .doOnSubscribe(disposable -> getView().ifAlive(V::showLoadingDataDialog))
//                    .doFinally(() -> getView().ifAlive(V::hideLoadingDataDialog))
//                    .flatMap(cityResponses -> {
//                        boolean isEmpty = cityResponses.stream().allMatch(CityResponse::isBusStopsListEmpty);
//
//                        if (!isEmpty) {
//                            City departure = cityResponses.get(0).getStartBusStop();
//                            City arrival = cityResponses.get(1).getStartBusStop();
//
//                            storage.setDirection(departure, arrival);
//                            storage.setDepartureCityStartBusStop(departure);
//
//                            getView().ifAlive(v -> v.setDirection(departure.getFullName(), arrival.getFullName()));
//
//                            return getBusScheduleObservable(departure, departureDate);
//                        } else {
//                            getView().ifAlive(V::showEmptyView);
//                            throw new Exception("Oops something went wrong!");
//                        }
//                    })
//                    .subscribeWith(getBusScheduleObserver()));
//        }
    }

    @Override
    public void onDirectionSwapButtonClick(String departureDate) {
        if (storage.isDirectionStored()) {
            City newArrivalCity = storage.getDepartureCity();
            City newDepartureCity = storage.getArrivalCity();

            storage.setDepartureCity(newDepartureCity);
            storage.setArrivalCity(newArrivalCity);

            getView().ifAlive(V::showSwapDirectionAnimation);
            getView().ifAlive(v -> v.setDirection(newDepartureCity.getFullName(), newArrivalCity.getFullName()));

//            addSubscription(getBusScheduleObservable(storage.getDepartureCity(), departureDate)
//                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
//                    .subscribeWith(getBusScheduleObserver()));
        } else {
            getView().ifAlive(v -> v.showError(R.string.error_complete_direction));
        }
    }

    @Override
    public void onDateClick(String departureDate) {
        if (storage.isDirectionStored()) {
            addSubscription(getBusScheduleObservable(storage.getDepartureCity(), departureDate)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .doFinally(() -> getView().ifAlive(V::jumpTop))
                    .subscribeWith(getBusScheduleObserver()));
        }
    }

    @Override
    public void onDepartureCityChange(City city, String departureDate) {
        storage.setDepartureCity(city);
        // delete
        getView().ifAlive(v -> v.setDepartureCity(city.getFullName()));

        if (storage.isArrivalCityStored()) {
//            addSubscription(getBusScheduleObservable(city, departureDate)
//                    .doOnSubscribe(disposable -> {
//                        getView().ifAlive(V::showProgress);
//                        getView().ifAlive(v -> v.setDepartureCity(city.getFullName()));
//                    })
//                    .subscribeWith(getBusScheduleObserver()));
        }
    }

    @Override
    public void onArrivalCityChange(City city, String departureDate) {
        storage.setArrivalCity(city);
        // delete
        getView().ifAlive(v -> v.setArrivalCity(city.getFullName()));

//        addSubscription(getBusScheduleObservable(city, departureDate)
//                .doOnSubscribe(disposable -> {
//                    getView().ifAlive(V::showProgress);
//                    getView().ifAlive(v -> v.setArrivalCity(city.getFullName()));
//                })
//                .subscribeWith(getBusScheduleObserver()));
    }

    @Override
    public void onFilterCollapsed() {
        if (storage.isDirectionStored()) {
            getView().ifAlive(v -> v.setToolbarSubtitle(storage.getDepartureCity().getName()));
        } else {
            onFilterExpanded();
        }
    }

    @Override
    public void onFilterExpanded() {
        getView().ifAlive(v -> v.setToolbarSubtitle(R.string.bus_schedule_filter_title));
    }

    @Override
    public void onBusTripClick(String departureDate, int id, int pos) {
        City departureCity = storage.getDepartureCity();

        addSubscription(getBusScheduleObservable(departureCity, departureDate)
                .doOnSubscribe(disposable -> getView().ifAlive(V::showBusTripLoading))
                .doFinally(() -> getView().ifAlive(V::hideBusTripLoading))
                .subscribeWith(new DisposableSingleObserver<BusScheduleResponse>() {
                    @Override
                    public void onSuccess(BusScheduleResponse updatedBusSchedule) {
                        setBusScheduleData(updatedBusSchedule);

                        Optional<BusTrip> optBusTrip = updatedBusSchedule.getBusTripById(id);

                        if (optBusTrip.isPresent()) {
                            String date = AppDatesHelper.formatDate(departureDate,
                                    AppDatesHelper.DatePattern.API_SCHEDULE_REQUEST,
                                    AppDatesHelper.DatePattern.SUMMARY);

                            getView().ifAlive(v -> v.openBusTripSummary(optBusTrip.get(), departureCity, date));
                        } else {
                            getView().ifAlive(v -> v.showError(R.string.error_no_bus_trip_message));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                    }
                }));
    }

    private void setBusScheduleData(BusScheduleResponse busScheduleResponse) {
        if (!busScheduleResponse.isBusTripsListEmpty()) {
            getView().ifAlive(v -> v.setBusScheduleData(busScheduleResponse));
        } else {
            getView().ifAlive(V::showEmptyView);
        }
    }

    private DisposableSingleObserver<BusScheduleResponse> getBusScheduleObserver() {
        return new DisposableSingleObserver<BusScheduleResponse>() {
            @Override
            public void onSuccess(BusScheduleResponse updatedBusSchedule) {
                setBusScheduleData(updatedBusSchedule);
            }

            @Override
            public void onError(Throwable throwable) {
                getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                getView().ifAlive(V::showEmptyView);
            }
        };
    }

//    private Single<List<CityResponse>> getCitiesDataObservable() {
//        return citiesModel.doGetCitiesData()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    private Single<BusScheduleResponse> getBusScheduleObservable(City city, String date) {
        return busScheduleModel.doGetBusScheduleData(city.getId(), date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
