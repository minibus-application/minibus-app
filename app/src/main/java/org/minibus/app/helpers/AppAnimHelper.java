package org.minibus.app.helpers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.TextView;

public class AppAnimHelper {

    public static void textFadeInOut(final TextView view, String text) {
        view.animate().setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setText(text);
                view.animate().setListener(null).setDuration(200).alpha(1.0f);
            }
        }).alpha(0.0f);
    }
}
