package com.haoche51.checker.fragment.evaluate.fillreport;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.util.DisplayUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 导航栏—问答按钮对应的界面
 * Created by wfx on 2016/7/1.
 */
public class AskFragment extends BaseReportFragment implements View.OnClickListener, TextWatcher {
    @ViewInject(R.id.et_seller_price)
    private EditText et_seller_price;

    @ViewInject(R.id.et_cheap_price)
    private EditText et_cheap_price;

    @ViewInject(R.id.ed_buycomment)
    private EditText buyCommnet;

    @ViewInject(R.id.ed_advantage)
    private EditText advantage;

    @ViewInject(R.id.ed_vehicle_usage)
    private EditText vehicleUsage;

    @ViewInject(R.id.ed_vehicle_maintenance)
    private EditText maintenance;

    @ViewInject(R.id.ed_oil_consumption)
    private EditText oilConsumption;

    @ViewInject(R.id.ed_additional_configuration)
    private EditText additional;

    @ViewInject(R.id.ed_checker_comment)
    private EditText checkerComment;

    @ViewInject(R.id.ed_final_comment)
    private EditText finalComment;

    @ViewInject(R.id.tv_content_max_input_count)
    private TextView tv_content_max_input_count;

    @ViewInject(R.id.layout_whychangename)
    private LinearLayout layout_whychangename;

    @ViewInject(R.id.ed_why_changename)
    private EditText whychangename;

    @ViewInject(R.id.hc_tag_layout)
    private LinearLayout hc_tag_layout;

    private boolean hasChangeName;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_ask, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initAskData();
        initFocusEvent();
        if (mIsTouchValidate) {
            validateCheck();
            mIsTouchValidate = false;
        }
    }

    private void initAskData() {
        if (mCheckReport == null) {
            return;
        }
        hasChangeName = mCheckReport.getTrasfer_record() != 0;

        if (mCheckReport.getSeller_price() != 0) {
            et_seller_price.setText(String.valueOf(mCheckReport.getSeller_price()));
        }
        if (mCheckReport.getCheap_price() != 0) {
            et_cheap_price.setText(String.valueOf(mCheckReport.getCheap_price()));
        }
        if (!TextUtils.isEmpty(mCheckReport.getBuy_comment())) {
            buyCommnet.setText(mCheckReport.getBuy_comment());
        }
        if (!TextUtils.isEmpty(mCheckReport.getVehicle_advantage())) {
            advantage.setText(mCheckReport.getVehicle_advantage());
        }
        if (!TextUtils.isEmpty(mCheckReport.getVehicle_usage())) {
            vehicleUsage.setText(mCheckReport.getVehicle_usage());
        }
        if (!TextUtils.isEmpty(mCheckReport.getVehicle_maintenance())) {
            maintenance.setText(mCheckReport.getVehicle_maintenance());
        }
        if (!TextUtils.isEmpty(mCheckReport.getOil_consumption())) {
            oilConsumption.setText(mCheckReport.getOil_consumption());
        }
        if (!TextUtils.isEmpty(mCheckReport.getAdditional_configuration())) {
            additional.setText(mCheckReport.getAdditional_configuration());
        }
        if (!TextUtils.isEmpty(mCheckReport.getChecker_comment())) {
            checkerComment.setText(mCheckReport.getChecker_comment());
        }
        if (!TextUtils.isEmpty(mCheckReport.getFinal_comment())) {
            finalComment.setText(mCheckReport.getFinal_comment());
            tv_content_max_input_count.setText(TaskConstants.MAX_COMMENT_TEXT_LENGTH - mCheckReport.getFinal_comment().length() + "");
        }
        finalComment.addTextChangedListener(this);
        if (hasChangeName) {
            layout_whychangename.setVisibility(View.VISIBLE);
            whychangename.setText(mCheckReport.getTransfer_reason());
        } else {
            layout_whychangename.setVisibility(View.GONE);
        }

        String[] tags = {"全车喷漆", "全车部分喷漆", "全车原车漆", "局部轻微划痕", "部分钣金", "有明显色差", "无明显色差", "内饰需要清洗",
                "轻微闯档", "发动机轻微渗油", "发动机变速箱运转正常", "女车主用车爱惜", "全程4s店保养"};
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, DisplayUtils.dip2px(getActivity(), Float.parseFloat(30 + "")));
        params.setMargins(10, 0, 10, 20);
        int fontSize = DisplayUtils.dip2px(getActivity(), 4);
        LinearLayout layout = null;
        for (int i = 0; i < tags.length; i++) {
            if (i == tags.length - 2) {
                layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                hc_tag_layout.addView(layout);
            } else if (i < tags.length - 2 && i % 3 == 0) {
                layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                hc_tag_layout.addView(layout);
            }
            TextView checkBox = (Button) View.inflate(getActivity(), R.layout.layout_tags_checkbox, null);
            checkBox.setPadding(10, 0, 10, 0);
            checkBox.setTextSize(fontSize);
            checkBox.setLayoutParams(params);
            checkBox.setOnClickListener(this);
            checkBox.setClickable(true);
            checkBox.setText(tags[i]);
            layout.addView(checkBox);
        }
    }

    private void saveAskData() {
        if (mCheckReport == null || et_seller_price == null) {
            return;
        }

        if (!TextUtils.isEmpty(et_seller_price.getText())) {
            mCheckReport.setSeller_price(Float.valueOf(et_seller_price.getText().toString()));
        } else {
            mCheckReport.setSeller_price(0);
        }

        if (!TextUtils.isEmpty(et_cheap_price.getText())) {
            mCheckReport.setCheap_price(Float.valueOf(et_cheap_price.getText().toString()));
        } else {
            mCheckReport.setCheap_price(0);
        }

        if (!TextUtils.isEmpty(buyCommnet.getText())) {
            mCheckReport.setBuy_comment(buyCommnet.getText().toString().trim());
        } else {
            mCheckReport.setBuy_comment("");
        }

        if (!TextUtils.isEmpty(advantage.getText())) {
            mCheckReport.setVehicle_advantage(advantage.getText().toString().trim());
        } else {
            mCheckReport.setVehicle_advantage("");
        }

        if (!TextUtils.isEmpty(vehicleUsage.getText())) {
            mCheckReport.setVehicle_usage(vehicleUsage.getText().toString().trim());
        } else {
            mCheckReport.setVehicle_usage("");
        }

        if (!TextUtils.isEmpty(maintenance.getText())) {
            mCheckReport.setVehicle_maintenance(maintenance.getText().toString().trim());
        } else {
            mCheckReport.setVehicle_maintenance("");
        }

        if (!TextUtils.isEmpty(oilConsumption.getText())) {
            mCheckReport.setOil_consumption(oilConsumption.getText().toString().trim());
        } else {
            mCheckReport.setOil_consumption("");
        }

        if (!TextUtils.isEmpty(additional.getText())) {
            mCheckReport.setAdditional_configuration(additional.getText().toString().trim());
        } else {
            mCheckReport.setAdditional_configuration("");
        }

        if (!TextUtils.isEmpty(checkerComment.getText())) {
            mCheckReport.setChecker_comment(checkerComment.getText().toString().trim());
        } else {
            mCheckReport.setChecker_comment("");
        }

        if (!TextUtils.isEmpty(finalComment.getText())) {
            mCheckReport.setFinal_comment(finalComment.getText().toString().trim());
        } else {
            mCheckReport.setFinal_comment("");
        }

        if (hasChangeName && !TextUtils.isEmpty(whychangename.getText())) {
            mCheckReport.setTransfer_reason(whychangename.getText().toString());
        } else {
            mCheckReport.setTransfer_reason("");
        }
        //本地标识为已完成了报告基本信息的录入
        mCheckReport.setComplete_check(TaskConstants.CHECK_TASK_PENDING);
    }


    private void initFocusEvent() {
        et_seller_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(et_seller_price.getText())) {
                        et_seller_price.setError("请填写车主报价");
                    } else if (et_seller_price.getText().toString().startsWith(".")) {
                        et_seller_price.setError(getString(R.string.seller_price_format_error));
                    } else if (Float.valueOf(et_seller_price.getText().toString()) == 0) {
                        showErrorMsg(et_seller_price, "车主报价不能为0");
                    } else {
                        et_seller_price.setError(null);
                    }
                }
            }
        });

        et_cheap_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(et_cheap_price.getText())) {
                        et_cheap_price.setError("车主底价不能为空");
                    } else if (et_cheap_price.getText().toString().startsWith(".")) {
                        et_cheap_price.setError(getString(R.string.bottom_price_format_error));
                    } else if (Float.valueOf(et_cheap_price.getText().toString()) == 0) {
                        showErrorMsg(et_cheap_price, "车主底价不能为0");
                    } else if (!TextUtils.isEmpty(et_seller_price.getText()) && Float.parseFloat(et_seller_price.getText().toString()) < Float.parseFloat(et_cheap_price.getText().toString())) {
                        et_cheap_price.setError(getString(R.string.sp_less_bp));
                    } else {
                        et_cheap_price.setError(null);
                    }
                }
            }
        });

        //渠寄任务 只用校验 车主报价、车主底价
        if (mCheckReport!=null && TaskConstants.CHECK_SOURCE_CHANNEL.equals(mCheckReport.getCheck_source())) {
            return;
        }

        buyCommnet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(buyCommnet.getText()) || TextUtils.isEmpty(buyCommnet.getText().toString().trim())) {
                        buyCommnet.setError("请填写何时何地购买");
                    } else {
                        buyCommnet.setError(null);
                    }
                }
            }
        });

        advantage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(advantage.getText()) || TextUtils.isEmpty(advantage.getText().toString().trim())) {
                        advantage.setError("请填写车辆优势");
                    } else {
                        advantage.setError(null);
                    }

                }
            }
        });

        vehicleUsage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(vehicleUsage.getText()) || TextUtils.isEmpty(vehicleUsage.getText().toString().trim())) {
                        vehicleUsage.setError("请填写车辆用途");
                    } else {
                        vehicleUsage.setError(null);
                    }
                }
            }
        });

        maintenance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(maintenance.getText()) || TextUtils.isEmpty(maintenance.getText().toString().trim())) {
                        maintenance.setError("请填写车辆保养状况");
                    } else {
                        maintenance.setError(null);
                    }
                }
            }
        });

        oilConsumption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(oilConsumption.getText()) || TextUtils.isEmpty(oilConsumption.getText().toString().trim())) {
                        oilConsumption.setError("请填写车辆油耗");
                    } else {
                        oilConsumption.setError(null);
                    }
                }
            }
        });

        additional.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(additional.getText()) || TextUtils.isEmpty(additional.getText().toString().trim())) {
                        additional.setError("请填写有无加装");
                    } else {
                        additional.setError(null);
                    }
                }
            }
        });

        checkerComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(checkerComment.getText()) || TextUtils.isEmpty(checkerComment.getText().toString().trim())) {
                        checkerComment.setError("请填写评估师评语");
                    } else {
                        checkerComment.setError(null);
                    }
                }
            }
        });

        finalComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(finalComment.getText()) || TextUtils.isEmpty(finalComment.getText().toString().trim())) {
                        finalComment.setError("请填写评估师备注");
                    } else {
                        finalComment.setError(null);
                    }
                }
            }
        });

        whychangename.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (hasChangeName) {
                        if (TextUtils.isEmpty(whychangename.getText())) {
                            whychangename.setError(getString(R.string.final_whychange));
                        } else {
                            whychangename.setError(null);
                        }
                    }
                }
            }
        });

    }

    @Override
    public boolean validateCheck() {
        mErrNum = 0;
        if (et_seller_price == null) {
            return validateData();
        }

        if (TextUtils.isEmpty(et_seller_price.getText())) {
            showErrorMsg(et_seller_price, "请填写车主报价");
        } else if (et_seller_price.getText().toString().startsWith(".")) {
            showErrorMsg(et_seller_price, getString(R.string.seller_price_format_error));
        } else if (Float.valueOf(et_seller_price.getText().toString()) == 0) {
            showErrorMsg(et_seller_price, "车主报价不能为0");
        }

        if (TextUtils.isEmpty(et_cheap_price.getText())) {
            showErrorMsg(et_cheap_price, "请填写车主底价");
        } else if (et_cheap_price.getText().toString().startsWith(".")) {
            showErrorMsg(et_cheap_price, getString(R.string.bottom_price_format_error));
        } else if (Float.valueOf(et_cheap_price.getText().toString()) == 0) {
            showErrorMsg(et_cheap_price, "车主底价不能为0");
        } else if (!TextUtils.isEmpty(et_seller_price.getText()) && Float.parseFloat(et_seller_price.getText().toString()) < Float.parseFloat(et_cheap_price.getText().toString())) {
            showErrorMsg(et_cheap_price, getString(R.string.sp_less_bp));
        }

        //渠寄任务 只用校验 车主报价、车主底价
        if (mCheckReport!=null && TaskConstants.CHECK_SOURCE_CHANNEL.equals(mCheckReport.getCheck_source())) {
            return mErrNum == 0;
        }

        if (TextUtils.isEmpty(buyCommnet.getText()) || TextUtils.isEmpty(buyCommnet.getText().toString().trim())) {
            showErrorMsg(buyCommnet, "请填写何时何地购买");
        }

        if (TextUtils.isEmpty(advantage.getText()) || TextUtils.isEmpty(advantage.getText().toString().trim())) {
            showErrorMsg(advantage, "请填写车辆优势");
        }

        if (TextUtils.isEmpty(vehicleUsage.getText()) || TextUtils.isEmpty(vehicleUsage.getText().toString().trim())) {
            showErrorMsg(vehicleUsage, "请填写主要用途");
        }

        if (TextUtils.isEmpty(maintenance.getText()) || TextUtils.isEmpty(maintenance.getText().toString().trim())) {
            showErrorMsg(maintenance, "请填写车辆保养状况");
        }

        if (TextUtils.isEmpty(oilConsumption.getText()) || TextUtils.isEmpty(oilConsumption.getText().toString().trim())) {
            showErrorMsg(oilConsumption, "请填写车辆油耗");
        }

        if (TextUtils.isEmpty(checkerComment.getText()) || TextUtils.isEmpty(checkerComment.getText().toString().trim())) {
            showErrorMsg(checkerComment, "请填写评估师评语");
        }

        if (TextUtils.isEmpty(additional.getText()) || TextUtils.isEmpty(additional.getText().toString().trim())) {
            showErrorMsg(additional, "请填写有无加装");
        }

        if (TextUtils.isEmpty(finalComment.getText()) || TextUtils.isEmpty(finalComment.getText().toString().trim())) {
            showErrorMsg(finalComment, "请填写评估师备注");
        }

        if (hasChangeName) {
            if (TextUtils.isEmpty(whychangename.getText())) {
                showErrorMsg(whychangename, getString(R.string.final_whychange));
            }
        }

        return mErrNum == 0;
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

        if (mCheckReport.getSeller_price() == 0) {
            mErrNum++;
        }

        if (mCheckReport.getCheap_price() == 0) {
            mErrNum++;
        }

        //渠寄任务 只用校验 车主报价、车主底价
        if (TaskConstants.CHECK_SOURCE_CHANNEL.equals(mCheckReport.getCheck_source())) {
            return mErrNum == 0;
        }

        if (TextUtils.isEmpty(mCheckReport.getBuy_comment())) {
            mErrNum++;
        }

        if (TextUtils.isEmpty(mCheckReport.getVehicle_advantage())) {
            mErrNum++;
        }

        if (TextUtils.isEmpty(mCheckReport.getVehicle_usage())) {
            mErrNum++;
        }

        if (TextUtils.isEmpty(mCheckReport.getVehicle_maintenance())) {
            mErrNum++;
        }

        if (TextUtils.isEmpty(mCheckReport.getOil_consumption())) {
            mErrNum++;
        }

        if (TextUtils.isEmpty(mCheckReport.getAdditional_configuration())) {
            mErrNum++;
        }

        if (TextUtils.isEmpty(mCheckReport.getChecker_comment())) {
            mErrNum++;
        }

        if (TextUtils.isEmpty(mCheckReport.getFinal_comment())) {
            mErrNum++;
        }

        if (mCheckReport.getTrasfer_record() != 0 && TextUtils.isEmpty(mCheckReport.getTransfer_reason())) {
            mErrNum++;
        }

        return mErrNum == 0;
    }

    @Event(R.id.rl_open_tags)
    private void rl_open_tags(View v) {
        if (v.getTag() != null && (Boolean) v.getTag()) {
            doAnimator(hc_tag_layout.getMeasuredHeight(), DisplayUtils.dip2px(getActivity(), 30), false);
            v.setTag(false);
        } else {
            hc_tag_layout.measure(0, 0);
            doAnimator(DisplayUtils.dip2px(getActivity(), 30), hc_tag_layout.getMeasuredHeight(), true);
            v.setTag(true);
        }
    }

    private void doAnimator(int targetWidth, int startWdith, final boolean enable) {
        final ViewGroup.LayoutParams layoutParams = hc_tag_layout.getLayoutParams();
        ValueAnimator va = ValueAnimator.ofInt(targetWidth, startWdith);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator va) {
                layoutParams.height = (Integer) va.getAnimatedValue();
                hc_tag_layout.setLayoutParams(layoutParams);
            }
        });
        va.setDuration(400);
        va.start();
    }

    @Override
    public void onClick(View view) {
        TextView tv = (TextView) view;
        tv.setTextColor(getResources().getColor(R.color.hc_self_white));
        tv.setBackgroundResource(R.drawable.shape_tag_checked);
        checkerComment.setText(checkerComment.getText().toString() + tv.getText().toString() + "，");
        checkerComment.setSelection(checkerComment.getText().toString().length());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            //已经超出长度了，输入无效
            if (s.toString().length() > TaskConstants.MAX_COMMENT_TEXT_LENGTH) {
                tv_content_max_input_count.setText("0");
                finalComment.removeTextChangedListener(this);//避免死循环
                finalComment.setText(s.toString().substring(0, TaskConstants.MAX_COMMENT_TEXT_LENGTH));
                finalComment.setSelection(finalComment.getText().toString().length());//将光标移动到最后
                finalComment.addTextChangedListener(this);
            } else {
                //更新剩余可输入字数
                tv_content_max_input_count.setText(String.valueOf(TaskConstants.MAX_COMMENT_TEXT_LENGTH - s.length()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void saveData() {
        try{
            saveAskData();
        }catch (Exception e){
            e.printStackTrace();

        }

    }
}
