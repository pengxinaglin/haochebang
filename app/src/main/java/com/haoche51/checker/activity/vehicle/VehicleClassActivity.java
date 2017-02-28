package com.haoche51.checker.activity.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.VehicleSubscribeSeriesListAdapter;
import com.haoche51.checker.entity.VehicleSeriesEntity;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.helper.BrandLogoHelper;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;


import java.util.List;

import okhttp3.Call;

/**
 * 车系界面
 * Created by wufx on 2016/1/21.
 */
public class VehicleClassActivity extends CommonTitleBaseActivity implements AdapterView.OnItemClickListener {
  /**
   * 品牌名称TextView
   */
  private TextView mBrandNameTv;
  /**
   * 品牌图标
   */
  private ImageView mBrandLogoIv;
  private VehicleSubscribeSeriesListAdapter seriesListAdapter;
  private ListView mListView;
  private List<VehicleSeriesEntity> seriesList = null;
  /**
   * 品牌id
   */

  private VehicleSourceEntity vehicleSource;

  private Call mCall;

  @Override
  public View getHCContentView() {
    View view = View.inflate(this, R.layout.activity_vehicle_class, null);
    mListView = (ListView) view.findViewById(R.id.lv_vehicle_subscribe_series);
    mListView.setVerticalScrollBarEnabled(true);
    mListView.setOnItemClickListener(this);
    mBrandNameTv = (TextView) view.findViewById(R.id.tv_brand_name);
    mBrandLogoIv = (ImageView) view.findViewById(R.id.iv_barnd_logo);
    return view;
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {
  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.hc_choose_class));
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (getIntent() == null) {
      return;
    }
    vehicleSource = getIntent().getParcelableExtra("vehicleSource");
    if (vehicleSource == null) {
      return;
    }
    mBrandNameTv.setText(vehicleSource.getBrand_name());
    boolean isContains = BrandLogoHelper.BRAND_LOGO.containsKey(vehicleSource.getBrand_id());
    int resId = isContains ? BrandLogoHelper.BRAND_LOGO.get(vehicleSource.getBrand_id()) : R.drawable.b100;
    mBrandLogoIv.setBackgroundResource(resId);
    //请求网络设置备注
    ProgressDialogUtil.showProgressDialog(VehicleClassActivity.this, getString(R.string.later));
    mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getClassByBrand(vehicleSource.getBrand_id()), this, 0);
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (seriesList == null || seriesList.size() == 0) {
      return;
    }
    VehicleSeriesEntity seriesEntity = seriesList.get(position);
    vehicleSource.setSeries_id(seriesEntity.getId());
    vehicleSource.setSeries_name(seriesEntity.getName());
    Intent intent = new Intent(this, VehicleYearActivity.class);
    intent.putExtra("vehicleSource", vehicleSource);
    startActivity(intent);
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    ProgressDialogUtil.closeProgressDialog();
    if (HttpConstants.ACTION_GET_ALL_CLASS_BY_BRAND.equals(action)) {
      switch (response.getErrno()) {
        case 0://0：表示接口请求成功
          doRequestSuccess(response.getData());
          break;
        default://非0：发生错误
          ToastUtil.showInfo(response.getErrmsg());
          break;
      }
    }
  }


  /**
   * 请求成功的处理
   *
   * @param jsonStr json串
   */
  private void doRequestSuccess(String jsonStr) {
//    seriesList = new HCJsonParse().parseVehicleSeriesList(jsonStr);
    seriesList = JsonParseUtil.fromJsonArray(jsonStr, VehicleSeriesEntity.class);
    if (seriesList == null) {
      return;
    }

    if (seriesListAdapter == null) {
      seriesListAdapter = new VehicleSubscribeSeriesListAdapter(this, seriesList, R.layout.vehicle_subcribe_series_list_item_layout);
      mListView.setAdapter(seriesListAdapter);
    } else {
      seriesListAdapter.setmList(seriesList);
      seriesListAdapter.notifyDataSetChanged();
    }
  }


  @Override
  protected void onDestroy() {
    ProgressDialogUtil.closeProgressDialog();
    //如果请求还没完成，界面就被关闭了，那么就取消请求
    OKHttpManager.getInstance().cancelRequest(mCall);
    super.onDestroy();
  }
}
