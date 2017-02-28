package com.haoche51.checker.adapter;

import android.content.Context;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.helper.UploadServiceHelper;
import com.haoche51.checker.util.ControlDisplayUtil;
import com.haoche51.checker.util.UnixTimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 15/12/21.
 */
public class CheckTaskAdapter extends HCCommonAdapter<CheckTaskEntity> {

    public CheckTaskAdapter(Context context, List<CheckTaskEntity> data, int layoutId) {
        super(context, data, layoutId);
        filterUploadingTask(data);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        CheckTaskEntity item = getItem(position);
        holder.setTextViewText(R.id.tv_check_vehicle, item.getVehicle_name());
        holder.setTextViewText(R.id.tv_item_check_seller_name, item.getSeller_name());
        holder.setTextViewText(R.id.tv_item_check_seller_phone, item.getSeller_phone());
        holder.setTextViewText(R.id.tv_item_check_time, UnixTimeUtil.formatYearMonthDay(item.getAppointment_starttime()) + " " + item.getStarttime_comment());
        holder.setTextViewText(R.id.tv_item_check_address, item.getAppointment_place());
        holder.setTextViewText(R.id.tv_item_check_comment, item.getChecker_comment());//此处为评估师备注
        TextView tv_check_status = holder.findTheView(R.id.tv_check_status);//任务状态
        ControlDisplayUtil.getInstance().setCheckStatus(tv_check_status, item.getCheck_status());
        TextView tv_check_help = holder.findTheView(R.id.tv_check_help);//帮检、转接单
        ControlDisplayUtil.getInstance().setCheckSource(tv_check_help, item.getCheck_source());
    }

    /**
     * 过滤掉正在上传的任务
     */
    public void filterUploadingTask(List<CheckTaskEntity> mTasks) {
        try {
            if (mTasks != null && !mTasks.isEmpty()) {
                //获取上传中任务队列
                List<UploadCheckTaskEntity> uploadTaskList = UploadServiceHelper.mUploadList;
                //是否有上传任务
                if (uploadTaskList != null && !uploadTaskList.isEmpty()) {
                    //保存没有在上传中的任务
                    List<CheckTaskEntity> newTasks = new ArrayList<>();
                    newTasks.addAll(mTasks);
                    //遍历上传中任务
                    for (UploadCheckTaskEntity uploadEntity : uploadTaskList) {
                        //遍历所有任务
                        for (CheckTaskEntity checkEntity : newTasks) {
                            //找到非上传任务
                            if (checkEntity.getId() == uploadEntity.getCheckTaskId())
                                mTasks.remove(checkEntity);//移除这条正在上传的任务
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
