package org.minibus.app.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.material.button.MaterialButton;

import org.minibus.app.ui.R;

public class ProgressMaterialButton extends MaterialButton {

    private boolean isLoading;
    private Canvas canvas;
    private CircularProgressDrawable drawable;
    private String text = "";
    private Context context;
    private int progressColor;

    public ProgressMaterialButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProgressMaterialButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressMaterialButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context) {
        this.context = context;
        this.text = getText().toString();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ProgressMaterialButton, 0, 0);

        progressColor = typedArray.getColor(R.styleable.ProgressMaterialButton_progressColor, context.getColor(R.color.colorPrimary));

        init(context);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(enabled ? 1.0f : 0.7f);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (text != null && text.length() > 0) {
            this.text = text.toString();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        setLoading(isLoading);
    }

    private CircularProgressDrawable drawProgressDrawable(Canvas canvas) {
        if (drawable == null) {
            drawable = new CircularProgressDrawable(this.getContext());

            int offset = (getWidth() - getHeight()) / 2;
            int left = offset + 15;
            int right = getWidth() - offset - 15;
            int bottom = getHeight() - 15;
            int top = 15;

            drawable.setColorSchemeColors(progressColor);
            drawable.setStyle(CircularProgressDrawable.DEFAULT);
            drawable.setStrokeWidth(10);
            drawable.setBounds(left, top, right, bottom);
            drawable.setCallback(this);
            drawable.start();
        }

        return drawable;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
        if (loading) {
            setText("");
            setIcon(drawProgressDrawable(canvas));
            setIconPadding(0);
            setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
        } else {
            if (!"".equals(text)) {
                setText(text);
            }
            setIcon(null);
        }
        // setEnabled(!loading);
    }
}
