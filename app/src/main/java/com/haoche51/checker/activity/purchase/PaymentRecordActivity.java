package com.haoche51.checker.activity.purchase;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.PaymentRecordAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.PaymentRecordEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ACache;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.NetInfoUtil;
import com.haoche51.checker.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 付款记录列表
 * Created by wfx on 2016/3/28.
 */
public class PaymentRecordActivity extends CommonTitleBaseActivity implements HCPullToRefresh.OnRefreshCallback {
    private HCPullToRefresh mPullToRefresh;
    private ListView mListView;
    private View contentView;
    private ACache mAcache;
    private List<PaymentRecordEntity> paymentRecordEntities = new ArrayList<>();
    private PaymentRecordAdapter mAdapter;
    private Call mCall;
    private int taskId;


    @Override
    public View getHCContentView() {
        contentView = View.inflate(this, R.layout.activity_pay_record, null);
        taskId = getIntent().getIntExtra("taskId", 0);
        return contentView;
    }


    @Override
    public void initContentView(Bundle saveInstanceState) {
        mPullToRefresh = (HCPullToRefresh) contentView.findViewById(R.id.pull_to_refresh);
        mPullToRefresh.setCanPull(true);
        mPullToRefresh.setOnRefreshCallback(this);
        mPullToRefresh.hideFooter();
        mListView = mPullToRefresh.getListView();
        mListView.setDivider(getResources().getDrawable(R.color.hc_self_gray_bg));
        mListView.setDividerHeight(DisplayUtils.dip2px(this, 10));
        //加载本地缓存数据
        loadCacheData();
    }


    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.pay_records));
    }


    /**
     * 加载本地缓存数据
     */
    protected void loadCacheData() {
        //初始化ACache
        if (mAcache == null) {
            mAcache = ACache.get(this);
        }
        //读取ACache中的缓存记录
        String json = mAcache.getAsString(TaskConstants.ACACHE_APPLY_PAYMENT);
        if (!TextUtils.isEmpty(json)) {
            this.paymentRecordEntities.clear();
//            List<PaymentRecordEntity> tempList = new HCJsonParse().parsePaymentRecordToList(json);
            List<PaymentRecordEntity> tempList = JsonParseUtil.fromJsonArray(json, PaymentRecordEntity.class);
            if (tempList != null && tempList.size() > 0) {
                this.paymentRecordEntities.addAll(tempList);
            }
        }
        if (mAdapter == null) {
            mAdapter = new PaymentRecordAdapter(this, paymentRecordEntities, R.layout.item_pay_record);
        }
        //设置数据
        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }
        if (NetInfoUtil.isNetAvaliable()) {
            onPullDownRefresh();
        }
    }


    @Override
    public void onPullDownRefresh() {
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getPayRecords(taskId),
                this, HttpConstants.GET_LIST_REFRESH);
    }

    @Override
    public void onLoadMoreRefresh() {
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        //停止刷新
        mPullToRefresh.finishRefresh();

        if (HttpConstants.ACTION_GET_APPLY_RECORDS_LIST.equals(action)) {
            parsePayRecordsToList(response, requestId);
        }
    }

    /**
     * 解析付款记录列表
     *
     * @param response
     * @param requestId
     */
    private void parsePayRecordsToList(HCHttpResponse response, int requestId) {
        try {
            switch (response.getErrno()) {
                case 0:
//                    List<PaymentRecordEntity> tempList = new HCJsonParse().parsePaymentRecordToList(response.getData());
                    List<PaymentRecordEntity> tempList = JsonParseUtil.fromJsonArray(response.getData(), PaymentRecordEntity.class);
                    mPullToRefresh.setFooterStatus(true);
                    //刷新
                    if (requestId == HttpConstants.GET_LIST_REFRESH) {
                        if (response.getData() != null) {
                            mAcache.put(TaskConstants.ACACHE_APPLY_PAYMENT, response.getData());//只缓存刷新的数据
                        }
                        paymentRecordEntities.clear();
                    }
                    //把后来加载的添加进来
                    if (tempList != null) {
                        paymentRecordEntities.addAll(tempList);
                    }
                    //重置数据
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    mPullToRefresh.setFooterStatus(true);
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        //停止刷新
        mPullToRefresh.finishRefresh();
        //如果请求还没完成，界面就被关闭了，那么就取消请求
        OKHttpManager.getInstance().cancelRequest(mCall);
        super.onDestroy();
    }
}
