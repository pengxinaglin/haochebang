package com.haoche51.checker.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.haoche51.checker.R;

public class FlawView extends View {

    private Paint mPaint;
    private int radius = 24;
    private float positionX = 30;
    private float positionY = 30;
    private int width;
    private int height;
    private Bitmap mBitmap;
    private int index = 1;

    public FlawView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public FlawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defect);
        width = mBitmap.getWidth();
        height = mBitmap.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    public float getPositionX() {
        return positionX / 2 - 11;
    }

    public float getPositionY() {
        return positionY / 2 - 11;
    }

    public void setPosition(float x, float y) {
        if (x < 30) {
            x = 30;
        }
        if (x > width) {
            x = width - 30;
        }
        positionX = x;
        if (y < 30) {
            y = 30;
        }
        if (y > height) {
            y = height - 30;
        }
        positionY = y;
    }

    /**
     * 重新设置布局
     */
    public void requestResetLayout(float positionX, float positionY) {
        this.positionX = positionX >= 30 ? positionX : 30;
        this.positionY = positionY >= 30 ? positionY : 30;
        invalidate();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        positionX = 30;
        positionY = 30;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(positionX, positionY, radius + 2, mPaint);
        mPaint.setColor(getResources().getColor(R.color.hc_indicator));
        canvas.drawCircle(positionX, positionY, radius, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(36);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("" + index, positionX, positionY + 12, mPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setPosition(event.getX(), event.getY());
                //positionX = event.getX();
                //positionY = event.getY();
                invalidate();
                performClick();
                break;
        }
        return true;
    }

}
