package com.haoche51.checker.activity.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.BrandAdapter;
import com.haoche51.checker.entity.VehicleBrandEntity;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCThreadUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.widget.SideBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 车源品牌界面
 * Created by wufx on 2016/1/21.
 */
public class VehicleBrandActivity extends CommonTitleBaseActivity implements View.OnClickListener {

  @ViewInject(R.id.lv_brand_main)
  private ListView mBrandLv;
  @ViewInject(R.id.tv_toast)
  private TextView mTvToast;
  @ViewInject(R.id.sidrbar)
  private SideBar mSideBar;

  private Call mCall;

  private BrandAdapter mBrandAdapter;
  private List<VehicleBrandEntity> mBrandsData = new ArrayList<>();

  private Map<String, Integer> mapBrandName2Position = new HashMap<>();
  private List<VehicleBrandEntity> brandList;

  private VehicleSourceEntity vehicleSource;

  @Override
  public View getHCContentView() {
    View view = View.inflate(this, R.layout.activity_vehicle_brand, null);
    mSideBar = (SideBar) view.findViewById(R.id.sidrbar);
    mBrandLv = (ListView) view.findViewById(R.id.lv_brand_main);
    mTvToast = (TextView) view.findViewById(R.id.tv_toast);
    mSideBar.setTextView(mTvToast);
    return view;
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {
    vehicleSource = getIntent().getParcelableExtra("vehicleSource");
    //请求网络设置备注
    ProgressDialogUtil.showProgressDialog(VehicleBrandActivity.this, getString(R.string.later));
    mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getAllBrands(), this, 0);

    mBrandAdapter = new BrandAdapter(mBrandsData, this);
    mBrandLv.setAdapter(mBrandAdapter);

    // 设置右侧触摸监听
    mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
      @Override
      public void onTouchingLetterChanged(String s) {
        // 该字母首次出现的位置
        int position = mBrandAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          mBrandLv.setSelection(position);
        }
      }
    });
    x.view().inject(this);
  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.hc_choose_brand));
  }

  private void fillMapData(final List<VehicleBrandEntity> brands) {
    Runnable command = new Runnable() {
      @Override
      public void run() {
        int size = brands.size();
        for (int i = 0; i < size; i++) {
          VehicleBrandEntity b = brands.get(i);
          String name = b.getName();
          mapBrandName2Position.put(name, i);
        }
      }
    };
    HCThreadUtil.execute(command);
  }


  @Override
  public void onClick(View v) {
    if (v.getId() != R.id.rel_brand_parent) {
      return;
    }

    VehicleBrandEntity mEntity = (VehicleBrandEntity) v.getTag(R.id.brand_convert_tag);
    // 取消点击品牌滚动
    int position = (Integer) v.getTag(R.id.for_brand_pos);
    int offset;
    if (0 == position) {
      offset = 0;
    } else {
      offset = -DisplayUtils.getDimenPixels(R.dimen.margin_45dp);
    }
    mBrandLv.smoothScrollToPositionFromTop(position, offset);
    //显示车系界面
    showVehicleClass(mEntity.getId(), mEntity.getName());
  }


  /**
   * 显示车源——车系界面
   *
   * @param brandId   品牌id
   * @param brandName 品牌名称
   */
  private void showVehicleClass(int brandId, String brandName) {
    Intent intent = new Intent(this, VehicleClassActivity.class);
    vehicleSource.setBrand_id(brandId);
    vehicleSource.setBrand_name(brandName);
    intent.putExtra("vehicleSource", vehicleSource);
    startActivity(intent);
  }


  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    ProgressDialogUtil.closeProgressDialog();
    if (HttpConstants.ACTION_ALL_BRANDS.equals(action)) {
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
//    brandList = new HCJsonParse().parseVehicleBrandList(jsonStr);
    brandList = JsonParseUtil.fromJsonArray(jsonStr, VehicleBrandEntity.class);
    if (brandList == null) {
      return;
    }

    List<VehicleBrandEntity> bList = new ArrayList<>();
    VehicleBrandEntity brand;
    for (VehicleBrandEntity brandEntity : brandList) {
      brand = new VehicleBrandEntity();
      brand.setId(brandEntity.getId());
      brand.setName(brandEntity.getName());
      brand.setFirst_char(brandEntity.getFirst_char());
      bList.add(brand);
    }
    mBrandsData = DisplayUtils.sortBrand(bList);
    fillMapData(mBrandsData);

    mBrandAdapter.setBrands(mBrandsData);
    mBrandAdapter.notifyDataSetChanged();
  }


  @Override
  protected void onDestroy() {
    ProgressDialogUtil.closeProgressDialog();
    //如果请求还没完成，界面就被关闭了，那么就取消请求
    OKHttpManager.getInstance().cancelRequest(mCall);
    super.onDestroy();
  }
}
