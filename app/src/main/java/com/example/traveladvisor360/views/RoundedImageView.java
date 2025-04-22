package com.example.traveladvisor360.views;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.traveladvisor360.R;

public class RoundedImageView extends AppCompatImageView {

    private float cornerRadius = 0f;
    private Path path;
    private RectF rect;

    public RoundedImageView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public RoundedImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundedImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView);
            cornerRadius = a.getDimension(R.styleable.RoundedImageView_cornerRadius, 0f);
            a.recycle();
        }

        path = new Path();
        rect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect.set(0, 0, getWidth(), getHeight());
        path.reset();
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    public void setCornerRadius(float radius) {
        this.cornerRadius = radius;
        invalidate();
    }

    public float getCornerRadius() {
        return cornerRadius;
    }
}
