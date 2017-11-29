package com.licon.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

public class CustomAnalogClock extends View {
    private int mHeight, mWidth = 0;
    private int mPadding = 0;
    private int mNumeralSpacing = 0;
    private int mHandTruncation, mHourHandTruncation = 0;
    private int mRadius = 0;
    private Paint mPaint;
    private Rect mRect = new Rect();
    private boolean isInit;
    private int[] mClockHours = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    public CustomAnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initializeClock();
        }
        canvas.drawColor(Color.DKGRAY);
        drawCircleBorder(canvas);
        drawClockCenter(canvas);
        drawNumericHourBorder(canvas);
        drawClockHands(canvas);
        postInvalidateDelayed(500);
        invalidate();
    }

    private void initializeClock() {
        mPaint = new Paint();
        mHeight = getHeight();
        mWidth = getWidth();
        mPadding = mNumeralSpacing + 50; // spacing from circle border
        int minAttr = Math.min(mHeight, mWidth);
        mRadius = minAttr / 2 - mPadding;
        // for maintaining different heights among the clock hands
        mHandTruncation = minAttr / 20;
        mHourHandTruncation = minAttr / 17;
        isInit = true;
    }

    private void drawCircleBorder(Canvas canvas) {
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius + mPadding - 10, mPaint);
    }

    private void drawClockCenter(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 12, mPaint);
    }

    private void drawNumericHourBorder(Canvas canvas) {
        int fontSize = (int) TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        mPaint.setTextSize(fontSize);
        for (int hour : mClockHours) {
            String tmp = String.valueOf(hour);
            mPaint.getTextBounds(tmp, 0, tmp.length(), mRect); // for circle-wise bounding
            double angle = Math.PI / 6 * (hour - 3); // as mathematical rule
            int x = (int) (mWidth / 2 + Math.cos(angle) * mRadius - mRect.width() / 2);
            int y = (int) (mHeight / 2 + Math.sin(angle) * mRadius + mRect.height() / 2);
            canvas.drawText(String.valueOf(hour), x, y, mPaint);
        }
    }

    private void drawClockHands(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        drawHandLine(canvas, (hour + calendar.get(Calendar.MINUTE) / 60) * 5f, true, false);
        drawHandLine(canvas, calendar.get(Calendar.MINUTE), false, false);
        drawHandLine(canvas, calendar.get(Calendar.SECOND), false, true);
    }

    private void drawHandLine(Canvas canvas, double moment, boolean isHour, boolean isSecond) {
        double angle = Math.PI * moment / 30 - Math.PI / 2;
        int handRadius =
            isHour ? mRadius - mHandTruncation - mHourHandTruncation : mRadius - mHandTruncation;
        if (isSecond) mPaint.setColor(Color.YELLOW);
        canvas
            .drawLine(mWidth / 2, mHeight / 2, (float) (mWidth / 2 + Math.cos(angle) * handRadius),
                (float) (mHeight / 2 + Math.sin(angle) * handRadius), mPaint);
    }
}
