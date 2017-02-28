package com.haoche51.checker.activity.offlinesold;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.entity.StockAttentionEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.HCArithUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;

/**
 * 回购库存——关注度
 * Created by wfx on 2016/7/7.
 */
public class StockAttentionActivity extends CommonTitleBaseActivity {
    @ViewInject(R.id.tv_vehicle_name)
    private TextView tv_vehicle_name;

    @ViewInject(R.id.tv_online_time)
    private TextView tv_online_time;

    @ViewInject(R.id.tv_scan_times)
    private TextView tv_scan_times;

    @ViewInject(R.id.tv_call_to_consult)
    private TextView tv_call_to_consult;

    @ViewInject(R.id.tv_door_to_car)
    private TextView tv_door_to_car;

    @ViewInject(R.id.tv_adjust_price)
    private TextView tv_adjust_price;

    private StockAttentionEntity mStockAttention;
    private int stockId;//库存id

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_stock_attention, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.attention));
        stockId = getIntent().getIntExtra("stock_id", 0);
        refreshView();
    }

    private void refreshView() {
        if (stockId > 0) {
            ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
            OKHttpManager.getInstance().post(HCHttpRequestParam.getStockAttention(stockId), this, 0);
        }
    }


    @Event(R.id.tv_adjust_price)
    private void adjustPrice(View v) {
        if (mStockAttention == null) {
            return;
        }
        alertAdjustPriceDialog();
    }

    /**
     * 弹出调价窗口
     */
    private void alertAdjustPriceDialog() {
        final Dialog dialog = new Dialog(this, R.style.shareDialog);
        final View rootView = View.inflate(this, R.layout.dialog_apply_adjust_price, null);
        Button btnCancel = (Button) rootView.findViewById(R.id.btn_layout_vehicle_sub_cancle);
        btnCancel.setText(getResources().getString(R.string.soft_update_cancel));
        Button btnCommit = (Button) rootView.findViewById(R.id.btn_layout_vehicle_sub_save);
        btnCommit.setText(getResources().getString(R.string.hc_commit));
        TextView tv_purchase_price = (TextView) rootView.findViewById(R.id.tv_purchase_price);
        TextView tv_old_offer_price = (TextView) rootView.findViewById(R.id.tv_old_offer_price);
        TextView tv_old_base_price = (TextView) rootView.findViewById(R.id.tv_old_base_price);
        tv_purchase_price.setText(HCArithUtil.div(mStockAttention.getBack_price(), 10000) + "");
        tv_old_offer_price.setText(HCArithUtil.div(mStockAttention.getSeller_price(), 10000) + "");
        tv_old_base_price.setText(HCArithUtil.div(mStockAttention.getCheap_price(), 10000) + "");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        final EditText ed_new_offer_price = (EditText) rootView.findViewById(R.id.ed_new_offer_price);
        final TextView tv_new_base_price = (TextView) rootView.findViewById(R.id.tv_new_base_price);
        ed_new_offer_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    changeBasePrice(ed_new_offer_price, tv_new_base_price);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(ed_new_offer_price.getText())) {
                    onValidateFailed(ed_new_offer_price, "新报价不能为空");
                    return;
                }

                BigDecimal unitWan = new BigDecimal(10000);
                double offerPrice = new BigDecimal(ed_new_offer_price.getText().toString()).multiply(unitWan).doubleValue();
                if (offerPrice == 0) {
                    onValidateFailed(ed_new_offer_price, "新报价不能为0");
                    return;
                }

                double basePrice = new BigDecimal(tv_new_base_price.getText().toString()).multiply(unitWan).doubleValue();
                if (basePrice == 0) {
                    onValidateFailed(tv_new_base_price, "新底价不能为0");
                    return;
                }

                mStockAttention.setNew_seller_price((int) offerPrice);
                OKHttpManager.getInstance().post(HCHttpRequestParam.getApplyToAdjustPrice(mStockAttention), StockAttentionActivity.this, 0);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(rootView);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.85);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
    }

    /**
     * 修改底价
     */
    private void changeBasePrice(EditText ed_offer_price, TextView tv_base_price) {
        if (TextUtils.isEmpty(ed_offer_price.getText())) {
            tv_base_price.setText("");
            onValidateFailed(ed_offer_price, "新报价不能为空");
            return;
        }

        //车辆报价 元
        double offerPrice = HCArithUtil.mul(Double.valueOf(ed_offer_price.getText().toString()), 10000);
        if (offerPrice <= 0) {
            onValidateFailed(ed_offer_price, "车辆报价必须大于0");
            return;
        }

        int diffPrice;
        if (offerPrice < 50000) {
            diffPrice = 1000;
        } else if (offerPrice < 80000) {
            diffPrice = 2000;
        } else if (offerPrice < 100000) {
            diffPrice = 3000;
        } else if (offerPrice < 150000) {
            diffPrice = 4000;
        } else {
            diffPrice = 5000;
        }
        double tmpPrice = HCArithUtil.sub(offerPrice, diffPrice);
        double basePrice = HCArithUtil.div(tmpPrice, 10000);
        basePrice = basePrice < 0 ? 0 : basePrice;
        tv_base_price.setText(String.valueOf(basePrice));
    }

    /**
     * 显示校验失败消息
     *
     * @param failedView
     * @param message
     */
    private void onValidateFailed(TextView failedView, String message) {
        failedView.requestFocus();
        failedView.setError(message);
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        ProgressDialogUtil.closeProgressDialog();
        switch (response.getErrno()) {
            case 0:
                if (action.equals(HttpConstants.ACTION_GET_STOCK_ATTENTION)) {
                    parseData(response);
                } else if (action.equals(HttpConstants.ACTION_APPLY_CHANGE_PRICE)) {
                    ToastUtil.showInfo("申请调价成功");
                    refreshView();
                }
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }

    }

    /**
     * 解析数据
     *
     * @param response
     */
    private void parseData(HCHttpResponse response) {
//        mStockAttention = new HCJsonParse().parseStockAttention(response.getData());
        mStockAttention = JsonParseUtil.fromJsonObject(response.getData(), StockAttentionEntity.class);
        if (mStockAttention == null) {
            return;
        }
        tv_vehicle_name.setText(mStockAttention.getTitle());
        tv_online_time.setText(mStockAttention.getOnline_time() + "天");
        tv_scan_times.setText(mStockAttention.getVisit() + "人次");
        tv_call_to_consult.setText(mStockAttention.getPhone_ask() + "人次");
        tv_door_to_car.setText(mStockAttention.getLook_cn() + "人次");
        if (mStockAttention.getIs_self() == 0) {
            tv_adjust_price.setVisibility(View.GONE);
        } else {
            tv_adjust_price.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }
}
