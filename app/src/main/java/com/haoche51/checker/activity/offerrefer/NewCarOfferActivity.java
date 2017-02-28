package com.haoche51.checker.activity.offerrefer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.NewCarOfferAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.OfferEntity;
import com.haoche51.checker.entity.OfferReferEntity;
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
 * 新车报价
 * Created by wfx on 2016/8/3.
 */
public class NewCarOfferActivity extends CommonTitleBaseActivity implements HCPullToRefresh.OnRefreshCallback {
    private HCPullToRefresh mPullToRefresh;
    private ListView mListView;
    private View contentView;
    private ACache mAcache;
    private List<OfferEntity> offerEntities = new ArrayList<>();
    private NewCarOfferAdapter mAdapter;
    private Call mCall;
    private OfferReferEntity offerReferEntity;

    @Override
    public View getHCContentView() {
        contentView = View.inflate(this, R.layout.activity_pay_record, null);
        offerReferEntity = getIntent().getParcelableExtra(TaskConstants.BINDLE_MARKET_CONDITION);
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
        mTitle.setText(getResources().getString(R.string.hc_new_car_offer));
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
        String json = mAcache.getAsString(TaskConstants.ACACHE_NEW_CAR_OFFER);
        if (!TextUtils.isEmpty(json)) {
            this.offerEntities.clear();
//            List<OfferEntity> tempList = new HCJsonParse().parseToOfferList(json);
            List<OfferEntity> tempList = JsonParseUtil.fromJsonArray(json, OfferEntity.class);
            if (tempList != null && tempList.size() > 0) {
                this.offerEntities.addAll(tempList);
            }
        }
        if (mAdapter == null) {
            mAdapter = new NewCarOfferAdapter(this, offerEntities, R.layout.item_new_car_offer);
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
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getNewCarOfferList(offerReferEntity),
                this, HttpConstants.GET_LIST_REFRESH);
    }

    @Override
    public void onLoadMoreRefresh() {
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        //停止刷新
        mPullToRefresh.finishRefresh();

        if (HttpConstants.ACTION_NEW_CAR_LIST.equals(action)) {
            parseNewCarToList(response, requestId);
        }
    }

    /**
     * 解析新车报价列表
     *
     * @param response
     * @param requestId
     */
    private void parseNewCarToList(HCHttpResponse response, int requestId) {
        try {
            switch (response.getErrno()) {
                case 0:
//                    List<OfferEntity> tempList = new HCJsonParse().parseToOfferList(response.getData());
                    List<OfferEntity> tempList = JsonParseUtil.fromJsonArray(response.getData(), OfferEntity.class);
                    //是否还有更多
//                    boolean isNoMoreData = tempList == null ? true : tempList.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT;
                    mPullToRefresh.setFooterStatus(true);
                    //刷新
                    if (requestId == HttpConstants.GET_LIST_REFRESH) {
                        if (response.getData() != null) {
                            //只缓存刷新的数据
                            mAcache.put(TaskConstants.ACACHE_NEW_CAR_OFFER, response.getData());
                        }
                        offerEntities.clear();
                    }
                    //把后来加载的添加进来
                    if (tempList != null) {
                        offerEntities.addAll(tempList);
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
