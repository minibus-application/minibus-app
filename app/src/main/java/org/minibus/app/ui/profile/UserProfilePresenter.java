package org.minibus.app.ui.profile;

import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.BookingModel;
import org.minibus.app.data.network.pojo.booking.BookingResponse;
import org.minibus.app.data.network.pojo.user.UserResponse;
import org.minibus.app.data.network.model.UserModel;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BasePresenter;
import org.minibus.app.helpers.ApiErrorHelper;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class UserProfilePresenter<V extends UserProfileContract.View> extends BasePresenter<V>
        implements UserProfileContract.Presenter<V> {

    private UserModel userModel;
    private BookingModel bookingModel;

    @Inject
    AppStorageManager storage;

    @Inject
    public UserProfilePresenter(UserModel userModel, BookingModel bookingModel) {
        this.userModel = userModel;
        this.bookingModel = bookingModel;
    }

    @Override
    public void onBusScheduleButtonClick() {
        getView().ifAlive(V::close);
    }

    @Override
    public void onBookingCancelButtonClick(int bookingId) {
        getView().ifAlive(v -> v.showAsk(R.string.warning_booking_cancel_message, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            addSubscription(getCancelBookingObservable(storage.getUserAuthToken(), bookingId)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .subscribeWith(new DisposableSingleObserver<BookingResponse>() {
                        @Override
                        public void onSuccess(BookingResponse bookingResponse) {
                            refreshUserData();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                            getView().ifAlive(V::hideProgress);
                        }
                    }));
        }));
    }

    @Override
    public void onStart() {
        getView().ifAlive(v -> v.setUserData(storage.getUserData().getName(), storage.getUserData().getPhone()));
        refreshUserData();
    }

    @Override
    public void onLogoutButtonClick() {
        getView().ifAlive(v -> v.showAsk(R.string.warning_logout_message, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            storage.clearUserSession();

            getView().ifAlive(V::logout);
        }));
    }

    private void refreshUserData() {
        addSubscription(getUserDataObservable(storage.getUserAuthToken())
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .subscribeWith(new DisposableSingleObserver<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse userResponse) {
                        storage.setUserData(userResponse);
                        getView().ifAlive(v -> v.setUserData(userResponse.getName(), userResponse.getPhone()));

                        if (!userResponse.isBookingsListEmpty()) {
                            getView().ifAlive(v -> v.setUserBookingsData(userResponse.getBookings()));
                        } else {
                            getView().ifAlive(V::showEmptyView);
                        }

                        getView().ifAlive(V::updateUserBookingsBadge);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                        getView().ifAlive(V::showEmptyView);
                    }
                }));
    }

    private Single<BookingResponse> doDeleteBookingData(String authToken, int bookingId) {
        return bookingModel.doDeleteBookingData(authToken, bookingId);
    }

    private Single<UserResponse> doGetUserData(String authToken) {
        return userModel.doGetUserData(authToken);
    }

    private Single<BookingResponse> getCancelBookingObservable(String authToken, int bookingId) {
        return doDeleteBookingData(authToken, bookingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<UserResponse> getUserDataObservable(String authToken) {
        return doGetUserData(authToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
