package com.haoche51.checker.activity.vehicle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseActivity;
import com.haoche51.checker.entity.VehicleSeriesEntity;
import com.haoche51.checker.entity.VehicleSubscribeRuleEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ContactUtil;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.PopupWindowUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangming on 2015/10/30.
 * 看车任务-->放弃购买-->放弃购买原因选择-->修改订阅条件页面
 */
public class VehicleSubScribeConditionActivity extends CommonBaseActivity {

  private final String TAG = "VehicleSubscribeConditionActivity";
  public static final String KEY_INTENT_EXTRA_BUYER_PHONE = "key_intent_extra_buyer_phone";
  public static final String KEY_INTENT_EXTRA_BUYER_NAME = "key_intent_extra_buyer_name";
  public static final String KEY_INTENT_EXTRA_FROM_WHERE = "key_intent_extra_FROM_WHERE";
  public static final String KEY_INTENT_EXTRA_SERIES = "key_intent_extra_series";
  public static final int KEY_REQUEST_ID_GET_SUBSCRIBE_RULE = 0;
  public static final int KEY_REQUEST_ID_SET_SUBSCRIBE_RULE = 1;
  public static final int KEY_REQUEST_ID_SET_BUYER_COMMENT = 2;
  public static final int KEY_REQUEST_CODE_SELECT_VEHICLE_BRAND = 2;
  public static final int KEY_INTENT_EXTRA_FROM_QUIT_BUY = 3;
  public static final int KEY_INTENT_EXTRA_FROM_FAILBUYER_LIST = 4;
  private final int MAX_REASON_TEXT_LENGTH = 50;//具体原因最大长度

  /**
   * 已选品牌
   */
  @ViewInject(R.id.ll_activity_vehicle_sub_brand_list)
  private LinearLayout ll_activity_vehicle_sub_brand_list;
  /**
   * 价格
   */
  @ViewInject(R.id.et_layout_vehicle_sub_pricee_low)
  private EditText et_layout_vehicle_sub_pricee_low;
  @ViewInject(R.id.et_layout_vehicle_sub_pricee_high)
  private EditText et_layout_vehicle_sub_pricee_high;
  /**
   * 排放标准
   */
  @ViewInject(R.id.tv_layout_vehicle_sub_dischange_standard)
  private TextView tv_layout_vehicle_sub_dischange_standard;
  /**
   * 车身结构
   */
  @ViewInject(R.id.tv_layout_vehicle_sub_structure)
  private TextView tv_layout_vehicle_sub_structure;
  /**
   * 排量
   */
  @ViewInject(R.id.et_layout_vehicle_sub_displacement_low)
  private EditText et_layout_vehicle_sub_displacement_low;
  @ViewInject(R.id.et_layout_vehicle_sub_displacement_high)
  private EditText et_layout_vehicle_sub_displacement_high;
  /**
   * 车龄
   */
  @ViewInject(R.id.et_layout_vehicle_sub_old_low)
  private EditText et_layout_vehicle_sub_old_low;
  @ViewInject(R.id.et_layout_vehicle_sub_old_high)
  private EditText et_layout_vehicle_sub_old_high;
  /**
   * 备注
   */
  @ViewInject(R.id.et_vehicle_sub_remark)
  private EditText et_vehicle_sub_remark;
  @ViewInject(R.id.tv_layout_vehicle_sub_remark_count)
  private TextView tv_layout_vehicle_sub_remark_count;
  @ViewInject(R.id.ll_activity_vehicle_sub_customer_info)
  private LinearLayout ll_activity_vehicle_sub_customer_info;
  @ViewInject(R.id.view_remark_line)
  private View view_remark_line;
  /**
   * 变数箱
   */
  @ViewInject(R.id.sp_layout_vehicle_sub_gearbox)
  private Spinner sp_layout_vehicle_sub_gearbox;
  /**
   * 颜色
   */
  @ViewInject(R.id.tv_layout_vehicle_sub_color)
  private TextView tv_layout_vehicle_sub_color;
  /**
   * 客户级别
   */
  @ViewInject(R.id.sp_layout_vehicle_sub_level)
  private Spinner sp_layout_vehicle_sub_level;

  private String phone;
  private String name;
  private int fromWhere;
  private VehicleSubscribeRuleEntity vehicleSubscribeRuleEntity;

  private PopupWindow popupWindowEmission;
  private ListView listViewPopupWindowEmission;
  private View viewPopupWindowEmission;
  private List<String> listPopupWindowEmission = new ArrayList<>();
  private List<VehicleSeriesEntity> listVehicleSeries = new ArrayList<>();
  private Map<String, VehicleSeriesEntity> entityMap = new HashMap<>();
  private Map<Integer, String> colorMap = new HashMap<>();
  private Map<Integer, String> bodyMap = new HashMap<>();

  /**
   * 排放标准 -1不限 0国二，1国三，2国四，3国五
   */
  private int emission = -1;
  /**
   * 变速箱，0未知，1手动，2自动，3双离合，4，手自一体5无级变速
   * -1未选择--回传0
   */
  private int gearbox = -1;
  /**
   * 客户级别,1, 无效 2,不跟进 3，普通4,独家5 重点
   */
  private int level = 0;

  private CharSequence temp;
  private int editStart;
  private int editEnd;

  @Override
  protected void initView() {
    super.initView();
    setContentView(R.layout.activity_vehicle_sub_condition);
    registerTitleBack();
    setScreenTitle(R.string.vehicle_subscribe_condition_title);
    x.view().inject(this);

    phone = getIntent().getStringExtra(KEY_INTENT_EXTRA_BUYER_PHONE);
    name = getIntent().getStringExtra(KEY_INTENT_EXTRA_BUYER_NAME);
    fromWhere = getIntent().getIntExtra(KEY_INTENT_EXTRA_FROM_WHERE, -1);
    if (phone == null) {
      ToastUtil.showText(getString(R.string.common_erro_intent_extra));
      return;
    }

    if (fromWhere == KEY_INTENT_EXTRA_FROM_FAILBUYER_LIST) {
      ll_activity_vehicle_sub_customer_info.setVisibility(View.GONE);
      view_remark_line.setVisibility(View.GONE);
    }

    et_layout_vehicle_sub_pricee_low.addTextChangedListener(DisplayUtils.textWatcherPrice);
    et_layout_vehicle_sub_pricee_high.addTextChangedListener(DisplayUtils.textWatcherPrice);
    et_layout_vehicle_sub_displacement_low.addTextChangedListener(DisplayUtils.textWatcherDisplacement);
    et_layout_vehicle_sub_displacement_high.addTextChangedListener(DisplayUtils.textWatcherDisplacement);


    et_vehicle_sub_remark.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s;
      }

      @Override
      public void afterTextChanged(Editable s) {
        try {
          editStart = et_vehicle_sub_remark.getSelectionStart();
          editEnd = et_vehicle_sub_remark.getSelectionEnd();
          //已经超出长度了，输入无效
          if (temp.length() > MAX_REASON_TEXT_LENGTH) {
            tv_layout_vehicle_sub_remark_count.setText("0");
            s.delete(editStart - 1, editEnd);
            int tempSelection = editStart;
            et_vehicle_sub_remark.removeTextChangedListener(this);
            et_vehicle_sub_remark.setText(s);
            et_vehicle_sub_remark.setSelection(tempSelection);
            et_vehicle_sub_remark.addTextChangedListener(this);
          }
          //更新剩余可输入字数
          tv_layout_vehicle_sub_remark_count.setText(String.valueOf(MAX_REASON_TEXT_LENGTH - s.length()));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

  }


  @Override
  protected void initData() {
    super.initData();
    ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
    OKHttpManager.getInstance().post(HCHttpRequestParam.getSubscribeRule(phone), this, KEY_REQUEST_ID_GET_SUBSCRIBE_RULE);
  }


  /**
   * 限制输入一个小数
   */
  private TextWatcher textWatcher = new TextWatcher() {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
      if ("".equals(editable.toString())) {
        return;
      }
      String temp = editable.toString();
      int posDot = temp.indexOf(".");
      if (posDot > 0) {
        if (temp.length() - posDot > 2) {
          editable.delete(posDot + 2, posDot + 3);
        }
      }
    }
  };

  /**
   * 网络请求完成回调
   *
   * @param action    当前请求action
   * @param requestId
   * @param response  hc 请求结果
   * @param error     网络问题造成failed 的error
   */
  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    super.onHttpComplete(action, requestId, response, error);
    ProgressDialogUtil.closeProgressDialog();
    if (response.getErrno() == 0) {
      switch (requestId) {
        case KEY_REQUEST_ID_GET_SUBSCRIBE_RULE:
          doGetSubscribeRuleResponse(response);
          break;
        case KEY_REQUEST_ID_SET_SUBSCRIBE_RULE:
          setResult(Activity.RESULT_OK);
          finish();
          break;
        case KEY_REQUEST_ID_SET_BUYER_COMMENT:
          setResult(Activity.RESULT_OK);
          finish();
          break;
      }
    } else {
      ToastUtil.showInfo(response.getErrmsg());
    }

  }

  /**
   * 处理获取订阅条件的返回结果
   *
   * @param response
   */
  private void doGetSubscribeRuleResponse(HCHttpResponse response) {
    Type type = new TypeToken<VehicleSubscribeRuleEntity>() {
    }.getType();
    try {
      vehicleSubscribeRuleEntity = response.getData(type);
    } catch (Exception e) {
      e.printStackTrace();
      ToastUtil.showText(getString(R.string.pars_subscribe_error));
      return;
    }
    if (vehicleSubscribeRuleEntity != null) {
      setViewData(vehicleSubscribeRuleEntity);
    }
  }

  /**
   * 设置页面数据
   *
   * @param vehicleSubscribeRuleEntity
   */
  private void setViewData(VehicleSubscribeRuleEntity vehicleSubscribeRuleEntity) {
    if (vehicleSubscribeRuleEntity.getSubscribe_rule() != null) {
      //已选车系
      if (vehicleSubscribeRuleEntity.getSubscribe_rule().getSubscribe_series() != null) {
        listVehicleSeries = vehicleSubscribeRuleEntity.getSubscribe_rule().getSubscribe_series();
        for (VehicleSeriesEntity vehicleSeriesEntity : listVehicleSeries) {
          if (!entityMap.containsKey(vehicleSeriesEntity.getName())) {
            entityMap.put(vehicleSeriesEntity.getName(), vehicleSeriesEntity);
          }
        }
        addSeriesView(entityMap);
      }
      //价格
      if (vehicleSubscribeRuleEntity.getSubscribe_rule().getPrice() != null
        && vehicleSubscribeRuleEntity.getSubscribe_rule().getPrice().size() > 1) {
        List<Float> listPrice = vehicleSubscribeRuleEntity.getSubscribe_rule().getPrice();
        et_layout_vehicle_sub_pricee_low.setText(listPrice.get(0) + "");
        et_layout_vehicle_sub_pricee_high.setText(listPrice.get(1) + "");
      }
      //排放标准 -1不限 0国二，1国三，2国四，3国五
      emission = vehicleSubscribeRuleEntity.getSubscribe_rule().getEmission();
      tv_layout_vehicle_sub_dischange_standard.setText(getResources().getStringArray(R.array.filter_standard)[emission + 1]);
      //车龄
      if (vehicleSubscribeRuleEntity.getSubscribe_rule().getYear() != null
        && vehicleSubscribeRuleEntity.getSubscribe_rule().getYear().size() > 1) {
        List<Integer> listYear = vehicleSubscribeRuleEntity.getSubscribe_rule().getYear();
        et_layout_vehicle_sub_old_low.setText(listYear.get(0) + "");
        et_layout_vehicle_sub_old_high.setText(listYear.get(1) + "");
      }

      //变速箱，0未知，1手动，2自动，3双离合，4，手自一体5无级变速
      gearbox = vehicleSubscribeRuleEntity.getSubscribe_rule().getGearbox();
      sp_layout_vehicle_sub_gearbox.setSelection(gearbox + 1);

      //排量
      if (vehicleSubscribeRuleEntity.getSubscribe_rule().getEmission_value() != null
        && vehicleSubscribeRuleEntity.getSubscribe_rule().getEmission_value().size() > 1) {
        List<Float> listDisplacement = vehicleSubscribeRuleEntity.getSubscribe_rule().getEmission_value();
        et_layout_vehicle_sub_displacement_low.setText(listDisplacement.get(0) + "");
        et_layout_vehicle_sub_displacement_high.setText(listDisplacement.get(1) + "");
      }

      //颜色
      if (vehicleSubscribeRuleEntity.getSubscribe_rule().getVehicle_color_type() != null
        && vehicleSubscribeRuleEntity.getSubscribe_rule().getVehicle_color_type().size() > 0) {
        List<Integer> listColor = vehicleSubscribeRuleEntity.getSubscribe_rule().getVehicle_color_type();
        tv_layout_vehicle_sub_color.setText(DisplayUtils.getVehicleColorString(listColor));
        for (Integer color : listColor) {
          colorMap.put(color, DisplayUtils.getVehicleColor(color));
        }
      }
      //车身结构
      if (vehicleSubscribeRuleEntity.getSubscribe_rule().getVehicle_structure() != null
        && vehicleSubscribeRuleEntity.getSubscribe_rule().getVehicle_structure().size() > 0) {
        List<Integer> listBody = vehicleSubscribeRuleEntity.getSubscribe_rule().getVehicle_structure();
        tv_layout_vehicle_sub_structure.setText(DisplayUtils.getVehicleStructureString(listBody));
        for (Integer body : listBody) {
          bodyMap.put(body, DisplayUtils.getVehicleStructure(body));
        }
      }

    }

    if (fromWhere == KEY_INTENT_EXTRA_FROM_QUIT_BUY) {
      //客户级别
      if (vehicleSubscribeRuleEntity.getLevel() > 0) {
        sp_layout_vehicle_sub_level.setSelection(vehicleSubscribeRuleEntity.getLevel() - 1);
      }
      //客户备注
      if (vehicleSubscribeRuleEntity.getComment() != null) {
        et_vehicle_sub_remark.setText(vehicleSubscribeRuleEntity.getComment());
      }
    }
  }

  /**
   * 点击修改品牌车系
   *
   * @param view
   */
  @Event(R.id.tv_activity_vehicle_sub_brand_modify)
  private void onBrandModifyClick(View view) {
    Intent intent = new Intent(VehicleSubScribeConditionActivity.this, VehicleSubBrandAddActivity.class);
    startActivityForResult(intent, KEY_REQUEST_CODE_SELECT_VEHICLE_BRAND);
  }

  /**
   * 点击排放标准
   *
   * @param view
   */
  @Event(R.id.tv_layout_vehicle_sub_dischange_standard)
  private void onDischangeStandardClick(View view) {
    showEmissionSelectWindow(view);
  }

  /**
   * 点击取消按钮
   *
   * @param view
   */
  @Event(R.id.btn_layout_vehicle_sub_cancle)
  private void onCancleClick(View view) {
    finish();
  }

  /**
   * 点击保存按钮
   *
   * @param view
   */
  @Event(R.id.btn_layout_vehicle_sub_save)
  private void onSaveClick(View view) {
    doValidateAndSubmit();
  }

  /**
   * 点击车身结构按钮
   *
   * @param view
   */
  @Event(R.id.tv_layout_vehicle_sub_structure)
  private void onVehicleStructureClick(View view) {
    PopupWindowUtil.vehicleBodyPopUpWindow(VehicleSubScribeConditionActivity.this, view, bodyMap,
      new PopupWindowUtil.OnCheckBoxCheckedListener() {
        @Override
        public void checkBoxChecked(Map<Integer, String> checkedMap) {
          bodyMap = checkedMap;
          tv_layout_vehicle_sub_structure.setText(DisplayUtils.getVehicleStructureString(bodyMap));

        }
      });
  }

  /**
   * 点击车身颜色
   *
   * @param view
   */
  @Event(R.id.tv_layout_vehicle_sub_color)
  private void onColorClick(View view) {
    PopupWindowUtil.vehicleColorPopUpWindow(VehicleSubScribeConditionActivity.this, view, colorMap, new PopupWindowUtil.OnCheckBoxCheckedListener() {
      @Override
      public void checkBoxChecked(Map<Integer, String> checkedMap) {
        colorMap = checkedMap;
        tv_layout_vehicle_sub_color.setText(DisplayUtils.getVehicleColorString(colorMap));
      }
    });
  }


  /**
   * 订阅条件确定校验
   * rule：
   * 1、没有必填限制，全都不填的话，点击确定，关闭页面，不回传订阅条件。
   * 2、车系不填的话，接口回传空array。
   * 3、价格范围不填，补全默认范围回传 0--9999.
   * 4、排放标准不填，回传不限（-1）。
   * 5、车龄范围不填，补全默认范围回传 0--99
   * 6、变数箱不填，回传不限（0未知）
   * 7、备注：如果订阅条件都没有填写，只填写了备注，单独修改备注，不修改订阅条件，如果订阅条件有修改，备注跟着一起回传修改。
   */
  private void doValidateAndSubmit() {

    //判断订阅条件是否填写
    if ((listVehicleSeries == null || listVehicleSeries.size() == 0)
      && (et_layout_vehicle_sub_pricee_low.getText() == null || "".equals(et_layout_vehicle_sub_pricee_low.getText().toString().trim()))
      && (et_layout_vehicle_sub_pricee_high.getText() == null || "".equals(et_layout_vehicle_sub_pricee_high.getText().toString().trim()))
      && (et_layout_vehicle_sub_displacement_high.getText() == null || "".equals(et_layout_vehicle_sub_displacement_low.getText().toString().trim()))
      && ("请选择".equals(tv_layout_vehicle_sub_dischange_standard.getText()))
      && (et_layout_vehicle_sub_old_low.getText() == null || "".equals(et_layout_vehicle_sub_old_low.getText().toString().trim()))
      && (et_layout_vehicle_sub_old_high.getText() == null || "".equals(et_layout_vehicle_sub_old_high.getText().toString().trim()))
      && "请选择".equals(sp_layout_vehicle_sub_gearbox.getSelectedItem().toString())) {
      //订阅条件没有填写，只处理备注
      if ("".equals(et_vehicle_sub_remark.getText().toString().trim())) {
        ToastUtil.showText("请填写备注");
        return;
      }
      OKHttpManager.getInstance().post(HCHttpRequestParam.setBuyerComment(phone
        , et_vehicle_sub_remark.getText().toString().trim(), sp_layout_vehicle_sub_level.getSelectedItemPosition() + 1)
        , this, KEY_REQUEST_ID_SET_BUYER_COMMENT);
    } else {
      //校验订阅条件,拼装数据
      doBuildData();
    }
  }

  /**
   * 校验订阅条件,拼装接口数据
   */
  private void doBuildData() {
    //品牌车系
    if (listVehicleSeries == null || listVehicleSeries.size() == 0) {//品牌车系未选择
      listVehicleSeries = new ArrayList<>();
    }
    //价格
    float priceLow = -1;
    float priceHigh = -1;

    if (TextUtils.isEmpty(et_layout_vehicle_sub_pricee_low.getText().toString().trim())
      && TextUtils.isEmpty(et_layout_vehicle_sub_pricee_high.getText().toString().trim())) {
      //没填不填
    } else {
      //填了
      if (et_layout_vehicle_sub_pricee_low.getText() != null && !"".equals(et_layout_vehicle_sub_pricee_low.getText().toString().trim())) {
        priceLow = Float.valueOf(et_layout_vehicle_sub_pricee_low.getText().toString().trim());
      } else {
        ToastUtil.showText("请填写最低价");
        return;
      }
      if (et_layout_vehicle_sub_pricee_high.getText() != null && !"".equals(et_layout_vehicle_sub_pricee_high.getText().toString().trim())) {
        priceHigh = Float.valueOf(et_layout_vehicle_sub_pricee_high.getText().toString().trim());
      } else {
        ToastUtil.showText("请填写最高价");
        return;
      }
      if (priceLow >= priceHigh) {
        ToastUtil.showText(getString(R.string.tip_low_less_height));
        return;
      }
    }


    //排量
    float displacementLow = -1;
    float displacementHigh = -1;
    if (TextUtils.isEmpty(et_layout_vehicle_sub_displacement_low.getText().toString().trim())
      && TextUtils.isEmpty(et_layout_vehicle_sub_displacement_high.getText().toString().trim())) {
      //没填不填
    } else {
      //填了
      if (et_layout_vehicle_sub_displacement_low.getText() != null && !"".equals(et_layout_vehicle_sub_displacement_low.getText().toString().trim())) {
        displacementLow = Float.valueOf(et_layout_vehicle_sub_displacement_low.getText().toString().trim());
      } else {
        ToastUtil.showText("请填写最低排量");
        return;
      }
      if (et_layout_vehicle_sub_displacement_high.getText() != null && !"".equals(et_layout_vehicle_sub_displacement_high.getText().toString().trim())) {
        displacementHigh = Float.valueOf(et_layout_vehicle_sub_displacement_high.getText().toString().trim());
      } else {
        ToastUtil.showText("请填写最高排量");
        return;
      }
      if (displacementLow >= displacementHigh) {
        ToastUtil.showText(getString(R.string.tip_low_less_height_displacement));
        return;
      }
    }

    //车龄
    int yearLow = 0;
    int yearHigh = 99;
    if (et_layout_vehicle_sub_old_low.getText() != null && !"".equals(et_layout_vehicle_sub_old_low.getText().toString().trim())) {
      yearLow = Integer.valueOf(et_layout_vehicle_sub_old_low.getText().toString().trim());
    }
    if (et_layout_vehicle_sub_old_high.getText() != null && !"".equals(et_layout_vehicle_sub_old_high.getText().toString().trim())) {
      yearHigh = Integer.valueOf(et_layout_vehicle_sub_old_high.getText().toString().trim());
    }

    if (yearLow >= yearHigh) {
      ToastUtil.showText(getString(R.string.tip_lowage_less_heightage));
      return;
    }

    //车龄
    List<Integer> listYear = new ArrayList<>();
    listYear.add(yearLow);
    listYear.add(yearHigh);

    //价格
    List<Float> listPrice = new ArrayList<>();
    if (priceLow != -1 && priceHigh != -1) {
      listPrice.add(priceLow);
      listPrice.add(priceHigh);
    } else {
      listPrice = null;
    }

    //排量
    List<Float> listDisplacement = new ArrayList<>();
    if (displacementLow != -1 && displacementHigh != -1) {
      listDisplacement.add(displacementLow);
      listDisplacement.add(displacementHigh);
    } else {
      listDisplacement = null;
    }

    //颜色
    List<Integer> listColor = new ArrayList<>();
    for (Integer key : colorMap.keySet()) {
      listColor.add(key);
    }

    //车身结构
    List<Integer> listStructure = new ArrayList<>();
    for (Integer key : bodyMap.keySet()) {
      listStructure.add(key);
    }

    VehicleSubscribeRuleEntity.SubscribeRuleEntity subscribeRuleEntity = new VehicleSubscribeRuleEntity.SubscribeRuleEntity();
    listVehicleSeries.clear();
    for (VehicleSeriesEntity seriesEntity : entityMap.values()) {
      listVehicleSeries.add(seriesEntity);
    }
    subscribeRuleEntity.setSubscribe_series(listVehicleSeries);
    subscribeRuleEntity.setYear(listYear);
    subscribeRuleEntity.setPrice(listPrice);
    subscribeRuleEntity.setEmission_value(listDisplacement);
    subscribeRuleEntity.setVehicle_color_type(listColor);
    subscribeRuleEntity.setVehicle_structure(listStructure);
    subscribeRuleEntity.setGearbox(sp_layout_vehicle_sub_gearbox.getSelectedItemPosition() == 0 ? 0 : sp_layout_vehicle_sub_gearbox.getSelectedItemPosition() - 1);
    subscribeRuleEntity.setEmission(emission);

    ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));

    if (fromWhere == KEY_INTENT_EXTRA_FROM_QUIT_BUY) {
      level = sp_layout_vehicle_sub_level.getSelectedItemPosition() + 1;
    }

    OKHttpManager.getInstance().post(HCHttpRequestParam.setSubscribeRule(phone, subscribeRuleEntity
      , et_vehicle_sub_remark.getText().toString().trim(), level), VehicleSubScribeConditionActivity.this, KEY_REQUEST_ID_SET_SUBSCRIBE_RULE);
  }

  /**
   * 显示排放标准的popwindow
   *
   * @param parent
   */
  private void showEmissionSelectWindow(View parent) {
    if (popupWindowEmission == null) {
      LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      viewPopupWindowEmission = layoutInflater.inflate(R.layout.vehicle_subscribe_condition_emission_select_pop_window_layout, null);
      listViewPopupWindowEmission = (ListView) viewPopupWindowEmission.findViewById(R.id.lv_pop);
      listPopupWindowEmission = Arrays.asList(getResources().getStringArray(R.array.filter_standard));
      EmissionSelectAdapter emissionSelectAdapter = new EmissionSelectAdapter(listPopupWindowEmission);
      listViewPopupWindowEmission.setAdapter(emissionSelectAdapter);
      popupWindowEmission = new PopupWindow(viewPopupWindowEmission, parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    popupWindowEmission.setFocusable(true);
    popupWindowEmission.setOutsideTouchable(true);
    popupWindowEmission.setBackgroundDrawable(new BitmapDrawable());
    popupWindowEmission.showAsDropDown(parent, 0, 0);

    listViewPopupWindowEmission.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tv_layout_vehicle_sub_dischange_standard.setText(listPopupWindowEmission.get(position));
        emission = position - 1;
        popupWindowEmission.dismiss();
      }
    });
  }

  /**
   * 排放标准popwindow的adapter
   */
  class EmissionSelectAdapter extends BaseAdapter {
    private List<String> mEmission;

    EmissionSelectAdapter(List<String> list) {
      mEmission = list;
    }

    @Override
    public int getCount() {
      return mEmission.size();
    }

    @Override
    public Object getItem(int position) {
      return mEmission.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder;
      if (convertView == null) {
        convertView = LayoutInflater.from(VehicleSubScribeConditionActivity.this)
          .inflate(R.layout.vehicle_subscribe_condition_emission_select_pop_window_layout_item, null);
        holder = new ViewHolder();
        convertView.setTag(holder);
        holder.tvContentItem = (TextView) convertView.findViewById(R.id.text_view_dashboard_fragment_group_list_item);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }
      if (mEmission.get(position).equals(tv_layout_vehicle_sub_dischange_standard.getText())) {
        String source = "<font color=\"#263238\">"
          + mEmission.get(position) + "</font>";
        holder.tvContentItem.setText(Html.fromHtml(source));
      } else {
        holder.tvContentItem.setText(mEmission.get(position));
      }
      return convertView;
    }
  }

  static class ViewHolder {
    TextView tvContentItem;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && data != null) {
      VehicleSeriesEntity seriesEntity = (VehicleSeriesEntity) data.getSerializableExtra(KEY_INTENT_EXTRA_SERIES);
      if (!entityMap.containsKey(seriesEntity.getName())) {
        entityMap.put(seriesEntity.getName(), seriesEntity);
        addSeriesView(seriesEntity);
      }
    }
  }

  /**
   * 选择中车系，添加到head
   *
   * @param vehicleSeriesEntity
   */
  private void addSeriesView(VehicleSeriesEntity vehicleSeriesEntity) {

    LinearLayout linearLayoutAll = new LinearLayout(VehicleSubScribeConditionActivity.this);
    LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    linearLayoutAll.setLayoutParams(paramL);
    linearLayoutAll.setOrientation(LinearLayout.VERTICAL);
    linearLayoutAll.setGravity(Gravity.CENTER_VERTICAL);

    LinearLayout linearLayout = new LinearLayout(VehicleSubScribeConditionActivity.this);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80);
    linearLayout.setLayoutParams(params);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
    linearLayout.setTag(vehicleSeriesEntity.getName());

    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
    TextView textView = new TextView(this);
    textView.setLayoutParams(tvParams);
    tvParams.weight = 1;
    textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    textView.setTextColor(getResources().getColor(R.color.self_black));
    textView.setText(vehicleSeriesEntity.getBrand_name() + " " + vehicleSeriesEntity.getName());
    textView.setTextSize(12);
    linearLayout.addView(textView);

    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    ImageView imageView = new ImageView(this);
    imgParams.setMargins(0, 0, 30, 0);

//      imageView.setPadding(0, 0, 30, 0);
    imageView.setLayoutParams(imgParams);
    imageView.setBackgroundResource(R.drawable.ic_hc_vehicle_sub_delete);
    imageView.setTag(vehicleSeriesEntity.getName());
    imageView.setOnClickListener(seriesOnClickListener);//

    linearLayout.addView(imageView);

    linearLayoutAll.addView(linearLayout);

    View view = new View(this);
    LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
    view.setLayoutParams(lineParams);
    view.setBackgroundResource(R.drawable.shape_hc_dashed);
    view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    linearLayoutAll.addView(view);

    ll_activity_vehicle_sub_brand_list.addView(linearLayoutAll);

  }

  /**
   * 选择中车系，添加到head
   *
   * @param seriesMap
   */
  private void addSeriesView(Map<String, VehicleSeriesEntity> seriesMap) {
    ll_activity_vehicle_sub_brand_list.removeAllViews();
    for (VehicleSeriesEntity vehicleSeriesEntity : seriesMap.values()) {
      addSeriesView(vehicleSeriesEntity);
    }
  }

  View.OnClickListener seriesOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      removeSeriesView(v);
    }
  };

  /**
   * 移除选中的车系
   *
   * @param v
   */
  private void removeSeriesView(View v) {
    String seriesName = (String) v.getTag();
    entityMap.remove(seriesName);
    ll_activity_vehicle_sub_brand_list.removeView((View) v.getParent().getParent());
  }

  @Override
  public void finish() {
    //Because some Android devices customization, write the contact will be very slow, so using asynchronous operations, to prevent blocking the main thread
    new AsyncTask() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        ProgressDialogUtil.showProgressDialog(VehicleSubScribeConditionActivity.this, getString(R.string.later));
      }

      @Override
      protected Object doInBackground(Object[] objects) {
        try {
          if (!ContactUtil.isPhoneExists(GlobalData.context, phone)) {
            Map<String, Object> map = new HashMap<>();
            if (listVehicleSeries != null && listVehicleSeries.size() > 1) {
              map.put("name", name + "-" + listVehicleSeries.get(0).getId());
            } else {
              map.put("name", name);
            }
            map.put("phone", phone);
            ContactUtil.insertPhoneBook(GlobalData.context, ContactUtil.getGroupId(GlobalData.context, getString(R.string.contact_group)), map);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }

      @Override
      protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ProgressDialogUtil.closeProgressDialog();
        VehicleSubScribeConditionActivity.super.finish();
      }
    }.execute();
  }
}
