package com.haoche51.checker.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.entity.MediaEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 16/2/15.
 */
public class HCCheckTaskUtil {

  /**
   * 1, 读取数据库 。
   * 2 。读取 Camera 列表
   * 3. 无照片只初始化示例图
   *
   * @param entity 评估报告
   * @return images // 图片详情
   */
  public static Map<String, List<PhotoEntity>> getTaskPhotoList(CheckReportEntity entity) {
    Map<String, List<PhotoEntity>> images = new HashMap<>();
    Gson mGson = new Gson();

    /** 外观图 */
    ArrayList<PhotoEntity> outImages = mGson.fromJson(entity.getOut_pics(), new TypeToken<ArrayList<PhotoEntity>>() {
    }.getType());
    if (outImages == null) outImages = new ArrayList<PhotoEntity>();
    //去除已经被用户删除了图片，保证显示图片的正确性
    List<PhotoEntity> needRemoveOutPic = new ArrayList<>();
    for (PhotoEntity e : outImages) {
      if (TextUtils.isEmpty(e.getPath()) || !new File(e.getPath()).exists())
        needRemoveOutPic.add(e);
    }
    outImages.removeAll(needRemoveOutPic);
    images.put(PictureConstants.OUTER_PICTURE_TYPE, outImages);

    /**初始化内饰图*/
    List<PhotoEntity> innerImages = mGson.fromJson(entity.getInner_pics(), new TypeToken<ArrayList<PhotoEntity>>() {
    }.getType());
    if (innerImages == null) innerImages = new ArrayList<PhotoEntity>();
    //去除已经被用户删除了图片，保证显示图片的正确性
    List<PhotoEntity> needRemoveInnerPic = new ArrayList<>();
    for (PhotoEntity e : innerImages) {
      if (TextUtils.isEmpty(e.getPath()) || !new File(e.getPath()).exists())
        needRemoveInnerPic.add(e);
    }
    innerImages.removeAll(needRemoveInnerPic);
    images.put(PictureConstants.INNER_PICTURE_TYPE, innerImages);

    /**初始化细节图*/
    List<PhotoEntity> detailImages = mGson.fromJson(entity.getDetail_pics(), new TypeToken<ArrayList<PhotoEntity>>() {
    }.getType());
    if (detailImages == null) detailImages = new ArrayList<PhotoEntity>();
    //去除已经被用户删除了图片，保证显示图片的正确性
    List<PhotoEntity> needRemoveDetailPic = new ArrayList<>();
    for (PhotoEntity e : detailImages) {
      if (TextUtils.isEmpty(e.getPath()) || !new File(e.getPath()).exists())
        needRemoveDetailPic.add(e);
    }
    detailImages.removeAll(needRemoveDetailPic);
    images.put(PictureConstants.DETAIL_PICTURE_TYPE, detailImages);

    /**初始化瑕疵图*/
    List<PhotoEntity> defectImages = mGson.fromJson(entity.getDefect_pics(), new TypeToken<ArrayList<PhotoEntity>>() {
    }.getType());
    if (defectImages == null) defectImages = new ArrayList<PhotoEntity>();
    //去除已经被用户删除了图片，保证显示图片的正确性
    List<PhotoEntity> needRemoveDefectPic = new ArrayList<>();
    for (PhotoEntity e : defectImages) {
      if (TextUtils.isEmpty(e.getPath()) || !new File(e.getPath()).exists())
        needRemoveDefectPic.add(e);
    }
    defectImages.removeAll(needRemoveDefectPic);
    images.put(PictureConstants.DEFECT_PICTURE_TYPE, defectImages);
    return images;
  }

  public static MediaEntity getTaskVideoEntity(CheckReportEntity checkReport) {
    if (checkReport == null) return null;
    String str = checkReport.getVideo_url();
    if (TextUtils.isEmpty(str)) return null;
    try {
      return new Gson().fromJson(str, MediaEntity.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static MediaEntity getTaskAudioEntity(CheckReportEntity checkReport) {
    if (checkReport == null) return null;
    String str = checkReport.getAudio_url();
    if (TextUtils.isEmpty(str)) return null;
    try {
      return new Gson().fromJson(str, MediaEntity.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
