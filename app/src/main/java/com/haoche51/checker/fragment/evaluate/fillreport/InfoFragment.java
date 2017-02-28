package com.haoche51.checker.fragment.evaluate.fillreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.evaluate.LiYangVehicleFilterActivity;
import com.haoche51.checker.constants.BasicConstants;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.LiYangVehicleEntity;
import com.haoche51.checker.entity.VehicleModelEntity;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.UnixTimeUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import okhttp3.Call;

/**
 * 导航栏—信息按钮对应的界面
 * Created by wfx on 2016/6/28.
 */
public class InfoFragment extends BaseReportFragment {

    //vin码
    @ViewInject(R.id.et_vin)
    private EditText et_vin;

    // 车牌号
    @ViewInject(R.id.ed_carnumber)
    private EditText carNumEdit;

    // 登记时间
    @ViewInject(R.id.regist_time)
    private TextView regist_time;
    // 年检
    @ViewInject(R.id.anual_check_time)
    private TextView anual_check_time;
    // 交强险
    @ViewInject(R.id.traffic_insurance_time)
    private TextView traffic_insurance_time;
    // 商业险
    @ViewInject(R.id.custom_insurance_time)
    private TextView tv_custom_insurance_time;

    @ViewInject(R.id.no_custom_insurance)
    private CheckBox noCustomInsCk;

    /**
     * 过户记录
     */
    @ViewInject(R.id.trasfer_record)
    private Switch mSwitchTransRecord;

    // 看到登记证
    @ViewInject(R.id.rg_registration)
    private RadioGroup rg_registration;

    // 看到登记证
    @ViewInject(R.id.view_registration)
    private RadioButton rb_view_regist;

    // 未看到登记证
    @ViewInject(R.id.no_view_registration)
    private RadioButton rb_not_view_regist;

    // 登记证标签
    @ViewInject(R.id.registration_lab)
    private TextView registration_lab;

    @ViewInject(R.id.transfer_year)
    private TextView tvTransYear;

    @ViewInject(R.id.transfer_times_lab)
    private TextView tv_transfer_times_lab;

    @ViewInject(R.id.transfer_times)
    private Spinner transTimesSpan;

    // 品牌,车系,车款
    @ViewInject(R.id.vehicle_brand)
    private TextView brandText;
    @ViewInject(R.id.vehicle_class)
    private TextView classText;

    @ViewInject(R.id.vehicle_model)
    private TextView modelText;

    // 变速箱
    @ViewInject(R.id.sp_gearbox_lab)
    private TextView sp_gearbox_lab;

    // 变速箱
    @ViewInject(R.id.sp_gearbox)
    private Spinner gearboxSpn;

    // 排放标准后标签
    @ViewInject(R.id.sp_emissions_standard_lab)
    private TextView sp_emissions_standard_lab;

    // 排放标准
    @ViewInject(R.id.sp_emissions_standard)
    private Spinner emssionStandardSpan;

    // 排量
    @ViewInject(R.id.ed_emission)
    private EditText emissionEt;

    @ViewInject(R.id.sp_emission_type_lab)
    private TextView sp_emission_type_lab;

    @ViewInject(R.id.sp_emission_type)
    private Spinner emissionTypeSpn;

    // 表显里程
    @ViewInject(R.id.ed_miles)
    private EditText milesEt;

    // 备用号码
    @ViewInject(R.id.ed_phonenum)
    private EditText ed_phonenum;

    // 驱动方式
    @ViewInject(R.id.rg_drive)
    private RadioGroup rg_drive;

    // 驱动方式文本
    @ViewInject(R.id.tv_drive_lab)
    private TextView tv_drive_lab;

    // 座位个数
    @ViewInject(R.id.edt_seatcount)
    private EditText edt_seatcount;

    @ViewInject(R.id.four_drive)
    private RadioButton four_drive;

    @ViewInject(R.id.front_drive)
    private RadioButton front_drive;

    @ViewInject(R.id.after_drive)
    private RadioButton after_drive;

    @ViewInject(R.id.ckb_iscardealer)
    private CheckBox cb_suspected_agent;

    @ViewInject(R.id.ckb_exclusive)
    private CheckBox cb_exclusive;

    private Call mCall;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_info, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initInfoData();
        initFocusEvent();
        if (mIsTouchValidate) {
            validateCheck();
            mIsTouchValidate = false;
        }
    }

    /**
     * 初始化报告数据
     */
    protected void initInfoData() {
        if (mCheckReport == null) {
            return;
        }

        if (!TextUtils.isEmpty(mCheckReport.getVin_code())) {
            //有vin码 不可修改
            et_vin.setText(mCheckReport.getVin_code() + "");
        }

        carNumEdit.setText(mCheckReport.getCar_num());
        ed_phonenum.setText(mCheckReport.getBackup_phone());
        edt_seatcount.setText(mCheckReport.getSeats_num() == 0 ? "" : String.valueOf(mCheckReport.getSeats_num()));

        if (mCheckReport.getRegister_time() != 0) {
            regist_time.setText(UnixTimeUtil.format(mCheckReport.getRegister_time(), "yyyy-MM"));
            Log.e("select_time", UnixTimeUtil.format(mCheckReport.getRegister_time()));
        }

        if (mCheckReport.getAnual_valid_time() != 0) {
            anual_check_time.setText(UnixTimeUtil.format(mCheckReport.getAnual_valid_time(), "yyyy-MM"));
        }

        if (mCheckReport.getTins_valid_time() != 0) {
            traffic_insurance_time.setText(UnixTimeUtil.format(mCheckReport.getTins_valid_time(), "yyyy-MM"));
        }

        if (mCheckReport.getIns_valid_time() != 0) {
            noCustomInsCk.setChecked(false);
//            noCustomInsCk.setButtonDrawable(R.drawable.ic_rb_nomal);
            tv_custom_insurance_time.setEnabled(true);
            if (mCheckReport.getIns_valid_time() != -1) {
                tv_custom_insurance_time.setText(UnixTimeUtil.format(mCheckReport.getIns_valid_time(), "yyyy-MM"));
            }

        } else {
            noCustomInsCk.setChecked(true);
//            noCustomInsCk.setButtonDrawable(R.drawable.ic_rb_checked);
            tv_custom_insurance_time.setEnabled(false);
        }

        if (mCheckReport.getTrasfer_record() != 0) {
            mSwitchTransRecord.setChecked(true);
            tvTransYear.setClickable(true);
            transTimesSpan.setEnabled(true);
            if (mCheckReport.getTrasfer_record() != -1) {
                tvTransYear.setText(UnixTimeUtil.format(mCheckReport.getTrasfer_record(), "yyyy-MM"));
            }

            int transferTimes = mCheckReport.getTransfer_times();
            transTimesSpan.setSelection(transferTimes + 1);
        } else {
            mSwitchTransRecord.setChecked(false);
            tvTransYear.setClickable(false);
            transTimesSpan.setEnabled(false);
        }

        if (mCheckReport.getGearbox() != 0) {
            gearboxSpn.setSelection(mCheckReport.getGearbox());
        }
        if (!TextUtils.isEmpty(mCheckReport.getEmissions())) {
            emissionEt.setText(mCheckReport.getEmissions().replaceAll("L|T|无", ""));
            if (mCheckReport.getEmissions().contains("T")) {
                emissionTypeSpn.setSelection(3);
            } else if (mCheckReport.getEmissions().contains("L")) {
                emissionTypeSpn.setSelection(2);
            } else if (mCheckReport.getEmissions().contains("无")) {
                emissionTypeSpn.setSelection(1);
            } else {
                emissionTypeSpn.setSelection(0);
            }
        }
        if (mCheckReport.getMiles() != 0) {
            milesEt.setText(String.valueOf(mCheckReport.getMiles()));
        }

        if (mCheckReport.getView_registration() == 1) {
            rb_view_regist.setChecked(true);
        } else if (mCheckReport.getView_registration() == 2) {
            rb_not_view_regist.setChecked(true);
        }

        if (mCheckReport.getEmissions_standard() != 0) {
            int em_standard = mCheckReport.getEmissions_standard();
            if (em_standard == 4) {
                em_standard -= 3;
            } else {
                em_standard += 1;
            }
            emssionStandardSpan.setSelection(em_standard);
        }
        if (mCheckReport.getDrive_mode() != 0) {
            switch (mCheckReport.getDrive_mode()) {
                case 1:
                    four_drive.setChecked(true);
                    break;
                case 2:
                    front_drive.setChecked(true);
                    break;
                case 3:
                    after_drive.setChecked(true);
                    break;
            }
        }

        if (mCheckReport.getSuspected_agent() == 1) {
            cb_suspected_agent.setChecked(true);
        } else {
            cb_suspected_agent.setChecked(false);
        }

        if (mCheckReport.getExclusive() == 1) {
            cb_exclusive.setChecked(true);
        } else {
            cb_exclusive.setChecked(false);
        }

        //有力洋显示力洋，没有显示自己的
        if (TextUtils.isEmpty(mCheckReport.getLiyang_model_name())) {
            modelText.setText(mCheckReport.getVehicle_name());
        } else {
            modelText.setText(mCheckReport.getLiyang_model_name());
        }

        if (TextUtils.isEmpty(mCheckReport.getLiyang_brand_nane()) || TextUtils.isEmpty(mCheckReport.getLiyang_series_name())) {
//            ProgressDialogUtil.showProgressDialog(getActivity(), getString(R.string.hc_loading));
            mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getVehicleModelById(mCheckReport.getVehicle_model_id()), this, 0);
        } else {
            brandText.setText(mCheckReport.getLiyang_brand_nane());
            classText.setText(mCheckReport.getLiyang_series_name());
        }

    }


    /**
     * 跳转到车源估价
     */
    @Event(R.id.btn_vehicle_valuation)
    private void btn_vehicle_valuation(View view) {
        Map params = new HashMap();
        params.put("url", "http://m.haoche51.com/vehicle_valuation?platform=checker_app");
        params.put(HCWebViewActivity.KEY_INTENT_EXTRA_VEHICLE_SOURCE_ESTIMATE, true);
        HCActionUtil.launchActivity(getActivity(), HCWebViewActivity.class, params);
    }

    /**
     * 筛选品牌车系button
     *
     * @param view
     */
    @Event(R.id.filter_vehicle_model)
    private void filterBtnClick(View view) {
        if (mCheckReport == null) {
            return;
        }

        if (!TextUtils.isEmpty(mCheckReport.getVin_code())) {
            //已保存过VIN码，直接跳转力洋查车型，筛选
            saveVINAndToFilter(mCheckReport.getVin_code(), false);
        } else if (TextUtils.isEmpty(mCheckReport.getVin_code()) && !TextUtils.isEmpty(et_vin.getText().toString())) {
            if (et_vin.getText().toString().trim().length() == 17) {
                //未保存过VIN码，但是页面上有填写，弹出确认提示
                AlertDialogUtil.createModifyVinCodeDialog(getActivity(), et_vin.getText().toString(), true, new confirmVINDialogDismissListener());
            } else {
                showErrorMsg(et_vin, "VIN码必须为17位");
            }
        } else if (TextUtils.isEmpty(mCheckReport.getVin_code()) && TextUtils.isEmpty(et_vin.getText().toString())) {
            //未保存过VIN码，页面上也没有填写，弹出填写提示
            AlertDialogUtil.createInputVinCodeDialog(getActivity(), new inputVINDialogDismissListener());
        }
    }

    /**
     * 保存VIN码，跳转力洋筛选车型页面
     */
    private void saveVINAndToFilter(String vinCode, boolean saveVIN) {
        if (mCheckReport == null) {
            return;
        }
        //保存vin
        if (saveVIN) {
            mCheckReport.setVin_code(vinCode);
            Executors.newCachedThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    CheckReportDAO.getInstance().update(mCheckReport.getId(), mCheckReport);
                }
            });
        }
        //跳转力洋车型筛选
        Intent intent = new Intent();
        intent.setClass(getActivity(), LiYangVehicleFilterActivity.class);
        intent.putExtra("reportId", mCheckReport.getId());
        intent.putExtra("vinCode", vinCode);
        startActivityForResult(intent, TaskConstants.KEY_INTENT_REQUEST_CODE_FILTER_LIYANG);
    }

    /**
     * 初始化焦点事件
     */
    public void initFocusEvent() {
        et_vin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //VIN码必须为17位！
                    if (TextUtils.isEmpty(et_vin.getText()) || et_vin.getText().toString().trim().length() != 17) {
                        et_vin.setError(getString(R.string.vin_format_error));
                    } else {
                        et_vin.setError(null);
                    }
                }
            }
        });

        gearboxSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sp_gearbox_lab.setError("请选择变速箱");
                } else {
                    sp_gearbox_lab.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        emissionEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(emissionEt.getText())) {
                        emissionEt.setError("请填写排量");
                    } else if (emissionEt.getText().toString().trim().startsWith(".")) {
                        emissionEt.setError(getString(R.string.displacement_format_error));
                    } else {
                        emissionEt.setError(null);
                    }
                }
            }
        });

        emissionTypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sp_emission_type_lab.setError("请选择排量类型");
                } else {
                    sp_emission_type_lab.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        milesEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(milesEt.getText())) {
                        milesEt.setError("请确认表显里程已填写");
                    } else if (milesEt.getText().toString().trim().startsWith(".")) {
                        milesEt.setError(getString(R.string.mileage_format_error));
                    } else {
                        milesEt.setError(null);
                    }
                }
            }
        });


        //渠寄的只用限定 VIN，上牌时间，行驶里程，排量，排量类型，变速箱
        if (mCheckReport != null && TaskConstants.CHECK_SOURCE_CHANNEL.equals(mCheckReport.getCheck_source())) {
            noCustomInsCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        tv_custom_insurance_time.setText("");
                    } else if (TextUtils.isEmpty(tv_custom_insurance_time.getText())) {
                    }
                    tv_custom_insurance_time.setEnabled(!isChecked);
                }

            });


            mSwitchTransRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        tvTransYear.setText("");
                        transTimesSpan.setSelection(0);
                    }

                    tvTransYear.setClickable(isChecked);
                    transTimesSpan.setEnabled(isChecked);
                }
            });
            return;
        }

        carNumEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(carNumEdit.getText()) || TextUtils.isEmpty(carNumEdit.getText().toString().trim())) {
                        carNumEdit.setError("请填写车牌号");
                    } else {
                        carNumEdit.setError(null);
                    }
                }
            }
        });

        rg_registration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == -1) {
                    registration_lab.setError("请选择是否见到登记证");
                } else {
                    registration_lab.setError(null);
                }
            }
        });

        mSwitchTransRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    tvTransYear.setText("");
                    transTimesSpan.setSelection(0);
                    tvTransYear.setError(null);
                    tv_transfer_times_lab.setError(null);
                } else {
                    if (TextUtils.isEmpty(tvTransYear.getText())) {
                        tvTransYear.setError("请选择最后过户时间");
                    } else {
                        tvTransYear.setError(null);
                    }

                    if (transTimesSpan.getSelectedItemPosition() == 0) {
                        tv_transfer_times_lab.setError("请选择过户次数");
                    } else {
                        tv_transfer_times_lab.setError(null);
                    }

                }
                tvTransYear.setClickable(isChecked);
                transTimesSpan.setEnabled(isChecked);
            }
        });

        noCustomInsCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                noCustomInsCk.setButtonDrawable(isChecked ? R.drawable.ic_rb_checked : R.drawable.ic_rb_nomal);
                if (isChecked) {
                    tv_custom_insurance_time.setText("");
                    tv_custom_insurance_time.setError(null);
                } else if (TextUtils.isEmpty(tv_custom_insurance_time.getText())) {
                    tv_custom_insurance_time.setError("请选择商业险到期时间");
                }
                tv_custom_insurance_time.setEnabled(!isChecked);
            }

        });

        emssionStandardSpan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sp_emissions_standard_lab.setError("请选择排放标准");
                } else {
                    sp_emissions_standard_lab.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edt_seatcount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(edt_seatcount.getText())) {
                        edt_seatcount.setError("请填写座位个数");
                    } else {
                        edt_seatcount.setError(null);
                    }
                }
            }
        });

        transTimesSpan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSwitchTransRecord.isChecked()) {
                    if (position == 0) {
                        tv_transfer_times_lab.setError(getString(R.string.trans_times_tip));
                    } else {
                        tv_transfer_times_lab.setError(null);
                    }
                } else {
                    tv_transfer_times_lab.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ed_phonenum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!TextUtils.isEmpty(ed_phonenum.getText())) {
                        if (ed_phonenum.getText().toString().length() < 7) {
                            ed_phonenum.setError(getString(R.string.standby_number_error));
                        } else {
                            ed_phonenum.setError(null);
                        }
                    }
                }
            }
        });

        modelText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //年份车款
                    if (TextUtils.isEmpty(modelText.getText())) {
                        modelText.setError(getString(R.string.year_modle));
                    } else {
                        modelText.setError(null);
                    }
                }
            }
        });

        rg_drive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == -1) {
                    tv_drive_lab.setError("请选择驱动方式");
                } else {
                    tv_drive_lab.setError(null);
                }
            }
        });
    }

    /**
     * 用于在页面未加载时的校验
     *
     * @return
     */
    public boolean validateData() {
        mErrNum = 0;
        if (mCheckReport == null) {
            return false;
        }

        //VIN码
        if (TextUtils.isEmpty(mCheckReport.getVin_code()) || mCheckReport.getVin_code().length() != 17) {
            mErrNum++;
        }

        //上牌时间
        if (mCheckReport.getRegister_time() == 0) {
            mErrNum++;
        }

        //行驶里程
        if (mCheckReport.getMiles() <= 0) {
            mErrNum++;
        }

        //排量
        if (TextUtils.isEmpty(mCheckReport.getEmissions())) {
            mErrNum++;
        }

        //排量类型
        if (!TextUtils.isEmpty(mCheckReport.getEmissions())) {
            String emission = mCheckReport.getEmissions().replaceAll("L|T|无", "*");
            if (!emission.contains("*")) {
                mErrNum++;
            }
        }

        //变速箱
        if (mCheckReport.getGearbox() == 0) {
            mErrNum++;
        }

        //渠寄任务 只用校验 VIN，上牌时间，行驶里程，排量，排量类型，变速箱
        if (TaskConstants.CHECK_SOURCE_CHANNEL.equals(mCheckReport.getCheck_source())) {
            return mErrNum == 0;
        }

        if (TextUtils.isEmpty(mCheckReport.getCar_num())) {
            mErrNum++;
        }

        if (mCheckReport.getAnual_valid_time() == 0) {
            mErrNum++;
        }

        if (mCheckReport.getTins_valid_time() == 0) {
            mErrNum++;
        }

        if (mCheckReport.getIns_valid_time() == -1) {
            mErrNum++;
        }

        if (mCheckReport.getTrasfer_record() == -1) {
            mErrNum++;
            if (mCheckReport.getTransfer_times() == 0) {
                mErrNum++;
            }
        }

        if (mCheckReport.getView_registration() == -1) {
            mErrNum++;
        }


        if (mCheckReport.getEmissions_standard() == 0) {
            mErrNum++;
        }

        if (mCheckReport.getDrive_mode() == 0) {
            mErrNum++;
        }

        if (mCheckReport.getSeats_num() == 0) {
            mErrNum++;
        }

        if (!TextUtils.isEmpty(mCheckReport.getBackup_phone())) {
            if (mCheckReport.getBackup_phone().length() < 7) {
                mErrNum++;
            }
        }

        return mErrNum == 0;
    }

    @Override
    public boolean validateCheck() {
        mErrNum = 0;

        if (et_vin == null) {
            return validateData();
        }

        //VIN码必须为17位！
        if (TextUtils.isEmpty(et_vin.getText()) || et_vin.getText().toString().trim().length() != 17) {
            showErrorMsg(et_vin, getString(R.string.vin_format_error));
        }

        //上牌时间
        if (TextUtils.isEmpty(regist_time.getText())) {
            showErrorMsg(regist_time, "请填写登记时间");
        }

        //行驶里程
        if (TextUtils.isEmpty(milesEt.getText())) {
            showErrorMsg(milesEt, "请确认表显里程已填写");
        } else if (milesEt.getText().toString().trim().startsWith(".")) {
            showErrorMsg(milesEt, getString(R.string.mileage_format_error));
        }

        //排量
        if (TextUtils.isEmpty(emissionEt.getText())) {
            showErrorMsg(emissionEt, "请填写排量");
        } else if (emissionEt.getText().toString().trim().startsWith(".")) {
            showErrorMsg(emissionEt, getString(R.string.displacement_format_error));
        }

        //排量类型
        if (emissionTypeSpn.getSelectedItemPosition() == 0) {
            showErrorMsg(sp_emission_type_lab, "请选择排量类型");
        }

        //变速箱
        if (gearboxSpn.getSelectedItemPosition() == 0) {
            showErrorMsg(sp_gearbox_lab, "请选择变速箱");
        }

        //渠寄任务 只用校验 VIN，上牌时间，行驶里程，排量，排量类型，变速箱
        if (TaskConstants.CHECK_SOURCE_CHANNEL.equals(mCheckReport.getCheck_source())) {
            return mErrNum == 0;
        }

        if (TextUtils.isEmpty(carNumEdit.getText()) || TextUtils.isEmpty(carNumEdit.getText().toString().trim())) {
            showErrorMsg(carNumEdit, "请填写车牌号");
        }

        if (TextUtils.isEmpty(anual_check_time.getText())) {
            showErrorMsg(anual_check_time, "请填写年检到期时间");
        }

        if (TextUtils.isEmpty(traffic_insurance_time.getText())) {
            showErrorMsg(traffic_insurance_time, "请填写交强险到期时间");
        }

        if (!noCustomInsCk.isChecked()) {
            if (TextUtils.isEmpty(tv_custom_insurance_time.getText())) {
                showErrorMsg(tv_custom_insurance_time, getString(R.string.customins_tip));
            }
        }

        if (mSwitchTransRecord.isChecked()) {//有过户记录
            if (transTimesSpan.getSelectedItemPosition() == 0) {
                showErrorMsg(tv_transfer_times_lab, getString(R.string.trans_times_tip));
            }

            if (TextUtils.isEmpty(tvTransYear.getText())) {
                showErrorMsg(tvTransYear, getString(R.string.transrecord_tip));
            }
        }

        if (rg_registration.getCheckedRadioButtonId() == -1) {
            showErrorMsg(registration_lab, "请选择是否见到登记证");
        }

        if (emssionStandardSpan.getSelectedItemPosition() == 0) {
            showErrorMsg(sp_emissions_standard_lab, "请选择排放标准");
        }

        if (rg_drive.getCheckedRadioButtonId() == -1) {
            showErrorMsg(tv_drive_lab, "请选择驱动方式");
        }

        if (TextUtils.isEmpty(edt_seatcount.getText())) {
            showErrorMsg(edt_seatcount, "请填写座位个数");
        }

        if (!TextUtils.isEmpty(ed_phonenum.getText())) {
            if (ed_phonenum.getText().toString().length() < 7) {
                showErrorMsg(ed_phonenum, getString(R.string.standby_number_error));
            }
        }

        //年份车款
        if (TextUtils.isEmpty(modelText.getText())) {
            showErrorMsg(modelText, getString(R.string.year_modle));
        }

        return mErrNum == 0;

    }


    /**
     * 保存数据
     */
    protected void saveInfoData() {
        if (mCheckReport == null || et_vin == null) {
            return;
        }
        //保存vin码
        if (!TextUtils.isEmpty(et_vin.getText())) {
            mCheckReport.setVin_code(et_vin.getText().toString().trim());
        } else {
            mCheckReport.setVin_code("");
        }

        if (!TextUtils.isEmpty(carNumEdit.getText())) {
            mCheckReport.setCar_num(carNumEdit.getText().toString().trim());
        } else {
            mCheckReport.setCar_num("");
        }

        if (!TextUtils.isEmpty(ed_phonenum.getText())) {
            mCheckReport.setBackup_phone(ed_phonenum.getText().toString());
        } else {
            mCheckReport.setBackup_phone("");
        }

        if (!TextUtils.isEmpty(edt_seatcount.getText())) {
            mCheckReport.setSeats_num(Integer.valueOf(edt_seatcount.getText().toString()));
        } else {
            mCheckReport.setSeats_num(0);
        }

        if (!TextUtils.isEmpty(regist_time.getText())) {
            mCheckReport.setRegister_time(UnixTimeUtil.getUnixTime(regist_time.getText().toString(), "yyyy-MM"));
        } else {
            mCheckReport.setRegister_time(0);
        }


        if (!TextUtils.isEmpty(anual_check_time.getText())) {
            mCheckReport.setAnual_valid_time(UnixTimeUtil.getUnixTime(anual_check_time.getText().toString(), "yyyy-MM"));
        } else {
            mCheckReport.setAnual_valid_time(0);
        }

        if (!TextUtils.isEmpty(traffic_insurance_time.getText())) {
            mCheckReport.setTins_valid_time(UnixTimeUtil.getUnixTime(traffic_insurance_time.getText().toString(), "yyyy-MM"));
        } else {
            mCheckReport.setTins_valid_time(0);
        }


        if (!noCustomInsCk.isChecked()) {
            if (!TextUtils.isEmpty(tv_custom_insurance_time.getText())) {
                mCheckReport.setIns_valid_time(UnixTimeUtil.getUnixTime(tv_custom_insurance_time.getText().toString(), "yyyy-MM"));
            } else {
                mCheckReport.setIns_valid_time(-1);
            }
        } else {
            mCheckReport.setIns_valid_time(0);
        }


        if (mSwitchTransRecord.isChecked()) {
            if (!TextUtils.isEmpty(tvTransYear.getText())) {
                mCheckReport.setTrasfer_record(UnixTimeUtil.getUnixTime(tvTransYear.getText().toString(), "yyyy-MM"));
            } else {
                mCheckReport.setTrasfer_record(-1);
            }

            int times = transTimesSpan.getSelectedItemPosition();
            times -= 1;
            times = times > -1 ? times : 0;
            mCheckReport.setTransfer_times(times);
        } else {
            mCheckReport.setTrasfer_record(0);
            mCheckReport.setTransfer_times(0);
        }

        if (rb_view_regist.isChecked()) {
            mCheckReport.setView_registration(1);
        } else if (rb_not_view_regist.isChecked()) {
            mCheckReport.setView_registration(2);
        } else {
            mCheckReport.setView_registration(-1);
        }

        mCheckReport.setGearbox(gearboxSpn.getSelectedItemPosition());

        // 排放标准
        if (emssionStandardSpan.getSelectedItemPosition() != 0) {
            int em_standard = emssionStandardSpan.getSelectedItemPosition();
            if (em_standard == 1) {
                em_standard += 3;
            } else {
                em_standard -= 1;
            }
            mCheckReport.setEmissions_standard(em_standard);
        } else {
            mCheckReport.setEmissions_standard(0);
        }

        if (!TextUtils.isEmpty(emissionEt.getText())) {
            if (emissionTypeSpn.getSelectedItemPosition() == 0) {
                mCheckReport.setEmissions(emissionEt.getText().toString());
            } else {
                mCheckReport.setEmissions(emissionEt.getText().toString() + emissionTypeSpn.getSelectedItem().toString());
            }
        } else {
            mCheckReport.setEmissions("");
        }

        if (!TextUtils.isEmpty(milesEt.getText())) {
            mCheckReport.setMiles(Float.valueOf(milesEt.getText().toString()));
        } else {
            mCheckReport.setMiles(0);
        }

        if (rg_drive.getCheckedRadioButtonId() != -1) {
            switch (rg_drive.getCheckedRadioButtonId()) {
                case R.id.four_drive:
                    mCheckReport.setDrive_mode(1);
                    break;
                case R.id.front_drive:
                    mCheckReport.setDrive_mode(2);
                    break;
                case R.id.after_drive:
                    mCheckReport.setDrive_mode(3);
                    break;
            }
        } else {
            mCheckReport.setDrive_mode(0);
        }

        mCheckReport.setSuspected_agent(cb_suspected_agent.isChecked() ? 1 : 0);
        mCheckReport.setExclusive(cb_exclusive.isChecked() ? 1 : 0);
        mCheckReport.setComplete_check(TaskConstants.CHECK_TASK_PENDING);
    }

    @Override
    public void saveData() {
        saveInfoData();
    }

    /**
     * 从服务器请求车型信息
     */
    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {

        ProgressDialogUtil.closeProgressDialog();
        if (HttpConstants.ACTION_GET_MODEL_BY_ID.equals(action)) {
            switch (response.getErrno()) {
                case 0:
//                    VehicleModelEntity vehicleModelEntity = new HCJsonParse().parseVehicleModel(response.getData());
                    VehicleModelEntity vehicleModelEntity = JsonParseUtil.fromJsonObject(response.getData(), VehicleModelEntity.class);
                    if (vehicleModelEntity != null) {
                        brandText.setText(vehicleModelEntity.getBrand_name());
                        classText.setText(vehicleModelEntity.getSeries_name());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 请求筛选返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (mCheckReport == null) {
                return;
            }
            switch (requestCode) {
                case BasicConstants.REQUEST_FILTER_VEHICLE:
                    ProgressDialogUtil.showProgressDialog(getActivity(), getString(R.string.hc_loading));
//                    CheckReportEntity mCheckReport = CheckReportDAO.getInstance().get(mReportId);

                    mCheckReport.setVehicle_brand_id(data.getIntExtra(BasicConstants.VEHICLE_BRAND_ID, 0));
                    mCheckReport.setVehicle_brand_name(data.getStringExtra(BasicConstants.VEHICLE_BRAND_NAME));
                    brandText.setText(data.getStringExtra(BasicConstants.VEHICLE_BRAND_NAME));
                    mCheckReport.setVehicle_class_id(data.getIntExtra(BasicConstants.VEHICLE_SERIES_ID, 0));
                    mCheckReport.setVehicle_series_name(data.getStringExtra(BasicConstants.VEHICLE_SERIES_NAME));
                    classText.setText(data.getStringExtra(BasicConstants.VEHICLE_SERIES_NAME));
                    mCheckReport.setVehicle_model_id(data.getIntExtra(BasicConstants.VEHICLE_MODEL_ID, 0));
                    mCheckReport.setVehicle_name(data.getStringExtra(BasicConstants.VEHICLE_MODEL_NAME));
                    modelText.setText(data.getStringExtra(BasicConstants.VEHICLE_MODEL_NAME));
                    CheckReportDAO.getInstance().update(mCheckReport.getId(), mCheckReport);
                    ProgressDialogUtil.closeProgressDialog();
                    break;
                case TaskConstants.KEY_INTENT_REQUEST_CODE_FILTER_LIYANG:
                    if (data != null) {
                        LiYangVehicleEntity vehicleEntity = (LiYangVehicleEntity) data.getSerializableExtra(TaskConstants.KEY_INTENT_EXTRA_RESULT_VEHICLE);
                        if (vehicleEntity == null) return;
//                        CheckReportEntity mCheckReport = CheckReportDAO.getInstance().get(mReportId);
                        //刷新页面vin输入框的值，因为力洋查车型页面可能会修改了vin
                        String vinCode = mCheckReport.getVin_code();
                        et_vin.setText(vinCode);

                        mCheckReport.setLiyang_model_id(vehicleEntity.getModel_id());
                        //刷新页面与数据库中的 品牌/车系/车款 数据
                        String[] vehicleInfo = vehicleEntity.getVehicle_name().split(" ");
                        String brand = "";
                        String series = "";
                        String model = "";
                        if (vehicleInfo.length > 1) {
                            brand = vehicleInfo[0];
                        }
                        if (vehicleInfo.length > 2) {
                            series = vehicleInfo[1];
                        }
                        if (vehicleInfo.length > 3) {
                            for (int i = 2; i < vehicleInfo.length; i++) {
                                model = model + vehicleInfo[i];
                            }
                        }
                        brandText.setText(brand);
                        classText.setText(series);
                        modelText.setText(model);

                        mCheckReport.setLiyang_brand_nane(brand);
                        mCheckReport.setLiyang_series_name(series);
                        mCheckReport.setLiyang_model_name(model);
                    } else {
                        //刷新页面vin输入框的值，因为力洋查车型页面可能会修改了vin
                        et_vin.setText(mCheckReport.getVin_code());
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        //如果请求还没完成，界面就被关闭了，那么就取消请求
        OKHttpManager.getInstance().cancelRequest(mCall);
        super.onDestroy();
    }

    /**
     * 输入VIN码对话框的点击监听
     */
    private class inputVINDialogDismissListener implements AlertDialogUtil.OnDismissListener {
        @Override
        public void onDismiss(Bundle data) {
            if (data != null) {
                String vinCode = data.getString("vinCode");
                et_vin.setText(vinCode);
                //Vin输入框不可输入
                et_vin.setEnabled(false);
                //提示确认对话框
                AlertDialogUtil.createModifyVinCodeDialog(getActivity(), et_vin.getText().toString(), true, new confirmVINDialogDismissListener());
            }
        }
    }

    /**
     * 确认VIN码对话框的点击监听
     */
    private class confirmVINDialogDismissListener implements AlertDialogUtil.OnDismissListener {
        @Override
        public void onDismiss(Bundle data) {
            boolean determine = data.getBoolean("determine");
            if (determine) {//确定
                //Vin输入框不可输入
                et_vin.setEnabled(false);
                saveVINAndToFilter(et_vin.getText().toString(), true);
            } else {//取消
                //Vin输入框可编辑
                et_vin.clearFocus();
                et_vin.setEnabled(true);
                et_vin.setFocusable(true);
                et_vin.requestFocus();
            }
        }
    }

}
