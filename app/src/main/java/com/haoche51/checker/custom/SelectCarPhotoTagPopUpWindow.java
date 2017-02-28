package com.haoche51.checker.custom;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.haoche51.checker.R;
import com.haoche51.checker.pager.SelectCarPhotoTagPopUpWindowPager;
import com.haoche51.checker.util.DisplayUtils;

/**
 * Created by mac on 15/9/14.
 */
public class SelectCarPhotoTagPopUpWindow extends PopupWindow {
  private Activity context;

  public SelectCarPhotoTagPopUpWindow(final Activity context) {

    this.context = context;

    SelectCarPhotoTagPopUpWindowPager popUpWindowPager = new SelectCarPhotoTagPopUpWindowPager(context);
    // 设置SelectPicPopupWindow的View
    this.setContentView(popUpWindowPager.getRootView());
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
    ColorDrawable dw = new ColorDrawable(Color.WHITE);
    // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
    this.setBackgroundDrawable(dw);
    // 设置SelectPicPopupWindow弹出窗体动画效果
    this.setAnimationStyle(R.anim.fade_ins);
  }

  /**
   * 显示弹窗
   */
  public void showPopupWindow(View v) {
    if (!this.isShowing()) {
      showAtLocation(v, Gravity.BOTTOM, 0, 0);
    } else
      this.dismiss();
  }

  /**
   * 关闭弹窗
   */
  public void dismissPopUpWindow() {
    if (this.isShowing())
      this.dismiss();
  }
}
