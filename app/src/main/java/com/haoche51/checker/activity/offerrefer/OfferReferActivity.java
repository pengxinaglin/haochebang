package com.haoche51.checker.activity.offerrefer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.Checker;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.vehicle.VehicleBrandActivity;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.OfferReferEntity;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 报价参考
 * Created by wfx on 2016/8/2.
 */
public class OfferReferActivity extends CommonTitleBaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_city)
    @Required(order = 1, message = "城市不能为空")
    private TextView tv_city;

    @ViewInject(R.id.tv_vehicle_type)
    @Required(order = 2, message = "车辆来源不能为空")
    private TextView tv_vehicle_type;

    @ViewInject(R.id.tv_first_board)
    @Required(order = 3, message = "首次上牌时间不能为空")
    private TextView tv_first_board;

    @ViewInject(R.id.ed_show_mile)
    @Required(order = 4, message = "表显里程不能为空")
    private EditText ed_show_mile;

    /**
     * 车源信息
     */
    private VehicleSourceEntity vehicleSource;

    @ViewInject(R.id.btn_positive)
    private Button btn_positive;

    private int city_id;
    private String fullName;
    private OfferReferEntity offerEntity;

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_offer_refer, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        Checker checker = GlobalData.userDataHelper.getChecker();
        city_id = checker.getCity();
        tv_city.setText(checker.getCity_name());
        tv_city.setOnClickListener(this);
        tv_vehicle_type.setOnClickListener(this);
        tv_first_board.setOnClickListener(this);
        btn_positive.setOnClickListener(this);
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getResources().getString(R.string.hc_offer_refer));
        btn_positive.setText(getResources().getString(R.string.hc_query_submit));
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

        fullName = vehicleSource.getFull_name().trim();
        if (!TextUtils.isEmpty(vehicleSource.getBrand_name()) && !TextUtils.isEmpty(fullName)) {
            tv_vehicle_type.setText(fullName);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_city:
                chooseCity();
                break;
            case R.id.tv_vehicle_type:
                chooseVehicleSource();
                break;
            case R.id.tv_first_board:
                chooseBoardDate();
                break;
            case R.id.btn_positive:
                commit();
                break;
        }
    }

    /**
     * 选择城市
     */
    private void chooseCity() {
        Intent intent = new Intent(this, GetCityActivity.class);
        startActivityForResult(intent, 100);
    }

    /**
     * 选择车辆来源
     */
    private void chooseVehicleSource() {
        VehicleSourceEntity vehicleSource = new VehicleSourceEntity();
        vehicleSource.setJump_source(OfferReferActivity.class.getName());
        Intent intent = new Intent();
        intent.putExtra("vehicleSource", vehicleSource);
        intent.setClass(this, VehicleBrandActivity.class);
        startActivity(intent);
    }

    /**
     * 选择上牌日期
     */
    private void chooseBoardDate() {
        //初始化时间滚轮
        DisplayUtils.displayYearAndMonthWhellNoControl(this, tv_first_board, R.string.select_regist_time);
    }

    /**
     * 提交
     */
    private void commit() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        offerEntity = new OfferReferEntity();
        if (vehicleSource != null) {
            offerEntity.setBrand_id(vehicleSource.getBrand_id());
            offerEntity.setClass_id(vehicleSource.getSeries_id());
            offerEntity.setModel_id(vehicleSource.getModel_id());
            offerEntity.setVehicle_year(vehicleSource.getYear());
        }
        offerEntity.setCity_id(city_id);
        offerEntity.setMile(Float.valueOf(ed_show_mile.getText().toString()));
        offerEntity.setPlate_time(UnixTimeUtil.getUnixTime(tv_first_board.getText().toString(), UnixTimeUtil.YEAR_MONTH_PATTERN));
        ProgressDialogUtil.showProgressDialog(this, "请稍候...");
        OKHttpManager.getInstance().post(HCHttpRequestParam.offerReffer(offerEntity), this, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            tv_city.setText(data.getStringExtra("city_name"));
            city_id = data.getIntExtra("city_id", 0);
        }
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        super.onHttpComplete(action, requestId, response, error);
        ProgressDialogUtil.closeProgressDialog();
        if (HttpConstants.ACTION_OFFER_REFFER.equals(action)) {
            switch (response.getErrno()) {
                case 0:
//                    OfferReferEntity offerReferEntity = new HCJsonParse().parseOfferReffer(response.getData());
                    OfferReferEntity offerReferEntity = JsonParseUtil.fromJsonObject(response.getData(), OfferReferEntity.class);
                    if(offerReferEntity==null){
                        return;
                    }
                    offerReferEntity.setVehicle_source(fullName);
                    if (offerEntity != null) {
                        offerReferEntity.setBrand_id(offerEntity.getBrand_id());
                        offerReferEntity.setClass_id(offerEntity.getClass_id());
                        offerReferEntity.setModel_id(offerEntity.getModel_id());
                        offerReferEntity.setVehicle_year(offerEntity.getVehicle_year());
                        offerReferEntity.setMile(offerEntity.getMile());
                        offerReferEntity.setPlate_time(offerEntity.getPlate_time());
                    }
                    Intent intent = new Intent(this, MarketConditionActivity.class);
                    intent.putExtra(TaskConstants.BINDLE_MARKET_CONDITION, offerReferEntity);
                    startActivity(intent);
                    break;
                default:
                    ToastUtil.showInfo("查询报价失败：" + response.getErrmsg());
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
