package com.haoche51.checker.activity.evaluate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.haoche51.checker.CheckerApplication;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.TempTaskAdapter;
import com.haoche51.checker.entity.TempTaskEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 添加临时任务
 * Created by wufx on 2016/2/24.
 */
public class AddTempTaskActivity extends CommonTitleBaseActivity implements OnGetSuggestionResultListener {
  /**
   * 空闲起始时间
   */
  @ViewInject(R.id.tv_idle_start_time)
  @Required(order = 1, message = "空闲起始时间不能为空")
  private TextView tv_idle_start_time;

  /**
   * 空闲结束时间
   */
  @ViewInject(R.id.tv_idle_end_time)
  @Required(order = 2, message = "空闲结束时间不能为空")
  private TextView tv_idle_end_time;

  /**
   * 合适地址
   */
  @ViewInject(R.id.act_suitable_place)
  @Required(order = 3, message = "合适地址不能为空")
  private AutoCompleteTextView act_suitable_place;


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
  public View getHCContentView() {
    return View.inflate(this, R.layout.activity_add_temp_task, null);
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {
    x.view().inject(this);
    //初始化建议查询
    initSuggestion();
    //初始化自动完成文本框
    initAutoTextView();
  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.temp_task));
    mRightFaction.setText(getString(R.string.hc_common_save));
    mRightFaction.setVisibility(View.VISIBLE);
    mRightFaction.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //检查必输项
        validator.validate();
      }
    });
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
    act_suitable_place.setAdapter(tempTaskAdapter);

    //设置文本变化监听器
    myTextWatcher = new MyTextWatcher();
    act_suitable_place.addTextChangedListener(myTextWatcher);
    act_suitable_place.setThreshold(1);
    act_suitable_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (tempTaskEntityList == null || tempTaskEntityList.size() == 0) {
          return;
        }
        mTempTaskEnty = tempTaskEntityList.get(position);
        StringBuilder sb = new StringBuilder();
        sb.append(TextUtils.isEmpty(mTempTaskEnty.getCity()) ? "" : mTempTaskEnty.getCity()).append(mTempTaskEnty.getKey());
        act_suitable_place.setText(sb.toString());
        //调整光标位置
        act_suitable_place.setSelection(sb.toString().length());
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
    act_suitable_place.setAdapter(tempTaskAdapter);
    tempTaskAdapter.notifyDataSetChanged();
  }


  /**
   * 保存按钮点击事件
   */
  @Event(R.id.btn_save)
  private void save(View v) {
    //检查必输项
    validator.validate();
  }

  /**
   * 空闲起始时间点击事件
   */
  @Event(R.id.tv_idle_start_time)
  private void clickStartTime(View v) {
    //初始化时间滚轮
    DisplayUtils.displayHourWhell(this, tv_idle_start_time, R.string.select_idle_start_time);
  }

  /**
   * 空闲结束时间点击事件
   */
  @Event(R.id.tv_idle_end_time)
  private void clickEndTime(View v) {
    //初始化时间滚轮
    DisplayUtils.displayHourWhell(this, tv_idle_end_time, R.string.select_idle_end_time);
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
   * 校验成功
   */
  @Override
  public void onValidationSucceeded() {
    super.onValidationSucceeded();
    if (Integer.parseInt(tv_idle_start_time.getText().toString().trim()) >= Integer.parseInt(tv_idle_end_time.getText().toString().trim())) {
      ToastUtil.showInfo("结束时间必须大于起始时间");
      return;
    }

    if (mTempTaskEnty == null) {
      onValidateFailed(act_suitable_place, "请选择百度提示的地址");
      return;
    }

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    //起始时间
    StringBuilder sb = new StringBuilder();
    sb.append(year).append("-").append(month + 1).append("-").append(day).append(" ").append(tv_idle_start_time.getText().toString().trim());
    mTempTaskEnty.setIdleStartTime(UnixTimeUtil.getUnixTime(sb.toString(), UnixTimeUtil.YEAR_MONTH_HOUR_PATTERN));
    //结束时间
    sb = new StringBuilder();
    sb.append(year).append("-").append(month + 1).append("-").append(day).append(" ").append(tv_idle_end_time.getText().toString().trim());
    mTempTaskEnty.setIdleEndTime(UnixTimeUtil.getUnixTime(sb.toString(), UnixTimeUtil.YEAR_MONTH_HOUR_PATTERN));
    mTempTaskEnty.setLocation(act_suitable_place.getText().toString().trim());
    //提交给服务器
    OKHttpManager.getInstance().post(HCHttpRequestParam.applyTempCheckTask(mTempTaskEnty), this, 0);
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    if (HttpConstants.ACTION_APPLY_TEMP_CHECK_TASK.equals(action)) {
      switch (response.getErrno()) {
        case 0://0：表示接口请求成功
          ToastUtil.showInfo("添加临时任务成功！");
          finish();
          break;
        default://1：发生错误
          ToastUtil.showInfo(response.getErrmsg());
          break;
      }
    }
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
        ToastUtil.showInfo("无法定位您的当前城市，请确认GPS可用");
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

  @Override
  protected void onDestroy() {
    //第五步，释放在线建议查询实例；
    if (mSuggestionSearch != null) {
      mSuggestionSearch.destroy();
    }
    super.onDestroy();
  }

}
