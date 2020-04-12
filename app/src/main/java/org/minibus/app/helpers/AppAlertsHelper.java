package org.minibus.app.helpers;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import org.minibus.app.ui.R;
import org.minibus.app.ui.custom.ProgressHud;
import org.minibus.app.ui.main.MainActivity;
import com.google.android.material.snackbar.Snackbar;

public class AppAlertsHelper {

    private static Toast toast;
    private static ProgressHud progressHud;

    public static ProgressHud showProgressHud(Context context) {
        progressHud = new ProgressHud(context, R.style.ProgressHud);
        progressHud.showHud(context.getString(R.string.progress_default_title), false, null);
        return progressHud;
    }

    public static void setProgressHudCompleted(Context context) {
        if (progressHud != null) progressHud.setCompleted(context.getString(R.string.progress_success_title));
    }

    public static void hideProgressHud() {
        if (progressHud != null && progressHud.isShowing()) progressHud.cancel();
    }

    public static AlertDialog showActionDialog(Context context,
                                               String title,
                                               String msg,
                                               @StringRes int negBtnResId,
                                               @StringRes int posBtnResId,
                                               DialogInterface.OnClickListener posBtnCallback) {
        return new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_AppTheme_Dialog_Alert)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(posBtnResId, posBtnCallback)
                .setNegativeButton(negBtnResId, null)
                .show();
    }

    public static AlertDialog showActionDialog(Context context,
                                               String title,
                                               String msg,
                                               @StringRes int posBtnResId,
                                               DialogInterface.OnClickListener posBtnCallback) {
        return showActionDialog(context, title, msg, R.string.close, posBtnResId, posBtnCallback);
    }

    public static AlertDialog showAlertDialog(Context context,
                                               String title,
                                               String msg,
                                               @StringRes int negBtnResId) {
        return new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_AppTheme_Dialog_Alert)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton(negBtnResId, null)
                .show();
    }

    public static AlertDialog showAlertDialog(Context context, String title, String msg) {
        return showAlertDialog(context, title, msg, R.string.close);
    }

    public static Snackbar showSnackbar(Context context, String msg) {
        View parentLayout = ((MainActivity) context).findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(parentLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.close, (View v) -> snackbar.dismiss())
                .setActionTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                .show();
        return snackbar;
    }

    public static Toast showToast(Context context, String msg) {
        LayoutInflater inflater = ((MainActivity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.view_toast,
                ((MainActivity) context).findViewById(R.id.layout_toast));

        TextView text = layout.findViewById(R.id.text_toast_message);
        text.setText(msg);

        if (toast != null) toast.cancel();

        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        return toast;
    }
}
