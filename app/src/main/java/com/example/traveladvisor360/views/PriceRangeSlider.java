package com.example.traveladvisor360.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.traveladvisor360.R;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

public class PriceRangeSlider extends LinearLayout {

    private RangeSlider rangeSlider;
    private TextView minValueText;
    private TextView maxValueText;
    private OnPriceRangeChangedListener listener;

    public interface OnPriceRangeChangedListener {
        void onPriceRangeChanged(float minPrice, float maxPrice);
    }

    public PriceRangeSlider(Context context) {
        super(context);
        init(context);
    }

    public PriceRangeSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PriceRangeSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_price_range_slider, this, true);

        rangeSlider = findViewById(R.id.range_slider);
        minValueText = findViewById(R.id.min_value_text);
        maxValueText = findViewById(R.id.max_value_text);

        rangeSlider.setValueFrom(0);
        rangeSlider.setValueTo(1000);
        rangeSlider.setValues(0f, 1000f);

        updateValueTexts(0f, 1000f);

        rangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            float minValue = values.get(0);
            float maxValue = values.get(1);

            updateValueTexts(minValue, maxValue);

            if (listener != null && fromUser) {
                listener.onPriceRangeChanged(minValue, maxValue);
            }
        });
    }

    private void updateValueTexts(float minValue, float maxValue) {
        minValueText.setText(String.format("$%.0f", minValue));
        maxValueText.setText(String.format("$%.0f", maxValue));
    }

    public void setOnPriceRangeChangedListener(OnPriceRangeChangedListener listener) {
        this.listener = listener;
    }

    public void setPriceRange(float min, float max) {
        rangeSlider.setValues(min, max);
    }

    public void setMinPrice(float min) {
        rangeSlider.setValueFrom(min);
    }

    public void setMaxPrice(float max) {
        rangeSlider.setValueTo(max);
    }

    public List<Float> getValues() {
        return rangeSlider.getValues();
    }
}
