package com.haoche51.checker.activity.evaluate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.haoche51.checker.CheckerApplication;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/2/24.
 */
public class ModifyCheckAddressActivity extends CommonStateActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

  @ViewInject(R.id.et_city)
  private EditText et_city;

  @ViewInject(R.id.et_search)
  private AutoCompleteTextView et_search;

  private PoiSearch mPoiSearch = null;
  private SuggestionSearch mSuggestionSearch = null;
//	private List<String> suggest;

  private ResultAdapter sugAdapter = null;
  private int loadIndex = 0;

  private int taskId;
  private PoiInfo mPoiInfo;
  private String district = "";
  private List<SuggestionResult.SuggestionInfo> allSuggestions;

  @Override
  protected int getContentView() {
    return R.layout.activity_modify_check_address;
  }

  @Override
  protected int getTitleView() {
    return R.layout.layout_common_titlebar_save;
  }

  @Override
  protected void initView() {
    super.initView();
    setScreenTitle(R.string.p_modify_address);

    //拿到地理信息
    BDLocation location = ((CheckerApplication) getApplication()).getLocation();

    if (location != null) {
      //把定位的当前城市填充上去
      String city = location.getCity();
      if (!TextUtils.isEmpty(city))
        et_city.setText(city.substring(0, city.length() - 1));
    } else {
      ToastUtil.showInfo("获取当前城市失败，请手动输入当前城市");
    }

    // 初始化搜索模块，注册搜索事件监听
    mPoiSearch = PoiSearch.newInstance();
    mPoiSearch.setOnGetPoiSearchResultListener(this);
    mSuggestionSearch = SuggestionSearch.newInstance();
    mSuggestionSearch.setOnGetSuggestionResultListener(this);
    sugAdapter = new ResultAdapter(this, R.layout.item_location_popup, allSuggestions);
    et_search.setAdapter(sugAdapter);
    et_search.setThreshold(1);
    /**
     * 当输入关键字变化时，动态更新建议列表
     */
    et_search.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable arg0) {

      }

      @Override
      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

      }

      @Override
      public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
        if (cs.length() <= 0) {
          return;
        }

        /**
         * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
         */
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(et_city.getText().toString()));
      }
    });
    //设置点击item事件
    et_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
          et_search.setText(allSuggestions.get(i).key);
          et_search.setSelection(allSuggestions.get(i).key.length());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  protected void initData() {
    super.initData();
    taskId = getIntent().getIntExtra("taskId", 0);
  }

  /**
   * 保存
   */
  @Event(R.id.tv_right_fuction)
  private void tv_right_fuction(View v) {
    mPoiSearch.searchInCity((new PoiCitySearchOption())
      .city(et_city.getText().toString())
      .keyword(et_search.getText().toString())
      .pageNum(loadIndex));
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    mPoiSearch.destroy();
    mSuggestionSearch.destroy();
    super.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
  }

  public void onGetPoiResult(PoiResult result) {
    if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
      Toast.makeText(this, "未找到结果", Toast.LENGTH_LONG).show();
      return;
    }
    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
      List<PoiInfo> allPoi = result.getAllPoi();
      if (allPoi != null && !allPoi.isEmpty()) {
        if (taskId == 0) {
          ToastUtil.showInfo("参数错误，任务id为null");
          return;
        }
        try {
          ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
          mPoiInfo = allPoi.get(0);
          if (allSuggestions != null) {
            for (SuggestionResult.SuggestionInfo info : allSuggestions) {
              if (info.pt == null)
                continue;

              if (mPoiInfo.location.latitude == info.pt.latitude && mPoiInfo.location.longitude == info.pt.longitude) {
                if (info.district != null)
                  district = info.district;
                break;
              }
            }
          }
          OKHttpManager.getInstance().post(HCHttpRequestParam.updateCheckTask(taskId, mPoiInfo.city + district + et_search.getText(), mPoiInfo.location.latitude, mPoiInfo.location.longitude), this, 0);
        } catch (Exception e) {
          e.printStackTrace();
          ProgressDialogUtil.closeProgressDialog();
        }
      }
      return;
    }
    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
      // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
      String strInfo = "在";
      for (CityInfo cityInfo : result.getSuggestCityList()) {
        strInfo += cityInfo.city;
        strInfo += ",";
      }
      strInfo += "找到结果，请手动切换城市";
      Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
    }
  }

  public void onGetPoiDetailResult(PoiDetailResult result) {
    if (result.error != SearchResult.ERRORNO.NO_ERROR) {
      Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT).show();
    }
  }


  @Override
  public void onGetSuggestionResult(SuggestionResult res) {
    if (res == null || res.getAllSuggestions() == null) {
      return;
    }
//		suggest = new ArrayList<String>();
    List<SuggestionResult.SuggestionInfo> delete = new ArrayList<>();
    allSuggestions = res.getAllSuggestions();
    for (SuggestionResult.SuggestionInfo info : allSuggestions) {
      if (info.key == null) {
        delete.add(info);
//				suggest.add(info.key);
      }
    }
    allSuggestions.removeAll(delete);
    sugAdapter = new ResultAdapter(this, R.layout.item_location_popup, allSuggestions);
    et_search.setAdapter(sugAdapter);
    sugAdapter.notifyDataSetChanged();
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    if (!isFinishing())
      ProgressDialogUtil.closeProgressDialog();

    if (action.equals(HttpConstants.ACTION_UPDATE_CHECK_TASK)) {
      responseUpdateCheckTask(response);
    }
  }

  private void responseUpdateCheckTask(HCHttpResponse response) {
    switch (response.getErrno()) {
      case 0:
        ToastUtil.showInfo("修改成功！");
        Intent intent = new Intent();
        intent.putExtra("appointment_place", mPoiInfo.city + district + et_search.getText());
        intent.putExtra("place_lat", mPoiInfo.location.latitude);
        intent.putExtra("place_lng", mPoiInfo.location.longitude);
        setResult(RESULT_OK, intent);
        finish();
        break;
      default:
        ToastUtil.showInfo(response.getErrmsg());
        break;
    }
  }

  private class ResultAdapter extends ArrayAdapter<SuggestionResult.SuggestionInfo> {

    public ResultAdapter(Context context, int resource, List<SuggestionResult.SuggestionInfo> objects) {
      super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder = null;
      if (convertView == null) {
        convertView = LayoutInflater.from(ModifyCheckAddressActivity.this).inflate(R.layout.item_location_popup, parent, false);
        holder = new ViewHolder();
        holder.tv_key = (TextView) convertView.findViewById(R.id.tv_key);
        convertView.setTag(holder);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }
      SuggestionResult.SuggestionInfo item = getItem(position);
      holder.tv_key.setText(item.key);
      return convertView;
    }

    private class ViewHolder {
      private TextView tv_key;
    }
  }
}
