package org.minibus.app.ui.schedule;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.BusScheduleModel;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.pojo.city.BusStop;
import org.minibus.app.data.network.pojo.city.CityResponse;
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

    @Inject
    AppStorageManager storage;

    @Inject
    public BusSchedulePresenter(BusScheduleModel busScheduleModel, CitiesModel citiesModel) {
        this.busScheduleModel = busScheduleModel;
        this.citiesModel = citiesModel;
    }

    @Override
    public void onDepartureBusStopClick() {
        getView().ifAlive(V::openDepartureBusStops);
    }

    @Override
    public void onArrivalBusStopClick() {
        getView().ifAlive(v -> v.showWarning(R.string.warning_arrival_stop_message));
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
    public void onDepartureStopsButtonClick() {
        onDepartureBusStopClick();
    }

    @Override
    public void onRefresh(String departureDate) {
        if (storage.isDirectionStored()) {
            addSubscription(getBusScheduleObservable(storage.getDepartureBusStop(), departureDate)
                    .doFinally(() -> getView().ifAlive(V::hideRefresh))
                    .subscribeWith(getBusScheduleObserver()));
        } else {
            getView().ifAlive(V::hideRefresh);
        }
    }

    @Override
    public void onStart(String departureDate) {
        if (storage.isDirectionStored()) {
            getView().ifAlive(v -> v.setDirection(storage.getDepartureBusStop().getJoinedCityBusStop(),
                    storage.getArrivalBusStop().getJoinedCityBusStop()));

            addSubscription(getBusScheduleObservable(storage.getDepartureBusStop(), departureDate)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .subscribeWith(getBusScheduleObserver()));
        } else {
            addSubscription(getCitiesDataObservable()
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showLoadingDataDialog))
                    .doFinally(() -> getView().ifAlive(V::hideLoadingDataDialog))
                    .flatMap(cityResponses -> {
                        boolean isEmpty = cityResponses.stream().allMatch(CityResponse::isBusStopsListEmpty);

                        if (!isEmpty) {
                            BusStop departure = cityResponses.get(0).getStartBusStop();
                            BusStop arrival = cityResponses.get(1).getStartBusStop();

                            storage.setDirection(departure, arrival);
                            storage.setDepartureCityStartBusStop(departure);

                            getView().ifAlive(v -> v.setDirection(departure.getJoinedCityBusStop(), arrival.getJoinedCityBusStop()));

                            return getBusScheduleObservable(departure, departureDate);
                        } else {
                            getView().ifAlive(V::showEmptyView);
                            throw new Exception("Oops something went wrong!");
                        }
                    })
                    .subscribeWith(getBusScheduleObserver()));
        }
    }

    @Override
    public void onDirectionSwapButtonClick(String departureDate) {
        if (storage.isDirectionStored()) {
            BusStop newArrivalBusStop = storage.getDepartureCityStartBusStop();
            BusStop newDepartureBusStop = storage.getArrivalBusStop();

            storage.setDepartureCityStartBusStop(newDepartureBusStop);
            storage.setDepartureBusStop(newDepartureBusStop);
            storage.setArrivalBusStop(newArrivalBusStop);

            getView().ifAlive(V::showDirectionSwapAnimation);
            getView().ifAlive(v -> v.setDirection(newDepartureBusStop.getJoinedCityBusStop(),
                    newArrivalBusStop.getJoinedCityBusStop()));

            addSubscription(getBusScheduleObservable(storage.getDepartureBusStop(), departureDate)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .subscribeWith(getBusScheduleObserver()));
        }
    }

    @Override
    public void onDateClick(String departureDate) {
        if (storage.isDirectionStored()) {
            addSubscription(getBusScheduleObservable(storage.getDepartureBusStop(), departureDate)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .doFinally(() -> getView().ifAlive(V::jumpTop))
                    .subscribeWith(getBusScheduleObserver()));
        }
    }

    @Override
    public void onDepartureBusStopChange(BusStop selectedDepartureBusStop, String departureDate) {
        storage.setDepartureBusStop(selectedDepartureBusStop);

        addSubscription(getBusScheduleObservable(selectedDepartureBusStop, departureDate)
                .doOnSubscribe(disposable -> {
                    getView().ifAlive(V::showProgress);
                    getView().ifAlive(v -> v.setDepartureBusStop(selectedDepartureBusStop.getJoinedCityBusStop()));
                })
                .subscribeWith(getBusScheduleObserver()));
    }

    @Override
    public void onArrivalBusStopChange(BusStop arrivalCityFinalBusStop, BusStop departureCityStartBusStop) {
        storage.setArrivalBusStop(arrivalCityFinalBusStop);
        storage.setDepartureCityStartBusStop(departureCityStartBusStop);

        getView().ifAlive(v -> v.setArrivalBusStop(arrivalCityFinalBusStop.getJoinedCityBusStop()));
    }

    @Override
    public void onFilterCollapsed() {
        if (storage.isDirectionStored()) {
            getView().ifAlive(v -> v.setToolbarSubtitle(storage.getDepartureBusStop().getName()));
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
        BusStop departureBusStop = storage.getDepartureBusStop();

        addSubscription(getBusScheduleObservable(departureBusStop, departureDate)
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

                            getView().ifAlive(v -> v.openBusTripSummary(optBusTrip.get(), departureBusStop, date));
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

    private Single<List<CityResponse>> getCitiesDataObservable() {
        return citiesModel.doGetCitiesData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<BusScheduleResponse> getBusScheduleObservable(BusStop busStop, String date) {
        return busScheduleModel.doGetBusScheduleData(busStop.getId(), date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
