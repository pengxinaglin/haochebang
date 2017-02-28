package com.haoche51.checker.activity.channel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.entity.CarSourceEntity;
import com.haoche51.checker.fragment.channel.VehicleSourceFragment;
import com.haoche51.checker.helper.ImageLoaderHelper;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import okhttp3.Call;

/**
 * 车源详情界面
 * Created by wufx on 2016/3/1.
 */
public class VehicleSourceDetailActivity extends CommonTitleBaseActivity {
  /**
   * 车源标题
   */
  @ViewInject(R.id.tv_vehilce_title)
  private TextView tv_vehilce_title;
  /**
   * 车源状态
   */
  @ViewInject(R.id.tv_status)
  private TextView tv_status;
  /**
   * 车源图标
   */
  @ViewInject(R.id.iv_vehicle_logo)
  private ImageView iv_vehicle_logo;
  /**
   * 车源编号
   */
  @ViewInject(R.id.tv_vehicle_no)
  private TextView tv_vehicle_no;
  /**
   * 车源价格
   */
  @ViewInject(R.id.tv_vehicle_price)
  private TextView tv_vehicle_price;
  /**
   * 更新时间
   */
  @ViewInject(R.id.tv_update_time)
  private TextView tv_update_time;
  /**
   * 从属商家
   */
  @ViewInject(R.id.tv_merchant)
  private TextView tv_merchant;
  /**
   * 商家地址
   */
  @ViewInject(R.id.tv_merchant_address)
  private TextView tv_merchant_address;
  /**
   * 联系人
   */
  @ViewInject(R.id.tv_contact_name)
  private TextView tv_contact_name;
  /**
   * 联系电话
   */
  @ViewInject(R.id.tv_contact_phone)
  private TextView tv_contact_phone;
  /**
   * 下线车源
   */
  @ViewInject(R.id.btn_negative)
  private Button btn_offline;
  /**
   * 确认在售
   */
  @ViewInject(R.id.btn_positive)
  private Button btn_confirm_online;
  /**
   * 车源id
   */
  private int vehicleSourceId;
  /**
   * 报价
   */
  private float offerPrice;
  /**
   * 底价
   */
  private float lowPrice;

  private Call mCall;
  private CarSourceEntity carSourceEntity;

  @Override
  public View getHCContentView() {
    return View.inflate(this, R.layout.activity_vehicle_source_detail, null);
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {
    x.view().inject(this);
    //获取商家id
    vehicleSourceId = getIntent().getIntExtra("vehicleSourceId", 0);
    initData();
  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.hc_vehicle_source_detail));
    btn_offline.setText(getString(R.string.hc_offline_vehicle));
    btn_offline.setVisibility(View.VISIBLE);
    btn_confirm_online.setText(getString(R.string.hc_confirm_online));
    btn_confirm_online.setVisibility(View.VISIBLE);
  }

  private void initData() {
    ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
    mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getVehicleDetail(vehicleSourceId), this, 0);
  }

  /**
   * 查看报告
   *
   * @param view
   */
  @Event(R.id.rb_check_report)
  private void checkReport(View view) {
    if (vehicleSourceId == 0) {
      ToastUtil.showInfo("暂无报告");
      return;
    }
    Intent intent = new Intent(this, HCWebViewActivity.class);
    intent.putExtra("url", "http://m.haoche51.com/details/" + vehicleSourceId + ".html");
    startActivity(intent);
  }

  /**
   * 查看商家
   *
   * @param view
   */
  @Event(R.id.rb_check_merchant)
  private void checkMerchant(View view) {
    if (carSourceEntity == null) {
      return;
    }
    Intent intent = new Intent(this, MerchantDetailActivity.class);
    intent.putExtra("merchantId", carSourceEntity.getDealer_id());
    startActivity(intent);
    finish();
  }

  /**
   * 调整报价
   *
   * @param view
   */
  @Event(R.id.rb_modify_price)
  private void modifyPrice(View view) {
    if (carSourceEntity == null) {
      return;
    }
    AlertDialogUtil.createModifyPriceDialog(this, carSourceEntity.getPrice(), carSourceEntity.getCheap_price(), new AlertDialogUtil.OnDismissListener() {
      @Override
      public void onDismiss(Bundle data) {
        if (data != null) {
          offerPrice = data.getFloat("offer_price");
          lowPrice = data.getFloat("low_price");
          OKHttpManager.getInstance().post(HCHttpRequestParam.changePrice(carSourceEntity.getId(), offerPrice, lowPrice), VehicleSourceDetailActivity.this, 0);
        }
      }
    });
  }

  /**
   * 下线车源
   */
  @Event(R.id.btn_negative)
  private void offline(View view) {
    if (carSourceEntity == null) {
      return;
    }
    AlertDialogUtil.createOfflineDialog(this, new AlertDialogUtil.OnDismissListener() {
      @Override
      public void onDismiss(Bundle data) {
        if (data != null) {
          String reason = data.getString("reason");
          //请求网络设置备注
          ProgressDialogUtil.showProgressDialog(VehicleSourceDetailActivity.this, getString(R.string.later));
          OKHttpManager.getInstance().post(HCHttpRequestParam.offlineCar(carSourceEntity.getId(), reason), VehicleSourceDetailActivity.this, 0);
        }
      }
    });

  }

  /**
   * 确认在售
   */
  @Event(R.id.btn_positive)
  private void confirmOnline(View view) {
    if (carSourceEntity == null) {
      return;
    }
    //请求网络设置备注
    ProgressDialogUtil.showProgressDialog(VehicleSourceDetailActivity.this, getString(R.string.later));
    OKHttpManager.getInstance().post(HCHttpRequestParam.confirmSell(carSourceEntity.getId()), VehicleSourceDetailActivity.this, 0);
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    ProgressDialogUtil.closeProgressDialog();
    if (HttpConstants.ACTION_GET_VEHICLE_SOURCE_DETAIL.equals(action)) {
      switch (response.getErrno()) {
        case 0://0：表示接口请求成功
          parseResultData(response.getData());
          break;
        default://1：发生错误
          ToastUtil.showInfo(response.getErrmsg());
          break;
      }
    } else if (HttpConstants.ACTION_CHANGE_PRICE.equals(action) || HttpConstants.ACTION_OFFLINE_CAR.equals(action) || HttpConstants.ACTION_CONFIRM_SELL.equals(action)) {
      switch (response.getErrno()) {
        case 0://0：表示接口请求成功
          ToastUtil.showInfo(getString(R.string.successful));
          if (HttpConstants.ACTION_CHANGE_PRICE.equals(action)) {
            //刷新当前界面
            initData();
            //刷新渠寄车源/商家车源列表
            Bundle bundle = new Bundle();
            bundle.putBoolean(VehicleSourceFragment.BUNDLE_CHANGE_PRICE, true);
            bundle.putInt("id", carSourceEntity.getId());
            bundle.putString("price", offerPrice + "");
            HCTasksWatched.getInstance().notifyWatchers(bundle);
          } else if (HttpConstants.ACTION_CONFIRM_SELL.equals(action)) {
            //刷新当前界面
            initData();
            //刷新渠寄车源/商家车源列表
            Bundle bundle = new Bundle();
            bundle.putBoolean(VehicleSourceFragment.BUNDLE_CONFIRM_SELL, true);
            bundle.putInt("id", carSourceEntity.getId());
            try {
              bundle.putString("up_time", new JSONObject(response.getData()).getString("up_time"));
            } catch (JSONException e) {
              e.printStackTrace();
            }
            HCTasksWatched.getInstance().notifyWatchers(bundle);
          } else if (HttpConstants.ACTION_OFFLINE_CAR.equals(action)) {
            //刷新渠寄车源/商家车源列表
            Bundle bundle = new Bundle();
            bundle.putBoolean(VehicleSourceFragment.BUNDLE_OFFLINE_CAR, true);
            bundle.putInt("id", carSourceEntity.getId());
            HCTasksWatched.getInstance().notifyWatchers(bundle);
            finish();
          }
          break;
        default://1：发生错误
          ToastUtil.showInfo(response.getErrmsg());
          break;
      }
    }
  }

  /**
   * 解析结果数据
   *
   * @param result 结果
   */
  private void parseResultData(String result) {
//    carSourceEntity = new HCJsonParse().parseCarSourceDetail(result);
    carSourceEntity = JsonParseUtil.fromJsonObject(result, CarSourceEntity.class);
    if (carSourceEntity == null) {
      return;
    }
    tv_vehilce_title.setText(carSourceEntity.getTitle());
    tv_status.setText(carSourceEntity.getStatus_text());
    //清除背景
    iv_vehicle_logo.setBackgroundResource(0);
    ImageLoaderHelper.displayImage(carSourceEntity.getCover_img(), iv_vehicle_logo);
    iv_vehicle_logo.setScaleType(ImageView.ScaleType.FIT_XY);
    tv_vehicle_no.setText("HC".concat(String.valueOf(carSourceEntity.getId())));
    tv_vehicle_price.setText(String.valueOf(carSourceEntity.getPrice()).concat("万"));
    tv_update_time.setText(carSourceEntity.getUp_time());
    tv_merchant.setText(carSourceEntity.getDealer());
    tv_merchant_address.setText(carSourceEntity.getAddress());
    tv_contact_name.setText(carSourceEntity.getContact_name());
    tv_contact_phone.setText(carSourceEntity.getContact_phone());
  }

  @Override
  protected void onDestroy() {
    ProgressDialogUtil.closeProgressDialog();
    //如果请求还没完成，界面就被关闭了，那么就取消请求
    OKHttpManager.getInstance().cancelRequest(mCall);
    super.onDestroy();
  }
}
