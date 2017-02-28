package com.haoche51.checker.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 验车任务之上传实体类
 * Created by wufx on 2015/12/21.
 */
public class UploadCheckTaskEntity extends BaseEntity implements Parcelable{
  /**
   * 主键id
   */
  private int id;
  /**
   * 验车任务对应的照片列表
   */
  private ArrayList<UploadPhotoEntity> photoList;
  /**
   * 验车任务id
   */
  private int checkTaskId;
  /**
   * 任务来源
   */
  private String checkSource;
  /**
   * 车辆名称
   */
  private String vehicleName;
  /**
   * 验车任务对应的报告id
   */
  private int reportId;
  /**
   * 上传失败的原因
   */
  private String failedReason = "";
  /**
   * 上传失败的操作
   */
  private String failedOperate = "";
  /**
   * 上传的状态：1、上传中  2、中断  3、排队中 4、已完成
   */
  private String uploadStatus;
  /**
   * 已经上传图片进度:上传多少张，进度就为多少
   */
  private int progress = 0;
  /**
   * 上传机会
   */
  private int uploadChance = 3;
  /**
   * 任务创建时间
   */
  private String createTime = "";

  //图片列表
  /**
   * 外观图json串
   */
  private String out_pics = "";
  /**
   * 内饰图json串
   */
  private String inner_pics = "";
  /**
   * 细节图json串
   */
  private String detail_pics = "";
  /**
   * 瑕疵图json串
   */
  private String defect_pics = "";
  /**
   * 该任务对应所有图片的json串
   */
  private String all_pics = "";

  /**
   * 起始时间：时间戳
   */
  private long startMills;

  /**
   * 最大进度
   */
  private int max;

  /**
   * 外观图
   */
  private List<PhotoEntity> outerPictures = new ArrayList<>();
  /**
   * 内饰图
   */
  private List<PhotoEntity> innerPictures = new ArrayList<>();
  /**
   * 细节图
   */
  private List<PhotoEntity> detailPictures = new ArrayList<>();
  /**
   * 瑕疵图
   */
  private List<PhotoEntity> defectPictures = new ArrayList<>();

  private MediaEntity videoEntity;

  private MediaEntity audioEntity;

  public UploadCheckTaskEntity() {
  }

  public UploadCheckTaskEntity(ArrayList<UploadPhotoEntity> photoList, int checkTaskId, int reportId) {
    this.photoList = photoList;
    this.checkTaskId = checkTaskId;
    this.reportId = reportId;
  }

  protected UploadCheckTaskEntity(Parcel in) {
    id = in.readInt();
    checkTaskId = in.readInt();
    checkSource = in.readString();
    vehicleName = in.readString();
    reportId = in.readInt();
    failedReason = in.readString();
    failedOperate = in.readString();
    uploadStatus = in.readString();
    progress = in.readInt();
    uploadChance = in.readInt();
    createTime = in.readString();
    out_pics = in.readString();
    inner_pics = in.readString();
    detail_pics = in.readString();
    defect_pics = in.readString();
    all_pics = in.readString();
    startMills = in.readLong();
    max = in.readInt();
    outerPictures = in.createTypedArrayList(PhotoEntity.CREATOR);
    innerPictures = in.createTypedArrayList(PhotoEntity.CREATOR);
    detailPictures = in.createTypedArrayList(PhotoEntity.CREATOR);
    defectPictures = in.createTypedArrayList(PhotoEntity.CREATOR);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeInt(checkTaskId);
    dest.writeString(checkSource);
    dest.writeString(vehicleName);
    dest.writeInt(reportId);
    dest.writeString(failedReason);
    dest.writeString(failedOperate);
    dest.writeString(uploadStatus);
    dest.writeInt(progress);
    dest.writeInt(uploadChance);
    dest.writeString(createTime);
    dest.writeString(out_pics);
    dest.writeString(inner_pics);
    dest.writeString(detail_pics);
    dest.writeString(defect_pics);
    dest.writeString(all_pics);
    dest.writeLong(startMills);
    dest.writeInt(max);
    dest.writeTypedList(outerPictures);
    dest.writeTypedList(innerPictures);
    dest.writeTypedList(detailPictures);
    dest.writeTypedList(defectPictures);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<UploadCheckTaskEntity> CREATOR = new Creator<UploadCheckTaskEntity>() {
    @Override
    public UploadCheckTaskEntity createFromParcel(Parcel in) {
      return new UploadCheckTaskEntity(in);
    }

    @Override
    public UploadCheckTaskEntity[] newArray(int size) {
      return new UploadCheckTaskEntity[size];
    }
  };

  public ArrayList<UploadPhotoEntity> getPhotoList() {
    return photoList;
  }

  public void setPhotoList(ArrayList<UploadPhotoEntity> photoList) {
    this.photoList = photoList;
  }

  public int getReportId() {
    return reportId;
  }

  public void setReportId(int reportId) {
    this.reportId = reportId;
  }

  public String getFailedReason() {
    return failedReason;
  }

  public void setFailedReason(String failedReason) {
    this.failedReason = failedReason;
  }

  public String getUploadStatus() {
    return uploadStatus;
  }

  public void setUploadStatus(String uploadStatus) {
    this.uploadStatus = uploadStatus;
  }

  public int getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public String getFailedOperate() {
    return failedOperate;
  }

  public void setFailedOperate(String failedOperate) {
    this.failedOperate = failedOperate;
  }

  public List<PhotoEntity> getOuterPictures() {
    return outerPictures;
  }

  public void setOuterPictures(List<PhotoEntity> outerPictures) {
    this.outerPictures = outerPictures;
  }

  public List<PhotoEntity> getInnerPictures() {
    return innerPictures;
  }

  public void setInnerPictures(List<PhotoEntity> innerPictures) {
    this.innerPictures = innerPictures;
  }

  public List<PhotoEntity> getDetailPictures() {
    return detailPictures;
  }

  public void setDetailPictures(List<PhotoEntity> detailPictures) {
    this.detailPictures = detailPictures;
  }

  public List<PhotoEntity> getDefectPictures() {
    return defectPictures;
  }

  public void setDefectPictures(List<PhotoEntity> defectPictures) {
    this.defectPictures = defectPictures;
  }

  public int getCheckTaskId() {
    return checkTaskId;
  }

  public void setCheckTaskId(int checkTaskId) {
    this.checkTaskId = checkTaskId;
  }

  public String getCheckSource() {
    return checkSource;
  }

  public void setCheckSource(String checkSource) {
    this.checkSource = checkSource;
  }

  public String getVehicleName() {
    return vehicleName;
  }

  public void setVehicleName(String vehicleName) {
    this.vehicleName = vehicleName;
  }

  public int getUploadChance() {
    return uploadChance;
  }

  public void setUploadChance(int uploadChance) {
    this.uploadChance = uploadChance;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getOut_pics() {
    return out_pics;
  }

  public void setOut_pics(String out_pics) {
    this.out_pics = out_pics;
  }

  public String getInner_pics() {
    return inner_pics;
  }

  public void setInner_pics(String inner_pics) {
    this.inner_pics = inner_pics;
  }

  public String getDetail_pics() {
    return detail_pics;
  }

  public void setDetail_pics(String detail_pics) {
    this.detail_pics = detail_pics;
  }

  public String getDefect_pics() {
    return defect_pics;
  }

  public void setDefect_pics(String defect_pics) {
    this.defect_pics = defect_pics;
  }

  public String getAll_pics() {
    return all_pics;
  }

  public void setAll_pics(String all_pics) {
    this.all_pics = all_pics;
  }

  public long getStartMills() {
    return startMills;
  }

  public void setStartMills(long startMills) {
    this.startMills = startMills;
  }

  /**
   * 重写其equals方法
   *
   * @return
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof UploadCheckTaskEntity)) {
      return false;
    }

    UploadCheckTaskEntity taskEntity = (UploadCheckTaskEntity) obj;

    return this.checkTaskId == taskEntity.getCheckTaskId();
  }

  /**
   * 重写其hashCode方法
   *
   * @return
   */
  @Override
  public int hashCode() {
    return this.checkTaskId;
  }

  public MediaEntity getVideoEntity() {
    return videoEntity;
  }

  public void setVideoEntity(MediaEntity videoEntity) {
    this.videoEntity = videoEntity;
  }

  public MediaEntity getAudioEntity() {
    return audioEntity;
  }

  public void setAudioEntity(MediaEntity audioEntity) {
    this.audioEntity = audioEntity;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  /**
   * 检查所有的图片是否都有url
   */
  public boolean checkAllPhotoHasUrl() {
    // 外观图
    if (getOuterPictures() != null && getOuterPictures().size() > 0) {
      for (int i = 0; i < getOuterPictures().size(); i++) {
        PhotoEntity photoEntity = getOuterPictures().get(i);
        if (TextUtils.isEmpty(photoEntity.getUnid()) || TextUtils.isEmpty(photoEntity.getUrl())) {
          return false;
        }
      }
    }

    // 内饰图
    if (getInnerPictures() != null && getInnerPictures().size() > 0) {
      for (int i = 0; i < getInnerPictures().size(); i++) {
        PhotoEntity photoEntity = getInnerPictures().get(i);
        if (TextUtils.isEmpty(photoEntity.getUnid()) || TextUtils.isEmpty(photoEntity.getUrl())) {
          return false;
        }
      }
    }

    // 细节图
    if (getDetailPictures() != null && getDetailPictures().size() > 0) {
      for (int i = 0; i < getDetailPictures().size(); i++) {
        PhotoEntity photoEntity = getDetailPictures().get(i);
        if (TextUtils.isEmpty(photoEntity.getUnid()) || TextUtils.isEmpty(photoEntity.getUrl())) {
          return false;
        }
      }
    }

    // 缺陷图
    if (getDefectPictures() != null && getDefectPictures().size() > 0) {
      for (int i = 0; i < getDefectPictures().size(); i++) {
        PhotoEntity photoEntity = getDefectPictures().get(i);
        if (TextUtils.isEmpty(photoEntity.getUnid()) || TextUtils.isEmpty(photoEntity.getUrl())) {
          return false;
        }
      }
    }

    return true;
  }

}
