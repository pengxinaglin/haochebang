package com.haoche51.checker.upload;

import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.UploadCheckTaskEntity;

/**
 * 压缩进度显示线程
 */
public class CompressShowThread extends Thread {

    private UploadService mUploadService;
    private UploadCheckTaskEntity mUploadingTask;
    private boolean mInterruptThread;

    public void setUploadService(UploadService uploadService) {
        mUploadService = uploadService;
    }

    public void setUploadTaskEntity(UploadCheckTaskEntity uploadingTask) {
        mUploadingTask = uploadingTask;
    }

    public void interruptThread() {
        mInterruptThread = true;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (!mInterruptThread && i < 100) {
                if (mUploadingTask == null) {
                    break;
                }
                mUploadingTask.setUploadStatus(TaskConstants.UPLOAD_STATUS_VIDEO_COMPRESSING);
                mUploadingTask.setProgress(i);
                mUploadingTask.setMax(100);
                mUploadService.sendEmptyMessage(PictureConstants.UPDATE_TASK_PROGRESS);
                Thread.currentThread().sleep(1000);
                i++;
            }
        } catch (Exception e) {
        }
    }
}
