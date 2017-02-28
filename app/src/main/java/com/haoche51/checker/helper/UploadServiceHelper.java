package com.haoche51.checker.helper;

import android.content.Intent;

import com.haoche51.checker.DAO.CheckUploadTaskDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.adapter.CheckUploadTaskAdapter;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.upload.UploadService;
import com.haoche51.checker.util.ServiceInfoUtil;
import java.util.ArrayList;
import java.util.List;

public class UploadServiceHelper {

    private static UploadServiceHelper mUploadHelper = new UploadServiceHelper();
    private UploadTaskListener mUploadTaskListener;
    private CheckUploadTaskAdapter mAdapter;
    public static List<UploadCheckTaskEntity> mUploadList = new ArrayList<>();

    private UploadServiceHelper() {
    }

    /**
     * 获得单列
     */
    public static UploadServiceHelper getInstance() {
        return mUploadHelper;
    }

    public void setUploadTaskListener(UploadTaskListener uploadTaskListener) {
        mUploadTaskListener = uploadTaskListener;
    }


    public void setAdapter(CheckUploadTaskAdapter adapter) {
        mAdapter = adapter;
    }

    public CheckUploadTaskAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 添加验车任务
     *
     * @param uploadCheckTaskEntity 验车任务
     */
    public void addUploadCheckTask(UploadCheckTaskEntity uploadCheckTaskEntity) {
        //防止重复添加
        if (mUploadList.contains(uploadCheckTaskEntity)) {
            return;
        }

        mUploadList.add(uploadCheckTaskEntity);

        if (mAdapter != null) {
            mAdapter.setUploadTaskList(mUploadList);
            mAdapter.notifyDataSetChanged();
        }
        //每添加一条记录就向本地数据库中存储一下
        CheckUploadTaskDAO.getInstance().insert(uploadCheckTaskEntity);

        //当服务停止时，开启服务
        if (!ServiceInfoUtil.isServiceRunning(GlobalData.mContext, UploadService.class.getCanonicalName())) {
            //加载本地存储的所有待上传的验车任务
            if (mUploadList.size() == 0) {
                List<UploadCheckTaskEntity> localTaskList = CheckUploadTaskDAO.getInstance().get(null);
                mUploadList.addAll(0, localTaskList);
            }
            Intent intent = new Intent(GlobalData.mContext, UploadService.class);
            GlobalData.mContext.startService(intent);
        } else if (mUploadTaskListener != null) {
            mUploadTaskListener.afterAddUploadTask();
        }
    }


    public interface UploadTaskListener {
        /**
         * 添加上传任务之后的处理
         */
        void afterAddUploadTask();
    }
}
