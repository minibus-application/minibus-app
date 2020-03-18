package org.minibus.app.ui.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class BadgeDrawable extends Drawable {

    private float textSize = 32;
    private int textColor = Color.WHITE;
    private int badgeBackgroundColor = Color.RED;
    private boolean isCountable = true;
    private boolean isDrawable = false;
    private String badgeValue = "";

    private Paint badgeBackground;
    private Paint textBackground;
    private Rect textRect = new Rect();

    public BadgeDrawable() {
        badgeBackground = new Paint();
        badgeBackground.setColor(badgeBackgroundColor);
        badgeBackground.setStyle(Paint.Style.FILL);
        badgeBackground.setAntiAlias(true);

        textBackground = new Paint();
        textBackground.setColor(textColor);
        textBackground.setTextSize(textSize);
        textBackground.setTypeface(Typeface.DEFAULT_BOLD);
        textBackground.setTextAlign(Paint.Align.CENTER);
        textBackground.setAntiAlias(true);
    }

    public void setCount(int count) {
        badgeValue = Integer.toString(count);
        isDrawable = count > 0;

        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isDrawable) return;

        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        float centerX = width - 15 - 1;
        float centerY = 15 + 1;

        canvas.drawCircle(centerX, centerY, 20, badgeBackground);

        if (isCountable) {
            textBackground.getTextBounds(badgeValue, 0, badgeValue.length(), textRect);
            float textHeight = textRect.bottom - textRect.top;
            float textY = centerY + (textHeight / 2f);
            canvas.drawText(badgeValue, centerX, textY, textBackground);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public static class Builder {
        private BadgeDrawable badgeDrawable;

        public Builder() {
            badgeDrawable = new BadgeDrawable();
        }

        public Builder withCounter(boolean isCountable) {
            badgeDrawable.isCountable = isCountable;
            return this;
        }

        public Builder withColor(int color) {
            badgeDrawable.badgeBackgroundColor = color;
            return this;
        }

        public Builder withTextColor(int color) {
            badgeDrawable.textColor = color;
            return this;
        }

        public Builder withTextSize(float size) {
            badgeDrawable.textSize = size;
            return this;
        }

        public BadgeDrawable build() {
            return badgeDrawable;
        }
    }
}
