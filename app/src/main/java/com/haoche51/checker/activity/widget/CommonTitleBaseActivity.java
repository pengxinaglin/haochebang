package com.haoche51.checker.activity.widget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpResponse;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;

/**
 * Created by xuhaibo on 15/9/11.
 */
public abstract class CommonTitleBaseActivity extends Activity implements Validator.ValidationListener, HCHttpCallback {
    public Context mContext;
    /*表单校验*/
    public Validator validator;
    /* titlebar return*/
    private TextView mReturn;
    /* title content */
    private TextView mTitle;
    /* titleBar rightFaction */
    private TextView mRightFaction;
    /* haoche51 content view*/
    private FrameLayout mContentViewContainer;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);
        mContext = this;

        // 初始化Validator
        validator = new Validator(this);
        validator.setValidationListener(this);

        //注册广播，用于退出时关闭页面
        registerReceiver(finishReceiver, new IntentFilter(TaskConstants.ACTION_FINISH_MAIN));

        //标题栏的view
        mReturn = (TextView) findViewById(R.id.tv_common_back);
        mTitle = (TextView) findViewById(R.id.tv_common_title);
        mRightFaction = (TextView) findViewById(R.id.tv_right_fuction);
        onHCReturn(mReturn);

        mContentViewContainer = (FrameLayout) findViewById(R.id.content_container);
        mContentViewContainer.addView(getHCContentView());

        initContentView(savedInstanceState);
        initTitleBar(mReturn, mTitle, mRightFaction);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(finishReceiver);
        super.onDestroy();
    }

    /**
     * @return view haoche51 content view
     */
    public abstract View getHCContentView();

    /**
     * @param saveInstanceState activity onCreate saveInstanceState
     */
    public abstract void initContentView(Bundle saveInstanceState);

    /**
     * @param mReturn
     * @param mTitle
     * @param mRightFaction
     */
    public abstract void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction);

    /**
     * back按钮的功能
     *
     * @param mReturn
     */
    public void onHCReturn(TextView mReturn) {
        mReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 标题栏右边功能键功能
     *
     * @param str
     */
    public void setmRightFactionText(String str) {
        mRightFaction.setText(str);
    }

    public FrameLayout getRootView() {
        return mContentViewContainer;
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
