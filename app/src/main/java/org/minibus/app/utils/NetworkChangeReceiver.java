package org.minibus.app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.minibus.app.helpers.AppAlertsHelper;
import org.minibus.app.ui.R;

import timber.log.Timber;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = NetworkConnectivityUtil.getConnectivityStatusString(context);

        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkConnectivityUtil.NETWORK_STATUS_NOT_CONNECTED) {
                Timber.i("Network connection is unstable or temporary lost");

                AppAlertsHelper.showToast(context,
                        context.getResources().getString(R.string.warning_bad_connectivity_message));
            } else {
                Timber.i("Network connection is stable or restored");
            }
        }
    }
}
