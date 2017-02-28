package com.haoche51.checker.activity.stocking;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.entity.StockVehicleEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.HCLogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


public class VehicleResultActivity extends CommonStateActivity {

    @ViewInject(R.id.iv_vehicle_img)
    private ImageView mResultImage;

    @ViewInject(R.id.tv_vehicle_name)
    private TextView tv_vehicle_name;

    @ViewInject(R.id.tv_vehicle_price)
    private TextView tv_vehicle_price;

    @ViewInject(R.id.tv_vehicle_id)
    private TextView tv_vehicle_id;

    @ViewInject(R.id.tv_backtask_id)
    private TextView tv_backtask_id;

    @ViewInject(R.id.ll_refresh)
    private LinearLayout ll_refresh;

    @ViewInject(R.id.pb_refresh)
    private ProgressBar pb_refresh;

    @ViewInject(R.id.iv_source)
    private ImageView iv_source;
    @Override
    protected int getContentView() {
        return R.layout.activity_stockingscan_result;

    }

    @Override
    protected void initView() {
        super.initView();
        setScreenTitle(getString(R.string.hc_scan_result));
    }


    @Override
    protected void initData() {
        super.initData();
        Bundle extras = getIntent().getExtras();
        String vehicle_source_id = extras.getString("vehicle_source_id");
        tv_vehicle_name.setText(vehicle_source_id);
        OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckStock(vehicle_source_id), this, 0);
        ll_refresh.setEnabled(false);//禁用按钮，等待信息显示完全
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (action.equals(HttpConstants.ACTION_BACKREPAIRAPI_GETCHECKSTOCK)) {
            responseGetCheckStock(response);

        } else if (action.equals(HttpConstants.ACTION_BACKREPAIRAPI_CHECKSTOCK)) {
            responseStocking(response);
        }
    }

    /**
     * 车源信息处理
     *
     * @param response
     */
    private void responseGetCheckStock(HCHttpResponse response) {

        switch (response.getErrno()) {
            case 0:
//                StockVehicleEntity entity = new HCJsonParse().parseStockVehicle(response.getData());
                StockVehicleEntity entity = JsonParseUtil.fromJsonObject(response.getData(), StockVehicleEntity.class);
                if (entity == null) {
                    Toast.makeText(VehicleResultActivity.this, getString(R.string.hc_invalid_info), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    refreshVehicleInfo(entity);
                }
                break;
            default:
                Toast.makeText(VehicleResultActivity.this, response.getErrmsg(), Toast.LENGTH_LONG).show();
        }
        pb_refresh.setVisibility(View.GONE);
        ll_refresh.setEnabled(true);
    }


    private void refreshVehicleInfo(StockVehicleEntity entity) {
        tv_vehicle_name.setText(entity.getTitle() != null ? entity.getTitle() : "");
        tv_vehicle_price.setText(entity.getSeller_price() != null ? GlobalData.resourceHelper.getString(R.string.hc_price_formate, entity.getSeller_price()) : "");
        tv_vehicle_id.setText(entity.getVehicle_source_id() != null ? entity.getVehicle_source_id() : "");
        tv_backtask_id.setText(entity.getBack_task_id() != null ? entity.getBack_task_id() : "");
        iv_source.setImageResource("1".equals(entity.getVehicle_type()) ? R.drawable.ic_back : R.drawable.ic_channel_tip);
        int mWidth = mResultImage.getWidth();
        int mHeight = mResultImage.getHeight();
        if (entity.getCover_img() != null) {
            StringBuffer sb = new StringBuffer(entity.getCover_img());
            sb.append("?imageView2/1/w/").append(mWidth).append("/h/").append(mHeight);
            ImageLoader.getInstance().displayImage(sb.toString(), mResultImage);
            HCLogUtil.e("TEST_COVER_IMAGE", sb.toString());
        }

    }
    /**
     * 盘点信息处理
     * @param response
     */
    private void responseStocking(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                finish();
                break;
            default:
                Toast.makeText(VehicleResultActivity.this, response.getErrmsg(), Toast.LENGTH_LONG).show();
        }

    }

    @Event(R.id.ll_refresh)
    private void refresh(View view) {
        OKHttpManager.getInstance().post(HCHttpRequestParam.checkStock(tv_vehicle_id.getText().toString()), this, 1);
        ll_refresh.setEnabled(false);
        pb_refresh.setVisibility(View.VISIBLE);
    }
}
