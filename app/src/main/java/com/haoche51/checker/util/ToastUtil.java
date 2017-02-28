package com.haoche51.checker.util;

import android.widget.Toast;

import com.haoche51.checker.GlobalData;

public class ToastUtil {

  static Toast toast;

  /**
   * 去掉context参数，，，因为会导致内存泄漏
   *
   * @param text text
   */
  public static void showInfo(String text) {
    if (toast == null)
      toast = Toast.makeText(GlobalData.mContext, text, Toast.LENGTH_SHORT);
    else
      toast.setText(text);
    toast.show();
  }

  public static void showText(String text) {
    if (toast == null)
      toast = Toast.makeText(GlobalData.mContext, text, Toast.LENGTH_SHORT);
    else
      toast.setText(text);

    toast.show();
  }


  public static void showText(int stringRes) {
    if (toast == null)
      toast = Toast.makeText(GlobalData.mContext, GlobalData.context.getResources().getString(stringRes), Toast.LENGTH_SHORT);
    else
      toast.setText(GlobalData.context.getResources().getString(stringRes));

    toast.show();
  }

}
