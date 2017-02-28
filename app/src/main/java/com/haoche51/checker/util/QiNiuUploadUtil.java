package com.haoche51.checker.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.persistent.FileRecorder;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 七牛上传工具类
 * create by wufx 2016/1/30
 */
public class QiNiuUploadUtil implements HCHttpCallback {
  /**
   * 当前处理事件handler
   */

  private static Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case PictureConstants.PHOTO_NOT_FOUND:
          ToastUtil.showInfo("图片上传失败，请插入SD卡！");
          break;
        case PictureConstants.QINIU_UPLOAD_RETURN_ERROR:
          ToastUtil.showInfo("图片服务器返回链接错误，请稍后重试！");
          break;
        case PictureConstants.UPLOAD_UNKNOW_ERROR:
          ToastUtil.showInfo("未知异常");
          break;
        case PictureConstants.UPLOAD_NETWORK_ERROR://网络错误
          ToastUtil.showInfo("网络连接超时！");
          break;
        case PictureConstants.QINIU_UPLOAD_SERVER_ERROR://七牛服务器错误
          ToastUtil.showInfo("图片服务器服务器繁忙，请稍后重试！");
          break;
      }
    }
  };
  private final String IMAGTAG = this.getClass().getCanonicalName();
  CountDownLatch signal = new CountDownLatch(1);
  /**
   * 图片列表
   */
  private List<PhotoEntity> pictures;
  /**
   * 上传成功图片数量
   */
  private int uploadedPhotoCount = 0;
  /**
   * 待上传图片数量
   */
  private int needUploadCount = 0;
  /**
   * 七牛uploadToken
   */
  private String uploadToken;
  /**
   * 七牛上传
   */
  private UploadManager uploadManager;
  private Activity mActivity;
  /**
   * 是否继续上传
   */
  private boolean isContinue = false;
  private QiniuUploadListener mQiniuUploadListener;

  public QiNiuUploadUtil(Activity mActivity, List<PhotoEntity> photoList, List<String> photoPathList) {
    this.mActivity = mActivity;
    if (photoList != null) {
      this.pictures = photoList;
    } else if (photoPathList != null && photoPathList.size() > 0) {
      this.pictures = new ArrayList<>();
      for (int i = 0; i < photoPathList.size(); i++) {
        PhotoEntity entity = new PhotoEntity();
        entity.setPath(photoPathList.get(i));
        this.pictures.add(entity);
      }
    }

    if (this.pictures != null) {
      //初始化准备上传
      initUploadManager();
    }
  }

  private void initUploadManager() {
    try {
      StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
      sb.append(File.separator).append(TaskConstants.TEMP_HOME_PATH).append(File.separator).append(TaskConstants.TEMP_RECORD_PATH);
      Recorder recorder = new FileRecorder(sb.toString());

      //默认使用 key 的url_safe_base64编码字符串作为断点记录文件的文件名。
      //避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
      KeyGenerator keyGen = new KeyGenerator() {
        public String gen(String key, File file) {
          // 不必使用url_safe_base64转换，uploadManager内部会处理
          // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
          return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
        }
      };
      // 重用 mUploadManager。一般地，只需要创建一个 mUploadManager 对象
      //UploadManager mUploadManager = new UploadManager(recorder);  // 1
      //UploadManager mUploadManager = new UploadManager(recorder, keyGen); // 2
      // 或 在初始化时指定：
      Configuration config = new Configuration.Builder()
        // recorder 分片上传时，已上传片记录器
        // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
        .recorder(recorder, keyGen)
        .build();
      uploadManager = new UploadManager(config);
    } catch (IOException e) { // 异常情况适用默认普通上传
      uploadManager = new UploadManager();
    }
  }

  /**
   * 启动上传
   */
  public void startUpload(QiniuUploadListener mQiniuUploadListener) {
    //设置回调
    setQiniuUploadListener(mQiniuUploadListener);
    startToWork();
  }

  /**
   * 开始工作
   */
  private void startToWork() {
    //检测是否用图片可传
    if (!checkPhoto()) {
      return;
    }
    //检测网络是否可用
    if (!NetInfoUtil.isNetConnected(mActivity)) {
      AlertDialogUtil.createDialog(this.mActivity, "网络连接不可用,是否设置?", new AlertDialogUtil.OnClickYesListener() {
        public void onClickYes() {
          Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
          mActivity.startActivity(intent);
        }
      });
      return;
    }
    isContinue = true;
    uploadedPhotoCount = 0;//成功上传数清零
    needUploadCount = getNeedUploadCount();//获得上传总数
    if (needUploadCount > 0) {
      ProgressDialogUtil.showProgressDialogWithProgress(this.mActivity, "正在上传中...", needUploadCount);
      // 获取上传七牛的token
      getQiniuUploadToken();
    } else {
      removeAllPhotos();
      finish();
    }
  }

  /**
   * 获取七牛上传token
   */
  private void getQiniuUploadToken() {
    OKHttpManager.getInstance().post(HCHttpRequestParam.getUploadToken(PictureConstants.UPLOAD_TYPE_PHOTO), this, 0);
  }

  /**
   * 检查上传列表是否为空
   */
  private boolean checkPhoto() {
    return this.pictures == null ? false : this.pictures.size() > 0 ? true : false;
  }

  /**
   * 上传结束
   */
  private void finish() {
    //关闭上传图片进度条
    ProgressDialogUtil.closeProgressDialog();
    //将上传完成的结果key回调返回
    if (this.mQiniuUploadListener != null) {
      List<String> urlList = new ArrayList<>();
      for (PhotoEntity entity : this.pictures) {
        urlList.add(entity.getUrl());
      }
      this.mQiniuUploadListener.onSuccess(urlList, this.pictures);
    }
  }

  /**
   * 获取待上传图片数量
   */
  private int getNeedUploadCount() {
    int count = 0;
    if (this.pictures != null && this.pictures.size() > 0) {
      for (PhotoEntity image : pictures) {
        if (!TextUtils.isEmpty(image.getPath()) && TextUtils.isEmpty(image.getUrl())) {
          HCLogUtil.d(IMAGTAG, "pictures" + count);
          count++;
        }
      }
    }
    return count;
  }


  @Override
  public void onHttpStart(String action, int requestId) {

  }

  /**
   * 压缩并上传
   *
   * @param photoEntity
   * @return
   */
  private void compressAndUpload(PhotoEntity photoEntity) {
    if (!TextUtils.isEmpty(photoEntity.getUrl())) {
      return;
    }
    PhotoResponseHandler responseHandler = new PhotoResponseHandler(photoEntity);
    StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
    sb.append(File.separator).append(TaskConstants.TEMP_HOME_PATH).append(File.separator).append(TaskConstants.TEMP_IMAGES_PATH);
    String destFileName = System.currentTimeMillis() + ".jpg";

    boolean compressResult = CompressImageUtil.compressImage(photoEntity.getPath(), sb.toString(), destFileName, CompressImageUtil.WIDTH_NORMAL_600, CompressImageUtil.WIDTH_NORMAL_600, 80);
    if (!compressResult) {
      uploadFailed(PictureConstants.PHOTO_NOT_FOUND);
      return;
    }
    sb.append(File.separator).append(destFileName);
    photoEntity.setTemp_path(sb.toString());
    //如果待上传图片不存在，提示用户插上SD卡
    if (!new File(photoEntity.getTemp_path()).exists()) {
      uploadFailed(PictureConstants.PHOTO_NOT_FOUND);
      return;
    }
    sb = new StringBuilder();
    sb.append(GlobalData.userDataHelper.getChecker().getId()).append(java.util.UUID.randomUUID()).append(".jpg");
    photoEntity.setUnid(sb.toString());
    if (signal.getCount() == 0) {
      signal = new CountDownLatch(1);
    }

    uploadManager.put(photoEntity.getTemp_path(), photoEntity.getUnid(), uploadToken, responseHandler, null);
    try {
      signal.await();
    } catch (InterruptedException e) {
      isContinue = false;
      e.printStackTrace();
    }
  }

  /**
   * 上传七牛服务器失败
   *
   * @param what 消息类型
   */
  private void uploadFailed(int what) {
    //删除所有已经压缩的临时照片
    removeAllPhotos();
    isContinue = false;
    ProgressDialogUtil.closeProgressDialog();
    mHandler.sendEmptyMessage(what);
  }

  /**
   * 删除所有的照片
   */
  private void removeAllPhotos() {
    PhotoEntity temPhoto;
    File tempFile;
    if (pictures != null && pictures.size() > 0) {
      for (int i = 0; i < pictures.size(); i++) {
        temPhoto = pictures.get(i);
        if (TextUtils.isEmpty(temPhoto.getTemp_path())) {
          continue;
        }
        tempFile = new File(temPhoto.getTemp_path());
        if (tempFile.exists()) {
          tempFile.delete();
        }
      }
    }

  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    //获得七牛token
    if (action.equals(HttpConstants.ACTION_GET_QINIU_TOKEN)) {
      switch (response.getErrno()) {
        case 0:
          //成功获得token
          uploadToken = response.getData();
          //发送消息开始压缩图片
          Thread mThread = new Thread(new CompressRunnable());
          mThread.start();
          break;
        default:
          mHandler.sendEmptyMessage(PictureConstants.GET_QINIU_TOKEN_FAILED);
          break;
      }
    }
  }

  /**
   * 停止上传
   */
  public void stopUpload() {
    ProgressDialogUtil.closeProgressDialog();
    isContinue = false;
  }

  @Override
  public void onHttpProgress(String action, int requestId, long bytesWritten, long totalSize) {

  }

  @Override
  public void onHttpRetry(String action, int requestId, int retryNo) {

  }

  @Override
  public void onHttpFinish(String action, int requestId) {

  }

  public void setQiniuUploadListener(QiniuUploadListener mQiniuUploadListener) {
    this.mQiniuUploadListener = mQiniuUploadListener;
  }

  /**
   * 上传完成回调
   */
  public interface QiniuUploadListener {
    /**
     * 上传成功
     *
     * @param photoUrlList
     * @param photoList
     */
    void onSuccess(List<String> photoUrlList, List<PhotoEntity> photoList);
  }

  /**
   * 压缩上传图片线程
   */
  private class CompressRunnable implements Runnable {

    @Override
    public void run() {

      PhotoEntity tempPhoto;
      for (int i = 0; i < pictures.size(); i++) {
        if (!isContinue) {
          break;
        }
        tempPhoto = pictures.get(i);
        if (!TextUtils.isEmpty(tempPhoto.getPath())) {
          //上传图片
          compressAndUpload(tempPhoto);
        }
      }
    }
  }

  /**
   * 返回结果处理
   */
  private class PhotoResponseHandler implements UpCompletionHandler {
    private PhotoEntity image;

    public PhotoResponseHandler(PhotoEntity image) {
      this.image = image;
    }

    @Override
    public void complete(String key, ResponseInfo info, JSONObject response) {
      if (info.isOK()) {
        if (response != null && !TextUtils.isEmpty(response.optString("key"))) {
          uploadSuccess(response.optString("key"));
        } else {
          uploadFailed(PictureConstants.QINIU_UPLOAD_RETURN_ERROR);
        }
      } else {
        if (info.isServerError()) {
          uploadFailed(PictureConstants.QINIU_UPLOAD_SERVER_ERROR);
        } else if (info.isNetworkBroken()) {
          uploadFailed(PictureConstants.UPLOAD_NETWORK_ERROR);
        } else {
          uploadFailed(PictureConstants.UPLOAD_UNKNOW_ERROR);
        }
      }
      signal.countDown();
    }


    /**
     * 上传单张图片至七牛服务器成功
     *
     * @param url 图片在七牛服务器上的url
     */
    private void uploadSuccess(String url) {
      image.setUrl(url);
      uploadedPhotoCount++;
      if (uploadedPhotoCount >= needUploadCount) {
        removeAllPhotos();
        finish();
      } else {
        ProgressDialogUtil.setProgress(uploadedPhotoCount);
      }
    }

  }
}
