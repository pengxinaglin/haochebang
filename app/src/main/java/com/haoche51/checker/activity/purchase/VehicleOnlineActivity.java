package com.haoche51.checker.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.haoche51.checker.Checker;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.vehicle.VehicleBrandActivity;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.entity.ReCheckTaskEntity;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 立即上架
 * Created by wufx on 2016/3/25.
 */
public class VehicleOnlineActivity extends CommonTitleBaseActivity {


    /**
     * 车主姓名
     */
    @ViewInject(R.id.ed_seller_name)
    @Required(order = 1, message = "车主姓名不能为空")
    private EditText ed_seller_name;

    /**
     * 车主电话
     */
    @ViewInject(R.id.ed_seller_phone)
    @Required(order = 2, message = "车主电话不能为空")
    private EditText ed_seller_phone;

    /**
     * 重新检测
     */
    @ViewInject(R.id.ll_recheck)
    private LinearLayout ll_recheck;

    /**
     * 重新检测
     */
    @ViewInject(R.id.rb_recheck)
    private RadioButton rb_recheck;

    /**
     * 使用旧报告
     */
    @ViewInject(R.id.rb_immediate_on)
    private RadioButton rb_immediate_on;

    /**
     * 复检时间
     */
    @ViewInject(R.id.tv_recheck_time)
    @Required(order = 1, message = "复检时间不能为空")
    private TextView tv_recheck_time;

    /**
     * 复检地点
     */
    @ViewInject(R.id.ed_recheck_place)
    private EditText ed_recheck_place;

    /**
     * 复检人员
     */
    @ViewInject(R.id.tv_recheck_person)
    private TextView tv_recheck_person;

    /**
     * 车辆款型
     */
    @ViewInject(R.id.tv_vehicle_type)
    private TextView tv_vehicle_type;

    /**
     * 车源信息，进行中详情界面传过来的
     */
    private VehicleSourceEntity vehicleSource;

    /**
     * 评估师id
     */
    private int choose_checker_id;

    /**
     * 收车任务，进行中详情界面传过来的
     */
    private PurchaseTaskEntity purchaseTask;

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_immediate_on, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        Checker checker = GlobalData.userDataHelper.getChecker();
        ed_seller_name.setText(checker.getName());
        ed_seller_phone.setText(checker.getPhone());

        purchaseTask = getIntent().getParcelableExtra("purchaseTask");
        //设置车辆款型
        if (purchaseTask != null && !TextUtils.isEmpty(purchaseTask.getTitle())) {
            if (!TextUtils.isEmpty(purchaseTask.getVehicle_name())) {
                tv_vehicle_type.setText(purchaseTask.getTitle());
            } else {
                tv_vehicle_type.setHint(purchaseTask.getTitle());
            }
        }
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.hc_vehicle_on));
    }

    /**
     * 重新检测
     */
    @Event(R.id.rb_recheck)
    private void recheck(View v) {
        ll_recheck.setVisibility(View.VISIBLE);
    }

    /**
     * 使用旧报告
     */
    @Event(R.id.rb_immediate_on)
    private void immediate_on(View v) {
        if (purchaseTask == null) {
            return;
        }

        if (purchaseTask.getIs_outside() == 1) {
            ToastUtil.showInfo("仅内网车源可使用旧报告");
            rb_immediate_on.setChecked(false);
            rb_recheck.setChecked(true);
            return;
        }

        if (purchaseTask.getOnline_now() != 1) {
            ToastUtil.showInfo(purchaseTask.getOnline_now_fail_reason());
            rb_immediate_on.setChecked(false);
            rb_recheck.setChecked(true);
            return;
        }
        ll_recheck.setVisibility(View.GONE);
    }


    /**
     * 提交上架
     */
    @Event(R.id.btn_commit_online)
    private void commitOnline(View v) {
        if (rb_recheck.isChecked()) {
            commitRecheck();
        } else if (rb_immediate_on.isChecked()) {
            commitSellerInfo();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent() == null) {
            return;
        }

        vehicleSource = getIntent().getParcelableExtra("vehicleSource");
        if (vehicleSource == null) {
            return;
        }

        String fullName = vehicleSource.getFull_name().trim();
        if (!TextUtils.isEmpty(vehicleSource.getBrand_name()) && !TextUtils.isEmpty(fullName)) {
            tv_vehicle_type.setText(fullName);
        }
    }


    /**
     * 复检时间点击事件
     */
    @Event(R.id.tv_recheck_time)
    private void clickTimeWhell(View v) {
        //初始化时间滚轮
        DisplayUtils.displayTimeWhell(this, tv_recheck_time, R.string.select_recheck_time);
    }

    /**
     * 检查复检项是否全部都填写过了
     */
    public boolean isRecheckValidate() {
        //检查车主信息是否填写
        if (!isSellerInfoValidate()) {
            return false;
        }

        //检查复检时间是否必填
        if (TextUtils.isEmpty(tv_recheck_time.getText())) {
            ToastUtil.showInfo("复检时间不能为空");
            return false;
        }

        //复检地点
        if (TextUtils.isEmpty(ed_recheck_place.getText())) {
            ToastUtil.showInfo("复检地点不能为空");
            return false;
        }

        //复检人员
        if (TextUtils.isEmpty(tv_recheck_person.getText())) {
            ToastUtil.showInfo("复检人员不能为空");
            return false;
        }

        //车辆款型
        if (TextUtils.isEmpty(tv_vehicle_type.getText())) {
            ToastUtil.showInfo("车辆款型不能为空");
            return false;
        }

        return true;
    }

    /**
     * 复检表单
     */
    public void commitRecheck() {
        //检查复检信息是否填写
        if (!isRecheckValidate()) {
            return;
        }
        ReCheckTaskEntity recheckEntity = new ReCheckTaskEntity();
        recheckEntity.setTask_id(purchaseTask.getTask_id());
        recheckEntity.setSeller_name(ed_seller_name.getText().toString().trim());
        recheckEntity.setSeller_phone(ed_seller_phone.getText().toString().trim());
        //复检时间
        String recheck_time_str = tv_recheck_time.getText().toString().trim();
        if (!TextUtils.isEmpty(recheck_time_str)) {
            recheckEntity.setRecheck_time(UnixTimeUtil.getUnixTime(recheck_time_str));
        }

        //复检地点
        recheckEntity.setRecheck_place(ed_recheck_place.getText().toString().trim());

        //复检人员
        recheckEntity.setRecheck_user_id(choose_checker_id);
        recheckEntity.setRecheck_user_name(tv_recheck_person.getText().toString().trim());

        //车辆款型
        if (vehicleSource == null) {
            vehicleSource = new VehicleSourceEntity();
            //设置品牌和车系、年款、车型
            vehicleSource.setBrand_id(purchaseTask.getBrand_id());
            vehicleSource.setBrand_name(purchaseTask.getBrand_name());
            vehicleSource.setSeries_id(purchaseTask.getClass_id());
            vehicleSource.setSeries_name(purchaseTask.getClass_name());
            vehicleSource.setYear(purchaseTask.getYear());
            vehicleSource.setModel_id(purchaseTask.getVehicle_id());
            vehicleSource.setModel_name(purchaseTask.getVehicle_name());
        }
        recheckEntity.setVehicleSource(vehicleSource);
        //显示对话框
        HCDialogUtil.showProgressDialog(this);
        //提交给服务器
        OKHttpManager.getInstance().post(HCHttpRequestParam.startReCheck(recheckEntity), this, 0);
    }

    /**
     * 检查使用旧报告信息是否都已填写
     */
    private boolean isSellerInfoValidate() {
        if (TextUtils.isEmpty(ed_seller_name.getText())) {
            onValidateFailed(ed_seller_name, "车主姓名不能为空");
            return false;
        }

        if (TextUtils.isEmpty(ed_seller_phone.getText())) {
            onValidateFailed(ed_seller_phone, "车主电话不能为空");
            return false;
        }

        String sellerPhone = ed_seller_phone.getText().toString().trim();
//    if (!PhoneNumberUtil.isPhoneNumberValid(sellerPhone)) {
//      onValidateFailed(ed_seller_phone, "车主电话不正确");
//      return false;
//    }
        if (sellerPhone.length() != 11) {
            onValidateFailed(ed_seller_phone, "车主电话不正确");
            return false;
        }

        return true;
    }

    /**
     * 提交车主信息
     */
    public void commitSellerInfo() {
        //检查车主信息是否填写
        if (!isSellerInfoValidate()) {
            return;
        }
        PurchaseTaskEntity purchaseTaskEntity = new PurchaseTaskEntity();
        purchaseTaskEntity.setTask_id(purchaseTask.getTask_id());
        purchaseTaskEntity.setSeller_name(ed_seller_name.getText().toString().trim());
        purchaseTaskEntity.setSeller_phone(ed_seller_phone.getText().toString().trim());
        //显示对话框
        HCDialogUtil.showProgressDialog(this);
        OKHttpManager.getInstance().post(HCHttpRequestParam.immediateOnline(purchaseTaskEntity), this, 0);
    }

    /**
     * 复检人员点击事件
     *
     * @param v
     */
    @Event(R.id.tv_recheck_person)
    private void getRecheckPerson(View v) {
        Intent helpCheckIntent = new Intent(VehicleOnlineActivity.this, GetRecheckPersonActivity.class);
        startActivityForResult(helpCheckIntent, TaskConstants.REQUEST_GET_RECHECK_PERSON);
    }


    /**
     * 补全车源信息
     *
     * @param view
     */
    @Event(R.id.tv_vehicle_type)
    private void completeVehicleSource(View view) {

        VehicleSourceEntity vehicleSource = new VehicleSourceEntity();
        //来源于复检界面
        vehicleSource.setJump_source(VehicleOnlineActivity.class.getName());
        Intent intent = new Intent();
        intent.putExtra("vehicleSource", vehicleSource);
        intent.setClass(this, VehicleBrandActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (TaskConstants.REQUEST_GET_RECHECK_PERSON == requestCode) {
            //获取复检人员的请求
            choose_checker_id = data.getIntExtra("choose_checker_id", 0);
            tv_recheck_person.setText(data.getStringExtra("choose_checker_name"));
        }
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
        HCDialogUtil.dismissProgressDialog();
        if (HttpConstants.ACTION_BACKTASKAPI_OLDREPORTONLINE.equals(action) || HttpConstants.ACTION_START_RECHECK.equals(action)) {
            switch (response.getErrno()) {
                case 0://0：表示接口请求成功
                    ToastUtil.showInfo("提交上架成功！");
                    finish();
                    break;
                default://1：发生错误
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
        }
    }

}