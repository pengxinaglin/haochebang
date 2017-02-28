package com.haoche51.checker.custom;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.haoche51.checker.R;
import com.haoche51.checker.util.DisplayUtils;

/**
 * Created by mac on 15/9/14.
 */

public class SelectDefectsPhotoPositionPopUpWindow extends PopupWindow implements FlawConfirmView.onFlawSelectedListener {
    private FlawConfirmView mFlawView;
    private OnFlawSelectListener mListener;

    public SelectDefectsPhotoPositionPopUpWindow(final Activity context) {


        View popView = LayoutInflater.from(context).inflate(R.layout.popupwindow_select_defects_position, null);
        mFlawView = (FlawConfirmView) popView.findViewById(R.id.flaw_selected);
        mFlawView.setOnFlawSelectedListener(this);
        // 设置SelectPicPopupWindow的View
        this.setContentView(popView);
        // 设置SelectPicPopupWindow弹出窗体的宽高
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//    this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(DisplayUtils.dip2px(context, 300));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.anim.fade_ins);
    }

    /**
     * 显示弹窗
     */
    public void showPopupWindow(View v, int index, float position_x, float position_y) {
        if (!this.isShowing()) {
            mFlawView.setPosition(index, position_x, position_y);
            showAtLocation(v, Gravity.BOTTOM, 0, 0);
        } else
            this.dismiss();
    }

    public void setOnFlawSelectListener(OnFlawSelectListener l) {
        mListener = l;
    }

    @Override
    public void confirmFlawSelected(float positionX, float positionY, int position) {
        if (mListener != null) {
            mListener.selected(positionX, positionY, position);
        }
        dismiss();
    }

    @Override
    public void cancelFlawSelected(int position) {
        if (mListener != null) {
            mListener.cancel(position);
        }
        dismiss();
    }

    /**
     * 关闭弹窗
     */
    public void dismissPopUpWindow() {
        if (this.isShowing())
            this.dismiss();
    }

    public interface OnFlawSelectListener {
        void selected(float positionX, float positionY, int position);

        void cancel(int position);
    }
}


