package org.minibus.app.ui.base;


import android.content.DialogInterface;

import androidx.annotation.StringRes;

import org.minibus.app.ui.R;
import org.minibus.app.ui.main.MainActivity;
import org.minibus.app.helpers.AppAlertsHelper;

public interface Contract {

    interface View {
        MainActivity getMainActivity();

        void showEmptyView();

        default void showProgress() {
            AppAlertsHelper.showProgressHud(getMainActivity());
        }

        default void hideProgress() {
            AppAlertsHelper.hideProgressHud();
        }

        default void showError(String msg) {
            showInfo(msg);
        }

        default void showError(@StringRes int msgResId) {
            showInfo(getMainActivity().getResources().getString(msgResId));
        }

        default void showInfo(@StringRes int msgResId) {
            AppAlertsHelper.showToast(getMainActivity(),
                    getMainActivity().getResources().getString(msgResId));
        }

        default void showInfo(String msg) {
            AppAlertsHelper.showToast(getMainActivity(), msg);
        }

        default void showQuestion(@StringRes int msgResId,
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
