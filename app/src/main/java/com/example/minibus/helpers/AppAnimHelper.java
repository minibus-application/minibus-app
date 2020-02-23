package com.example.minibus.helpers;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class AppAnimHelper {

    public static void textFadeInOut(final TextView view, String text) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                view.setText(text);
            }
        });

        view.startAnimation(alphaAnimation);
    }
}
