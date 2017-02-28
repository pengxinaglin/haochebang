package com.haoche51.checker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.FillExamReportActivity;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.item.ECheckItem;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpResponse;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;

import org.xutils.x;

/**
 * 填写评估报告基本Fragment
 * Created by wfx on 2016/6/28.
 */
public abstract class BaseReportFragment extends Fragment implements Validator.ValidationListener, HCHttpCallback {
    protected int mErrNum;
    protected CheckReportEntity mCheckReport;

    /**
     * 是否触发校验
     */
    protected boolean mIsTouchValidate;
    private View mRootView;
    private Validator mValidator;

    public void setCheckReport(CheckReportEntity checkReport) {
        this.mCheckReport = checkReport;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckReport = ((FillExamReportActivity) getActivity()).getCheckReport();
        loadDBData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = initView(inflater);
        x.view().inject(this, mRootView);
        // 初始化Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化界面
     *
     * @param inflater
     * @return
     */
    public abstract View initView(LayoutInflater inflater);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    public abstract void initData(Bundle savedInstanceState);

    /**
     * 加载数据库数据
     */
    protected void loadDBData() {
    }

    /**
     * 保存数据
     */
    public abstract void saveData();

    public void setTouchValidate(boolean touchValidate) {
        mIsTouchValidate = touchValidate;
    }


    /**
     * 校验失败
     *
     * @param failedView
     * @param failedRule
     */
    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        // 验证失败
        showErrorMsg(failedView, failedRule.getFailureMessage());
    }


    /**
     * 显示校验失败错误消息
     *
     * @param failedView
     * @param errMsg
     */
    protected void showErrorMsg(View failedView, String errMsg) {
        mErrNum++;
        if(failedView==null){
            return;
        }

        if (failedView instanceof EditText) {
            ((EditText) failedView).setError(errMsg);
            return;
        }

        if (failedView instanceof TextView) {
            ((TextView) failedView).setError(errMsg);
            return;
        }

    }

    @Override
    public void onValidationSucceeded() {

    }

    /**
     * 校验数据
     *
     * @return
     */
    public boolean validateCheck() {
        return true;
    }

    public int getErrNum() {
        return mErrNum;
    }

    public void setErrNum(int errNum) {
        this.mErrNum = errNum;
    }


    protected ECheckItem getSingleFeature(String data, boolean extra) {
        Gson mGson = new Gson();
        ECheckItem checkItem;
        if (!TextUtils.isEmpty(data)) {
            checkItem = mGson.fromJson(data, ECheckItem.class);
        } else {
            if (extra) {
                checkItem = new ECheckItem(0, 0, "", true);
            } else {
                checkItem = new ECheckItem(1, 0, "", false);
            }
        }
        return checkItem;
    }


    protected String saveSingleFeature(ECheckItem checkItem) {
        Gson mGson = new Gson();
        return mGson.toJson(checkItem);
    }


    /**
     * 改变状态的样式
     *
     * @param tv_label  文本标签
     * @param tv_status 状态
     * @param status    状态值
     */
    protected void changeStatusStyle(TextView tv_label, TextView tv_status, int status) {
        if (status != 0) {
            tv_label.setTextColor(GlobalData.mContext.getResources().getColor(R.color.self_black));
            tv_label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tv_status.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_indicator));
        } else {
            tv_label.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_self_gray_hint));
            tv_label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            tv_status.setTextColor(GlobalData.mContext.getResources().getColor(R.color.self_black));
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

    /**
     * 请求完成
     *
     * @param action
     * @param requestId
     */
    @Override
    public void onHttpFinish(String action, int requestId) {

    }
}
