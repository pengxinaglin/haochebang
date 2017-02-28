package com.haoche51.checker.DAO;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.entity.BaseEntity;
import com.haoche51.checker.entity.MediaEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.entity.UploadPhotoEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 验车任务待上传实体类
 * Created by wufx on 2015/12/24.
 */
public class CheckUploadTaskDAO extends BaseDAO {
  public static final String TABLE_NAME = "check_upload_task";
  public static final String CREATE_TABLE = "create table " + TABLE_NAME
    + "(" + "id integer primary key autoincrement,"
    + "task_id integer unique not null,"
    + "report_id integer unique not null,"
    + "progress integer not null default 0,"
    + "check_source text not null default '',"
    + "vehicle_name text not null default '',"
    + "upload_chance integer not null default 0,"
    + "upload_status text not null default '',"
    + "failed_reason text not null default '',"
    + "failed_operate text not null default '',"
    + "out_pics text not null default '',"
    + "inner_pics text not null default '',"
    + "detail_pics text not null default '',"
    + "defect_pics text not null default '',"
    + "all_pics text not null default '',"
    + "start_mills text not null default '0',"
    + "create_time text not null default '',"
    + "max integer not null,"
    + "video_json text default '',"
    + "audio_json text default ''"
    + ")";
  private static final String[] COLUMNS = {
    "id",
    "task_id",
    "report_id",
    "progress",
    "check_source",
    "vehicle_name",
    "upload_chance",
    "upload_status",
    "failed_reason",
    "failed_operate",
    "out_pics",
    "inner_pics",
    "detail_pics",
    "defect_pics",
    "all_pics",
    "start_mills",
    "create_time",
    "max", "video_json", "audio_json"
  };
  private static final String DEFAULT_ORDER_BY = "create_time asc";
  private static CheckUploadTaskDAO dao = new CheckUploadTaskDAO();
  private Gson mGson = new Gson();
  private Type type;
  private List<PhotoEntity> photoEntities;
  private ArrayList<UploadPhotoEntity> uploadPhotoEntities;

  private CheckUploadTaskDAO() {
  }

  public static CheckUploadTaskDAO getInstance() {
    return dao;
  }

  @Override
  protected ContentValues getContentValues(BaseEntity entity) {
    UploadCheckTaskEntity uploadCheck = (UploadCheckTaskEntity) entity;
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMNS[1], uploadCheck.getCheckTaskId());
    contentValues.put(COLUMNS[2], uploadCheck.getReportId());
    contentValues.put(COLUMNS[3], uploadCheck.getProgress());
    contentValues.put(COLUMNS[4], uploadCheck.getCheckSource());
    contentValues.put(COLUMNS[5], uploadCheck.getVehicleName());
    contentValues.put(COLUMNS[6], uploadCheck.getUploadChance());
    contentValues.put(COLUMNS[7], uploadCheck.getUploadStatus());
    contentValues.put(COLUMNS[8], uploadCheck.getFailedReason());
    contentValues.put(COLUMNS[9], uploadCheck.getFailedOperate());
    contentValues.put(COLUMNS[10], mGson.toJson(uploadCheck.getOuterPictures()));
    contentValues.put(COLUMNS[11], mGson.toJson(uploadCheck.getInnerPictures()));
    contentValues.put(COLUMNS[12], mGson.toJson(uploadCheck.getDetailPictures()));
    contentValues.put(COLUMNS[13], mGson.toJson(uploadCheck.getDefectPictures()));
    contentValues.put(COLUMNS[14], mGson.toJson(uploadCheck.getPhotoList()));
    contentValues.put(COLUMNS[15], String.valueOf(uploadCheck.getStartMills()));
    contentValues.put(COLUMNS[16], uploadCheck.getCreateTime());
    contentValues.put(COLUMNS[17], uploadCheck.getMax());
    contentValues.put(COLUMNS[18], uploadCheck.getVideoEntity() == null ? "" : uploadCheck.getVideoEntity().toJson());
    contentValues.put(COLUMNS[19], uploadCheck.getAudioEntity() == null ? "" : uploadCheck.getAudioEntity().toJson());
    return contentValues;
  }

  @Override
  protected BaseEntity getEntityFromCursor(Cursor mCursor) {
    UploadCheckTaskEntity uploadCheckTask = new UploadCheckTaskEntity();
    uploadCheckTask.setId(mCursor.getInt(0));
    uploadCheckTask.setCheckTaskId(mCursor.getInt(1));
    uploadCheckTask.setReportId(mCursor.getInt(2));
    uploadCheckTask.setProgress(mCursor.getInt(3));
    uploadCheckTask.setCheckSource(mCursor.getString(4));
    uploadCheckTask.setVehicleName(mCursor.getString(5));
    uploadCheckTask.setUploadChance(mCursor.getInt(6));
    uploadCheckTask.setUploadStatus(mCursor.getString(7));
    uploadCheckTask.setFailedReason(mCursor.getString(8));
    uploadCheckTask.setFailedOperate(mCursor.getString(9));
    type = new TypeToken<List<PhotoEntity>>() {
    }.getType();
    photoEntities = mGson.fromJson(mCursor.getString(10), type);
    uploadCheckTask.setOuterPictures(photoEntities);
    photoEntities = mGson.fromJson(mCursor.getString(11), type);
    uploadCheckTask.setInnerPictures(photoEntities);
    photoEntities = mGson.fromJson(mCursor.getString(12), type);
    uploadCheckTask.setDetailPictures(photoEntities);
    photoEntities = mGson.fromJson(mCursor.getString(13), type);
    uploadCheckTask.setDefectPictures(photoEntities);
    type = new TypeToken<List<UploadPhotoEntity>>() {
    }.getType();
    uploadPhotoEntities = mGson.fromJson(mCursor.getString(14), type);
    uploadCheckTask.setPhotoList(uploadPhotoEntities);
    uploadCheckTask.setStartMills(Long.parseLong(mCursor.getString(15)));
    uploadCheckTask.setCreateTime(mCursor.getString(16));
    uploadCheckTask.setMax(mCursor.getInt(17));
    uploadCheckTask.setVideoEntity(mGson.fromJson(mCursor.getString(18), MediaEntity.class));
    uploadCheckTask.setAudioEntity(mGson.fromJson(mCursor.getString(19), MediaEntity.class));
    return uploadCheckTask;
  }

  @Override
  protected String getTableName() {
    return TABLE_NAME;
  }

  @Override
  protected String[] getColumns() {
    return COLUMNS;
  }

  @Override
  protected String getDefaultOrderby() {
    return DEFAULT_ORDER_BY;
  }

  @Override
  public UploadCheckTaskEntity get(int id) {
    return (UploadCheckTaskEntity) super.get(id);
  }


  /**
   * 根据条件查询上传任务列表
   *
   * @param where 查询条件：null表示查询全部
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<UploadCheckTaskEntity> get(String where) {
    return (List<UploadCheckTaskEntity>) super.get(where);
  }


  /**
   * 根据任务ID更新上传中任务
   *
   * @param entity
   * @return
   */
  public int updateByTaskId(UploadCheckTaskEntity entity) {
    if (mDb == null || entity == null) return 0;

    ContentValues mValues = getContentValues(entity);
    int ret = update(mValues, "task_id=" + entity.getCheckTaskId());
    return ret;
  }

  /**
   * 根据验车任务ID删除验车任务
   *
   * @param taskId 任务ID
   * @return
   */
  public void deleteByTaskId(int taskId) {
    mDb.execSQL("delete from check_upload_task where task_id=" + taskId);
  }
}
