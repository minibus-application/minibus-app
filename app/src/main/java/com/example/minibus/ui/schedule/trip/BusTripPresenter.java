package com.example.minibus.ui.schedule.trip;

import com.example.minibus.data.local.AppStorageManager;
import com.example.minibus.data.network.pojo.booking.BookingRequest;
import com.example.minibus.data.network.model.BookingModel;
import com.example.minibus.data.network.pojo.booking.BookingResponse;
import com.example.minibus.data.network.pojo.city.BusStop;
import com.example.minibus.data.network.pojo.schedule.BusTrip;
import com.example.minibus.ui.R;
import com.example.minibus.ui.base.BasePresenter;
import com.example.minibus.helpers.ApiErrorHelper;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BusTripPresenter<V extends BusTripContract.View> extends BasePresenter<V>
        implements BusTripContract.Presenter<V> {

    private BookingModel bookingModel;

    @Inject AppStorageManager storage;

    @Inject
    public BusTripPresenter(BookingModel bookingModel) {
        this.bookingModel = bookingModel;
    }

    @Override
    public void onCancelClick() {
        getView().ifAlive(V::close);
    }

    @Override
    public void onSetupPassengersOptions(int seatsCount) {
        final int max = storage.getUserBookingsLimit();
        final int left = storage.getUserBookingsLeft();
        final int available = seatsCount;

        if (available == 0 || left == 0) {
            if (available <= left) {
                getView().ifAlive(v -> v.showError(R.string.warning_no_seats_message));
            } else {
                getView().ifAlive(v -> v.showError(R.string.warning_bookings_limit_message));
            }
            getView().ifAlive(V::close);
            return;
        }

        if (available < max || left < max) {
            if (available <= left) {
                getView().ifAlive(v -> v.setPassengersCount(available));
            } else {
                getView().ifAlive(v -> v.setPassengersCount(left));
            }
        } else {
            getView().ifAlive(v -> v.setPassengersCount(max));
        }
    }

    @Override
    public void onBookClick(String departureDate, BusTrip busTrip, BusStop departureBusStop, int seatsCount) {
        if (storage.isUserLoggedIn()) {
            BookingRequest booking = new BookingRequest(storage.getUserData(),
                    departureBusStop, busTrip.getSelected(), seatsCount, departureDate);

            addSubscription(getBusTripBookingObservable(storage.getUserAuthToken(), booking)
                    .doOnSubscribe(disposable -> {
                        getView().ifAlive(V::hide);
                        getView().ifAlive(V::showProgress);
                    })
                    .subscribeWith(new DisposableSingleObserver<BookingResponse>() {
                        @Override
                        public void onSuccess(BookingResponse bookingResponse) {
                            storage.setUserBookingsCount(storage.getUserBookingsCount() + seatsCount);

                            getView().ifAlive(V::hideProgress);
                            getView().ifAlive(V::closeOnBooked);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            getView().ifAlive(V::hideProgress);
                            getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                        }
                    }));
        } else {
            getView().ifAlive(V::openLogin);
        }
    }

    private Single<BookingResponse> doPostBookingData(String authToken, BookingRequest booking) {
        return bookingModel.doPostBookingData(authToken, booking);
    }

    private Single<BookingResponse> getBusTripBookingObservable(String authToken, BookingRequest booking) {
        return doPostBookingData(authToken, booking)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
