package com.haoche51.checker.fragment.channel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.channel.ChannelMainActivity;
import com.haoche51.checker.activity.channel.UpdateMaintainerActivity;
import com.haoche51.checker.activity.vehicle.VehicleSubBrandAddActivity;
import com.haoche51.checker.activity.vehicle.VehicleSubScribeConditionActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.LocalCheckerEntity;
import com.haoche51.checker.entity.VehicleSeriesEntity;
import com.haoche51.checker.fragment.CommonBaseFragment;


/**
 * 渠寄筛选
 * Created by PengXianglin on 16/4/13.
 */
public class FilterFragment extends CommonBaseFragment implements View.OnClickListener {

    /**
     * 设置是否只筛选维护人
     */
    boolean onlyFilterMD = false;
    private TextView tv_clear;//清理结果
    private TextView tv_dofilter;//确定筛选
    private LinearLayout ll_brand;//品牌车系
    private TextView tv_brand_name;//展示品牌车系
    private LinearLayout ll_maintenance;//维护人
    private TextView tv_maintenance_name;//展示维护人
    private LocalCheckerEntity maintenancePersonnel;//筛选维护人
    private VehicleSeriesEntity vehicleSeries;//筛选品牌车系
    private OnBackClickListener mOnBackClickListener;

    public FilterFragment() {
    }

    public FilterFragment(OnBackClickListener mOnBackClickListener) {
        super();
        this.mOnBackClickListener = mOnBackClickListener;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_channel_filter;
    }

    @Override
    protected void initView(View view) {
        tv_clear = (TextView) view.findViewById(R.id.tv_clear);
        tv_dofilter = (TextView) view.findViewById(R.id.tv_dofilter);
        tv_brand_name = (TextView) view.findViewById(R.id.tv_brand_name);
        ll_brand = (LinearLayout) view.findViewById(R.id.ll_brand);
        ll_maintenance = (LinearLayout) view.findViewById(R.id.ll_maintenance);
        tv_maintenance_name = (TextView) view.findViewById(R.id.tv_maintenance_name);

        //设置点击
        tv_clear.setOnClickListener(this);
        tv_dofilter.setOnClickListener(this);
        ll_brand.setOnClickListener(this);
        ll_maintenance.setOnClickListener(this);
        view.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();
        ll_brand.setVisibility(onlyFilterMD ? View.GONE : View.VISIBLE);
    }

    public void setOnlyFilterMerchantDetail(boolean flag) {
        this.onlyFilterMD = flag;
        if (ll_brand != null)
            ll_brand.setVisibility(onlyFilterMD ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                clearFilter(view);
                break;
            case R.id.tv_dofilter:
                doFilter(view);
                break;
            case R.id.ll_brand:
                chooseBrand(view);
                break;
            case R.id.ll_maintenance:
                chooseMaintenance(view);
                break;
        }
    }

    /**
     * 清理结果
     */
    private void clearFilter(View v) {
        if (this.mOnBackClickListener != null)
            mOnBackClickListener.onbackClick(null);

        maintenancePersonnel = null;
        vehicleSeries = null;
        tv_brand_name.setText("");
        tv_maintenance_name.setText("");
    }

    public void setBrand(VehicleSeriesEntity seriesEntity) {
        this.vehicleSeries = seriesEntity;
        if (tv_brand_name != null)
            tv_brand_name.setText(this.vehicleSeries == null ? "" : vehicleSeries.getBrand_name() + " " + vehicleSeries.getName());
    }

    public void setMaintenance(LocalCheckerEntity maintenancePersonnel) {
        this.maintenancePersonnel = maintenancePersonnel;
        if (tv_maintenance_name != null)
            tv_maintenance_name.setText(this.maintenancePersonnel == null ? "" : maintenancePersonnel.getName());
    }

    /**
     * 确定搜索
     */
    private void doFilter(View v) {
        if (this.mOnBackClickListener != null) {
            Bundle data = new Bundle();
            data.putSerializable(ChannelMainActivity.BUNDLE_CHOOSE_MAINTENANCE_FILTER, maintenancePersonnel);
            data.putSerializable(VehicleSourceFragment.BUNDLE_CHOOSE_VEHICLE_FILTER, vehicleSeries);
            mOnBackClickListener.onbackClick(data);
        }
    }

    /**
     * 选择品牌
     */
    private void chooseBrand(View v) {
        Intent intent = new Intent(getActivity(), VehicleSubBrandAddActivity.class);
        //传递车系不限
        intent.putExtra(TaskConstants.BINDLE_UNLIMITED_CLASS, true);
        getActivity().startActivityForResult(intent, TaskConstants.REQUEST_GET_BRAND_CLASS);
    }

    /**
     * 选择维护人
     */
    private void chooseMaintenance(View v) {
        Intent intent = new Intent(getActivity(), UpdateMaintainerActivity.class);
        //传递只选择人员
        intent.putExtra(UpdateMaintainerActivity.INTENT_KEY_ONLY_CHOOSE_MAINTAINER, true);
        getActivity().startActivityForResult(intent, TaskConstants.REQUEST_UPDATE_MERCHANT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case TaskConstants.REQUEST_GET_BRAND_CLASS:
                //读取选择的车系
                vehicleSeries = (VehicleSeriesEntity) data.getSerializableExtra(VehicleSubScribeConditionActivity.KEY_INTENT_EXTRA_SERIES);
                if (vehicleSeries != null) {
                    tv_brand_name.setText(vehicleSeries.getBrand_name() + " " + vehicleSeries.getName());
                }
                break;
            case TaskConstants.REQUEST_UPDATE_MERCHANT:
                //读取选择的维护人
                maintenancePersonnel = (LocalCheckerEntity) data.getSerializableExtra(UpdateMaintainerActivity.KEY_INTENT_EXTRA_MAINTAINER);
                if (maintenancePersonnel != null) {
                    tv_maintenance_name.setText(maintenancePersonnel.getName());
                }
                break;
        }
    }

    /**
     * 设置关闭监听
     */
    public interface OnBackClickListener {
        void onbackClick(Bundle bundle);
    }
}