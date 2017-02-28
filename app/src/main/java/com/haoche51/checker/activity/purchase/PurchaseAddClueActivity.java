package com.haoche51.checker.activity.purchase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.haoche51.checker.Checker;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.vehicle.VehicleSubBrandAddActivity;
import com.haoche51.checker.activity.vehicle.VehicleSubScribeConditionActivity;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CityInfoEntity;
import com.haoche51.checker.entity.PurchaseClueEntity;
import com.haoche51.checker.entity.VehicleSeriesEntity;
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
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加收车线索
 * Created by wufx on 2016/1/12.
 */
public class PurchaseAddClueActivity extends CommonTitleBaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {

//    public static final String INTENT_KEY_CREAT_RECYCLING = "creat_recycling";//验车任务创建收车
    /**
     * 选择车源类型
     */
//    @ViewInject(R.id.ll_clue_type)
//    private LinearLayout ll_clue_type;
    @ViewInject(R.id.rg_clue_type)
    private RadioGroup rg_clue_type;
    /**
     * 外网车源
     */
    @ViewInject(R.id.rb_outside)
    private RadioButton rb_outside;
    /**
     * 内网车源
     */
    @ViewInject(R.id.rb_inside)
    private RadioButton rb_inside;
    /**
     * 外网车源输入信息
     */
    @ViewInject(R.id.ll_outside_source)
    private LinearLayout ll_outside_source;
    /**
     * 内网车源输入信息
     */
    @ViewInject(R.id.ll_inside_source)
    private LinearLayout ll_inside_source;
    /**
     * 车源编号
     */
    @ViewInject(R.id.et_vehicle_source_id)
    private EditText et_vehicle_source_id;
    /**
     * 车主姓名
     */
    @ViewInject(R.id.ed_seller_name)
    private EditText ed_seller_name;
    /**
     * 车主电话
     */
    @ViewInject(R.id.ed_seller_phone)
    private EditText ed_seller_phone;
    /**
     * 品牌车系
     */
    @ViewInject(R.id.tv_vehicle_model)
    private TextView tv_vehicle_model;
    /**
     * 任务备注
     */
    @ViewInject(R.id.ed_remark)
    private EditText ed_remark;
    /**
     * 注释
     */
//    @ViewInject(R.id.tv_task_note)
//    private TextView tv_task_note;
    @ViewInject(R.id.spn_city)
    private Spinner spn_city;
    /**
     * 品牌车系实体类
     */
    private VehicleSeriesEntity vehicleSeries;
    //    private CheckTaskEntity mCheckTaskEntity;//验车任务详情
    private List<CityInfoEntity> cityInfoList = new ArrayList<>();

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_purchase_add_clue, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (GlobalData.userDataHelper.getPurchaseClueRight() > 2 || GlobalData.userDataHelper.getPurchaseClueRight() < 1) {
            rb_outside.setChecked(true);//内网、外网均可选择
        } else if (GlobalData.userDataHelper.getPurchaseClueRight() > 1) {
            rb_outside.setChecked(true);//只能选外网
            rb_inside.setEnabled(false);//不能选择内网
            showOutsideSource();
        } else {
            rb_inside.setChecked(true);//只能选内网
            rb_outside.setEnabled(false);//不能选择外网
            showInsideSource();
        }
        OKHttpManager.getInstance().post(HCHttpRequestParam.getOnlineCity(), this, 0);

        //监听选择车源类型
        rg_clue_type.setOnCheckedChangeListener(this);
        //监听车源id输入框焦点
        et_vehicle_source_id.setOnFocusChangeListener(this);
//        if (getIntent().hasExtra(INTENT_KEY_CREAT_RECYCLING)) {
//            //不显示选择车源类型
////            ll_clue_type.setVisibility(View.GONE);
//            mCheckTaskEntity = getIntent().getParcelableExtra(INTENT_KEY_CREAT_RECYCLING);
//            tv_task_note.setText(getString(R.string.hc_check_task_note));
//            if (mCheckTaskEntity != null) {
//                ed_seller_name.setText(mCheckTaskEntity.getSeller_name());
//                ed_seller_name.setSelection(mCheckTaskEntity.getSeller_name().length());
//                ed_seller_phone.setText(mCheckTaskEntity.getSeller_phone());
//                //从验车详情里获取品牌车系信息
//                vehicleSeries = new VehicleSeriesEntity();
//                vehicleSeries.setBrand_id(mCheckTaskEntity.getBrand_id());
//                vehicleSeries.setBrand_name(mCheckTaskEntity.getBrand_name());
//                vehicleSeries.setId(mCheckTaskEntity.getSeries_id());
//                vehicleSeries.setName(mCheckTaskEntity.getClass_name());
//
//                tv_vehicle_model.setText(vehicleSeries.getBrand_name() + " " + vehicleSeries.getName());
//            }
//        }
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
        //内网车
        if (rg_clue_type.getCheckedRadioButtonId() == R.id.rb_inside) {
            if (TextUtils.isEmpty(et_vehicle_source_id.getText().toString())) {
                onValidateFailed(et_vehicle_source_id, "车源编号不能为空");
                return;
            }
        }
        //外网车
        else {
            if (TextUtils.isEmpty(ed_seller_name.getText()) || TextUtils.isEmpty(ed_seller_name.getText().toString().trim())) {
                onValidateFailed(ed_seller_name, "车主姓名不能为空");
                return;
            }

            if (TextUtils.isEmpty(ed_seller_phone.getText()) || TextUtils.isEmpty(ed_seller_phone.getText().toString().trim())) {
                onValidateFailed(ed_seller_phone, "车主电话不能为空");
                return;
            }

            String sellerPhone = ed_seller_phone.getText().toString().trim();
            if (sellerPhone.length() != 11) {
                onValidateFailed(ed_seller_phone, "车主电话必须为11位");
                return;
            }

//            if (!PhoneNumberUtil.isPhoneNumberValid(sellerPhone)) {
//                onValidateFailed(ed_seller_phone, "车主电话不正确");
//                return;
//            }

            if (TextUtils.isEmpty(tv_vehicle_model.getText().toString()) || vehicleSeries == null) {
                ToastUtil.showInfo("品牌车系不能为空");
                return;
            }
        }

        if (TextUtils.isEmpty(ed_remark.getText().toString())) {
            onValidateFailed(ed_remark, "任务备注不能为空");
            return;
        }

        PurchaseClueEntity purchaseClue = new PurchaseClueEntity();
        Checker checker = GlobalData.userDataHelper.getChecker();
        purchaseClue.setId(checker.getId());
        purchaseClue.setName(checker.getName());
        //设置品牌和车系信息
        if (vehicleSeries != null) {
            purchaseClue.setBrand_id(vehicleSeries.getBrand_id());
            purchaseClue.setBrand_name(vehicleSeries.getBrand_name());
            purchaseClue.setClass_id(vehicleSeries.getId());
            purchaseClue.setClass_name(vehicleSeries.getName());
        }
        purchaseClue.setSeller_name(ed_seller_name.getText().toString().trim());
        purchaseClue.setSeller_phone(ed_seller_phone.getText().toString().trim());
        purchaseClue.setRemark(ed_remark.getText().toString().trim());
//        purchaseClue.setCheck_id(mCheckTaskEntity == null ? 0 : mCheckTaskEntity.getId());
        purchaseClue.setVehicle_source_id(rb_inside.isChecked() ? Integer.parseInt(et_vehicle_source_id.getText().toString().trim()) : 0);
        purchaseClue.setCity_id(cityInfoList.isEmpty() ? 0 : ((CityInfoEntity) spn_city.getSelectedItem()).getId());
        ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
        OKHttpManager.getInstance().post(HCHttpRequestParam.addPurchaseClue(purchaseClue), this, 0);
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

    /**
     * 选择车系
     */
    @Event(R.id.tv_vehicle_model)
    private void tv_vehicle_model(View v) {
        Intent intent = new Intent(this, VehicleSubBrandAddActivity.class);
        startActivityForResult(intent, TaskConstants.REQUEST_GET_BRAND_CLASS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) {
            return;
        }
        vehicleSeries = (VehicleSeriesEntity) data.getSerializableExtra(VehicleSubScribeConditionActivity.KEY_INTENT_EXTRA_SERIES);
        if (vehicleSeries != null) {
            tv_vehicle_model.setText(vehicleSeries.getBrand_name() + " " + vehicleSeries.getName());
        }
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
        } else if (HttpConstants.ACTION_GET_SOURCETITLE.equals(action)) {
            switch (response.getErrno()) {
                case 0://0：表示接口请求成功
                    if (TextUtils.isEmpty(response.getData())) {
                        tv_vehicle_model.setText("未查询到结果");
                    } else {
                        //选择了内网车源 否则不显示
                        if (rb_inside.isChecked())
                            tv_vehicle_model.setText(response.getData());
                    }
                    break;
                default://1：发生错误
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
        } else if (action.equals(HttpConstants.ACTION_GET_ONLINE_CITY)) {
            responseGetOnLineCity(response);
        }
    }


    /**
     * 返回获得在线城市
     */
    private void responseGetOnLineCity(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
//                cityInfoList = new HCJsonParse().parseOnLineCityInfos(response.getData());
                cityInfoList = JsonParseUtil.fromJsonArray(response.getData(), CityInfoEntity.class);
                if (cityInfoList != null) {
                    Checker user = GlobalData.userDataHelper.getChecker();
                    spn_city.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityInfoList));
                    int size = cityInfoList.size();
                    for (int i = 0; i < size; i++) {
                        if (user.getCity() == cityInfoList.get(i).getId()) {
                            spn_city.setSelection(i);
                            break;
                        }
                    }
                }
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (id) {
            case R.id.rb_outside://外网
                showOutsideSource();
                break;
            case R.id.rb_inside://内网
                showInsideSource();
                break;
        }
    }

    /**
     * 展示外网车源填写界面
     */
    private void showOutsideSource() {
        //隐藏内网表单 可点击选择车系
        ll_outside_source.setVisibility(View.VISIBLE);
        ll_inside_source.setVisibility(View.GONE);
        tv_vehicle_model.setClickable(true);
        tv_vehicle_model.setHint("点击选择");
    }

    /**
     * 展示内网车源
     */
    private void showInsideSource() {
        ll_inside_source.setVisibility(View.VISIBLE);
        ll_outside_source.setVisibility(View.GONE);
        tv_vehicle_model.setClickable(false);
        tv_vehicle_model.setHint("自动检索");
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            if (!rb_inside.isChecked()) return;

            if (TextUtils.isEmpty(et_vehicle_source_id.getText().toString().trim())) {
                onValidateFailed(et_vehicle_source_id, "请输入车源编号");
                return;
            }
            int sourceId = Integer.parseInt(et_vehicle_source_id.getText().toString());
            ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
            OKHttpManager.getInstance().post(HCHttpRequestParam.getVehicleSourceTitle(sourceId), this, 0);
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }
}