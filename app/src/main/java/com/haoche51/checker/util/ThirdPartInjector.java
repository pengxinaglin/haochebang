package com.haoche51.checker.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.haoche51.checker.BuildConfig;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.service.LocationService;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lightman_mac on 11/2/15.
 * 统计,不直接调用umeng提供的API,以防被测试环境数据污染
 * <p>
 * 在这里统一第三方集成
 */
public class ThirdPartInjector {

  /**
   * 百度push key
   */
  public final static String BD_PUSH_KEY = "U8EB7WquN3HNM6Svt8GRY9ad";

//  /**
//   * 听云 key
//   */
//  public final static String TINGYUN_KEY_TEST = "1dfd4b97b1a0403195a05c11054dfcf2";//测试
//  /**
//   * 听云 key
//   */
//  public final static String TINGYUN_KEY = "a1598e69eed94ad380c8bdd54de328d9";//正式

  /**
   * 集成听云监控
   *
   * @param mAct activity
   */
//  public static void startTingyun(Activity mAct) {
//    if (BuildConfig.DEBUG_TINGYUN) {//统计正式版本
//      NBSAppAgent.setLicenseKey(TINGYUN_KEY).withLocationServiceEnabled(true).start(mAct);
//    } else {//统计测试版本
//      NBSAppAgent.setLicenseKey(TINGYUN_KEY_TEST).withLocationServiceEnabled(true).start(mAct);
//    }
//    NBSAppAgent.setUserCrashMessage("userName", GlobalData.userDataHelper.getChecker().getUsername());
//    NBSAppAgent.setUserCrashMessage("userName", GlobalData.userDataHelper.getChecker().getPhone());
//
//  }

  /**
   * 启动百度推送
   */
  public static void startBaiduPush() {
    PushManager.startWork(GlobalData.mContext, PushConstants.LOGIN_TYPE_API_KEY, BD_PUSH_KEY);
    HCLogUtil.d(" start baidu push now ....");
  }

  /**
   * 开启百度定位
   */
  public static void startBaiduLocation() {
    HCLogUtil.d(" start baidu location now ....");
    GlobalData.mContext.startService(new Intent(GlobalData.mContext, LocationService.class));
  }

  /**
   * 友盟 页面统计
   *
   * @param tag
   */
  public static void onPageStart(String tag) {
    if (TextUtils.isEmpty(tag)) return;

    if (BuildConfig.ENABLE_UMENG_ANALYTICS) {
      MobclickAgent.onPageStart(tag);
    }
  }

  /**
   * 友盟 页面统计
   *
   * @param tag
   */
  public static void onPageEnd(String tag) {
    if (TextUtils.isEmpty(tag)) return;

    if (BuildConfig.ENABLE_UMENG_ANALYTICS) {
      MobclickAgent.onPageEnd(tag);
    }
  }

  /**
   * 友盟 页面统计
   *
   * @param mAct
   */
  public static void onResume(Activity mAct) {
    if (mAct == null) return;

    if (BuildConfig.ENABLE_UMENG_ANALYTICS) {
      MobclickAgent.onResume(mAct);
    }
  }

  /**
   * 友盟 页面统计
   *
   * @param mAct
   */
  public static void onPause(Activity mAct) {
    if (mAct == null) return;

    if (BuildConfig.ENABLE_UMENG_ANALYTICS) {
      MobclickAgent.onPause(mAct);
    }
  }

//  /**
//   * umeng 页面统计
//   */
//  public static void enableUMUpdateOnlineConfig() {
//    MobclickAgent.updateOnlineConfig(GlobalData.mContext);
//  }

  //禁止默认的页面统计方式，这样将不会再自动统计Activity。 false

  /**
   * 关闭友盟自动页面统计(基于Activity)
   */
  public static void disableUMAutoActivityAnalysis() {
    MobclickAgent.openActivityDurationTrack(false);
  }

  /**
   * 关闭友盟错误统计
   */
  public static void disableUMCrashAnalysis() {
    MobclickAgent.setCatchUncaughtExceptions(false);
  }

//  public static void setUMUpdateOnlineConfig(Activity mAct) {
//    MobclickAgent.updateOnlineConfig(mAct);
//  }

  /**
   * umeng更新
   */
//  public static void checkupdateUM(Activity mAct) {
//    final Activity act = mAct;
//    UmengUpdateAgent.setDeltaUpdate(false);
//    UmengUpdateAgent.setUpdateAutoPopup(false);
//    UmengUpdateAgent.setUpdateOnlyWifi(false);
//    UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//      @Override
//      public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//        switch (updateStatus) {
//          case UpdateStatus.Yes: // has update
//            UmengUpdateAgent.showUpdateDialog(act, updateInfo);
//            break;
//          default:
//            break;
//        }
//      }
//
//    });
//    UmengUpdateAgent.update(mAct);
//  }

}
