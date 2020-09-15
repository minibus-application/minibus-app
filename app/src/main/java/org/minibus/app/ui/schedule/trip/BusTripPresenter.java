package org.minibus.app.ui.schedule.trip;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.pojo.booking.BookingRequest;
import org.minibus.app.data.network.model.BookingModel;
import org.minibus.app.data.network.pojo.booking.BookingResponse;
import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.schedule.BusTrip;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BasePresenter;
import org.minibus.app.helpers.ApiErrorHelper;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BusTripPresenter<V extends BusTripContract.View> extends BasePresenter<V>
        implements BusTripContract.Presenter<V> {

    private BookingModel bookingModel;

    @Inject
    AppStorageManager storage;

    @Inject
    public BusTripPresenter(BookingModel bookingModel) {
        this.bookingModel = bookingModel;
    }

    @Override
    public void onStart(int availableSeats) {
        final int min = AppConstants.MIN_SEATS_PER_BOOKING;
        final int max = AppConstants.MAX_SEATS_PER_BOOKING;
        final int available = Math.min(availableSeats, max);

        getView().ifAlive(v -> v.setSeatsCounterRange(min, available));
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

    //    private Single<BookingResponse> getBusTripBookingObservable(String authToken,
//                                                                String depDate,
//                                                                String busTripId,
//                                                                int seatsToReserve) {
//        return bookingModel.doPostBookingData(authToken, depDate, busTripId, seatsToReserve)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
