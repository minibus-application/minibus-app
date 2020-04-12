package org.minibus.app.ui.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.RotateDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;

import org.minibus.app.ui.R;


public class ProgressHud extends Dialog {

    private Context context;

    public ProgressHud(Context context) {
        super(context, R.style.ProgressHud);
        this.context = context;
    }

    public ProgressHud(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        ImageView imageView = findViewById(R.id.image_hub_progress);
        RotateDrawable rotateDrawable = (RotateDrawable) imageView.getBackground();
        ObjectAnimator oa = ObjectAnimator.ofInt(rotateDrawable, "level", 0, 10000);
        oa.setDuration(1000);
        oa.setInterpolator(new LinearInterpolator());
        oa.setRepeatCount(ValueAnimator.INFINITE);
        oa.start();
    }

    public void setMessage(CharSequence message) {
        if(message != null && message.length() > 0) {
            findViewById(R.id.text_hub_message).setVisibility(View.VISIBLE);
            TextView txt = findViewById(R.id.text_hub_message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public void showHud(CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        setContentView(R.layout.layout_progress_hud);
        setTitle("");

        if (message == null || message.length() == 0) {
            findViewById(R.id.text_hub_message).setVisibility(View.GONE);
        } else {
            TextView txt = findViewById(R.id.text_hub_message);
            txt.setText(message);
        }

        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
        getWindow().getAttributes().gravity = Gravity.CENTER;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        getWindow().setAttributes(lp);

        show();
    }

    public void setCompleted(CharSequence message) {
        setMessage(message);
        ImageView imageView = findViewById(R.id.image_hub_progress);
        imageView.setVisibility(View.GONE);
        showUp(findViewById(R.id.image_hub_checkmark));
    }

    private void showUp(View v) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .0f, 1f);
        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(v, "scaleX", .0f, v.getScaleX());
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(v, "scaleY", .0f, v.getScaleY());
        fadeIn.setDuration(200);
        scaleInX.setDuration(350);
        scaleInY.setDuration(350);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeIn).with(scaleInX).with(scaleInY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                v.setVisibility(View.VISIBLE);
            }
        });
        animatorSet.start();
    }
}
