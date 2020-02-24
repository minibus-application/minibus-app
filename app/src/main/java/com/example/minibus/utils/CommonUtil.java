package com.example.minibus.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.minibus.ui.main.MainActivity;

import static android.content.Context.WINDOW_SERVICE;

public class CommonUtil {

    public static int dpToPx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static void adjustFontSize(MainActivity activity, float fontScaleFactor) {
        Configuration config = activity.getResources().getConfiguration();
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();

        if (config.fontScale > fontScaleFactor) {
            config.fontScale = fontScaleFactor;
            WindowManager wm = (WindowManager) activity.getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = config.fontScale * metrics.density;
            activity.getBaseContext().getResources().updateConfiguration(config, metrics);
        }
    }
}
