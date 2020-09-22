package org.minibus.app.ui.profile;

import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.pojo.user.User;
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

    @Inject
    AppStorageManager storage;

    @Inject
    public UserProfilePresenter(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void onRouteScheduleButtonClick() {
        getView().ifAlive(V::close);
    }

    @Override
    public void onBookingCancelButtonClick(String bookingId) {
        getView().ifAlive(v -> v.showAsk(R.string.warning_booking_cancel_message, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            addSubscription(getRevokeBookingObservable(storage.getAuthToken(), bookingId)
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .subscribeWith(new DisposableSingleObserver<UserResponse>() {
                        @Override
                        public void onSuccess(UserResponse response) {
                            getView().ifAlive(v -> v.setUserBookingsData(response.getBookings()));
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

        onActiveBookingsTabSelected();
    }

    @Override
    public void onActiveBookingsTabSelected() {
        addSubscription(getUserDataObservable(storage.getAuthToken(), false)
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .subscribeWith(getUserDataObserver()));
    }

    @Override
    public void onBookingsHistoryTabSelected() {
        addSubscription(getUserDataObservable(storage.getAuthToken(), true)
                .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                .subscribeWith(getUserDataObserver()));
    }

    @Override
    public void onRefreshActiveBookings() {
        refreshUserData(false);
    }

    @Override
    public void onRefreshBookingsHistory() {
        refreshUserData(true);
    }

    @Override
    public void onCloseButtonClick() {
        getView().ifAlive(V::close);
    }

    @Override
    public void onLogoutButtonClick() {
        getView().ifAlive(v -> v.showAsk(R.string.warning_logout_message, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            storage.clearUserSession();
            getView().ifAlive(V::logout);
        }));
    }

    private void refreshUserData(boolean withBookingsHistory) {
        addSubscription(getUserDataObservable(storage.getAuthToken(), withBookingsHistory)
                .doFinally(() -> getView().ifAlive(V::hideRefresh))
                .subscribeWith(getUserDataObserver()));
    }

    private DisposableSingleObserver<UserResponse> getUserDataObserver() {
        return new DisposableSingleObserver<UserResponse>() {
            @Override
            public void onSuccess(UserResponse response) {
                User received = response.getUser();
                User stored = storage.getUserData();
                if (!received.getName().equals(stored.getName()) || !received.getPhone().equals(stored.getPhone())) {
                    storage.setUserData(response.getUser());
                    getView().ifAlive(v -> v.setUserData(response.getUser().getName(), response.getUser().getPhone()));
                }

                getView().ifAlive(v -> v.setActiveTabCounter(response.getBookings().size()));
                getView().ifAlive(v -> v.setUserBookingsData(response.getBookings()));
            }

            @Override
            public void onError(Throwable throwable) {
                getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                getView().ifAlive(V::showEmptyView);
            }
        };
    }

    private Single<UserResponse> getRevokeBookingObservable(String authToken, String bookingId) {
        return userModel.doDeleteUserBookingData(authToken, bookingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<UserResponse> getUserDataObservable(String authToken, boolean withBookingsHistory) {
        return userModel.doGetUserData(authToken, withBookingsHistory)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
