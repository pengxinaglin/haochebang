package com.haoche51.checker.util;

import android.app.Activity;

import android.app.ProgressDialog;

public class HCDialogUtil {

  private static ProgressDialog progressDialog;

  public static void showProgressDialog(Activity mAct) {
    if (mAct == null || mAct.isFinishing())
      return;

    progressDialog = new ProgressDialog(mAct);
    progressDialog.setCancelable(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setMessage("处理中...");
    progressDialog.show();
  }

  public static void dismissProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
      progressDialog = null;
    }
  }


}
