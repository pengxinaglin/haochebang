package com.haoche51.checker.activity.user;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haoche51.checker.CheckerApplication;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.UpdateVersionEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.service.AutoUpdateVersionService;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.DeviceInfoUtil;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.FileDownloadUtil;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.NetInfoUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.SharedPreferencesUtils;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 设置页面
 * Created by yangming on 2015/11/25.
 */
public class SettingActivity extends CommonTitleBaseActivity {

    @ViewInject(R.id.tv_layout_user_welcome_name)
    private TextView textViewName;
    @ViewInject(R.id.tv_layout_user_setting_version)
    private TextView textViewVersion;

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_seting, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);

        DisplayUtils.getInstance().setWelcome(textViewName);

        try {
            String versionString = getPackageManager().getPackageInfo("com.haoche51.checker", 0).versionName.toString();
            textViewVersion.setText(versionString);
        } catch (PackageManager.NameNotFoundException e) {
            textViewVersion.setText("Unknow");
            e.printStackTrace();
        }
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText("设置");
    }

    @Event(R.id.tv_layout_user_setting_qrcode)
    private void qrcodeClick(View view) {
        HCActionUtil.launchActivity(this, QRShareActivity.class, null);
    }

    @Event(R.id.tv_layout_user_setting_pwd)
    private void pwdClick(View view) {
        HCActionUtil.launchActivity(this, ChangePwdActivity.class, null);
    }

    @Event(R.id.ll_layout_user_setting_update)
    private void checkVersionClick(View view) {
        checkUpdate();
    }

    @Event(R.id.tv_layout_user_setting_logout)
    private void logoutClick(View view) {
        AlertDialogUtil.createNormalDialog(this, "确认退出登录？", "取消", "确认", true, new AlertDialogUtil.OnDismissListener() {
            @Override
            public void onDismiss(Bundle data) {
                if (data != null) {
                    boolean determine = data.getBoolean("determine");
                    if (determine) {
                        //确认
                        ProgressDialogUtil.showProgressDialog(SettingActivity.this, getString(R.string.cancellation));
                        OKHttpManager.getInstance().post(HCHttpRequestParam.unbind(), SettingActivity.this, 0);
                    }
//                    else {
//                        //取消
//                    }
                }
            }
        });
    }

    private void checkUpdate() {
        if (!NetInfoUtil.isNetAvaliable()) {
            ToastUtil.showInfo("当前网络不可用，请检查网络设置");
            return;
        }
        OKHttpManager.getInstance().post(HCHttpRequestParam.checkVersion(DeviceInfoUtil.getAppCurrVersion()), this, 0);
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        super.onHttpComplete(action, requestId, response, error);
        if (action.equals(HttpConstants.ACTION_UNBIND_BAIDUPUSH)) {//退出，取消百度推送
            onLogoutResult(response);
            ProgressDialogUtil.closeProgressDialog();
        } else if (HttpConstants.ACTION_CHECK_VERSION.equals(action)) {
            switch (response.getErrno()) {
                case 0:
                    parseUpdateVersion(response.getData());
                    break;
                default:
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
        }
    }

    /**
     * 解析更新版本的json串
     *
     * @param json
     */
    private void parseUpdateVersion(String json) {
//        UpdateVersionEntity versionEntity = new HCJsonParse().parseUpdateVersion(json);
        UpdateVersionEntity versionEntity = JsonParseUtil.fromJsonObject(json, UpdateVersionEntity.class);
        //0：不是最新包，需要更新 1：不需要更新
        if (versionEntity != null && versionEntity.getIs_new() == 0) {
            checkDownloadStatus();
        } else {
            ToastUtil.showInfo(getString(R.string.app_version_isnew));
        }
    }


    /**
     * 退出登录
     */
    private void onLogoutResult(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                CheckerApplication.logout();
                break;
            default:
                ToastUtil.showText(response.getErrmsg());
                break;
        }
    }


    /**
     * 检查下载任务的状态
     */
    private void checkDownloadStatus() {
        //第一次下载的 downloadManager.enqueue(req)会返回一个downloadId，把downloadId保存到本地，
        // 用户下次进来的时候，取出保存的downloadId，然后通过downloadId来获取下载的状态信息
        long downloadId = SharedPreferencesUtils.getLong(TaskConstants.KEY_DOWNLOAD_ID, -1L);
        if (downloadId == -1) {
            startUpdateService();
            return;
        }
        int status = FileDownloadUtil.getDownloadStatus(downloadId);
        switch (status) {
            case DownloadManager.STATUS_RUNNING:
                ToastUtil.showInfo(getString(R.string.version_downloading));
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                Uri uri = FileDownloadUtil.getDownloadUri(downloadId);
                if (uri == null) {
                    FileDownloadUtil.resetDownloadId(downloadId);
                    startUpdateService();
                } else {
                    PackageInfo downApkInfo = FileDownloadUtil.getApkInfo(uri.getPath());
                    if (FileDownloadUtil.isNewAPK(downApkInfo)) {
                        FileDownloadUtil.installApk(uri);
                        finish();
                    } else {
                        FileDownloadUtil.resetDownloadId(downloadId);
                        startUpdateService();
                    }
                }
                break;
            default:
                FileDownloadUtil.resetDownloadId(downloadId);
                startUpdateService();
                break;
        }
    }

    /**
     * 开启更新服务
     */
    private void startUpdateService() {
        Intent intent = new Intent(this, AutoUpdateVersionService.class);
        startService(intent);
    }

}
