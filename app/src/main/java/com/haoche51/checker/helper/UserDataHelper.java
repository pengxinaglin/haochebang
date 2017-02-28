package com.haoche51.checker.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.haoche51.checker.Checker;
import com.haoche51.checker.LoginToken;
import com.haoche51.checker.entity.UserRightEntity;
import com.haoche51.checker.util.JsonParseUtil;

import java.util.List;

public class UserDataHelper {
  private static final String USER_DATA_FILE = "user_data";

  private static final String IS_LOGIN = "is_login";
  private static final String LOGIN_NAME = "login_name";
  private static final String LOGIN_PWD = "login_pwd";
  private static final String BD_HAS_BIND = "baidu_has_bind";
  private static final String BD_USER_ID = "baidu_user_id";
  private static final String BD_CHANNEL_ID = "baidu_channel_id";
  private static final String CHECKER = "checker_json";
  private static final String LAST_CHECKER_ID = "last_checker_id";
  private static final String BIND_PUSH_ID = "bind_baidu_push_id";//修改preference 名字，让客户端重新绑定推送。避免百度升级造成老版本绑定id 不能推送
  private static final String IMPORT_VEHICLE_DATA = "import_vehicle_data";
//  public static final String HAS_NEW_PERSONAL_MESSAGE = "hasNewPersonalMessage";
  public static final String LAST_UPDATE_TIME = "last_update_time";
  public static final String CHECKER_SAVED_REPORT = "checker_saved_report";
  public static final String LOGIN_TOKEN = "login_token";
  public static final String USER_RIGHT = "user_right";

  public static final String POS_LOGIN_NAME = "pos_login_name";//pos操作员
  public static final String POS_RSA_YT_PUBLIC = "pos_rsa_yt_public";//银通支付公钥
  public static final String POS_MD5_KEY = "pos_md5_key";//银通支付
  /**
   * 渠寄权限
   * 1:普通权限,2:中级权限,4:高级权限
   */
  private int mChannelRight;
  /**
   * 收车权限
   * 1:普通权限,2:中级权限,4:高级权限
   */
  private int mPurchaseRight;
  /**
   * 创建回购线索
   * 1:内网,2:外网,4:全选权限
   */
  private int mPurchaseClueRight;
  private SharedPreferences reader = null;
  private SharedPreferences.Editor writer = null;
  private Gson gson = null;

  public UserDataHelper(Context mContext) {
    reader = mContext.getSharedPreferences(USER_DATA_FILE, Context.MODE_PRIVATE);
    writer = reader.edit();
    gson = new Gson();
  }

  /***
   * 清除登录和绑定推送状态
   */
  public void clearLoginAndPushStatus() {
    writer.putBoolean(IS_LOGIN, false).putBoolean(BD_HAS_BIND, false).apply();
  }

  public UserDataHelper setLogin() {
    writer.putBoolean(IS_LOGIN, true);
    return this;
  }

  public UserDataHelper clearLogin() {
    writer.remove(IS_LOGIN);
    return this;
  }

  public int getChannelRight() {
    return mChannelRight;
  }

  public void setChannelRight(int channelRight) {
    this.mChannelRight = channelRight;
  }

  public boolean isLogin() {
    return reader.getBoolean(IS_LOGIN, false);
  }

  /**
   * 登陆token 相关
   *
   * @param token
   * @return
   */
  public UserDataHelper setLoginToken(LoginToken token) {
    writer.putString(LOGIN_TOKEN, gson.toJson(token));
    return this;
  }

  /**
   * 清除登陆token
   *
   * @return
   */
  public UserDataHelper clearLoginToken() {
    writer.remove(LOGIN_TOKEN);
    return this;
  }

  /**
   * 获取登录token
   *
   * @return
   */
  public LoginToken getLoginToken() {
    String token_str = reader.getString(LOGIN_TOKEN, "{}");
    return gson.fromJson(token_str, LoginToken.class);
  }


  public UserDataHelper setLoginName(String username) {
    writer.putString(LOGIN_NAME, username);
    return this;
  }

  public UserDataHelper clearLoginName() {
    writer.remove(LOGIN_NAME);
    return this;
  }

  public String getLoginName() {
    return reader.getString(LOGIN_NAME, "");
  }

  public UserDataHelper setLoginPwd(String password) {
    writer.putString(LOGIN_PWD, password);
    return this;
  }

  public UserDataHelper clearLoginPwd() {
    writer.remove(LOGIN_PWD);
    return this;
  }

  public String getLoginPwd() {
    return reader.getString(LOGIN_PWD, "");
  }

  public UserDataHelper bindBaiduPush() {
    writer.putBoolean(BD_HAS_BIND, true);
    return this;
  }

  public UserDataHelper unbindBaiduPush() {
    writer.putBoolean(BD_HAS_BIND, false);
    return this;
  }

  public boolean hasBindBaiduPush() {
    return reader.getBoolean(BD_HAS_BIND, false);
  }

  public UserDataHelper setPushUserId(String userId) {
    writer.putString(BD_USER_ID, userId);
    return this;
  }

  public UserDataHelper clearPushUserId() {
    writer.remove(BD_USER_ID);
    return this;
  }

  public String getPushUserId() {
    return reader.getString(BD_USER_ID, null);
  }

  public UserDataHelper setPushChannelId(String channelId) {
    writer.putString(BD_CHANNEL_ID, channelId);
    return this;
  }

  public UserDataHelper clearPushChannelId() {
    writer.remove(BD_CHANNEL_ID);
    return this;
  }

  public String getPushChannelId() {
    return reader.getString(BD_CHANNEL_ID, null);
  }

  public UserDataHelper setChecker(Checker checker) {
    writer.putString(CHECKER, gson.toJson(checker));
    return this;
  }

  public UserDataHelper clearChecker() {
    writer.remove(CHECKER);
    return this;
  }

  public Checker getChecker() {
    String checkerJson = reader.getString(CHECKER, "{}");
    return gson.fromJson(checkerJson, Checker.class);
  }

  public UserDataHelper setLastCheckerId(int checkerId) {
    writer.putInt(LAST_CHECKER_ID, checkerId);
    return this;
  }

  public UserDataHelper clearLastCheckerId() {
    writer.remove(LAST_CHECKER_ID);
    return this;
  }

  public int getLastCheckerId() {
    return reader.getInt(LAST_CHECKER_ID, 0);
  }

  public UserDataHelper setPushId(int pushId) {
    writer.putInt(BIND_PUSH_ID, pushId);
    return this;
  }

  public UserDataHelper clearPushId() {
    writer.remove(BIND_PUSH_ID);
    return this;
  }

  public int getPushId() {
    return reader.getInt(BIND_PUSH_ID, 0);
  }

  public UserDataHelper setImportVehicleData() {
    writer.putBoolean(IMPORT_VEHICLE_DATA, true);
    return this;
  }

  public UserDataHelper clearImportVehicleData() {
    writer.remove(IMPORT_VEHICLE_DATA);
    return this;
  }


  public UserDataHelper setUserRight(String userRight) {
    writer.putString(USER_RIGHT, userRight);
    return this;
  }

  public List<UserRightEntity> getUserRight() {
    String userRightJson = reader.getString(USER_RIGHT, "");
    List<UserRightEntity> list = JsonParseUtil.fromJsonArray(userRightJson, UserRightEntity.class);
    return list;
  }

  public UserDataHelper clearUserRight() {
    writer.remove(USER_RIGHT);
    return this;
  }

  public UserDataHelper setPosLoginName(String posLoginName) {
    writer.putString(POS_LOGIN_NAME, posLoginName);
    return this;
  }

  public String getPosLoginName() {
    return reader.getString(POS_LOGIN_NAME, "");
  }

  public UserDataHelper setPosRSAYTPublic(String posRSAYTPublic) {
    writer.putString(POS_RSA_YT_PUBLIC, posRSAYTPublic);
    return this;
  }

  public String getPosRSAYTPublic() {
    return reader.getString(POS_RSA_YT_PUBLIC, "");
  }

  public UserDataHelper setPosMD5Key(String posMD5Key) {
    writer.putString(POS_MD5_KEY, posMD5Key);
    return this;
  }

  public String getPosMD5Key() {
    return reader.getString(POS_MD5_KEY, "");
  }

  public boolean commit() {
    return writer.commit();
  }

  public int getPurchaseRight() {
    return mPurchaseRight;
  }

  public void setPurchaseRight(int purchaseRight) {
    this.mPurchaseRight = purchaseRight;
  }

  public int getPurchaseClueRight() {
    return mPurchaseClueRight;
  }

  public void setPurchaseClueRight(int purchaseClueRight) {
    this.mPurchaseClueRight = purchaseClueRight;
  }

}
