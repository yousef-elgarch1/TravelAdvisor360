package com.example.traveladvisor360.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.traveladvisor360.R;

public class RatingView extends View {

    private int numStars = 5;
    private float rating = 0f;
    private int starSize = 48;
    private int starPadding = 8;
    private int activeColor;
    private int inactiveColor;

    private Paint activePaint;
    private Paint inactivePaint;
    private Path starPath;

    public RatingView(Context context) {
        super(context);
        init(context, null);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingView);

        numStars = a.getInt(R.styleable.RatingView_numStars, 5);
        rating = a.getFloat(R.styleable.RatingView_rating, 0f);
        starSize = a.getDimensionPixelSize(R.styleable.RatingView_starSize, 48);
        starPadding = a.getDimensionPixelSize(R.styleable.RatingView_starPadding, 8);
        activeColor = a.getColor(R.styleable.RatingView_activeColor, getResources().getColor(R.color.rating_active));
        inactiveColor = a.getColor(R.styleable.RatingView_inactiveColor, getResources().getColor(R.color.rating_inactive));

        a.recycle();

        activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activePaint.setColor(activeColor);
        activePaint.setStyle(Paint.Style.FILL);

        inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactivePaint.setColor(inactiveColor);
        inactivePaint.setStyle(Paint.Style.FILL);

        starPath = createStarPath();
    }

    private Path createStarPath() {
        Path path = new Path();
        float mid = starSize / 2f;
        float min = starSize * 0.1f;
        float fat = starSize * 0.4f;
        float thin = starSize * 0.25f;

        path.moveTo(mid, min);
        path.lineTo(mid + thin, mid - thin);
        path.lineTo(starSize - min, mid - thin);
        path.lineTo(mid + fat, mid);
        path.lineTo(starSize - min, mid + thin);
        path.lineTo(mid + thin, mid + thin);
        path.lineTo(mid, starSize - min);
        path.lineTo(mid - thin, mid + thin);
        path.lineTo(min, mid + thin);
        path.lineTo(mid - fat, mid);
        path.lineTo(min, mid - thin);
        path.lineTo(mid - thin, mid - thin);
        path.close();

        return path;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (starSize + starPadding) * numStars;
        int height = starSize;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < numStars; i++) {
            float starRating = Math.min(Math.max(rating - i, 0), 1);

            canvas.save();
            canvas.translate(i * (starSize + starPadding), 0);

            // Draw the inactive star
            canvas.drawPath(starPath, inactivePaint);

            // Draw the active partial star
            if (starRating > 0) {
                canvas.save();
                canvas.clipRect(0, 0, starSize * starRating, starSize);
                canvas.drawPath(starPath, activePaint);
                canvas.restore();
            }

            canvas.restore();
        }
    }

    public void setRating(float rating) {
        this.rating = rating;
        invalidate();
    }

    public float getRating() {
        return rating;
    }
}