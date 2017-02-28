package com.haoche51.checker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.CheckUploadTaskDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.CheckMainActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.fragment.evaluate.CheckTaskFragment;
import com.haoche51.checker.helper.UploadServiceHelper;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.upload.UploadService;
import com.haoche51.checker.util.DeviceInfoUtil;
import com.haoche51.checker.util.HCLogUtil;

import java.util.List;

/**
 * 上传验车任务图片数据适配器
 * Created by wufuxian on 2015/12/20.
 */
public class CheckUploadTaskAdapter extends BaseAdapter {
    private Context mContext;
    private List<UploadCheckTaskEntity> uploadTaskList;
    private Drawable blueProgress;
    private Drawable redProgress;
    private int blackColor;//黑色
    private int redColor;//红色

    /**
     * 上传服务代理人
     */
    private UploadService.ReuploadBinder uploadTaskBinder;

    public CheckUploadTaskAdapter(Context context) {
        this.mContext = context;
        this.uploadTaskList = UploadServiceHelper.mUploadList;
        this.blackColor = mContext.getResources().getColor(R.color.hc_self_black);
        this.redColor = mContext.getResources().getColor(R.color.hc_self_red);
    }

    public void setUploadTaskList(List<UploadCheckTaskEntity> uploadTaskList) {
        this.uploadTaskList = uploadTaskList;
    }

    @Override
    public int getCount() {
        if (uploadTaskList == null) {
            return 0;
        }
        return uploadTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        if (getCount() == 0) {
            return null;
        }
        return uploadTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_check_uplod, parent, false);
            mHolder = new ViewHolder();
            mHolder.tv_check_source = (TextView) convertView.findViewById(R.id.tv_check_source);
            mHolder.tv_check_title = (TextView) convertView.findViewById(R.id.tv_check_title);
            mHolder.tv_check_uploadstatus = (TextView) convertView.findViewById(R.id.tv_check_upload_status);
            mHolder.ll_check_upload_failed = (LinearLayout) convertView.findViewById(R.id.ll_check_upload_failed);
            mHolder.tv_check_uploadstop_reason = (TextView) convertView.findViewById(R.id.tv_check_upload_stop_reason);
            mHolder.tv_check_uploadstop_operate = (TextView) convertView.findViewById(R.id.tv_check_upload_stop_operate);
            mHolder.pb_check_uploadprogress = (ProgressBar) convertView.findViewById(R.id.pb_check_upload_progress);
            mHolder.tv_check_uploadprogress = (TextView) convertView.findViewById(R.id.tv_check_upload_progress);
            mHolder.tv_check_uploadmax = (TextView) convertView.findViewById(R.id.tv_check_upload_max);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        //填充条目数据
        fillItemData(mHolder, position);
        return convertView;
    }

    /**
     * 填充条目数据
     *
     * @param holder
     * @param position
     */
    private void fillItemData(final ViewHolder holder, int position) {
        UploadCheckTaskEntity uploadCheckTask = uploadTaskList.get(position);
        //验车任务来源
        if (TextUtils.isEmpty(uploadCheckTask.getCheckSource())) {
            holder.tv_check_source.setVisibility(View.GONE);
        } else {
            holder.tv_check_source.setVisibility(View.VISIBLE);
            holder.tv_check_source.setText(uploadCheckTask.getCheckSource());
        }
        //任务上传状态
        holder.tv_check_uploadstatus.setText(uploadCheckTask.getUploadStatus());
        //进度条的上传进度
        holder.pb_check_uploadprogress.setMax(uploadCheckTask.getMax());
        holder.pb_check_uploadprogress.setProgress(uploadCheckTask.getProgress());
        holder.tv_check_uploadmax.setText(String.valueOf(uploadCheckTask.getMax()));
        holder.tv_check_uploadprogress.setText(String.valueOf(uploadCheckTask.getProgress()));
        //任务标题
        holder.tv_check_title.setText(uploadCheckTask.getVehicleName());
        //去除已完成的记录
        if (TaskConstants.UPLOAD_STATUS_REPORT_FINISHED.equals(uploadCheckTask.getUploadStatus())) {
            if (position < UploadServiceHelper.mUploadList.size()) {
                UploadServiceHelper.mUploadList.remove(position);
                notifyDataSetChanged();
                //删除这条待上传的记录，去已完成界面查看
                CheckUploadTaskDAO.getInstance().deleteByTaskId(uploadCheckTask.getCheckTaskId());
            }
        } else if (!TaskConstants.UPLOAD_STATUS_STOP.equals(uploadCheckTask.getUploadStatus())) {//非中断：排队中或者上传中
            holder.ll_check_upload_failed.setVisibility(View.GONE);
            //进度条颜色 蓝色
            blueProgress = mContext.getResources().getDrawable(R.drawable.progressbar_blue);
            holder.pb_check_uploadprogress.setProgressDrawable(blueProgress);
            //字体颜色 黑色
            holder.tv_check_uploadstatus.setTextColor(blackColor);
        } else {
            holder.ll_check_upload_failed.setVisibility(View.VISIBLE);
            //进度条颜色 红色
            redProgress = mContext.getResources().getDrawable(R.drawable.progressbar_red);
            //进度条颜色 红色
            holder.pb_check_uploadprogress.setProgressDrawable(redProgress);
            //字体颜色 红色
            holder.tv_check_uploadstatus.setTextColor(redColor);
            String failReason = uploadCheckTask.getFailedReason();
            //任务上传失败的原因
            holder.tv_check_uploadstop_reason.setText(failReason);
            //任务上传失败的操作
            holder.tv_check_uploadstop_operate.setText(uploadCheckTask.getFailedOperate());
            //注册点击事件
            holder.ll_check_upload_failed.setOnClickListener(new MyViewClick(uploadCheckTask, uploadCheckTask.getFailedOperate(), position));
        }

    }

    /**
     * 存储上传完的图片信息,上传报告时使用
     */
    private CheckReportEntity updateReportUrl(UploadCheckTaskEntity uploadCheckTaskEntity) {
        CheckReportEntity checkReport = CheckReportDAO.getInstance().get(uploadCheckTaskEntity.getReportId());
        if (checkReport == null || uploadCheckTaskEntity == null) {
            return null;
        }
        Gson gson = new Gson();
        //设置app的当前版本号，便于知道用户使用哪个版本上传的报告
        checkReport.setApp_version(DeviceInfoUtil.getAppVersion());
        checkReport.setOut_pics(gson.toJson(uploadCheckTaskEntity.getOuterPictures()));
        checkReport.setInner_pics(gson.toJson(uploadCheckTaskEntity.getInnerPictures()));
        checkReport.setDetail_pics(gson.toJson(uploadCheckTaskEntity.getDetailPictures()));
        checkReport.setDefect_pics(gson.toJson(uploadCheckTaskEntity.getDefectPictures()));
        if (uploadCheckTaskEntity.getVideoEntity() != null) {
            checkReport.setVideo_url(uploadCheckTaskEntity.getVideoEntity().toJson());
        }
        if (uploadCheckTaskEntity.getAudioEntity() != null) {
            checkReport.setAudio_url(uploadCheckTaskEntity.getAudioEntity().toJson());
        }
        /*更新本地存储报告相关的图片信息 */
        CheckReportDAO.getInstance().update(checkReport.getId(), checkReport);
        return checkReport;
    }

    @Override
    public void notifyDataSetChanged() {
        try{
            if("main".equals(Thread.currentThread().getName())){
                super.notifyDataSetChanged();
            }
        }catch (Exception e){
            HCLogUtil.e(this.getClass().getName(), e.getMessage());
        }

    }

    private class ViewHolder {
        TextView tv_check_title;
        TextView tv_check_source;
        TextView tv_check_uploadstatus;
        ProgressBar pb_check_uploadprogress;
        TextView tv_check_uploadstop_reason;
        TextView tv_check_uploadstop_operate;
        LinearLayout ll_check_upload_failed;
        TextView tv_check_uploadprogress;
        TextView tv_check_uploadmax;
    }

    /**
     * 失败后操作的点击事件
     */
    private class MyViewClick implements View.OnClickListener {
        private String failedOperate;
        private int position;
        private UploadCheckTaskEntity uploadCheckTask;

        public MyViewClick(UploadCheckTaskEntity uploadCheckTask, String failedOperate, int position) {
            this.failedOperate = failedOperate;
            this.position = position;
            this.uploadCheckTask = uploadCheckTask;
        }

        @Override
        public void onClick(View v) {
            if (TaskConstants.UPLOAD_FAILED_OPERATE_RECOMPRESS.equals(failedOperate)) {
                if (position < UploadServiceHelper.mUploadList.size()) {
                    // 删除重新上传
                    UploadServiceHelper.mUploadList.remove(position);
                    notifyDataSetChanged();
                    //删除之前，先将其url什么的信息保存到报告中
                    updateReportUrl(uploadCheckTask);
                    //删除这条待上传的记录，让其自动回到待检测列表中
                    CheckUploadTaskDAO.getInstance().deleteByTaskId(uploadCheckTask.getCheckTaskId());
                    //刷新待处理任务列表
                    Bundle data = new Bundle();
                    data.putBoolean(CheckTaskFragment.ONCHANGED_REFRESH_DATA, true);
                    HCTasksWatched.getInstance().notifyWatchers(data);
                }
            } else if (TaskConstants.UPLOAD_FAILED_OPERATE_SETTING.equals(failedOperate)) {
                // 打开网络设置
                Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GlobalData.mContext.startActivity(intent);
            } else {
                // 重试
                uploadTaskBinder = ((CheckMainActivity) mContext).getUploadTaskBinder();
                if (uploadTaskBinder != null) {
                    uploadTaskBinder.reupload(position);
                }
            }
        }
    }
}
