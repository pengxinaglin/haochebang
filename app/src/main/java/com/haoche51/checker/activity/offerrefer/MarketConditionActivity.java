package com.haoche51.checker.activity.offerrefer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.OfferReferEntity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 市场行情
 * Created by wfx on 2016/8/3.
 */
public class MarketConditionActivity extends CommonTitleBaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_vehicle_name)
    private TextView tv_vehicle_name;

    @ViewInject(R.id.tv_transaction_section)
    private TextView tv_transaction_section;

    @ViewInject(R.id.tv_newest_market_offer)
    private TextView tv_newest_market_offer;

    @ViewInject(R.id.tv_average_offer)
    private TextView tv_average_offer;

    @ViewInject(R.id.tv_deal_price_section)
    private TextView tv_deal_price_section;

    @ViewInject(R.id.tv_min_price)
    private TextView tv_min_price;

    @ViewInject(R.id.ll_second_hand_car)
    private LinearLayout ll_second_hand_car;

    @ViewInject(R.id.ll_new_car)
    private LinearLayout ll_new_car;

    private OfferReferEntity offerReferEntity;

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_market_condition, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        initData();
    }

    private void initData() {
        offerReferEntity = getIntent().getParcelableExtra(TaskConstants.BINDLE_MARKET_CONDITION);
        if(offerReferEntity==null){
            return;
        }
        tv_vehicle_name.setText(offerReferEntity.getVehicle_source());
        tv_transaction_section.setText(offerReferEntity.getDeal_price());
        tv_newest_market_offer.setText(offerReferEntity.getMarket_price());
        tv_average_offer.setText(offerReferEntity.getAverage_price() + "万元");
        tv_deal_price_section.setText(offerReferEntity.getNew_car_price());
        tv_min_price.setText(offerReferEntity.getLow_match_price() + "万元");
        ll_second_hand_car.setOnClickListener(this);
        ll_new_car.setOnClickListener(this);
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getResources().getString(R.string.market_condition));
    }

    @Override
    public void onClick(View v) {
        if(offerReferEntity==null){
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_second_hand_car:
                intent = new Intent(this, SecondHandCarOfferActivity.class);
                intent.putExtra(TaskConstants.BINDLE_MARKET_CONDITION, offerReferEntity);
                startActivity(intent);
                break;
            case R.id.ll_new_car:
                intent = new Intent(this, NewCarOfferActivity.class);
                intent.putExtra(TaskConstants.BINDLE_MARKET_CONDITION, offerReferEntity);
                startActivity(intent);
                break;
        }
    }
}
