package com.haoche51.checker.upload;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.CheckUploadTaskDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.MediaEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.entity.UploadPhotoEntity;
import com.haoche51.checker.helper.MediaHelper;
import com.haoche51.checker.helper.UploadServiceHelper;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DeviceInfoUtil;
import com.haoche51.checker.util.NetInfoUtil;
import com.haoche51.checker.util.SharedPreferencesUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * 上传照片、视频、音频的服务
 */
public class UploadService extends Service {

    /**
     * 七牛上传管理器
     */
    private UploadManager mUploadManager;
    /**
     * 网络状态变化广播接收器
     */
    private NetworkChangeReceiver mNetworkChangeReceiver;
    /**
     * 上传帮助器
     */
    private UploadServiceHelper mUploadHelper;
    /**
     * 上传索引
     */
    private int mUploadIndex = 0;
    /**
     * 上传中的任务
     */
    private UploadCheckTaskEntity mUploadingTask;
    /**
     * 上传文件类型
     */
    private int mUploadFileType = PictureConstants.UPLOAD_TYPE_PHOTO;
    /**
     * 七牛上传所需要的Token
     */
    public String mUploadToken;
    /**
     * 上传照片线程
     */
    private UploadPhotoThread mUploadPhotoThread;
    /**
     * 压缩视频线程
     */
    private CompressVideoThread mCompressVideoThread;
    /**
     * 压缩视频进度显示线程
     */
    private CompressShowThread mCompressShowThread;
    /**
     * 是否正在上传视频或音频
     */
    private boolean mIsUploadingMedia;

    /**
     * 当前处理事件handler
     */
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 网络连接超时
                case PictureConstants.UPLOAD_NETWORK_ERROR:
                    uploadFailed(TaskConstants.UPLOAD_FAILED_NETWORK_TIMEOUT, TaskConstants.UPLOAD_FAILED_OPERATE_RETRY);
                    break;
                // 获取Token成功
                case PictureConstants.GET_QINIU_TOKEN_SUCCESS:
                    if (mUploadFileType == PictureConstants.UPLOAD_TYPE_PHOTO) {
                        startUploadPhoto();
                    } else if (mUploadFileType == PictureConstants.UPLOAD_TYPE_VIDEO) {
                        startUploadVideo();
                    } else {
                        startUploadAudio();
                    }
                    break;
                // 照片丢失
                case PictureConstants.PHOTO_NOT_FOUND:
                    findFileFailed(TaskConstants.UPLOAD_FAILED_FIND_PHOTO, TaskConstants.UPLOAD_FAILED_OPERATE_RECOMPRESS);
                    break;
                // 更新进度
                case PictureConstants.UPDATE_TASK_PROGRESS:
                    notifyRefreshListView();
                    notifyDetailView();
                    break;
                // 图片上传成功
                case PictureConstants.FINISH_UPLOAD_PHOTO:
                    startCompressVideo();
                    break;
                // 视频压缩失败
                case PictureConstants.FAILED_COMPRESS_MEDIA:
                    uploadFailed(TaskConstants.UPLOAD_FAILED_COMPRESS_VIDEO, TaskConstants.UPLOAD_FAILED_OPERATE_RETRY);
                    break;
                // 视频压缩完成
                case PictureConstants.FINISH_COMPRESS_VIDEO:
                    getUploadToken(PictureConstants.UPLOAD_TYPE_VIDEO);
                    break;
                // 开始上传音频
                case PictureConstants.FINISH_UPLOAD_VIDEO:
                    startUploadAudio();
                    break;
                // 上传报告
                case PictureConstants.FINISH_UPLOAD_AUDIO:
                    uploadReport();
                    break;
                // 报告上传失败
                case PictureConstants.UPLOAD_REPORT_FAILED:
                    uploadFailed((String) msg.obj, TaskConstants.UPLOAD_FAILED_OPERATE_RETRY);
                    break;
                // 报告上传成功
                case PictureConstants.UPLOAD_REPORT_SUCCESS:
                    uploadReportSuccess();
                    break;
            }
        }
    };

    /**
     * Handler发送空消息
     */
    public void sendEmptyMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initUploadManager();

        // 注册网络变化接收器
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReceiver, intentFilter);

        mUploadHelper = UploadServiceHelper.getInstance();
        mUploadHelper.setUploadTaskListener(new UploadServiceHelper.UploadTaskListener() {
            @Override
            public void afterAddUploadTask() {
                if (checkUploadStatus()) {
                    startUpload();
                }
            }
        });
        startUpload();
    }

    /**
     * 初始化UploadManager
     */
    private void initUploadManager() {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator)
                    .append(TaskConstants.TEMP_HOME_PATH).append(File.separator).append(TaskConstants.TEMP_RECORD_PATH);
            FileRecorder fileRecorder = new FileRecorder(builder.toString());

            KeyGenerator keyGen = new KeyGenerator() {
                public String gen(String key, File file) {
                    return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                }
            };

            Configuration config = new Configuration.Builder().recorder(fileRecorder, keyGen).build();
            mUploadManager = new UploadManager(config);
        } catch (Exception e) {
            mUploadManager = new UploadManager();
        }
    }

    /**
     * 检测网络状态变化的广播接收者
     */
    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mUploadingTask == null) {
                return;
            }
            // 判断是否在上传照片
            if (mUploadPhotoThread != null && !mUploadPhotoThread.isInterrupt() && mUploadPhotoThread.isAlive()) {
                return;
            }
            // 判断是否在压缩视频
            if (mCompressVideoThread != null && mCompressVideoThread.isAlive()) {
                return;
            }
            // 判断是否在上传视频或音频
            if (mIsUploadingMedia) {
                return;
            }

            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (NetInfoUtil.isNetAvaliable()) {
                    networkConnect();
                }
            }
        }
    }

    /**
     * 添加任务后检查上传状态
     */
    private boolean checkUploadStatus() {
        if (mUploadingTask == null) {
            return true;
        }
        if (UploadServiceHelper.mUploadList != null) {
            if (UploadServiceHelper.mUploadList.size() == 1) {
                // 列表页只剩下刚添加进来的一条数据
                return true;
            } else if (UploadServiceHelper.mUploadList.size() > 1) {
                // 列表页倒数第二条数据的状态
                if (mUploadIndex == UploadServiceHelper.mUploadList.size() - 2) {
                    if (TaskConstants.UPLOAD_FAILED_OPERATE_RECOMPRESS.equals(mUploadingTask.getFailedOperate())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 开始上传
     */
    private void startUpload() {
        if (UploadServiceHelper.mUploadList == null || UploadServiceHelper.mUploadList.size() == 0) {
            return;
        }
        if (mUploadIndex >= UploadServiceHelper.mUploadList.size()) {
            mUploadIndex = 0;
        }
        mUploadingTask = UploadServiceHelper.mUploadList.get(mUploadIndex);

        // 检查所有图片是否上传完成
        if (!mUploadingTask.checkAllPhotoHasUrl()) {
            getUploadToken(PictureConstants.UPLOAD_TYPE_PHOTO);
            return;
        }

        // 上传视频、音频和报告
        startCompressVideo();
    }

    /**
     * 开始上传照片的线程
     */
    private synchronized void startUploadPhoto() {
        if (mUploadPhotoThread != null && !mUploadPhotoThread.isInterrupt() && mUploadPhotoThread.isAlive()) {
            return;
        }
        mUploadPhotoThread = new UploadPhotoThread();
        mUploadPhotoThread.setUploadService(this);
        mUploadPhotoThread.setUploadTaskEntity(mUploadingTask);
        mUploadPhotoThread.setUploadManager(mUploadManager);
        mUploadPhotoThread.setUploadToken(mUploadToken);
        mUploadPhotoThread.start();
    }

    /**
     * 上传成功
     */
    public void uploadSuccess(String url, PhotoEntity photoEntity, int position) {
        if (mUploadingTask == null || photoEntity == null) {
            return;
        }
        photoEntity.setUrl(url);
        switch (photoEntity.getType()) {
            case PictureConstants.OUTER_PICTURE_CHOSE:
                if (mUploadingTask.getOuterPictures() != null && mUploadingTask.getOuterPictures().size() > 0) {
                    mUploadingTask.getOuterPictures().set(position, photoEntity);
                    updatePhotoProgress(true);
                }
                break;
            case PictureConstants.INNER_PICTURE_CHOSE:
                if (mUploadingTask.getInnerPictures() != null && mUploadingTask.getInnerPictures().size() > 0) {
                    mUploadingTask.getInnerPictures().set(position, photoEntity);
                    updatePhotoProgress(true);
                }
                break;
            case PictureConstants.DETAIL_PICTURE_CHOSE:
                if (mUploadingTask.getDetailPictures() != null && mUploadingTask.getDetailPictures().size() > 0) {
                    mUploadingTask.getDetailPictures().set(position, photoEntity);
                    updatePhotoProgress(true);
                }
                break;
            case PictureConstants.DEFECT_PICTURE_CHOSE:
                if (mUploadingTask.getDefectPictures() != null && mUploadingTask.getDefectPictures().size() > 0) {
                    mUploadingTask.getDefectPictures().set(position, photoEntity);
                    updatePhotoProgress(true);
                }
                break;
        }
    }

    /**
     * 更新图片的上传进度和数据库
     */
    public void updatePhotoProgress(boolean isSaveDB) {
        if (mUploadingTask == null) {
            return;
        }
        int max = mUploadingTask.getPhotoList().size();
        int progress = mUploadingTask.getProgress() + 1;
        if (progress > max) {
            progress = max;
        }
        mUploadingTask.setUploadStatus(TaskConstants.UPLOAD_STATUS_PHOTO_UPLOADING);
        mUploadingTask.setProgress(progress);
        mUploadingTask.setMax(max);
        if (isSaveDB) {
            CheckUploadTaskDAO.getInstance().updateByTaskId(mUploadingTask);
        }
        mHandler.sendEmptyMessage(PictureConstants.UPDATE_TASK_PROGRESS);

        if (mUploadingTask.getProgress() >= mUploadingTask.getPhotoList().size()) {
            if (mUploadingTask.checkAllPhotoHasUrl()) {
                mHandler.sendEmptyMessage(PictureConstants.FINISH_UPLOAD_PHOTO);
            } else {
                startUploadPhoto();
            }
        }
    }

    /**
     * 终止上传照片的线程
     */
    private void stopUploadPhoto() {
        if (mUploadPhotoThread != null) {
            mUploadPhotoThread.interruptThread();
            mUploadPhotoThread = null;
        }
    }

    /**
     * 开始压缩视频
     */
    private void startCompressVideo() {
        if (mUploadingTask == null) {
            return;
        }
        // 判断是否有视频文件
        MediaEntity videoEntity = mUploadingTask.getVideoEntity();
        if (videoEntity != null && !TextUtils.isEmpty(videoEntity.getCopyedPath())) {
            if (!new File(videoEntity.getCopyedPath()).exists()) {
                findFileFailed(TaskConstants.UPLOAD_FAILED_FIND_VIDEO, TaskConstants.UPLOAD_FAILED_OPERATE_RECOMPRESS);
                return;
            }

            String url = videoEntity.getUrl();
            String compressedPath = videoEntity.getCompressedPath();
            boolean isCompressed = videoEntity.isCompressed();
            if (TextUtils.isEmpty(url)) {
                if (TextUtils.isEmpty(compressedPath) || !isCompressed) {
                    // 压缩视频
                    compressVideo();
                    return;
                } else {
                    File file = new File(compressedPath);
                    if (!file.exists()) {
                        // 压缩视频
                        compressVideo();
                        return;
                    } else {
                        // 获得上传Token
                        getUploadToken(PictureConstants.UPLOAD_TYPE_VIDEO);
                        return;
                    }
                }
            }
        }

        // 判断是否有音频文件
        MediaEntity audioEntity = mUploadingTask.getAudioEntity();
        if (audioEntity != null && !TextUtils.isEmpty(audioEntity.getPath()) && TextUtils.isEmpty(audioEntity.getUrl())) {
            getUploadToken(PictureConstants.UPLOAD_TYPE_AUDIO);
            return;
        }

        // 上传报告
        uploadReport();
    }

    /**
     * 压缩视频
     */
    private synchronized void compressVideo() {
        if (mCompressVideoThread != null && mCompressVideoThread.isAlive()) {
            return;
        }
        mCompressVideoThread = new CompressVideoThread();
        mCompressVideoThread.setUploadService(this);
        mCompressVideoThread.setUploadTaskEntity(mUploadingTask);
        mCompressVideoThread.start();

        mCompressShowThread = new CompressShowThread();
        mCompressShowThread.setUploadService(this);
        mCompressShowThread.setUploadTaskEntity(mUploadingTask);
        mCompressShowThread.start();
    }

    /**
     * 停止压缩进度显示
     */
    public void stopCompressShow() {
        if (mCompressShowThread != null) {
            mCompressShowThread.interruptThread();
            mCompressShowThread = null;
        }
    }

    /**
     * 开始上传视频
     */
    private synchronized void startUploadVideo() {
        if (mUploadingTask == null) {
            return;
        }
        if (TextUtils.isEmpty(mUploadingTask.getVideoEntity().getCompressedPath())
                || !new File(mUploadingTask.getVideoEntity().getCompressedPath()).exists()) {
            findFileFailed(TaskConstants.UPLOAD_FAILED_FIND_VIDEO, TaskConstants.UPLOAD_FAILED_OPERATE_RETRY);
            return;
        }

        if (mIsUploadingMedia) {
            return;
        }
        mIsUploadingMedia = true;
        setMediaEntityUuid(mUploadingTask.getVideoEntity());
        mUploadManager.put(mUploadingTask.getVideoEntity().getCompressedPath(), mUploadingTask.getVideoEntity().getUuid(),
                mUploadToken, new VideoResponseHandler(), new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        // 主线程返回的进度才是正确的
                        if ("main".equals(Thread.currentThread().getName())) {
                            updateMediaProgress(percent, TaskConstants.UPLOAD_STATUS_VIDEO_UPLOADING);
                        }
                    }
                }, null));
    }

    /**
     * 视频请求回应处理器
     */
    private class VideoResponseHandler implements UpCompletionHandler {

        @Override
        public void complete(String key, ResponseInfo responseInfo, JSONObject response) {
            mIsUploadingMedia = false;
            if (responseInfo != null && responseInfo.isOK() && !TextUtils.isEmpty(response.optString("key"))) {
                // 上传视频成功
                mUploadingTask.setProgress(100);
                mUploadingTask.setMax(100);
                mUploadingTask.getVideoEntity().setUrl(response.optString("key"));
                CheckUploadTaskDAO.getInstance().updateByTaskId(mUploadingTask);
                mHandler.sendEmptyMessage(PictureConstants.FINISH_UPLOAD_VIDEO);
            } else {
                // 网络错误
                mHandler.sendEmptyMessage(PictureConstants.UPLOAD_NETWORK_ERROR);
            }
        }
    }

    /**
     * 开始上传音频
     */
    private synchronized void startUploadAudio() {
        if (mUploadingTask == null) {
            return;
        }
        if (mUploadingTask.getAudioEntity() != null && !TextUtils.isEmpty(mUploadingTask.getAudioEntity().getPath())
                && TextUtils.isEmpty(mUploadingTask.getAudioEntity().getUrl())) {
            if (!new File(mUploadingTask.getAudioEntity().getPath()).exists()) {
                findFileFailed(TaskConstants.UPLOAD_FAILED_FIND_AUDIO, TaskConstants.UPLOAD_FAILED_OPERATE_RECOMPRESS);
                return;
            }

            if (mIsUploadingMedia) {
                return;
            }
            mIsUploadingMedia = true;
            setMediaEntityUuid(mUploadingTask.getAudioEntity());
            mUploadManager.put(mUploadingTask.getAudioEntity().getPath(), mUploadingTask.getAudioEntity().getUuid(), mUploadToken,
                    new AudioResponseHandler(), new UploadOptions(null, "audio/mpeg", false, new UpProgressHandler() {
                        @Override
                        public void progress(String key, double percent) {
                            // 主线程返回的进度才是正确的
                            if ("main".equals(Thread.currentThread().getName())) {
                                updateMediaProgress(percent, TaskConstants.UPLOAD_STATUS_AUDIO_UPLOADING);
                            }
                        }
                    }, null));
        } else {
            // 上传报告
            uploadReport();
        }
    }


    /**
     * 音频请求回应处理器
     */
    private class AudioResponseHandler implements UpCompletionHandler {

        @Override
        public void complete(String key, ResponseInfo responseInfo, JSONObject response) {
            mIsUploadingMedia = false;
            if (responseInfo != null && responseInfo.isOK() && !TextUtils.isEmpty(response.optString("key"))) {
                // 上传音频成功
                mUploadingTask.setProgress(100);
                mUploadingTask.setMax(100);
                mUploadingTask.getAudioEntity().setUrl(response.optString("key"));
                CheckUploadTaskDAO.getInstance().updateByTaskId(mUploadingTask);
                mHandler.sendEmptyMessage(PictureConstants.FINISH_UPLOAD_AUDIO);
            } else {
                // 网络错误
                mHandler.sendEmptyMessage(PictureConstants.UPLOAD_NETWORK_ERROR);
            }
        }
    }

    /**
     * 设置uuid
     */
    private void setMediaEntityUuid(MediaEntity mediaEntity) {
        if (TextUtils.isEmpty(mediaEntity.getUuid())) {
            String name = MediaHelper.getExtensionName(mediaEntity.getPath());
            StringBuilder builder = new StringBuilder();
            builder.append(GlobalData.userDataHelper.getChecker().getId()).append(java.util.UUID.randomUUID()).append(".").append(name);
            mediaEntity.setUuid(builder.toString());
        }
    }

    /**
     * 更新视频或音频的进度
     */
    private void updateMediaProgress(double percent, String status) {
        if (mUploadingTask == null) {
            return;
        }
        mUploadingTask.setUploadStatus(status);
        mUploadingTask.setProgress((int) (percent * 100));
        mUploadingTask.setMax(100);
        mHandler.sendEmptyMessage(PictureConstants.UPDATE_TASK_PROGRESS);
    }

    /**
     * 上传报告
     */
    private void uploadReport() {
        mUploadingTask.setUploadStatus(TaskConstants.UPLOAD_STATUS_REPORT_UPLOADING);
        mUploadingTask.setProgress(0);
        mUploadingTask.setMax(100);
        notifyRefreshListView();
        notifyDetailView();

        CheckReportEntity checkReportEntity = CheckReportDAO.getInstance().get(mUploadingTask.getReportId());
        if (checkReportEntity == null) {
            findFileFailed(TaskConstants.UPLOAD_FAILED_FIND_REPORT, TaskConstants.UPLOAD_FAILED_OPERATE_RECOMPRESS);
            return;
        }
        Gson gson = new Gson();
        checkReportEntity.setApp_version(DeviceInfoUtil.getAppVersion());
        checkReportEntity.setOut_pics(gson.toJson(mUploadingTask.getOuterPictures()));
        checkReportEntity.setInner_pics(gson.toJson(mUploadingTask.getInnerPictures()));
        checkReportEntity.setDetail_pics(gson.toJson(mUploadingTask.getDetailPictures()));
        checkReportEntity.setDefect_pics(gson.toJson(mUploadingTask.getDefectPictures()));
        if (mUploadingTask.getVideoEntity() != null) {
            checkReportEntity.setVideo_url(mUploadingTask.getVideoEntity().toJson());
        }
        if (mUploadingTask.getAudioEntity() != null) {
            checkReportEntity.setAudio_url(mUploadingTask.getAudioEntity().toJson());
        }
        CheckReportDAO.getInstance().update(checkReportEntity.getId(), checkReportEntity);
        OKHttpManager.getInstance().post(HCHttpRequestParam.uploadCheckReport(checkReportEntity), new RequestCallback(), 0);
    }

    /**
     * 上传报告成功
     */
    private void uploadReportSuccess() {
        if (mUploadingTask == null) {
            return;
        }
        // 删除对应的数据
        removeAllPhotos(mUploadingTask.getPhotoList());
        removeCompressedVideo();
        removeCopyedVideo();
        CheckUploadTaskDAO.getInstance().deleteByTaskId(mUploadingTask.getCheckTaskId());
        CheckReportDAO.getInstance().deleteByReportId(mUploadingTask.getReportId());

        // 刷新界面
        mUploadingTask.setUploadStatus(TaskConstants.UPLOAD_STATUS_REPORT_FINISHED);
        mUploadingTask.setProgress(100);
        mUploadingTask.setMax(100);
        notifyRefreshListView();
        notifyDetailView();
        notifyFinishTask();

        // 发送通知
        String title = "好车邦成功提示";
        String content = mUploadingTask.getCheckSource() + " " + mUploadingTask.getVehicleName();
        UploadNotification.sendNotice(this, title, content, false, true);

        // 移除当前数据，开始下一条
        UploadServiceHelper.mUploadList.remove(mUploadingTask);
        startUpload();
    }

    /**
     * 通知刷新ListView
     */
    private void notifyRefreshListView() {
        if (mUploadHelper != null && mUploadHelper.getAdapter() != null) {
            mUploadHelper.getAdapter().setUploadTaskList(UploadServiceHelper.mUploadList);
            mUploadHelper.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 通知上传中页签“任务详情界面”，更新上传进度
     */
    private void notifyDetailView() {
        if (mUploadingTask == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean(TaskConstants.BINDLE_DETAIL_TASK, true);
        bundle.putParcelable(TaskConstants.BINDLE_UPLOAD_TASK, mUploadingTask);
        HCTasksWatched.getInstance().notifyWatchers(bundle);
    }

    /**
     * 通知已完成页签对应的界面，刷新数据
     */
    private void notifyFinishTask() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(TaskConstants.BINDLE_NEW_TASK, true);
        HCTasksWatched.getInstance().notifyWatchers(bundle);
    }

    /**
     * 删除所有已经压缩成功的照片
     */
    private void removeAllPhotos(List<UploadPhotoEntity> photoList) {
        if (photoList == null || photoList.size() == 0) {
            return;
        }
        for (UploadPhotoEntity uploadPhoto : photoList) {
            PhotoEntity photo = uploadPhoto.getPhotoEntity();
            File file = new File(photo.getTemp_path());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 删除压缩的视频
     */
    public void removeCompressedVideo() {
        if (mUploadingTask == null || mUploadingTask.getVideoEntity() == null) {
            return;
        }
        if (!TextUtils.isEmpty(mUploadingTask.getVideoEntity().getCompressedPath())) {
            File file = new File(mUploadingTask.getVideoEntity().getCompressedPath());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 删除复制的视频
     */
    private void removeCopyedVideo() {
        if (mUploadingTask == null || mUploadingTask.getVideoEntity() == null) {
            return;
        }
        if (!TextUtils.isEmpty(mUploadingTask.getVideoEntity().getCopyedPath())) {
            File file = new File(mUploadingTask.getVideoEntity().getCopyedPath());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 上传过程中发现本地文件丢失
     */
    private void findFileFailed(String failedReason, String failedOperate) {
        if (mUploadingTask == null) {
            return;
        }

        mUploadingTask.setUploadStatus(TaskConstants.UPLOAD_STATUS_STOP);
        mUploadingTask.setFailedReason(failedReason);
        mUploadingTask.setFailedOperate(failedOperate);
        notifyRefreshListView();

        String title = "好车邦失败提示";
        String content = mUploadingTask.getCheckSource() + " " + mUploadingTask.getVehicleName();
        UploadNotification.sendNotice(this, title, content, false, false);

        if (mUploadIndex < UploadServiceHelper.mUploadList.size() - 1) {
            mUploadIndex++;
            startUpload();
        }
    }

    /**
     * 网络断开
     */
    private void networkDisconnect() {
        for (int i = 0; i < UploadServiceHelper.mUploadList.size(); i++) {
            UploadCheckTaskEntity taskEntity = UploadServiceHelper.mUploadList.get(i);
            taskEntity.setUploadStatus(TaskConstants.UPLOAD_STATUS_STOP);
            taskEntity.setFailedReason(TaskConstants.UPLOAD_FAILED_NO_NETWORK);
            taskEntity.setFailedOperate(TaskConstants.UPLOAD_FAILED_OPERATE_SETTING);
        }
        notifyRefreshListView();

        String title = "好车邦网络中断提示";
        String content = "点击打开网络设置，否则无法上传报告";
        UploadNotification.sendNotice(this, title, content, true, false);
    }

    /**
     * 网络连接
     */
    private void networkConnect() {
        for (int i = 0; i < UploadServiceHelper.mUploadList.size(); i++) {
            UploadCheckTaskEntity taskEntity = UploadServiceHelper.mUploadList.get(i);
            taskEntity.setUploadStatus(TaskConstants.UPLOAD_STATUS_WAITING);
            taskEntity.setFailedReason("");
            taskEntity.setFailedOperate("");
        }
        notifyRefreshListView();
        startUpload();
    }

    /**
     * 上传失败
     */
    private void uploadFailed(String failedReason, String failedOperate) {
        if (mUploadingTask == null) {
            return;
        }
        mUploadingTask.setUploadStatus(TaskConstants.UPLOAD_STATUS_STOP);
        mUploadingTask.setFailedReason(failedReason);
        mUploadingTask.setFailedOperate(failedOperate);
        notifyRefreshListView();
    }

    /**
     * 获取七牛上传的Token
     */
    private void getUploadToken(int uploadFileType) {
        if (!NetInfoUtil.isNetAvaliable()) {
            networkDisconnect();
        } else {
            mUploadFileType = uploadFileType;
            OKHttpManager.getInstance().post(HCHttpRequestParam.getUploadToken(mUploadFileType), new RequestCallback(), 0);
        }
    }

    /**
     * Http请求回调
     */
    private class RequestCallback implements HCHttpCallback {

        @Override
        public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
            // 获得Token
            if (HttpConstants.ACTION_GET_QINIU_TOKEN.equals(action)) {
                if (response != null && response.getErrno() == 0 && !TextUtils.isEmpty(response.getData())) {
                    mUploadToken = response.getData();
                    mHandler.sendEmptyMessage(PictureConstants.GET_QINIU_TOKEN_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(PictureConstants.UPLOAD_NETWORK_ERROR);
                }
            } else if (HttpConstants.ACTION_UPLOAD_REPORT.equals(action)) {
                // 上传报告，重复请求返回错误码2
                if (response != null && (response.getErrno() == 0 || response.getErrno() == 2)) {
                    if (mUploadingTask != null) {
                        String taskId = String.valueOf(mUploadingTask.getCheckTaskId());
                        SharedPreferencesUtils.removeData(taskId);
                        SharedPreferencesUtils.removeData("rq_" + taskId);
                    }
                    mHandler.sendEmptyMessage(PictureConstants.UPLOAD_REPORT_SUCCESS);
                } else {
                    Message msg = mHandler.obtainMessage(PictureConstants.UPLOAD_REPORT_FAILED);
                    if(response==null){
                        msg.obj ="上传报告失败，无返回信息";
                    }else{
                        msg.obj = response.getErrmsg();
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }

        @Override
        public void onHttpStart(String action, int requestId) {
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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ReuploadBinder();
    }

    /**
     * 重试
     */
    public class ReuploadBinder extends Binder {

        public void reupload(int position) {
            if (UploadServiceHelper.mUploadList == null || UploadServiceHelper.mUploadList.size() == 0) {
                return;
            }
            if (position >= UploadServiceHelper.mUploadList.size()) {
                position = 0;
            }

            UploadCheckTaskEntity taskEntity = UploadServiceHelper.mUploadList.get(position);
            taskEntity.setUploadStatus(TaskConstants.UPLOAD_STATUS_WAITING);
            taskEntity.setFailedReason("");
            taskEntity.setFailedOperate("");
            notifyRefreshListView();
            startUpload();
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mNetworkChangeReceiver);
        stopUploadPhoto();
        stopCompressShow();
        super.onDestroy();
    }
}
