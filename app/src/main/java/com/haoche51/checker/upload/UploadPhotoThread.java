package com.haoche51.checker.upload;

import android.text.TextUtils;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.entity.UploadPhotoEntity;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * 上传照片线程
 */
public class UploadPhotoThread extends Thread {

    private UploadService mUploadService;
    private UploadCheckTaskEntity mUploadingTask;
    private UploadManager mUploadManager;
    private String mUploadToken;
    private CountDownLatch mCountDownLatch = new CountDownLatch(1);
    private boolean mInterruptThread;

    public void setUploadService(UploadService uploadService) {
        mUploadService = uploadService;
    }

    public void setUploadTaskEntity(UploadCheckTaskEntity uploadingTask) {
        mUploadingTask = uploadingTask;
    }

    public void setUploadManager(UploadManager uploadManager) {
        mUploadManager = uploadManager;
    }

    public void setUploadToken(String uploadToken) {
        mUploadToken = uploadToken;
    }

    public void interruptThread() {
        mInterruptThread = true;
    }

    public boolean isInterrupt() {
        return mInterruptThread;
    }

    @Override
    public void run() {
        if (mUploadingTask == null) {
            return;
        }
        ArrayList<UploadPhotoEntity> photoList = mUploadingTask.getPhotoList();
        if (photoList == null || photoList.size() == 0) {
            return;
        }

        mUploadingTask.setProgress(0);
        for (int i = 0; i < photoList.size(); i++) {
            if (mInterruptThread) {
                break;
            }
            UploadPhotoEntity uploadPhoto = photoList.get(i);
            if (uploadPhoto != null) {
                PhotoEntity photoEntity = uploadPhoto.getPhotoEntity();
                if (photoEntity != null) {
                    if (TextUtils.isEmpty(photoEntity.getTemp_path()) || !new File(photoEntity.getTemp_path()).exists()) {
                        // 发现压缩图片不存在，立即中断线程
                        mUploadService.sendEmptyMessage(PictureConstants.PHOTO_NOT_FOUND);
                        break;
                    }
                    if (!TextUtils.isEmpty(photoEntity.getUnid()) && !TextUtils.isEmpty(photoEntity.getUrl())) {
                        mUploadService.updatePhotoProgress(false);
                    } else {
                        uploadPhoto(photoEntity, uploadPhoto.getPosition());
                    }
                }
            }
        }
    }

    /**
     * 上传图片
     */
    public void uploadPhoto(PhotoEntity photoEntity, int position) {
        if (TextUtils.isEmpty(photoEntity.getUnid())) {
            StringBuilder builder = new StringBuilder();
            builder.append(GlobalData.userDataHelper.getChecker().getId()).append(java.util.UUID.randomUUID()).append(".jpg");
            photoEntity.setUnid(builder.toString());
        }

        PhotoResponseHandler photoHandler = new PhotoResponseHandler(photoEntity, position);
        mUploadManager.put(photoEntity.getTemp_path(), photoEntity.getUnid(), mUploadToken, photoHandler, null);

        if (mCountDownLatch.getCount() == 0) {
            mCountDownLatch = new CountDownLatch(1);
        }
        try {
            mCountDownLatch.await();
        } catch (Exception e) {
        }
    }

    /**
     * 照片请求回应处理器
     */
    private class PhotoResponseHandler implements UpCompletionHandler {

        private PhotoEntity mPhotoEntity;
        private int mPosition;

        public PhotoResponseHandler(PhotoEntity photoEntity, int position) {
            mPhotoEntity = photoEntity;
            mPosition = position;
        }

        @Override
        public void complete(String key, ResponseInfo responseInfo, JSONObject response) {
            if (responseInfo != null && responseInfo.isOK() && !TextUtils.isEmpty(response.optString("key"))) {
                // 上传照片成功
                mUploadService.uploadSuccess(response.optString("key"), mPhotoEntity, mPosition);
            } else {
                // 网络错误
                interruptThread();
                mUploadService.sendEmptyMessage(PictureConstants.UPLOAD_NETWORK_ERROR);
            }
            mCountDownLatch.countDown();
        }
    }
}
