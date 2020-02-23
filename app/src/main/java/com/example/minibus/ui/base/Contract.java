package com.example.minibus.ui.base;


import android.content.DialogInterface;
import androidx.annotation.StringRes;

import com.example.minibus.ui.R;
import com.example.minibus.ui.main.MainActivity;
import com.example.minibus.helpers.AppAlertsHelper;

public interface Contract {

    interface View {

        MainActivity getMainActivity();

        void showEmptyView();

        default void showProgress() {
            AppAlertsHelper.showProgressDialog(getMainActivity());
        }

        default void hideProgress() {
            AppAlertsHelper.hideProgressDialog();
        }

        default void showError(String msg) {
            AppAlertsHelper.showAlertDialog(getMainActivity(),
                    getMainActivity().getResources().getString(R.string.error_default_title), msg);
        }

        default void showError(@StringRes int msgResId) {
            AppAlertsHelper.showAlertDialog(getMainActivity(),
                    getMainActivity().getResources().getString(R.string.error_default_title),
                    getMainActivity().getResources().getString(msgResId));
        }

        default void showInfo(@StringRes int msgResId) {
            AppAlertsHelper.showToast(getMainActivity(),
                    getMainActivity().getResources().getString(msgResId));
        }

        default void showInfo(String msg) {
            AppAlertsHelper.showToast(getMainActivity(), msg);
        }

        default void showAsk(@StringRes int msgResId,
                             DialogInterface.OnClickListener posBtnCallback) {
            AppAlertsHelper.showActionDialog(getMainActivity(),
                    getMainActivity().getResources().getString(R.string.warning_default_title),
                    getMainActivity().getResources().getString(msgResId),
                    R.string.no,
                    R.string.yes,
                    posBtnCallback);
        }

        default void showAction(@StringRes int titleResId,
                                @StringRes int msgResId,
                                @StringRes int posBtnResId,
                                DialogInterface.OnClickListener posBtnCallback) {
            AppAlertsHelper.showActionDialog(getMainActivity(),
                    getMainActivity().getResources().getString(titleResId),
                    getMainActivity().getResources().getString(msgResId),
                    posBtnResId,
                    posBtnCallback);
        }

        default void showAction(@StringRes int msgResId,
                                @StringRes int posBtnResId,
                                DialogInterface.OnClickListener posBtnCallback) {
            AppAlertsHelper.showActionDialog(getMainActivity(),
                    getMainActivity().getResources().getString(R.string.warning_default_title),
                    getMainActivity().getResources().getString(msgResId),
                    posBtnResId,
                    posBtnCallback);
        }

        default void showWarning(@StringRes int msgResId) {
            AppAlertsHelper.showAlertDialog(getMainActivity(),
                    getMainActivity().getResources().getString(R.string.warning_default_title),
                    getMainActivity().getResources().getString(msgResId));
        }
    }

    interface Presenter<V extends Contract.View> {

        WeakViewReference<V> getView();
        void attachView(V v);
        void detachView();
    }
}
