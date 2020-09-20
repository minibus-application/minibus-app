package org.minibus.app.ui.schedule.trip;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.RouteScheduleModel;
import org.minibus.app.data.network.pojo.user.UserResponse;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RouteTripPresenter<V extends RouteTripContract.View> extends BasePresenter<V>
        implements RouteTripContract.Presenter<V> {

    private RouteScheduleModel routeScheduleModel;

    @Inject
    AppStorageManager storage;

    @Inject
    public RouteTripPresenter(RouteScheduleModel routeScheduleModel) {
        this.routeScheduleModel = routeScheduleModel;
    }

    @Override
    public void onStart(int availableSeats) {
        final int min = AppConstants.MIN_SEATS_PER_BOOKING;
        final int max = AppConstants.MAX_SEATS_PER_BOOKING;
        final int available = Math.min(availableSeats, max);

        if (available < min) {
            getView().ifAlive(v -> v.showError(R.string.warning_no_seats_message));
            getView().ifAlive(V::close);
            return;
        }

        getView().ifAlive(v -> v.setSeatsCount(available));
        getView().ifAlive(v -> v.setPassengerName(storage.getUserData().getName()));
    }

    @Override
    public void onConfirmReservationButtonClick(String depDate, String busTripId, int seatsToReserve) {
//        addSubscription(getBusTripBookingObservable(storage.getUserAuthToken(), depDate, busTripId, seatsToReserve)
//                .doOnSubscribe(disposable -> {
//                    getView().ifAlive(V::showProgress);
//                    getView().ifAlive(V::disableConfirmReservationButton);
//                })
//                .doFinally(() -> getView().ifAlive(V::enableConfirmReservationButton))
//                .subscribeWith(new DisposableSingleObserver<BookingResponse>() {
//                    @Override
//                    public void onSuccess(BookingResponse bookingResponse) {
//                        storage.setUserBookingsCount(storage.getUserBookingsCount() + seatsCount);
//
//                        getView().ifAlive(V::hideProgress);
//                        getView().ifAlive(V::closeOnBooked);
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        getView().ifAlive(V::hideProgress);
//                        getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
//                    }
//                }));
    }

    @Override
    public void onSeatsCountChanged(int newValue) {

    }

    private Single<UserResponse> getRouteTripBookingObservable(String authToken,
                                                               String depDate,
                                                               String routeId,
                                                               String tripId,
                                                               int seatsCount) {
        return routeScheduleModel.doPostRouteTripData(authToken, depDate, routeId, tripId, seatsCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
