package org.minibus.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectivityUtil {

    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int TYPE_WIRELESS = 1;
    public static final int TYPE_MOBILE = 2;

    public static final int NETWORK_STATUS_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_WIRELESS = 1;
    public static final int NETWORK_STATUS_MOBILE = 2;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) return TYPE_WIRELESS;
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE;
        }

        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = NetworkConnectivityUtil.getConnectivityStatus(context);
        int status = 0;

        switch (conn) {
            case (NetworkConnectivityUtil.TYPE_WIRELESS):
                status = NETWORK_STATUS_WIRELESS;
                break;
            case (NetworkConnectivityUtil.TYPE_MOBILE):
                status = NETWORK_STATUS_MOBILE;
                break;
            case (NetworkConnectivityUtil.TYPE_NOT_CONNECTED):
                status = NETWORK_STATUS_NOT_CONNECTED;
                break;
        }
        return status;
    }
}
