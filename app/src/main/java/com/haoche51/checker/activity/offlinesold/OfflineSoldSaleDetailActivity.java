package com.haoche51.checker.activity.offlinesold;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.OfflineSoldTaskEntity;
import com.haoche51.checker.fragment.offlinesold.OfflineSoldSaleFragment;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 线下售出——出售中、完成详情页面
 */
public class OfflineSoldSaleDetailActivity extends CommonStateActivity {

    private final int CONFIRM_TRANS = 15;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.tv_status)
    private TextView tv_status;
    @ViewInject(R.id.tv_receive_user)
    private TextView tv_receive_user;
    @ViewInject(R.id.tv_receive_user_phone)
    private TextView tv_receive_user_phone;
    @ViewInject(R.id.tv_task_num)
    private TextView tv_task_num;
    @ViewInject(R.id.tv_plate_number)
    private TextView tv_plate_number;
    @ViewInject(R.id.tv_price)
    private TextView tv_price;
    @ViewInject(R.id.tv_cheap_price)
    private TextView tv_cheap_price;
    @ViewInject(R.id.tv_plate_time)
    private TextView tv_plate_time;
    @ViewInject(R.id.tv_mile)
    private TextView tv_mile;
    @ViewInject(R.id.tv_transfer_count)
    private TextView tv_transfer_count;
    @ViewInject(R.id.btn_positive)
    private Button btn_positive;
    @ViewInject(R.id.tv_sale_method)
    private TextView tv_sale_method;
    @ViewInject(R.id.tv_vehicle_source_id)
    private TextView tv_vehicle_source_id;
    @ViewInject(R.id.tv_sale_price)
    private TextView tv_sale_price;
    @ViewInject(R.id.tv_buyer_name)
    private TextView tv_buyer_name;
    @ViewInject(R.id.tv_buyer_phone)
    private TextView tv_buyer_phone;
    @ViewInject(R.id.tv_saler)
    private TextView tv_saler;
    private int taskId;//根据任务Id去请求任务详情
    private OfflineSoldTaskEntity mTask;//根据任务Id请求的任务详情

    @Override
    protected int getContentView() {
        return R.layout.activity_offlinesold_sale_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setScreenTitle(R.string.hc_purchasetask_detail_sale_title);
        btn_positive.setVisibility(View.GONE);
        btn_positive.setText(getString(R.string.hc_offlinesold_transfer_confirm));
    }

    @Override
    protected void initData() {
        super.initData();
        //获取查询的任务id
        taskId = getIntent().getIntExtra("id", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (taskId > 0) {
            ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
            OKHttpManager.getInstance().post(HCHttpRequestParam.getBacksellapiDetail(taskId), this, 0);
        }
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        ProgressDialogUtil.closeProgressDialog();

        if (action.equals(HttpConstants.ACTION_BACKSELLAPI_GET_DETAIL)) {
            responseTask(response);
        }
    }

    /**
     * 处理请求任务详情
     */
    private void responseTask(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
//        mTask = new HCJsonParse().parseOfflineSoldTaskEntity(response.getData());
                mTask = JsonParseUtil.fromJsonObject(response.getData(), OfflineSoldTaskEntity.class);
                if (mTask != null) {
                    setData(mTask);
                }
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    /**
     * 设置UI界面数据
     */
    public void setData(OfflineSoldTaskEntity data) {
        tv_title.setText(data.getTitle());
        tv_status.setText(data.getStatus_text());
        tv_status.setVisibility(View.VISIBLE);
        tv_receive_user.setText(data.getReceive_user_name() + "（" + data.getReceive_user_role() + "）");
        tv_receive_user_phone.setText(data.getReceive_user_phone());
        tv_task_num.setText(data.getTask_num());
        tv_plate_number.setText(data.getPlate_number());
        tv_price.setText(data.getPrice() + "元");
        tv_cheap_price.setText(data.getCheap_price() + "元");
        tv_plate_time.setText(data.getPlate_time());
        tv_mile.setText(data.getMile() + "万公里");
        tv_transfer_count.setText(data.getTransfer_count() + "");
        //显示售出信息
        showSoldInfo(data);

        //售出转账待地收确认 此状态外不显示确认转账按钮
        if (TaskConstants.FINANCE_STATUS_SOLD_WAIT_TRANS == mTask.getStatus()) {
            btn_positive.setVisibility(View.VISIBLE);
        }


//    if (!TextUtils.isEmpty(data.getSold_type()) && data.getSold_type().toUpperCase().contains("C2C")) {
//      return;
//    }
    }

    /**
     * 显示出售信息
     */
    private void showSoldInfo(OfflineSoldTaskEntity soldTaskEntity) {
        tv_sale_method.setText(soldTaskEntity.getSold_type());
        tv_vehicle_source_id.setText(String.valueOf(soldTaskEntity.getVehicle_source_id()));
        tv_sale_price.setText(String.valueOf(soldTaskEntity.getSold_price()).concat("元"));
        tv_buyer_name.setText(soldTaskEntity.getBuyer_name());
        tv_buyer_phone.setText(soldTaskEntity.getBuyer_phone());
        tv_saler.setText(soldTaskEntity.getSaler_name());
    }

    /**
     * 确定转账
     */
    @Event(R.id.btn_positive)
    private void btn_positive(View v) {
        if (mTask == null) return;
        Intent intent = new Intent(this, OfflineSoldConfirmTransferActivity.class);
        intent.putExtra("stockId", mTask.getStock_id());
        startActivityForResult(intent, CONFIRM_TRANS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONFIRM_TRANS:
                    btn_positive.setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(OfflineSoldSaleFragment.BINDLE_CONFIRM_TRANS, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
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