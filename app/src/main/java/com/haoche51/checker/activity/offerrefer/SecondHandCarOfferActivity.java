package com.haoche51.checker.activity.offerrefer;

import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.adapter.SecondHandCarOfferAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.OfferEntity;
import com.haoche51.checker.entity.OfferReferEntity;
import com.haoche51.checker.fragment.offerrefer.OrderFragment;
import com.haoche51.checker.fragment.offerrefer.SourceFragment;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ACache;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.NetInfoUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 二手车报价
 * Created by wfx on 2016/8/3.
 */
public class SecondHandCarOfferActivity extends CommonBaseFragmentActivity implements View.OnClickListener, HCPullToRefresh.OnRefreshCallback {

    @ViewInject(R.id.tv_order)
    public TextView tv_order;
    @ViewInject(R.id.tv_source)
    public TextView tv_source;
    @ViewInject(R.id.ll_order)
    private LinearLayout ll_order;
    @ViewInject(R.id.tv_common_title)
    private TextView tv_common_title;
    @ViewInject(R.id.tv_common_back)
    private TextView tv_common_back;
    @ViewInject(R.id.ll_source)
    private LinearLayout ll_source;
    @ViewInject(R.id.pull_to_refresh)
    private HCPullToRefresh mPullToRefresh;

    @ViewInject(R.id.fl_condition)
    private FrameLayout fl_condition;

    private OrderFragment mOrderFragment;
    private SourceFragment mSourceFragment;

    private ListView mListView;
    private ACache mAcache;
    private List<OfferEntity> offerEntities = new ArrayList<>();
    private SecondHandCarOfferAdapter mAdapter;
    private Call mCall;
    private OfferReferEntity offerReferEntity;
    private int pageIndex;

    public OfferReferEntity getOfferReferEntity() {
        return offerReferEntity;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_second_hand_car_offer);
        x.view().inject(this);

    }

    @Override
    protected void initData() {
        tv_common_title.setText(getResources().getString(R.string.second_hand_car_offer));
        tv_common_back.setOnClickListener(this);
        ll_order.setOnClickListener(this);
        ll_source.setOnClickListener(this);
        fl_condition.setOnClickListener(this);
        offerReferEntity = getIntent().getParcelableExtra(TaskConstants.BINDLE_MARKET_CONDITION);
        mPullToRefresh.setCanPull(true);
        mPullToRefresh.setOnRefreshCallback(this);
        mListView = mPullToRefresh.getListView();
        mListView.setDivider(getResources().getDrawable(R.color.hc_self_gray_bg));
        mListView.setDividerHeight(DisplayUtils.dip2px(this, 10));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mOrderFragment = new OrderFragment();
        mSourceFragment = new SourceFragment();
        ft.add(R.id.fl_condition, mOrderFragment);
        ft.add(R.id.fl_condition, mSourceFragment);
        ft.hide(mOrderFragment);
        ft.hide(mSourceFragment);
        ft.commitAllowingStateLoss();
        //加载本地缓存数据
        loadCacheData();
        super.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_order:
                order();
                break;
            case R.id.ll_source:
                souce();
                break;
            case R.id.fl_condition:
                closeChoosePanel();
                break;
            case R.id.tv_common_back:
                finish();
                break;
        }
    }

    /**
     * 关闭选择面板
     */
    private void closeChoosePanel() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_up_in, R.anim.anim_slide_down_out);
        if (mOrderFragment != null && mOrderFragment.isVisible()) {
            changeOrderStyle(false);
            ft.hide(mOrderFragment);
        }

        if (mSourceFragment != null && mSourceFragment.isVisible()) {
            changeSourceStyle(false);
            ft.hide(mSourceFragment);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 排序
     */
    private void order() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_up_in, R.anim.anim_slide_down_out);
        if (mOrderFragment == null) {
            mOrderFragment = new OrderFragment();
        }

        if (mOrderFragment.isVisible()) {
            changeOrderStyle(false);
            ft.hide(mOrderFragment);
        } else {
            if (mSourceFragment != null && mSourceFragment.isVisible()) {
                changeSourceStyle(false);
                ft.hide(mSourceFragment);
            }
            changeOrderStyle(true);
            ft.show(mOrderFragment);
        }
        ft.commitAllowingStateLoss();

    }

    /**
     * 来源
     */
    private void souce() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_up_in, R.anim.anim_slide_down_out);
        if (mSourceFragment == null) {
            mSourceFragment = new SourceFragment();
        }

        if (mSourceFragment.isVisible()) {
            changeSourceStyle(false);
            ft.hide(mSourceFragment);
        } else {
            if (mOrderFragment != null && mOrderFragment.isVisible()) {
                changeOrderStyle(false);
                ft.hide(mOrderFragment);
            }
            changeSourceStyle(true);
            ft.show(mSourceFragment);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 修改"排序"的样式
     *
     * @param isOpen 是否打开：true是,false否
     */
    public void changeOrderStyle(boolean isOpen) {
        if (isOpen) {
            tv_order.setTextColor(getResources().getColor(R.color.hc_self_red));
            tv_order.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_open, 0);
            changeBgAlpha(0.1f);
            fl_condition.setVisibility(View.VISIBLE);
        } else {
            tv_order.setTextColor(getResources().getColor(R.color.self_black));
            tv_order.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_close, 0);
            changeBgAlpha(1f);
            fl_condition.setVisibility(View.GONE);
        }
    }

    /**
     * 修改"来源"的样式
     *
     * @param isOpen 是否打开：true是,false否
     */
    public void changeSourceStyle(boolean isOpen) {
        if (isOpen) {
            tv_source.setTextColor(getResources().getColor(R.color.hc_self_red));
            tv_source.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_open, 0);
            changeBgAlpha(0.1f);
            fl_condition.setVisibility(View.VISIBLE);
        } else {
            tv_source.setTextColor(getResources().getColor(R.color.self_black));
            tv_source.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_close, 0);
            changeBgAlpha(1f);
            fl_condition.setVisibility(View.GONE);
        }
    }

    /**
     * 改变背景透明度
     *
     * @param bgAlpha
     */
    private void changeBgAlpha(float bgAlpha) {
        mPullToRefresh.setAlpha(bgAlpha);
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
        String json = mAcache.getAsString(TaskConstants.ACACHE_SECOND_HAND_CAR_OFFER);
        if (!TextUtils.isEmpty(json)) {
            this.offerEntities.clear();
//            List<OfferEntity> tempList = new HCJsonParse().parseToOfferList(json);
            List<OfferEntity> tempList = JsonParseUtil.fromJsonArray(json, OfferEntity.class);
            if (tempList != null && tempList.size() > 0) {
                this.offerEntities.addAll(tempList);
            }
        }
        if (mAdapter == null) {
            mAdapter = new SecondHandCarOfferAdapter(this, offerEntities, R.layout.item_second_hand_car_offer);
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
        pageIndex = 0;
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getSecondHandCarList(offerReferEntity, pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT),
                this, HttpConstants.GET_LIST_REFRESH);
    }

    @Override
    public void onLoadMoreRefresh() {
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getSecondHandCarList(offerReferEntity, ++pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT), this, HttpConstants.GET_LIST_LOADMORE);//上拉刷新
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        //停止刷新
        mPullToRefresh.finishRefresh();

        if (HttpConstants.ACTION_SECOND_HAND_LIST.equals(action)) {
            parseSecondHandToList(response, requestId);
        }
    }

    /**
     * 解析二手车报价列表
     *
     * @param response
     * @param requestId
     */
    private void parseSecondHandToList(HCHttpResponse response, int requestId) {
        try {
            switch (response.getErrno()) {
                case 0:
//                    List<OfferEntity> tempList = new HCJsonParse().parseToOfferList(response.getData());
                    List<OfferEntity> tempList = JsonParseUtil.fromJsonArray(response.getData(), OfferEntity.class);
                    //是否还有更多
                    boolean isNoMoreData = tempList == null ? true : tempList.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT;
                    mPullToRefresh.setFooterStatus(isNoMoreData);
                    //刷新
                    if (requestId == HttpConstants.GET_LIST_REFRESH) {
                        if (response.getData() != null) {
                            //只缓存刷新的数据
                            mAcache.put(TaskConstants.ACACHE_SECOND_HAND_CAR_OFFER, response.getData());
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
                    mPullToRefresh.setFooterStatus(false);
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
            //停止刷新
//            mPullToRefresh.finishRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        //停止刷新
        mPullToRefresh.finishRefresh();
        OKHttpManager.getInstance().cancelRequest(mCall);
        super.onDestroy();
    }
}
