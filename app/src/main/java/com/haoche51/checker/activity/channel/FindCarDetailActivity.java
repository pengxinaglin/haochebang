package com.haoche51.checker.activity.channel;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.FindCarEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;


public class FindCarDetailActivity extends CommonStateActivity implements View.OnClickListener {


    @ViewInject(R.id.tv_buyer_name)
    private TextView tv_buyer_name;

    @ViewInject(R.id.tv_buyer_phone)
    private TextView tv_buyer_phone;

    @ViewInject(R.id.tv_status)
    private TextView tv_status;

    @ViewInject(R.id.tv_vehicle)
    private TextView tv_vehicle;

    @ViewInject(R.id.tv_price)
    private TextView tv_price;

    @ViewInject(R.id.tv_age)
    private TextView tv_age;

    @ViewInject(R.id.tv_gearbox)
    private TextView tv_gearbox;

    @ViewInject(R.id.tv_color)
    private TextView tv_color;

    @ViewInject(R.id.tv_other)
    private TextView tv_other;

    @ViewInject(R.id.tv_e_saler)
    private TextView tv_e_saler;

    @ViewInject(R.id.tv_saler_phone)
    private TextView tv_saler_phone;

    @ViewInject(R.id.btn_negative)
    private Button btn_negative;

    @ViewInject(R.id.btn_positive)
    private Button btn_positive;

    private FindCarEntity mFindCarEntity;
    private boolean isAppointedClicked;

    @Override
    protected int getContentView() {
        return R.layout.activity_find_car_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setScreenTitle(R.string.hc_channel_find_car);
        mFindCarEntity = getIntent().getParcelableExtra(TaskConstants.BINDLE_FIND_CAR);
        setData();
    }

    /**
     * 设置UI界面数据
     */
    public void setData() {
        if (mFindCarEntity == null) {
            return;
        }

        //待匹配
        if (mFindCarEntity.getStatus() == TaskConstants.FIND_CAR_STATUS_WAIT_MATCH) {
            btn_negative.setVisibility(View.VISIBLE);
            btn_negative.setText(R.string.no_match);
            btn_positive.setVisibility(View.VISIBLE);
            btn_positive.setText(R.string.take_look);
            //已匹配
        } else if (mFindCarEntity.getStatus() == TaskConstants.FIND_CAR_STATUS_MATCHED) {
            btn_positive.setVisibility(View.VISIBLE);
            btn_positive.setText(R.string.take_look);
        }

        btn_negative.setOnClickListener(this);
        btn_positive.setOnClickListener(this);
        tv_buyer_name.setText(mFindCarEntity.getBuyer_name());
        tv_buyer_phone.setText(mFindCarEntity.getBuyer_phone());
        tv_status.setText(mFindCarEntity.getStatus_text());
        tv_vehicle.setText(mFindCarEntity.getVehicle_text());
        tv_price.setText(mFindCarEntity.getPrice_text());
        tv_age.setText(mFindCarEntity.getAge_text());
        tv_gearbox.setText(mFindCarEntity.getGearbox_text());
        tv_color.setText(mFindCarEntity.getColor_text());
        tv_other.setText(mFindCarEntity.getOther_text());
        tv_e_saler.setText(mFindCarEntity.getCustom_service_name());
        tv_saler_phone.setText(mFindCarEntity.getCustom_service_phone());
    }

    @Override
    public void onClick(View v) {
        if (mFindCarEntity == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_negative:
                ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
                OKHttpManager.getInstance().post(HCHttpRequestParam.unMatch(mFindCarEntity.getRequest_id()), this, 0);
                break;
            case R.id.btn_positive:
                if (isAppointedClicked) {
                    ToastUtil.showInfo("请前往卖车神器看车任务中处理");
                    return;
                }
                ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
                OKHttpManager.getInstance().post(HCHttpRequestParam.makeAppointment(mFindCarEntity.getRequest_id()), this, 0);
                break;
        }
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (!isFinishing())
            ProgressDialogUtil.closeProgressDialog();
        if (HttpConstants.ACTION_MAKE_APPOINTMENT.equals(action)) {
            responseMakeAppoint(response);
        } else if (HttpConstants.ACTION_UN_MATCH.equals(action)) {
            responseNoMatch(response);
        }


    }

    /**
     * 处理无法匹配
     *
     * @param response
     */
    private void responseNoMatch(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                ToastUtil.showInfo("操作成功！");
                tv_status.setText(R.string.no_match);
                btn_negative.setVisibility(View.GONE);
                btn_positive.setVisibility(View.GONE);
                EventBus.getDefault().post(mFindCarEntity);
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    /**
     * 处理创建带看
     *
     * @param response
     */
    private void responseMakeAppoint(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                ToastUtil.showInfo("创建带看成功！");
                btn_negative.setAlpha(0.5f);
                isAppointedClicked = true;
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }


}
