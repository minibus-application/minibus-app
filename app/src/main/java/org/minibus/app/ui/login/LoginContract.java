package org.minibus.app.ui.login;

import androidx.annotation.StringRes;

import org.minibus.app.ui.base.Contract;

public interface LoginContract {

    interface View extends Contract.View {
        void close();
        void showWelcomeMessage(@StringRes int msgResId, String userName);
        void showNameFieldError(@StringRes int msgResId);
        void showPhoneFieldError(@StringRes int msgResId);
        void showPasswordFieldError(@StringRes int msgResId);
        void showConfirmPasswordFieldError(@StringRes int msgResId);
        void closeOnSuccessLogin();
        void hideNameFieldError();
        void hidePhoneFieldError();
        void hidePasswordFieldError();
        void hideConfirmPasswordFieldError();
    }

    interface Presenter<V extends LoginContract.View> extends Contract.Presenter<V> {
        void onCloseButtonClick();
        void onConfirmButtonClick(String userName,
                                  String userPhone,
                                  String userPassword,
                                  String userConfirmPassword,
                                  boolean isLoginForm);
    }
}
