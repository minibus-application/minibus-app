package org.minibus.app.ui.schedule.trip;

import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.RouteScheduleModel;
import org.minibus.app.data.network.pojo.user.UserResponse;
import org.minibus.app.helpers.ApiErrorHelper;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BasePresenter;

import java.time.LocalDate;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RouteTripPresenter<V extends RouteTripContract.View> extends BasePresenter<V>
        implements RouteTripContract.Presenter<V> {

    @Inject
    AppStorageManager storage;

    private RouteScheduleModel routeScheduleModel;

    @Inject
    public RouteTripPresenter(RouteScheduleModel routeScheduleModel) {
        this.routeScheduleModel = routeScheduleModel;
    }

    @Override
    public void onStart(final int availableSeats) {
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
    public void onConfirmReservationButtonClick(LocalDate depDate, String tripId, String routeId, int seatsCount) {
        addSubscription(getRouteTripBookingObservable(depDate, tripId, routeId, seatsCount)
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .doFinally(() -> getView().ifAlive(V::hideProgress))
                .subscribeWith(new DisposableSingleObserver<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse response) {
                        storage.setUserData(response.getUser());

                        getView().ifAlive(V::closeOnBooked);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                    }
                }));
    }

    private Single<UserResponse> getRouteTripBookingObservable(LocalDate depDate,
                                                               String tripId,
                                                               String routeId,
                                                               int seatsCount) {
        return routeScheduleModel.doPostRouteTripData(storage.getAuthToken(), depDate, routeId, tripId, seatsCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
