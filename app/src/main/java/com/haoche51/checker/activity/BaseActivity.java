package com.haoche51.checker.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpResponse;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;

public class BaseActivity extends Activity implements ValidationListener, HCHttpCallback {
    public Validator validator;
    /**
     * 广播--接收关闭页面的广播--finsh
     */
    private BroadcastReceiver finishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TaskConstants.ACTION_FINISH_MAIN.equals(intent.getAction())) {
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置禁止横屏
        // 初始化Validator
        validator = new Validator(this);
        validator.setValidationListener(this);

        //注册广播，用于退出时关闭页面
        registerReceiver(finishReceiver, new IntentFilter(TaskConstants.ACTION_FINISH_MAIN));

    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        // 验证失败
        String message = failedRule.getFailureMessage();
        validation(failedView, message);
    }

    @Override
    public void onValidationSucceeded() {
        // 验证成功
    }

    public void showErrorMsg(View failedView, String message) {
        validation(failedView, message);
    }

    private void validation(View failedView, String message) {
        if (failedView!=null && (failedView instanceof EditText || !failedView.isEnabled())) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(finishReceiver);
        super.onDestroy();
    }


    /**
     * 网络请求开始
     *
     * @param action 当前请求action
     */
    @Override
    public void onHttpStart(String action, int requestId) {

    }

    /**
     * 网络请求结束,请求服务器成功，请求服务器失败
     *
     * @param action    当前请求action
     * @param requestId
     * @param response  hc 请求结果
     * @param error     网络问题造成failed 的error
     */
    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {

    }

    /**
     * 网络请求进度
     *
     * @param action       当前请求action
     * @param bytesWritten
     * @param totalSize
     */
    @Override
    public void onHttpProgress(String action, int requestId, long bytesWritten, long totalSize) {

    }

    /**
     * 重试次数回调
     *
     * @param action  当前请求action
     * @param retryNo 重试次数
     */
    @Override
    public void onHttpRetry(String action, int requestId, int retryNo) {

    }

    @Override
    public void onHttpFinish(String action, int requestId) {

    }
}
