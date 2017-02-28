package com.haoche51.checker.activity.channel;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.haoche51.checker.CheckerApplication;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.CheckMainActivity;
import com.haoche51.checker.activity.vehicle.VehicleBrandActivity;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.adapter.TempTaskAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.TempTaskEntity;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.entity.transaction.ChannelVehicleSourceEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PengXianglin on 16/3/1.
 * 添加车源
 */
public class AddVehicleSourceActivity extends CommonStateActivity implements OnGetSuggestionResultListener {

    @ViewInject(R.id.tv_vehicle_model)
    private TextView tv_vehicle_model;//车型

    @ViewInject(R.id.et_vin)
//	@Required(order = 1, message = "VIN码不能为空")
    private EditText et_vin;//VIN码

    @ViewInject(R.id.et_seller_name)
    @Required(order = 1, message = "车主姓名不能为空")
    private EditText et_seller_name;//车主姓名

    @ViewInject(R.id.et_seller_phone)
    @Required(order = 2, message = "车主电话不能为空")
    private EditText et_seller_phone;//车主电话

    @ViewInject(R.id.et_appoint_time)
    @Required(order = 3, message = "验车时间不能为空")
    private TextView et_appoint_time;//验车时间

    @ViewInject(R.id.act_appoint_place)
    private AutoCompleteTextView act_appoint_place;//验车地点

    @ViewInject(R.id.btn_positive)
    private Button btn_positive;//保存

    private int dealer_id;

    /**
     * 品牌车系实体类
     */
    private VehicleSourceEntity mVehicleSource;
    private SuggestionSearch mSuggestionSearch;
    /**
     * 当前位置
     */
    private BDLocation mLocation;
    private TempTaskAdapter tempTaskAdapter;
    private TempTaskEntity mTempTaskEnty;
    private List<TempTaskEntity> tempTaskEntityList;
    private MyTextWatcher myTextWatcher;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_vehicle_source;
    }

    @Override
    protected void initView() {
        super.initView();
        setScreenTitle(R.string.hc_add_vhicle_source_title);
        et_appoint_time.setText(UnixTimeUtil.formatYearMonthDay((int) (System.currentTimeMillis() / 1000L)));
        btn_positive.setText(getString(R.string.hc_common_save));
        //初始化建议查询
        initSuggestion();
        //初始化自动完成文本框
        initAutoTextView();
    }

    @Override
    protected void initData() {
        dealer_id = getIntent().getIntExtra("dealer_id", 0);
        String name = GlobalData.userDataHelper.getChecker().getName();
        et_seller_name.setText(TextUtils.isEmpty(name) ? "" : name);
        String phone = GlobalData.userDataHelper.getChecker().getPhone();
        et_seller_phone.setText(TextUtils.isEmpty(phone) ? "" : phone);
//        et_vin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    if (!TextUtils.isEmpty(et_vin.getText())) {
//                        String vin_code = et_vin.getText().toString();
//                        if (vin_code.length() != 17) {
//                            et_vin.setError("VIN码必须为17位！");
//                            return;
//                        }
//                        ProgressDialogUtil.showProgressDialog(AddVehicleSourceActivity.this, getString(R.string.later));
//                        OKHttpManager.getInstance().post(HCHttpRequestParam.validVIN(vin_code), AddVehicleSourceActivity.this, 0);
//                    }
//                    et_vin.setError(null);
//                }
//            }
//        });

        et_vin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(et_vin.getText())) {
                    String vin_code = et_vin.getText().toString();
                    if (vin_code.length() != 17) {
                        et_vin.setError("VIN码必须为17位！");
                        return;
                    }
                    OKHttpManager.getInstance().post(HCHttpRequestParam.validVIN(vin_code), AddVehicleSourceActivity.this, 0);
                }
                et_vin.setError(null);

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent() == null) {
            return;
        }

        mVehicleSource = getIntent().getParcelableExtra("vehicleSource");
        if (mVehicleSource == null) {
            return;
        }

        String fullName = mVehicleSource.getFull_name().trim();
        if (!TextUtils.isEmpty(mVehicleSource.getBrand_name()) && !TextUtils.isEmpty(fullName)) {
            tv_vehicle_model.setText(fullName);
        }
    }

    /**
     * 初始化建议查询
     */
    private void initSuggestion() {
        //第一步，创建在线建议查询实例；
        mSuggestionSearch = SuggestionSearch.newInstance();

        //第三步，设置在线建议查询监听者
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }


    /**
     * 初始化自动完成文本框
     */
    private void initAutoTextView() {
        mLocation = ((CheckerApplication) getApplicationContext()).getLocation();
        tempTaskEntityList = new ArrayList<>();
        tempTaskAdapter = new TempTaskAdapter(this, R.layout.item_location_popup, tempTaskEntityList);
        act_appoint_place.setAdapter(tempTaskAdapter);

        //设置文本变化监听器
        myTextWatcher = new MyTextWatcher();
        act_appoint_place.addTextChangedListener(myTextWatcher);
        act_appoint_place.setThreshold(1);
        act_appoint_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tempTaskEntityList == null || tempTaskEntityList.size() == 0) {
                    return;
                }
                mTempTaskEnty = tempTaskEntityList.get(position);
                StringBuilder sb = new StringBuilder();
                sb.append(TextUtils.isEmpty(mTempTaskEnty.getCity()) ? "" : mTempTaskEnty.getCity()).append(mTempTaskEnty.getKey());
                act_appoint_place.setText(sb.toString());
                //调整光标位置
                act_appoint_place.setSelection(sb.toString().length());
            }
        });
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        //未找到相关结果
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        //每次提供数据源的时候 都要清除上一次搜索的结果
        tempTaskEntityList.clear();
        //循环读取结果 并且添加到 adapter中
        TempTaskEntity tempTask;
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null && info.pt != null) {
                tempTask = new TempTaskEntity();
                if (info.city != null) {
                    tempTask.setCity(info.city);
                }
                if (info.district != null) {
                    tempTask.setCity(tempTask.getCity().concat(info.district));
                }
                tempTask.setKey(info.key);
                tempTask.setLatitude((float) info.pt.latitude);
                tempTask.setLongitude((float) info.pt.longitude);
                tempTaskEntityList.add(tempTask);
            }
        }
        tempTaskAdapter = new TempTaskAdapter(this, R.layout.item_location_popup, tempTaskEntityList);
        act_appoint_place.setAdapter(tempTaskAdapter);
        tempTaskAdapter.notifyDataSetChanged();
    }

    /**
     * 选择车型
     */
    @Event(R.id.tv_vehicle_model)
    private void chooseModelEvent(View v) {
        VehicleSourceEntity vehicleSource = new VehicleSourceEntity();
        //来源于添加车源页面
        vehicleSource.setJump_source(AddVehicleSourceActivity.class.getName());
        Intent intent = new Intent(this, VehicleBrandActivity.class);
        intent.putExtra("vehicleSource", vehicleSource);
        startActivity(intent);
    }

    /**
     * 选择时间
     */
    @Event(R.id.et_appoint_time)
    private void chooseTimeEvent(View v) {
        DisplayUtils.displayTimeWhellYMD(this, et_appoint_time, R.string.select_time);
    }

    /**
     * 保存
     */
    @Event(R.id.btn_positive)
    private void btn_positive(View v) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        isDataValid();
    }

    /**
     * 数据是否有效
     */
    private void isDataValid() {

        if (dealer_id == 0) {
            ToastUtil.showInfo("缺少参数商家ID");
            return;
        }
        if (mVehicleSource == null) {
            ToastUtil.showInfo("请选择车辆款型");
            return;
        }

        if (mTempTaskEnty == null) {
            onValidateFailed(act_appoint_place, "请选择百度提示的地址");
            return;
        }

        String vin_code = "";
        if (!TextUtils.isEmpty(et_vin.getText())) {
            if (et_vin.getText().toString().length() != 17) {
                onValidateFailed(et_vin, "VIN码必须为17位！");
                return;
            }
            vin_code = et_vin.getText().toString();
        }

        String seller_name = et_seller_name.getText().toString();
        String seller_phone = et_seller_phone.getText().toString();
        String appoint_time = et_appoint_time.getText().toString();

        if (TextUtils.isEmpty(seller_phone)) {
            ToastUtil.showInfo("请输入车主联系方式");
            return;
        }
//        if (!PhoneNumberUtil.isPhoneNumberValid(seller_phone)) {
//            ToastUtil.showInfo("车主联系方式号码格式不正确");
//            return;
//        }

        if (seller_phone.length()!=11) {
            ToastUtil.showInfo("车主联系电话必须为11位");
            return;
        }

        String appoint_place = act_appoint_place.getText().toString().trim();
        double place_lat = mTempTaskEnty.getLatitude();
        double place_lng = mTempTaskEnty.getLongitude();

        ChannelVehicleSourceEntity vehicleSourceEntity = new ChannelVehicleSourceEntity();
        vehicleSourceEntity.setDealer_id(dealer_id);
        vehicleSourceEntity.setBrand_id(mVehicleSource.getBrand_id());
        vehicleSourceEntity.setBrand_name(mVehicleSource.getBrand_name());
        vehicleSourceEntity.setClass_id(mVehicleSource.getSeries_id());
        vehicleSourceEntity.setClass_name(mVehicleSource.getSeries_name());
        vehicleSourceEntity.setVehicle_id(mVehicleSource.getModel_id());
        vehicleSourceEntity.setVehicle_name(mVehicleSource.getModel_name());
        vehicleSourceEntity.setYear(mVehicleSource.getYear());
        vehicleSourceEntity.setSeller_name(seller_name);
        vehicleSourceEntity.setSeller_phone(seller_phone);
        vehicleSourceEntity.setAppoint_time(appoint_time);
        vehicleSourceEntity.setAppoint_place(appoint_place);
        vehicleSourceEntity.setPlace_lat(place_lat);
        vehicleSourceEntity.setPlace_lng(place_lng);
        vehicleSourceEntity.setVin_code(vin_code);

        ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
        OKHttpManager.getInstance().post(HCHttpRequestParam.addVehicleSource(vehicleSourceEntity), this, 0);
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

        if (HttpConstants.ACTION_DEALCARAPI_ADD.equals(action)) {
            responseAddVehicleSource(response);
        } else if (HttpConstants.ACTION_VALID_VIN.equals(action)) {
            responseValidVin(response);
        }
    }

    /**
     * 处理发起添加车源信息请求
     */
    private void responseAddVehicleSource(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                Intent intent = new Intent(this, CheckMainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    /**
     * 处理校验VIN码的请求
     */
    private void responseValidVin(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                if (!TaskConstants.RESPONSE_VIN_VALID.equals(response.getData())) {
                    et_vin.setError("VIN码与在线车源重复！");
                }else{
                    et_vin.setError(null);
                }
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        //第五步，释放在线建议查询实例；
        if (mSuggestionSearch != null) {
            mSuggestionSearch.destroy();
        }
        super.onDestroy();
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) {
                return;
            }

            if (mLocation == null || TextUtils.isEmpty(mLocation.getCity())) {
                return;
            }
            //第四步，发起在线建议查询；
            // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
            mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                    .keyword(s.toString())
                    .city(mLocation.getCity())
            );
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
