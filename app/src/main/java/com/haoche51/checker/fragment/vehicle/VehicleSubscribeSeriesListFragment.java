package com.haoche51.checker.fragment.vehicle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.adapter.VehicleSubscribeSeriesListAdapter;
import com.haoche51.checker.entity.VehicleSeriesEntity;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.helper.BrandLogoHelper;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 车系列表
 * Created by yangming on 2015/11/2.
 */
public class VehicleSubscribeSeriesListFragment extends CommonBaseFragment implements View.OnClickListener {

  private FragmentActivity fragmentActivity;
  private VehicleSubscribeSeriesListAdapter seriesListAdapter;
  private ListView mListView;
  private List<VehicleSeriesEntity> seriesList = new ArrayList<>();
  private int brandId;
  private String brandName;
  private OnSeriesListItemClickListener onSeriesListItemClickListener;
  private OnBackClickListener onBackClickListener;

  private TextView mBackTv;
  private TextView mForClickTv;
  private ImageView mLogoIv;
  private View mCarseriesParent;
  private View mCarseriesLine;

  private Call mCall;

  private boolean unlimited_class = false;//标识车系不限

  public interface OnSeriesListItemClickListener {
    void onSeriesClick(VehicleSeriesEntity seriesEntity);
  }

  public interface OnBackClickListener {
    void onbackClick();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      onSeriesListItemClickListener = (OnSeriesListItemClickListener) activity;
      onBackClickListener = (OnBackClickListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
    }
  }


  public static VehicleSubscribeSeriesListFragment newInstance(FragmentActivity fragmentActivity, int brandId, String brandName, boolean unlimited_class) {
    VehicleSubscribeSeriesListFragment fragment = new VehicleSubscribeSeriesListFragment();
    fragment.setFragmentActivity(fragmentActivity);
    fragment.setBrandId(brandId);
    fragment.setBrandName(brandName);
    fragment.setUnlimited_class(unlimited_class);
    return fragment;
  }

  public VehicleSubscribeSeriesListFragment() {
  }

  public void setFragmentActivity(FragmentActivity fragmentActivity) {
    this.fragmentActivity = fragmentActivity;
  }

  public void setBrandId(int brandId) {
    this.brandId = brandId;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public void setUnlimited_class(boolean unlimited_class) {
    this.unlimited_class = unlimited_class;
  }

  @Override
  protected int getContentView() {
    return R.layout.vehicle_subscribe_series_list_fragment;
  }

  @Override
  protected void initView(View view) {
    super.initView(view);
    mListView = (ListView) view.findViewById(R.id.lv_vehicle_subscribe_series);
    mListView.setVerticalScrollBarEnabled(true);

    mBackTv = (TextView) view.findViewById(R.id.tv_carseries_back);
    mForClickTv = (TextView) view.findViewById(R.id.tv_carseries_for_click);
    mLogoIv = (ImageView) view.findViewById(R.id.iv_carseries_logo);
    mCarseriesParent = view.findViewById(R.id.rel_carseries_parent);
    mCarseriesLine = view.findViewById(R.id.line_carseries);

    mBackTv.setOnClickListener(this);
    mForClickTv.setOnClickListener(this);

    boolean isContains = BrandLogoHelper.BRAND_LOGO.containsKey(brandId);
    int resId = isContains ? BrandLogoHelper.BRAND_LOGO.get(brandId) : R.drawable.b100;
    mLogoIv.setImageResource(resId);

  }

  @Override
  protected void initData(Bundle savedInstanceState) {
    super.initData(savedInstanceState);

    mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getClassByBrand(brandId), this, 0);
    seriesListAdapter = new VehicleSubscribeSeriesListAdapter(GlobalData.mContext, seriesList, R.layout.vehicle_subcribe_series_list_item_layout);
    mListView.setAdapter(seriesListAdapter);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onSeriesListItemClickListener.onSeriesClick(seriesList.get(position));
      }
    });
  }

  public void reloadData(int brandId) {
    setBrandId(brandId);
    mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getClassByBrand(brandId), this, 0);
    boolean isContains = BrandLogoHelper.BRAND_LOGO.containsKey(brandId);
    int resId = isContains ? BrandLogoHelper.BRAND_LOGO.get(brandId) : R.drawable.b100;
    mLogoIv.setImageResource(resId);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_carseries_back:
        onBackClickListener.onbackClick();
        break;
      case R.id.tv_carseries_for_click:
        onBackClickListener.onbackClick();
        break;
    }
  }


  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
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
    //品牌不限
    if (unlimited_class){
      VehicleSeriesEntity entity = new VehicleSeriesEntity();
      entity.setBrand_id(brandId);
      entity.setBrand_name(brandName);
      entity.setName("不限");
      seriesList.add(0, entity);
    }
    seriesListAdapter.setmList(seriesList);
    seriesListAdapter.notifyDataSetChanged();
  }


  @Override
  public void onDestroyView() {
    //如果请求还没完成，界面就被关闭了，那么就取消请求
    OKHttpManager.getInstance().cancelRequest(mCall);
    super.onDestroyView();
  }

}
