package com.haoche51.checker;

import android.content.Context;

import com.haoche51.checker.helper.BrandLogoHelper;
import com.haoche51.checker.helper.DatabaseHelper;
import com.haoche51.checker.helper.ResourceHelper;
import com.haoche51.checker.helper.SettingHelper;
import com.haoche51.checker.helper.UserDataHelper;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.ValidationTipUtils;

public class GlobalData {
  public static Context context = null;
  public static DatabaseHelper dbHelper = null;
  public static UserDataHelper userDataHelper = null;
  public static ResourceHelper resourceHelper = null;
  public static ValidationTipUtils mTips = null;
  public static BrandLogoHelper brandLogo = null;

  public static void init(Context mContext) {
    HCLogUtil.e("push", "init global data");
    context = mContext;
    userDataHelper = new UserDataHelper(mContext);
    resourceHelper = new ResourceHelper(mContext);
    dbHelper = new DatabaseHelper(mContext);
    mTips = new ValidationTipUtils(mContext);
    brandLogo = new BrandLogoHelper(context);
  }

  public static Context mContext = null;
  public static SettingHelper mSetting = null;

  public static void initSetting(Context context) {
    mContext = context;
    mSetting = new SettingHelper(mContext);
  }
}
