package com.haoche51.checker.custom;

import com.haoche51.checker.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FlawConfirmView extends RelativeLayout {
  private LayoutInflater mInflater;
  private TextView locationShow;
  private Button cancelBtn;
  private Button confirmBtn;
  private FlawView mFlawView;
  private onFlawSelectedListener mListener;
  public float positionX = 0;
  public float positionY = 0;

  public FlawConfirmView(Context context) {
    super(context);
    init(context);
  }

  public FlawConfirmView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public FlawConfirmView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  public void init(Context context) {
    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mInflater.inflate(R.layout.flaw_selected, this);
    locationShow = (TextView) findViewById(R.id.tv_show_location);
    cancelBtn = (Button) findViewById(R.id.btn_cancel_location);
    confirmBtn = (Button) findViewById(R.id.btn_confirm_location);
    mFlawView = (FlawView) findViewById(R.id.flawView);
    mFlawView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (mFlawView.getPositionX() >= 0) {
          positionX = mFlawView.getPositionX();
        } else {
          positionX = 0;
        }
        if (mFlawView.getPositionY() >= 0) {
          positionY = mFlawView.getPositionY();
        } else {
          positionY = 0;
        }
        locationShow.setText("标记位置:" + positionX + "/" + positionY);
      }

    });
    cancelBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (null != mListener) {
          mListener.cancelFlawSelected(mFlawView.getIndex());
        }

      }

    });
    confirmBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (null != mListener) {
          mListener.confirmFlawSelected(positionX, positionY, mFlawView.getIndex());
        }

      }

    });
  }

  public void setFlawIndex(int index) {
    mFlawView.setIndex(index);
  }

  public void setOnFlawSelectedListener(onFlawSelectedListener l) {
    mListener = l;
  }

  public void setPosition(int index, float position_x, float position_y) {
    mFlawView.setIndex(index);
    mFlawView.requestResetLayout(position_x * 2 + 11, position_y * 2 + 11);
    setPositionX(position_x);
    setPositionY(position_y);
    locationShow.setText((position_x == 0 && position_y == 0) ? "标记位置" : "标记位置:" + position_x + "/" + position_y);
  }

  public interface onFlawSelectedListener {
    void confirmFlawSelected(float positionX, float positionY, int position);

    void cancelFlawSelected(int position);
  }

  public void setPositionX(float positionX) {
    this.positionX = positionX;
  }

  public void setPositionY(float positionY) {
    this.positionY = positionY;
  }
}
