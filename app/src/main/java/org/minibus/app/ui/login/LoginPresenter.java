package org.minibus.app.ui.login;


import org.minibus.app.AppConstants;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.data.network.model.UserModel;
import org.minibus.app.data.network.pojo.user.UserResponse;
import org.minibus.app.ui.R;
import org.minibus.app.ui.base.BasePresenter;
import org.minibus.app.helpers.ApiErrorHelper;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class LoginPresenter<V extends LoginContract.View> extends BasePresenter<V>
        implements LoginContract.Presenter<V> {

    @Inject
    AppStorageManager storage;

    private UserModel userModel;

    @Inject
    public LoginPresenter(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void onConfirmButtonClick(String name, String phone, String password, String confirmedPassword, boolean isLoginForm) {
        if (isFormValid(name, phone, password, confirmedPassword, isLoginForm)) {

            Single<UserResponse> observable = isLoginForm
                    ? getUserAuthObservable(name, phone, password)
                    : getUserCreateObservable(name, phone, password);

            addSubscription(observable
                    .doOnSubscribe(disposable -> getView().ifAlive(V::showProgress))
                    .subscribeWith(new DisposableSingleObserver<UserResponse>() {
                        @Override
                        public void onSuccess(UserResponse userResponse) {
                            if (userResponse.getToken() == null)
                                throw new RuntimeException("Can't retrieve user token");

                            storage.setUserSession(userResponse.getToken(), userResponse.getUser());
                            getView().ifAlive(v -> v.showWelcomeMessage(R.string.welcome_user_message, userResponse.getUser().getName()));
                            getView().ifAlive(V::closeOnSuccessLogin);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            getView().ifAlive(V::hideProgress);
                            getView().ifAlive(v -> v.showError(ApiErrorHelper.parseResponseMessage(throwable)));
                        }
                    }));
        }
    }

    @Override
    public void onCloseButtonClick() {
        getView().ifAlive(V::close);
    }

    private Single<UserResponse> getUserAuthObservable(String name, String phone, String password) {
        return userModel.doAuthUserData(name, phone, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<UserResponse> getUserCreateObservable(String userName, String userPhone, String userPassword) {
        return userModel.doCreateUserData(userName, userPhone, userPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private boolean isFormValid(String userName, String userPhone, String userPassword, String userConfirmPassword, boolean isLoginForm) {
        boolean isValid = true;

        if (userPhone.length() < AppConstants.DEFAULT_PHONE_NUMBER_LENGTH) {
            isValid = false;
            getView().ifAlive(v -> v.showPhoneFieldError(R.string.user_phone_format_error));
        } else {
            getView().ifAlive(V::hidePhoneFieldError);
        }

        if (userPassword.isEmpty()) {
            isValid = false;
            getView().ifAlive(v -> v.showPasswordFieldError(R.string.user_pass_empty_error));
        } else {
            getView().ifAlive(V::hidePasswordFieldError);
        }

        if (!isLoginForm) {
            if (userName.isEmpty()) {
                isValid = false;
                getView().ifAlive(v -> v.showNameFieldError(R.string.user_name_empty_error));
            } else {
                getView().ifAlive(V::hideNameFieldError);
            }

            if (userConfirmPassword.isEmpty()) {
                isValid = false;
                getView().ifAlive(v -> v.showConfirmPasswordFieldError(R.string.user_confirm_pass_empty_error));
            } else if (!userConfirmPassword.equals(userPassword)) {
                isValid = false;
                getView().ifAlive(v -> v.showConfirmPasswordFieldError(R.string.user_confirm_pass_error));
            } else {
                getView().ifAlive(V::hideConfirmPasswordFieldError);
            }
        }

        return isValid;
    }
}
