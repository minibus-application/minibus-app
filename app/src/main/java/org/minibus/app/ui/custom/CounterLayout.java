package org.minibus.app.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import org.minibus.app.ui.R;

public class CounterLayout extends LinearLayout {

    public interface OnChangedValueListener {
        void onChanged(int value);
    }

    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

    private View rootView;
    private EditText valueView;
    private AppCompatImageButton incrementButton;
    private AppCompatImageButton decrementButton;
    private OnChangedValueListener onChangedValueListener;
    private Context context;

    public CounterLayout(Context context) {
        super(context);
        init(context);
    }

    public CounterLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CounterLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CounterLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setOnChangedValueListener(OnChangedValueListener onChangedValueListener) {
        this.onChangedValueListener = onChangedValueListener;
    }

    private void init(Context context) {
        this.context = context;

        setOrientation(HORIZONTAL);

        rootView = inflate(context, R.layout.layout_counter, this);
        valueView = (EditText) rootView.findViewById(R.id.edt_counter_value);

        decrementButton = rootView.findViewById(R.id.btn_counter_decrement);
        incrementButton = rootView.findViewById(R.id.btn_counter_increment);

        decrementButton.setOnClickListener(view -> {
            int currentValue = Integer.parseInt(valueView.getText().toString());

            if (currentValue > minValue) {
                currentValue--;
                valueView.setText(String.valueOf(currentValue));
                onChangedValueListener.onChanged(currentValue);
            }
        });

        incrementButton.setOnClickListener(view -> {
            int currentValue = Integer.parseInt(valueView.getText().toString());

            if (currentValue < maxValue) {
                currentValue++;
                valueView.setText(String.valueOf(currentValue));
                onChangedValueListener.onChanged(currentValue);
            }
        });
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getValue() {
        return Integer.parseInt(valueView.getText().toString());
    }

    public void setValue(int newValue) {
        int value = newValue;

        if (newValue < minValue) value = minValue;
        else if (newValue > maxValue) value = maxValue;

        valueView.setText(String.valueOf(value));
    }
}
