package com.haoche51.checker.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.CompressedPhotoDAO;
import com.haoche51.checker.activity.evaluate.ImageUploadActivity;
import com.haoche51.checker.constants.CameraConstants;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.CompressedPhotoEntity;
import com.haoche51.checker.entity.MediaEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.entity.UploadPhotoEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.BitmapUtil;
import com.haoche51.checker.util.DeviceInfoUtil;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.netcompss.ffmpeg4android.GeneralUtils;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上传任务帮助类
 */
public class UploadTaskPhotoHelper {
    /**
     * 获取待上传图片数量
     */
    private final String TAG = "UploadCheckTaskPhotoHelper";
    //音频
    public MediaEntity audioEntity;
    //视频
    public MediaEntity videoEntity;
    //图片列表
    private List<PhotoEntity> outerPictures, innerPictures, detailPictures, defectPictures;
    // 当前图片选择类型，默认为外观图
    private int taskId;
    private int reportId = 0;
    // 已压缩图片数量
    private int compressed_photo_count = 0;
    /**
     * 图片总数
     */
    private int totalPhotoCount;
    // 压缩图片异步线程
    private Thread mCopmressThread;
    private CompressImageRunnable mRunnable;
    private Gson mGson;
    private Activity mActivity;
    private OnCompressPhotoSuccessedListener onCompressPhotoSuccessedListener;
    /**
     * 验车模块：压缩完待上传七牛服务器的图片列表
     */
    private ArrayList<UploadPhotoEntity> uploadPhotoList = new ArrayList<>();
    private ProgressDialog progressBar;
    private AlertDialog alertDialog;
    /**
     * 当前处理事件handler
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PictureConstants.COMPRESS_IMAGE_SUCCESS:// 压缩图片成功。刷新progress
                    ProgressDialogUtil.setProgress(compressed_photo_count);
                    break;
                case PictureConstants.COMPRESS_IMAGE_FAILED:// 压缩图片错误时返回
                    enableCompress(false);
                    ToastUtil.showInfo("图片压缩错误，请确认图片正确性!");
                    ((ImageUploadActivity) UploadTaskPhotoHelper.this.mActivity).click = false;//TODO
                    break;
                case PictureConstants.COMPRESS_ALL_COMPLETE: // 图片全部压缩完成，复制视频
                    ProgressDialogUtil.closeProgressDialog();//关闭上传图片进度条
                    copyVideoAndShowProgress();
                    break;
                case PictureConstants.FINISH_COPY_VIDEO: //视频复制完成
                    if (progressBar != null) {
                        progressBar.dismiss();
                        progressBar = null;
                    }
                    showAllCompressedTip();
                    break;
                case PictureConstants.FAILED_COPY_VIDEO: //视频复制失败
                    if (progressBar != null) {
                        progressBar.dismiss();
                        progressBar = null;
                    }
                    showCopyFailedTip((String) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public UploadTaskPhotoHelper(Map<String, List<PhotoEntity>> images, MediaEntity audioEntity, MediaEntity videoEntity, int taskId, int reportId, Activity mActivity) {
        this.outerPictures = images.get(PictureConstants.OUTER_PICTURE_TYPE);
        this.innerPictures = images.get(PictureConstants.INNER_PICTURE_TYPE);
        this.detailPictures = images.get(PictureConstants.DETAIL_PICTURE_TYPE);
        this.defectPictures = images.get(PictureConstants.DEFECT_PICTURE_TYPE);
        this.audioEntity = audioEntity;
        this.videoEntity = videoEntity;
        this.mActivity = mActivity;
        this.taskId = taskId;
        this.reportId = reportId;
        mGson = new Gson();
    }

    //启动上传
    public void startUpload(boolean isChannel) {
        compressImageAndUpload(isChannel);
    }

    /**
     * 一、压缩图片流程：报告填完后（自动保存报告到本地）
     * 1、先判断该任务中是否存在部分已经压缩完的图片
     * 存在：过滤掉已经压缩过的图片
     * 2、逐张压缩图片、并将压缩后的图片添加至List中，并保存到report数据表中
     * 3、图片全部压缩完后，统一将该任务中对应的所有压缩后的图片传给服务的列表中。
     * （上传图片至七牛服务器和上传报告到业务服务器的动作由服务进行，图片全部上传成功后，上传报告）
     * <p/>
     * 二、上传图片流程：图片全部压缩完
     * 检查是否有未上传完的图片需要上传
     * 没有：直接上传报告
     * 有：检查网络设置可行，直接上传报告
     */
    private void compressImageAndUpload(boolean isChannel) {
        //检查必选图片是否已选
        if (!checkPhoto(isChannel)) {
            HCLogUtil.e("uploadReport", "change click+++++++++++++");
            ((ImageUploadActivity) this.mActivity).click = false;//TODO
            return;
        }

        //检查音频、视频是否已选
        if (!checkIsChooseMedia()) {
            ((ImageUploadActivity) this.mActivity).click = false;
            return;
        }

        //检查所有图片是否已选
        alertMsg(isChannel);
    }

    /**
     * 检查音频、视频是否已选
     */
    private Boolean checkIsChooseMedia() {
        if (this.videoEntity != null && !new File(this.videoEntity.getPath()).exists()) {
            ((ImageUploadActivity) this.mActivity).click = false;
            ToastUtil.showInfo("该视频文件不存在，请重新选择");
            return false;
        }
        if (this.audioEntity != null && !new File(this.audioEntity.getPath()).exists()) {
            ToastUtil.showInfo("该音频文件不存在，请重新选择");
            return false;
        }
        return true;
    }

    /**
     * 是否允许压缩
     *
     * @param enable：true表示继续压缩 ；false表示结束线程
     */
    public void enableCompress(boolean enable) {
        if (progressBar != null) {
            progressBar.dismiss();
            progressBar = null;
        }
        if (!enable) {
            ProgressDialogUtil.closeProgressDialog();
            if (mCopmressThread != null) {
                mCopmressThread.interrupt();
                mCopmressThread = null;
            }
        }
    }

    /**
     * 关闭对话框
     */
    public void disAlertDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    /**
     * 检查外观图、内饰图、细节图，是否均已选择
     *
     * @return
     */
    private boolean checkPhoto(boolean isChannel) {
        //渠寄车源
        if (isChannel) {
            int count = 0;
            for (PhotoEntity image : outerPictures) {
                if (!TextUtils.isEmpty(image.getPath())) {
                    count++;
                }
            }
            if (count == 0) { // 外观图不能空
                ToastUtil.showInfo("请选择外观图");
                return false;
            }
            count = 0;
            for (PhotoEntity image : innerPictures) {
                if (!TextUtils.isEmpty(image.getPath())) {
                    count++;
                }
            }
            if (count == 0) {
                ToastUtil.showInfo("请选择内饰图");
                return false;
            }
            count = 0;
            for (PhotoEntity image : detailPictures) {
                if (!TextUtils.isEmpty(image.getPath())) {
                    count++;
                }
            }
            if (count == 0) {
                ToastUtil.showInfo("请选择细节图");
                return false;
            }
        }
        //非渠寄车源
        else {
            //必选项，否则不允许上传
            Map<String, Boolean> mustChoosePictures = new HashMap<>();
            //外观 左前45，正车头，正侧，正车尾，右后45，前灯，车顶漆膜仪厚度，轮胎，轮胎花纹
            mustChoosePictures.put(CameraConstants.defaultindicatorText[8], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[6], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[11], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[13], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[14], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[9], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[16], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[18], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[19], false);
            //内饰 内饰全景，方向盘，仪表盘，一键启动，中控特写，排挡把手，天窗，主驾车门控件，左前门，左前门驾驶位空间
            mustChoosePictures.put(CameraConstants.defaultindicatorText[35], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[22], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[23], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[21], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[24], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[25], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[28], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[29], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[30], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[32], false);
            //细节 引擎盖全景，底盘全景，车钥匙，铭牌
            mustChoosePictures.put(CameraConstants.defaultindicatorText[2], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[7], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[20], false);
            mustChoosePictures.put(CameraConstants.defaultindicatorText[4], false);

            //全部图片
            List<PhotoEntity> allPictures = new ArrayList<>();
            allPictures.addAll(outerPictures);
            allPictures.addAll(innerPictures);
            allPictures.addAll(detailPictures);

            //遍历那些图片已选
            for (PhotoEntity picture : allPictures) {
                for (String name : mustChoosePictures.keySet()) {
                    if (name.equals(picture.getName())) {
                        mustChoosePictures.put(name, true);
                        break;
                    }
                }
            }

            //统计以及未选必选项
            List<String> noChoose = new ArrayList<>();
            for (String name : mustChoosePictures.keySet()) {
                if (!mustChoosePictures.get(name)) {
                    noChoose.add(name);
                }
            }
            if (noChoose.size() >= 2) {
                ToastUtil.showInfo("缺少「" + noChoose.get(0) + "」「" + noChoose.get(1) + "」及标红位置等照片请选择");
                return false;
            } else if (noChoose.size() > 0) {
                ToastUtil.showInfo("缺少「" + noChoose.get(0) + "」照片请选择");
                return false;
            }
        }
        return true;
    }

    private void alertMsg(boolean isChannel) {
        //渠寄车源
        if (isChannel) {
            perform();
            return;
        }
        //全部图片
        List<PhotoEntity> allPictures = new ArrayList<>();
        allPictures.addAll(outerPictures);
        allPictures.addAll(innerPictures);
        allPictures.addAll(detailPictures);
        //统计未选标签的图片数量
        int noChooseTagCount = CameraConstants.defaultindicatorText.length;
        for (PhotoEntity e : allPictures) {
            //过滤掉没选标签默认归类的细节图
            if (TextUtils.isEmpty(e.getName()))
                continue;
            switch (e.getType()) {
                case PictureConstants.OUTER_PICTURE_CHOSE:
                    noChooseTagCount--;
                    break;
                case PictureConstants.INNER_PICTURE_CHOSE:
                    noChooseTagCount--;
                    break;
                case PictureConstants.DETAIL_PICTURE_CHOSE:
                    noChooseTagCount--;
                    break;
            }
        }
        //有未选标签的图片，提示
        if (noChooseTagCount > 0) {
            AlertDialogUtil.createDetermineCancelDialog(mActivity, "您还有" + noChooseTagCount + "张照片没有选择，请确认是否上传", "确定", "取消", new AlertDialogUtil.OnDismissListener() {
                @Override
                public void onDismiss(Bundle data) {
                    if (data != null) {
                        if (data.getBoolean("determine")) {
                            perform();
                        } else {
                            ((ImageUploadActivity) mActivity).click = false;
                        }
                    } else {
                        ((ImageUploadActivity) mActivity).click = false;
                    }
                }
            });
        } else {
            perform();
        }
    }

    private void perform() {
        this.compressed_photo_count = getAlreadyCompressImagesCount();//已经压缩数
        //统计待上传图片总数
        this.totalPhotoCount = 0;
        this.totalPhotoCount += this.outerPictures == null ? 0 : this.outerPictures.size();
        this.totalPhotoCount += this.innerPictures == null ? 0 : this.innerPictures.size();
        this.totalPhotoCount += this.detailPictures == null ? 0 : this.detailPictures.size();
        this.totalPhotoCount += this.defectPictures == null ? 0 : this.defectPictures.size();
        HCLogUtil.e(TAG, "=================================totalPhotoCount=" + totalPhotoCount);
        enableCompress(true);
        ProgressDialogUtil.showProgressDialogWithProgress(this.mActivity, "图片正在压缩中，请勿拔掉SD卡", this.totalPhotoCount);
        ProgressDialogUtil.setProgress(this.compressed_photo_count);
        // 启动压缩
        this.mRunnable = new CompressImageRunnable();
        this.mCopmressThread = new Thread(this.mRunnable);
        this.mCopmressThread.start();
    }

    /**
     * 统计已经压缩图片总数
     *
     * @return
     */
    private int getAlreadyCompressImagesCount() {
        int count = 0;
        File imageFile;
        for (PhotoEntity image : outerPictures) {
            if (!TextUtils.isEmpty(image.getPath()) && !TextUtils.isEmpty(image.getTemp_path())) {
                if (!TextUtils.isEmpty(image.getUrl())) {
                    count++;
                } else {
                    imageFile = new File(image.getTemp_path());
                    if (imageFile.exists()) {
                        HCLogUtil.d(TAG, "outerPictures" + count + ", type = " + image.getType());
                        count++;
                    }
                }
            }
        }

        for (PhotoEntity image : innerPictures) {
            if (!TextUtils.isEmpty(image.getPath()) && !TextUtils.isEmpty(image.getTemp_path())) {
                if (!TextUtils.isEmpty(image.getUrl())) {
                    count++;
                } else {
                    imageFile = new File(image.getTemp_path());
                    if (imageFile.exists()) {
                        HCLogUtil.d(TAG, "innerPictures" + count + ", type = " + image.getType());
                        count++;
                    }
                }
            }
        }

        for (PhotoEntity image : detailPictures) {
            if (!TextUtils.isEmpty(image.getPath()) && !TextUtils.isEmpty(image.getTemp_path())) {
                if (!TextUtils.isEmpty(image.getUrl())) {
                    count++;
                } else {
                    imageFile = new File(image.getTemp_path());
                    if (imageFile.exists()) {
                        HCLogUtil.d(TAG, "detailPictures" + count + ", type = " + image.getType());
                        count++;
                    }
                }
            }
        }

        for (PhotoEntity image : defectPictures) {
            if (!TextUtils.isEmpty(image.getPath()) && !TextUtils.isEmpty(image.getTemp_path())) {
                if (!TextUtils.isEmpty(image.getUrl())) {
                    count++;
                } else {
                    imageFile = new File(image.getTemp_path());
                    if (imageFile.exists()) {
                        HCLogUtil.d(TAG, "defectPictures" + count + ", type = " + image.getType());
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * 压缩图片:压缩完成，将图片加入当前任务的图片列表中
     */
    private synchronized void compressImages(PhotoEntity image, int position) {
        HCLogUtil.d("ceshi", "compressImages type = " + image.getType());

        if (TextUtils.isEmpty(image.getPath())) {
            return;
        }

        //排除已经压缩上传过和只是压缩过的
        if (!TextUtils.isEmpty(image.getTemp_path()) && (!TextUtils.isEmpty(image.getUrl()) || new File(image.getTemp_path()).exists())) {
            UploadPhotoEntity uploadPhoto = new UploadPhotoEntity();
            uploadPhoto.setPhotoEntity(image);
            uploadPhoto.setPosition(position);
            if (!uploadPhotoList.contains(uploadPhoto)) {
                uploadPhotoList.add(uploadPhoto);
            }

            if (uploadPhotoList.size() == totalPhotoCount) {
                mHandler.sendEmptyMessage(PictureConstants.COMPRESS_ALL_COMPLETE);
            }
        } else {
            //先读取exif信息记录， 再压缩图片，将拍摄照片的时间回传给服务器
            try {
                ExifInterface exInfo = new ExifInterface(image.getPath());
                image.setCreate_time(UnixTimeUtil.getUnixTime(exInfo.getAttribute(ExifInterface.TAG_DATETIME), "yyyy:MM:dd HH:mm:ss"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, Object> map = BitmapUtil.readCorrectBitMapFromFile(image.getPath(), BitmapUtil.ResolutionLevel.kResolutionFile);// test
            switch (Integer.valueOf(map.get("code").toString())) {
                case 0: // 成功
                    Bitmap bitmap = (Bitmap) map.get("bitmap");
                    if (bitmap == null) {
                        break;
                    }

                    StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
                    sb.append(File.separator).append(TaskConstants.TEMP_HOME_PATH).append(File.separator).append(TaskConstants.TEMP_IMAGES_PATH);
                    File dirFile = new File(sb.toString());
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                    String localName = System.currentTimeMillis() + ".jpg";
                    sb.append("/").append(localName);
                    boolean ret = BitmapUtil.writeCorrectBitmapToFile(bitmap, sb.toString());
                    if (!ret) {
                        new File(sb.toString()).delete();
                        break;
                    }
                    image.setTemp_path(sb.toString());
                    //TODO 实时更新
                    updateReportInfo();
                    if (!bitmap.isRecycled()) { // 回收bitmap
                        bitmap.recycle();
                    }
                    UploadPhotoEntity uploadPhoto = new UploadPhotoEntity();
                    uploadPhoto.setPhotoEntity(image);
                    uploadPhoto.setPosition(position);
                    if (!uploadPhotoList.contains(uploadPhoto)) {
                        uploadPhotoList.add(uploadPhoto);
                    }
                    compressed_photo_count++; // 压缩成功数+1
                    //判断图片是否全部压缩完成，并根据判断的结果分别做相应的处理
                    if (totalPhotoCount == uploadPhotoList.size()) {
                        mHandler.sendEmptyMessage(PictureConstants.COMPRESS_ALL_COMPLETE);
                    } else {
                        mHandler.sendEmptyMessage(PictureConstants.COMPRESS_IMAGE_SUCCESS);
                    }
                    //将压缩完的图片信息保存到数据表中
                    CompressedPhotoEntity compressedPhoto = new CompressedPhotoEntity();
                    compressedPhoto.setSd_photo_name((String) map.get("sd_photo_name"));
                    compressedPhoto.setLocal_photo_name(localName);
                    compressedPhoto.setCreate_mills(System.currentTimeMillis());
                    CompressedPhotoDAO.getInstance().insert(compressedPhoto);
                    break;
                default:
                    // 压缩图片失败，终止当前压缩线程，并给出提示
                    mHandler.sendEmptyMessage(PictureConstants.COMPRESS_IMAGE_FAILED);
                    break;
            }
        }


    }

    /**
     * 上传报告和压缩后的图片
     */
    private void uploadPhotoAndReport() {
        //保存压缩后图片的临时路径
        CheckReportEntity checkReport = updateReportInfo();
        if (checkReport == null) {
            return;
        }

        UploadCheckTaskEntity uploadCheckTaskEntity = new UploadCheckTaskEntity(uploadPhotoList, taskId, reportId);
        uploadCheckTaskEntity.setCheckSource(checkReport.getCheck_source());
        uploadCheckTaskEntity.setVehicleName(checkReport.getVehicle_name());
        uploadCheckTaskEntity.setOuterPictures(this.outerPictures);
        uploadCheckTaskEntity.setInnerPictures(this.innerPictures);
        uploadCheckTaskEntity.setDetailPictures(this.detailPictures);
        uploadCheckTaskEntity.setDefectPictures(this.defectPictures);
        uploadCheckTaskEntity.setMax(uploadCheckTaskEntity.getPhotoList().size());
        uploadCheckTaskEntity.setVideoEntity(videoEntity);
        uploadCheckTaskEntity.setAudioEntity(audioEntity);
        //设置其具有的上传机会
        uploadCheckTaskEntity.setUploadChance(TaskConstants.DEFAULT_UPLOAD_CHANCE);
        //上传起始时间
        uploadCheckTaskEntity.setStartMills(System.currentTimeMillis());
        uploadCheckTaskEntity.setCreateTime(UnixTimeUtil.getCurrTime());//创建任务的时间
        uploadCheckTaskEntity.setUploadStatus(TaskConstants.UPLOAD_STATUS_WAITING);//默认：排队中
        //初始化图片顺序
        initPhotoOrder();
        UploadServiceHelper.getInstance().addUploadCheckTask(uploadCheckTaskEntity);
        //通知待处理界面，重新加载界面内容
        HCTasksWatched.getInstance().notifyWatchers();
    }

    /**
     * 初始化图片顺序
     */
    private void initPhotoOrder() {
        for (int i = 0; i < uploadPhotoList.size(); i++) {
            uploadPhotoList.get(i).getPhotoEntity().setIndex(i + 1);
        }
    }

    /**
     * 复制视频
     *
     * @author wfx
     */
    private void copyVideoAndShowProgress() {
        if (videoEntity == null) {
            showAllCompressedTip();
            return;
        }
        //已经复制过了
        if (!TextUtils.isEmpty(videoEntity.getCopyedPath()) && new File(videoEntity.getCopyedPath()).exists()) {
            showAllCompressedTip();
            return;
        }

        if (!new File(videoEntity.getPath()).exists()) {
            ToastUtil.showInfo("该视频不存在，请连接SDK，进行重试");
            return;
        }
        //显示视频复制进度
        showCopyVideoProgress();
        //复制视频
        copyVideo();
    }

    /**
     * 复制视频
     *
     * @author wfx
     */
    private void copyVideo() {
        new Thread() {
            @Override
            public void run() {
                //复制视频
                String errMsg = "";
                String destFilePath = "";
                try {
                    StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
                    sb.append(File.separator).append(TaskConstants.TEMP_HOME_PATH).append(File.separator).append(TaskConstants.TEMP_VIDEO_PATH);
                    String folderPath = sb.toString();
                    new File(folderPath).mkdirs();
                    String extName = GeneralUtils.getExtensionName(videoEntity.getPath());
                    String validFileName = String.valueOf(System.currentTimeMillis()).concat(".").concat(extName);
                    File destFile = new File(folderPath, validFileName);
                    destFilePath = destFile.getAbsolutePath();
                    GeneralUtils.copyFileToFolder(videoEntity.getPath(), destFile);
                    videoEntity.setCopyedPath(destFilePath);
                } catch (Exception e) {
                    HCLogUtil.e(TAG, e.getMessage());
                    videoEntity.setCopyedPath("");
                    errMsg = e.getMessage();
                } finally {
                    if (TextUtils.isEmpty(videoEntity.getCopyedPath()) || !new File(videoEntity.getCopyedPath()).exists()) {
                        //删除复制的视频
                        deleteCopyVideo(destFilePath);
                        Message msg = Message.obtain();
                        msg.what = PictureConstants.FAILED_COPY_VIDEO;
                        msg.obj = errMsg;
                        mHandler.sendMessage(msg);
                    } else {
                        //保存视频路径，避免重复复制
                        updateReportInfo();
                        mHandler.sendEmptyMessage(PictureConstants.FINISH_COPY_VIDEO);
                    }
                }
            }
        }.start();
    }

    /**
     * 删除复制的视频
     *
     * @param destFilePath 目标文件路径
     */
    private void deleteCopyVideo(String destFilePath) {
        if (TextUtils.isEmpty(destFilePath) || !new File(destFilePath).exists()) {
            return;
        }
        new File(destFilePath).delete();
    }

    /**
     * 显示视频复制进度
     *
     * @author wfx
     */
    private void showCopyVideoProgress() {
        if (mActivity.isFinishing()) {
            return;
        }
        progressBar = new ProgressDialog(mActivity);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setTitle("提示");
        progressBar.setMessage("正在复制视频中....");
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
    }

    /**
     * 展示视频复制失败的提示
     */
    private void showCopyFailedTip(String errMsg) {
        if (this.mActivity.isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        builder.setMessage("视频复制失败:".concat(errMsg).concat("，请连接SD卡，重新进行上传"));
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * 图片全部压缩完成后的提示
     */
    private void showAllCompressedTip() {
        if (mActivity.isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        if (videoEntity != null) {
            builder.setMessage("图片压缩、视频复制均已结束，你可将SD卡拔出");
        } else {
            builder.setMessage("图片压缩已结束，你可将SD卡拔出");
        }
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (onCompressPhotoSuccessedListener != null) {
                    onCompressPhotoSuccessedListener.onCompressPhotoSuccess();
                }
                //上传图片和报告
                uploadPhotoAndReport();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ImageUploadActivity) mActivity).click = false;
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void setOnCompressPhotoSuccessedListener(OnCompressPhotoSuccessedListener onCompressPhotoSuccessedListener) {
        this.onCompressPhotoSuccessedListener = onCompressPhotoSuccessedListener;
    }

    /**
     * 更新报告相关信息：
     * 避免程序在上传中途突然退出，再打开时找不到已压缩图片信息的情况
     */
    private CheckReportEntity updateReportInfo() {
        CheckReportEntity checkReport = CheckReportDAO.getInstance().get(reportId);
        if (checkReport == null) {
            return null;
        }
        mGson = new Gson();
        //设置app的当前版本号，便于知道用户使用哪个版本上传的报告
        checkReport.setApp_version(DeviceInfoUtil.getAppVersion());
        checkReport.setOut_pics(mGson.toJson(this.outerPictures));
        checkReport.setInner_pics(mGson.toJson(this.innerPictures));
        checkReport.setDetail_pics(mGson.toJson(this.detailPictures));
        checkReport.setDefect_pics(mGson.toJson(this.defectPictures));
        if (videoEntity != null) {
            checkReport.setVideo_url(videoEntity.toJson());
        }
        if (audioEntity != null) {
            checkReport.setAudio_url(audioEntity.toJson());
        }
    /*更新本地存储报告相关的图片信息 */
        CheckReportDAO.getInstance().update(checkReport.getId(), checkReport);
        return checkReport;
    }

    public interface OnCompressPhotoSuccessedListener {
        /**
         * 当图片全部压缩成功后，处理的动作
         */
        void onCompressPhotoSuccess();
    }

    /**
     * 压缩图片线程
     */
    private class CompressImageRunnable implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < outerPictures.size(); i++) {
                try {
                    compressImages(outerPictures.get(i), i);
                } catch (Exception e) {
                    LogUtil.e(TAG, e);
                    // 压缩图片失败，终止当前压缩线程，并给出提示
                    mHandler.sendEmptyMessage(PictureConstants.COMPRESS_IMAGE_FAILED);
                    break;
                }
            }

            for (int i = 0; i < innerPictures.size(); i++) {
                try {
                    compressImages(innerPictures.get(i), i);
                } catch (Exception e) {
                    LogUtil.e(TAG, e);
                    // 压缩图片失败，终止当前压缩线程，并给出提示
                    mHandler.sendEmptyMessage(PictureConstants.COMPRESS_IMAGE_FAILED);
                    break;
                }
            }

            for (int i = 0; i < detailPictures.size(); i++) {
                try {
                    compressImages(detailPictures.get(i), i);
                } catch (Exception e) {
                    LogUtil.e(TAG, e);
                    // 压缩图片失败，终止当前压缩线程，并给出提示
                    mHandler.sendEmptyMessage(PictureConstants.COMPRESS_IMAGE_FAILED);
                    break;
                }
            }

            for (int i = 0; i < defectPictures.size(); i++) {
                try {
                    compressImages(defectPictures.get(i), i);
                } catch (Exception e) {
                    LogUtil.e(TAG, e);
                    // 压缩图片失败，终止当前压缩线程，并给出提示
                    mHandler.sendEmptyMessage(PictureConstants.COMPRESS_IMAGE_FAILED);
                    break;
                }
            }
        }
    }
}
