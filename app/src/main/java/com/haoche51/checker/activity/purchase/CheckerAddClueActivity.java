package com.haoche51.checker.activity.purchase;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.entity.PurchaseClueEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 添加收车线索
 * Created by wufx on 2016/1/12.
 */
public class CheckerAddClueActivity extends CommonTitleBaseActivity {
    public static final String INTENT_KEY_CREAT_RECYCLING = "creat_recycling";//验车任务创建收车
    /**
     * 任务备注
     */
    @ViewInject(R.id.ed_remark)
    @Required(order = 1, message = "任务备注不能为空")
    private EditText ed_remark;

    private CheckTaskEntity mCheckTaskEntity;//验车任务详情

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_checker_add_clue, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        mCheckTaskEntity = getIntent().getParcelableExtra(INTENT_KEY_CREAT_RECYCLING);
    }


    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.hc_add_purchase_clue));
    }

    /**
     * 提交收车线索
     */
    @Event(R.id.btn_commit_clue)
    private void commit(View v) {

        if (TextUtils.isEmpty(ed_remark.getText()) || TextUtils.isEmpty(ed_remark.getText().toString().trim())) {
            onValidateFailed(ed_remark, "任务备注不能为空");
            return;
        }

        PurchaseClueEntity purchaseClue = new PurchaseClueEntity();
        purchaseClue.setRemark(ed_remark.getText().toString().trim());
        purchaseClue.setCheck_id(mCheckTaskEntity == null ? 0 : mCheckTaskEntity.getId());
        ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
        OKHttpManager.getInstance().post(HCHttpRequestParam.addCheckerPurchaseClue(purchaseClue), this, 0);
    }

    /**
     * 显示校验失败消息
     *
     * @param failedView
     * @param message
     */
    private void onValidateFailed(EditText failedView, String message) {
        failedView.requestFocus();
        failedView.setError(message);
    }


    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (!isFinishing())
            ProgressDialogUtil.closeProgressDialog();

        if (HttpConstants.ACTION_ADD_BACK_TASK.equals(action)) {
            switch (response.getErrno()) {
                case 0://0：表示接口请求成功
                    ToastUtil.showInfo("添加线索成功！");
                    //刷新待跟进列表
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TaskConstants.BINDLE_ADD_ITEM, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    setResult(RESULT_OK);
                    finish();
                    break;
                default://1：发生错误
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }
}