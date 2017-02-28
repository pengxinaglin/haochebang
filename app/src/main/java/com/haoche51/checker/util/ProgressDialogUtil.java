package com.haoche51.checker.util;

import android.app.Activity;
import android.app.ProgressDialog;


/**
 * ProgressDialog工具类
 */

public class ProgressDialogUtil {
  private static ProgressDialog progressDialog;
  private static boolean isShowProgressDialog;

  public static void showProgressDialog(Activity activity, String msg) {
    if (activity == null || activity.isFinishing()) {
      return;
    }
    progressDialog = new ProgressDialog(activity);
    progressDialog.setCancelable(true);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    if (msg == null) {
      progressDialog.setMessage("处理中...");
    } else {
      progressDialog.setMessage(msg);
    }
    progressDialog.show();
    isShowProgressDialog = true;
  }

  public static void showProgressDialogWithProgress(Activity activity, String title, int maxProgress) {
    if (activity == null || activity.isFinishing()) {
      return;
    }
    progressDialog = new ProgressDialog(activity);
    progressDialog.setCancelable(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setMax(maxProgress);
    if (title == null) {
      progressDialog.setTitle("处理中...");
    } else {
      progressDialog.setTitle(title);
    }
    progressDialog.show();
    isShowProgressDialog = true;
  }


  public static void setProgress(int progress) {
    if (progressDialog != null) {
      progressDialog.setProgress(progress);
    }
  }

  public static void closeProgressDialog() {
    try {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.dismiss();
        progressDialog = null;
        isShowProgressDialog = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean isShowProgress() {
    return isShowProgressDialog;
  }
}
