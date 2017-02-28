package com.haoche51.checker.upload;

import android.os.Environment;

import com.haoche51.checker.DAO.CheckUploadTaskDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.netcompss.ffmpeg4android.GeneralUtils;
import com.netcompss.loader.LoadJNI;

import java.io.File;

/**
 * 压缩视频线程
 */
public class CompressVideoThread extends Thread {

    private UploadService mUploadService;
    private UploadCheckTaskEntity mUploadingTask;

    public void setUploadService(UploadService uploadService) {
        mUploadService = uploadService;
    }

    public void setUploadTaskEntity(UploadCheckTaskEntity uploadingTask) {
        mUploadingTask = uploadingTask;
    }

    @Override
    public void run() {
        if (mUploadingTask == null || mUploadingTask.getVideoEntity() == null) {
            return;
        }

        StringBuilder builder = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
        builder.append(File.separator).append(TaskConstants.TEMP_HOME_PATH).append(File.separator)
                .append(TaskConstants.TEMP_VIDEO_PATH).append(File.separator);
        String destVideoFolder = builder.toString();
        new File(destVideoFolder).mkdirs();

        // 清除压缩目录下的证书和log
        String vkLogPath = destVideoFolder.concat("vk.log");
        new File(destVideoFolder.concat("ffmpeglicense.lic")).delete();
        new File(vkLogPath).delete();
        GeneralUtils.copyLicenseFromAssetsToSDIfNeeded(GlobalData.context, destVideoFolder);
        GeneralUtils.isLicenseValid(GlobalData.context, destVideoFolder);

        // 获取指定压缩路径
        String srcFilePath = mUploadingTask.getVideoEntity().getCopyedPath();
        String compressPath = GeneralUtils.getDestCompressPath(srcFilePath);
        mUploadingTask.getVideoEntity().setCompressedPath(compressPath);

        String[] complexCommand = {"ffmpeg", "-y", "-i", srcFilePath, "-strict", "experimental", "-vcodec", "libx264",
                "-qscale", "4", "-s", "800x600", "-r", "29.97", "-ab", "128k", "-ac", "2", "-ar", "22050", compressPath};
        LoadJNI vk = new LoadJNI();
        boolean commandFailed = false;
        try {
            vk.run(complexCommand, destVideoFolder, mUploadService.getApplicationContext());
        } catch (Exception e) {
            commandFailed = true;
            mUploadService.removeCompressedVideo();
        }

        if (commandFailed) {
            if (mUploadService != null) {
                mUploadService.stopCompressShow();
                mUploadService.sendEmptyMessage(PictureConstants.FAILED_COMPRESS_MEDIA);
            }
        } else {
            if (mUploadService != null && mUploadingTask != null) {
                mUploadingTask.getVideoEntity().setCompressed(true);
                CheckUploadTaskDAO.getInstance().updateByTaskId(mUploadingTask);
                mUploadService.stopCompressShow();
                mUploadService.sendEmptyMessage(PictureConstants.FINISH_COMPRESS_VIDEO);
            }
        }
    }
}
