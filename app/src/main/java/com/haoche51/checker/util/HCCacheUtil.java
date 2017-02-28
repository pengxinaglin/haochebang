package com.haoche51.checker.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.entity.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class HCCacheUtil {

  private static Gson mGson = new Gson();
  private static ACache mCache = ACache.get(GlobalData.mContext);

  /* 缓存推送消息信息 */
  private final static String KEY_CACHED_PUSHMESSAGE = "key_push_messages";

  /**
   * 获取本地缓存的推送消息
   *
   * @return
   */
  public static List<MessageEntity> getCacheMessages() {
    List<MessageEntity> mData = null;
    int checker_id = GlobalData.userDataHelper.getChecker().getId();
    String cachedStr = mCache.getAsString(KEY_CACHED_PUSHMESSAGE + checker_id);
    if (!TextUtils.isEmpty(cachedStr)) {
      mData = mGson.fromJson(cachedStr, new TypeToken<List<MessageEntity>>() {
      }.getType());
    }
    if (mData == null) {
      mData = new ArrayList<MessageEntity>();
    }
    return mData;

  }

  /**
   * 存储推送的消息信息
   *
   * @param entities
   */
  public static void saveCacheMessages(final List<MessageEntity> entities) {
    if (entities == null || entities.isEmpty()) return;
    Runnable command = new Runnable() {
      @Override
      public void run() {
        String value = mGson.toJson(entities);
        int checker_id = GlobalData.userDataHelper.getChecker().getId();
        mCache.remove(KEY_CACHED_PUSHMESSAGE + checker_id);
        mCache.put(KEY_CACHED_PUSHMESSAGE + checker_id, value, Integer.MAX_VALUE);
      }
    };

    HCThreadUtil.execute(command);
  }

  /**
   * 更新缓存中指定信息
   *
   * @param entity
   */
  public static void updateCacheMessage(final MessageEntity entity) {

    Runnable command = new Runnable() {
      @Override
      public void run() {
        List<MessageEntity> mCacheData = getCacheMessages();
        if (mCacheData != null && !mCacheData.isEmpty()) {
          int size = mCacheData.size();
          int index = -1;
          for (int i = 0; i < size; i++) {
            MessageEntity cEntity = mCacheData.get(i);
            if (cEntity.getId() == entity.getId()) {
              index = i;
              break;
            }
          }
          if (index != -1) {
            mCacheData.remove(index);
            mCacheData.add(index, entity);
            saveCacheMessages(mCacheData);
          }
        }

      }
    };

    HCThreadUtil.execute(command);

  }
}
