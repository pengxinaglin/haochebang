package com.haoche51.checker.activity.evaluate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.LiYangVehicleDAO;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.LiYangDistinctEntity;
import com.haoche51.checker.entity.LiYangHttpRequestEntity;
import com.haoche51.checker.entity.LiYangVehicleEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 力洋差车型 筛选 页面
 * Created by yangming on 2015/11/18.
 */
public class LiYangVehicleFilterActivity extends CommonTitleBaseActivity implements View.OnClickListener {

  private static final String TAG = "LiYangVehicleFilterActivity";
  private static final int HTTP_REQUEST_ID_GET_LIYANG_LIST = 1;


  @ViewInject(R.id.grid_layout_liyang_vehicle_filter_params)
  private GridLayout gridLayout;
  @ViewInject(R.id.hc_pull_to_refresh_liyang_vehicle_filter)
  private HCPullToRefresh hcPullToRefresh;

  private ListView mListView;
  private String vinCode = "";
  private int reportId = -1;
  private List<LiYangDistinctEntity> distinctEntities;
  private List<LiYangVehicleEntity> liYangVehicleEntitiesSourceData;
  private List<LiYangVehicleEntity> liYangVehicleEntitiesFilterData;
  private Call mCall;
  @Override
  public View getHCContentView() {
    return View.inflate(this, R.layout.liyang_vehicle_filter_activity, null);
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {
    vinCode = getIntent().getStringExtra("vinCode");
    reportId = getIntent().getIntExtra("reportId", -1);
    if (reportId == -1) {
      ToastUtil.showText("传递参数出错");
      return;
    }
    //    vinCode = "lsgpc54r1af075546";//    只取到一款车型
    //    vinCode = "LFPM4ACC491A45633";//    未获取过报告: LFPM4ACC491A45633
    //    vinCode = "LSGPC54U8DF183073";//    已成功获取报告: LSGPC54U8DF183073
    //    vinCode = "LGWEF3K53CF034151";//    获取报告失败: LGWEF3K53CF034151
    x.view().inject(this);
    View emptyView = findViewById(R.id.ll_liyang_vehicle_filter_empty_view);
    hcPullToRefresh.setEmptyView(emptyView);
    hcPullToRefresh.setCanPull(false);
    mListView = hcPullToRefresh.getListView();
    mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    initData();
  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.liyang_vehicle_filter_title));
    mRightFaction.setText(getString(R.string.common_title_commit));
    mRightFaction.setOnClickListener(this);
    mRightFaction.setVisibility(View.VISIBLE);
  }

  private void initData() {
    ProgressDialogUtil.showProgressDialog(LiYangVehicleFilterActivity.this, getString(R.string.hc_loading));
    mCall=OKHttpManager.getInstance().post(HCHttpRequestParam.getLiYangModelList(vinCode), this, HTTP_REQUEST_ID_GET_LIYANG_LIST);
  }

  private void initView(List<LiYangDistinctEntity> distinctEntities) {
    if (distinctEntities != null) {
      for (LiYangDistinctEntity distinctEntity : distinctEntities) {
        addSpinner(distinctEntity);
      }
    }
  }

  private void initList(List<LiYangVehicleEntity> liYangVehicleEntities, boolean isInit) {
    List<String> vehicles = new ArrayList<>();
    if(liYangVehicleEntities==null || liYangVehicleEntities.size()==0){
       return;
    }

    for (LiYangVehicleEntity vehicleEntity : liYangVehicleEntities) {
      vehicles.add(vehicleEntity.getVehicle_name());
    }
    mListView.setAdapter(new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_single_choice, vehicles.toArray(new String[vehicles.size()])));
    if (isInit) {
      LiYangVehicleDAO.getInstance().clear();
      LiYangVehicleDAO.getInstance().insert(liYangVehicleEntities);
    }
    mListView.setBackgroundColor(getResources().getColor(R.color.self_white));
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_right_fuction:
        int position = mListView.getCheckedItemPosition();
        if (ListView.INVALID_POSITION != position) {
          LiYangVehicleEntity vehicleEntity = liYangVehicleEntitiesFilterData.get(position);
          Intent intent = new Intent();
          intent.putExtra(TaskConstants.KEY_INTENT_EXTRA_RESULT_VEHICLE, vehicleEntity);
          setResult(Activity.RESULT_OK, intent);
          finish();
        }
        break;
    }
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    super.onHttpComplete(action, requestId, response, error);
    switch (response.getErrno()) {
      case 0:
        Type type = new TypeToken<LiYangHttpRequestEntity>() {
        }.getType();
        LiYangHttpRequestEntity requestEntity = null;
        try {
          requestEntity = response.getData(type);
        } catch (Exception e) {
          ProgressDialogUtil.closeProgressDialog();
          e.printStackTrace();
        }
        if (requestEntity == null) {
          ProgressDialogUtil.closeProgressDialog();
          return;
        }
        distinctEntities = requestEntity.getDistinct_key_list();
        liYangVehicleEntitiesSourceData = requestEntity.getModel_list();
        liYangVehicleEntitiesFilterData = liYangVehicleEntitiesSourceData;
        initView(distinctEntities);
        initList(liYangVehicleEntitiesSourceData, true);
        ProgressDialogUtil.closeProgressDialog();
        break;
      default:
        ProgressDialogUtil.closeProgressDialog();
//        ToastUtil.showText(response.getErrmsg());
        String content = "您输入的VIN码为：\n" + vinCode + "，\n未检索到车型，请确认是否输入正确，若错误请点击修改";
        AlertDialogUtil.createNormalDialog(this, content, "修改", "关闭", true, new confirmVINDialogDismissListener());
        break;
    }
  }

  private void addSpinner(LiYangDistinctEntity distinctEntity) {
    Spinner spinner = new Spinner(mContext);
    //绑定Tag
    spinner.setTag(distinctEntity.getKey());
    // 设置空间控件监听
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filterVehicles();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    //设置控件属性
    spinner.setBackgroundResource(R.drawable.spinner_selector);
    LinearLayout.LayoutParams paramsL = new LinearLayout.LayoutParams(DisplayUtils.getScreenWidth(mContext) / 2, LinearLayout.LayoutParams.MATCH_PARENT);
    paramsL.setMargins(3, 6, 3, 6);
    spinner.setLayoutParams(paramsL);
    //绑定adapter和数据
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item);
    spinner.setAdapter(arrayAdapter);
    arrayAdapter.clear();
    arrayAdapter.add(distinctEntity.getName());
    arrayAdapter.addAll(distinctEntity.getValue());
    spinner.setSelection(0);

    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
    params.setGravity(Gravity.FILL);
    params.width = DisplayUtils.getScreenWidth(mContext) / 2;//指定item的宽，不然就是最小宽度
    gridLayout.addView(spinner, params);
  }

  private void filterVehicles() {
    String where = getFilterString();
    liYangVehicleEntitiesFilterData = LiYangVehicleDAO.getInstance().get(where);
    initList(liYangVehicleEntitiesFilterData, false);
  }

  private String getFilterString() {
    StringBuilder sb = new StringBuilder();
    int count = gridLayout.getChildCount();
    boolean first = true;
    for (int i = 0; i < count; i++) {
      Spinner spinner = (Spinner) gridLayout.getChildAt(i);
      if (spinner.isEnabled() && spinner.getSelectedItemPosition() != -1 && spinner.getSelectedItemPosition() != 0) {
        if (!first) {
          sb.append(" and ");
        }
        String key = (String) spinner.getTag();
        String value = spinner.getSelectedItem().toString();
        sb.append(key).append("='").append(value).append("'");
        first = false;
      }
    }
    return sb.toString();
  }

  @Override
  protected void onDestroy() {
    OKHttpManager.getInstance().cancelRequest(mCall);
    super.onDestroy();
  }

  /**
   * 确认VIN码对话框的点击监听
   */
  private class confirmVINDialogDismissListener implements AlertDialogUtil.OnDismissListener {
    @Override
    public void onDismiss(Bundle data) {
      boolean determine = data.getBoolean("determine");
      if (determine) {//关闭
        setResult(Activity.RESULT_OK);
        finish();
      } else {//修改
        //弹出输入对话框
        AlertDialogUtil.createInputVinCodeDialog(LiYangVehicleFilterActivity.this, new inputVINDialogDismissListener());
      }
    }
  }

  /**
   * 输入VIN码对话框的点击监听
   */
  private class inputVINDialogDismissListener implements AlertDialogUtil.OnDismissListener {
    @Override
    public void onDismiss(Bundle data) {
      if (data != null) {
        vinCode = data.getString("vinCode");
        //修改存储的VIN码
        CheckReportEntity checkReport = CheckReportDAO.getInstance().get(reportId);
        checkReport.setVin_code(vinCode);
        CheckReportDAO.getInstance().update(checkReport.getId(), checkReport);
        //重新查询
        initData();
      } else {
        setResult(Activity.RESULT_OK);
        finish();
      }
    }
  }
}
