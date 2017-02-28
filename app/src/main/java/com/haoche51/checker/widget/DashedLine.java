package com.haoche51.checker.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * 虚线控件
 * Created by pxl on 15/7/8.
 */
public class DashedLine extends View {

  public DashedLine(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    WindowManager wm = (WindowManager) getContext()
      .getSystemService(getContext().WINDOW_SERVICE);
    int width = wm.getDefaultDisplay().getWidth();

    Paint paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(Color.GRAY);
    Path path = new Path();
    path.moveTo(0, 10);
    path.lineTo(width, 10);
    PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
    paint.setPathEffect(effects);
    canvas.drawPath(path, paint);
  }
}