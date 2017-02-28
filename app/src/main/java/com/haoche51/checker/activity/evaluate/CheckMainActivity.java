package com.haoche51.checker.activity.evaluate;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.haoche51.checker.DAO.CheckUploadTaskDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.adapter.CheckPopupAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckPopupEntity;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.fragment.evaluate.CheckFinishTaskFragment;
import com.haoche51.checker.fragment.evaluate.CheckTaskFragment;
import com.haoche51.checker.fragment.evaluate.CheckUploadTaskFragment;
import com.haoche51.checker.helper.UploadServiceHelper;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.upload.UploadService;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ServiceInfoUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.widget.FragmentItem;
import com.haoche51.checker.widget.HCSmartTabLayout;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 验车任务列表
 * Created by mac on 15/12/21.
 */
public class CheckMainActivity extends CommonBaseFragmentActivity {

    private static final String TAG = "CheckMainActivity";
    List<FragmentItem> mFragmentItemList;
    @ViewInject(R.id.content_view)
    private HCSmartTabLayout hcSmartTabLayout;
    private UploadService.ReuploadBinder uploadTaskBinder;
    private UploadTaskServiceConnection uploadServiceConn;
    private PopupWindow popupWindow;
    private List<CheckPopupEntity> checkPopupEntityList;
    private CheckPopupAdapter mAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_checktask_main);
        x.view().inject(this);
        setScreenTitle(R.string.hc_right_vehicle_check);
        registerTitleBack();//设置返回
    }

    @Override
    protected void initData() {
        //清除历史的证书和日志
        StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator).append(TaskConstants.TEMP_HOME_PATH).append(File.separator);
        String workFolder = sb.toString();
        removeFile(new File(workFolder.concat("ffmpeglicense.lic")));
        removeFile(new File(workFolder.concat("vk.log")));
        //加载本地存储的所有待上传的验车任务
        if (UploadServiceHelper.mUploadList.size() == 0) {
            List<UploadCheckTaskEntity> localTaskList = CheckUploadTaskDAO.getInstance().get(null);
            if (localTaskList != null) {
                UploadServiceHelper.mUploadList.addAll(0, localTaskList);
            }

        }
        //上传中任务的服务意图
        Intent intent = new Intent(GlobalData.mContext, UploadService.class);
        //服务停止时，开启服务
        if (!ServiceInfoUtil.isServiceRunning(GlobalData.mContext, UploadService.class.getCanonicalName())) {
            GlobalData.mContext.startService(intent);
        }
        //绑定服务
        uploadServiceConn = new UploadTaskServiceConnection();
        bindService(intent, uploadServiceConn, Context.BIND_AUTO_CREATE);

        //初始化悬浮窗
        initPopupWindow();

        mFragmentItemList = new ArrayList<>();
        mFragmentItemList.add(new FragmentItem(getString(R.string.hc_check_task_untreated), new CheckTaskFragment()));
        mFragmentItemList.add(new FragmentItem(getString(R.string.hc_check_task_uploading), new CheckUploadTaskFragment()));
        mFragmentItemList.add(new FragmentItem(getString(R.string.hc_check_task_finish), new CheckFinishTaskFragment()));
        //设置界面
        hcSmartTabLayout.setContentFragments(this, mFragmentItemList);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //服务停止时，开启服务
        Intent intent = new Intent(GlobalData.mContext, UploadService.class);
        if (!ServiceInfoUtil.isServiceRunning(GlobalData.mContext, UploadService.class.getCanonicalName())) {
            GlobalData.mContext.startService(intent);
        }
    }

    /**
     * 删除指定文件
     *
     * @param file
     */
    private void removeFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        HCLogUtil.e(TAG, "onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        HCLogUtil.e(TAG, "onResumeFragments");
        if (getIntent() == null) return;
        //获取默认展示界面的标签索引，默认值：待处理界面
        int index = getIntent().getIntExtra(TaskConstants.BINDLE_FRAGMENT_INDEX, TaskConstants.FRAGMENT_PENDING_TASK);
        if (index != 0 && index < hcSmartTabLayout.getViewPager().getChildCount()) {
            setIntent(null);
            hcSmartTabLayout.getViewPager().setCurrentItem(index);
        }
    }

    public UploadService.ReuploadBinder getUploadTaskBinder() {
        return uploadTaskBinder;
    }


    /**
     * 展示数据
     *
     * @param view
     */
    @Event(R.id.fab_show_data)
    private void showData(View view) {
        if (popupWindow != null && getTitleView() != null && !popupWindow.isShowing()) {
            popupWindow.showAsDropDown(getTitleView());
        } else if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 添加临时任务
     *
     * @param view
     */
    @Event(R.id.tv_temp_task)
    private void addTempTask(View view) {
        Intent intent = new Intent(this, AddTempTaskActivity.class);
        startActivity(intent);
    }

    /**
     * 添加任务
     *
     * @param view
     */
    @Event(R.id.tv_right_fuction)
    private void addTask(View view) {
        HCActionUtil.launchActivity(this, AddTaskActivity.class, null);
    }

    /**
     * 初始化悬浮窗
     */
    private void initPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.layout_check_popup, null);
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        ListView listView = (ListView) contentView.findViewById(R.id.lv_check_popup);
        checkPopupEntityList = new ArrayList<>();
        mAdapter = new CheckPopupAdapter(this, checkPopupEntityList);
        listView.setAdapter(mAdapter);
        OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckPopupData(), this, 0);
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (HttpConstants.ACTION_GET_CHECK_POPUP_DATA.equals(action)) {
            switch (response.getErrno()) {
                case 0:
                    if (!TextUtils.isEmpty(response.getData())) {
//						List<CheckPopupEntity> entities = new HCJsonParse().parseCheckPopupData(response.getData());
                        List<CheckPopupEntity> entities = JsonParseUtil.fromJsonArray(response.getData(), CheckPopupEntity.class);
                        if (entities != null) {
                            checkPopupEntityList.addAll(entities);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                default:
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        HCLogUtil.e(TAG, "onDestroy");
        UploadServiceHelper.getInstance().setAdapter(null);
        uploadTaskBinder = null;
        //解绑服务
        unbindService(uploadServiceConn);
        uploadServiceConn = null;
        //关闭悬浮窗
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        super.onDestroy();
    }

    private class UploadTaskServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            uploadTaskBinder = (UploadService.ReuploadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}

