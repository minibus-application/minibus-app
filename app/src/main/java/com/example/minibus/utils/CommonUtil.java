package com.example.minibus.utils;

import android.content.Context;

public class CommonUtil {

    public static int dpToPx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
