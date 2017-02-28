package com.haoche51.checker.fragment.lead;

import android.os.Bundle;
import android.view.View;

import com.haoche51.checker.adapter.BuyerCluesAdapter;
import com.haoche51.checker.entity.BuyerCluesEntity;
import com.haoche51.checker.fragment.BaseFragment;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * @File:BuyerCluesFragment.java
 * @Package:com.haoche51.checker.fragment
 * @desc:买家线索Frament
 * @author:zhuzuofei
 * @date:2015-4-1 下午3:11:28
 */
public class BuyerLeadFragment extends BaseFragment implements HCHttpCallback {
    private final int PAGE_SIZE = 10; //每次拉取条数
    private List<BuyerCluesEntity> allLeads = new ArrayList<>();
    private BuyerCluesAdapter mAdapter = null;
    private int PAGE = 0;
    private boolean mloadEnd;
    private Call mCall;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpFragment();
    }

    private void setUpFragment() {
        searchBox.setVisibility(View.GONE);
        mListView.setFooterInvisible();
        mListView.setOnItemClickListener(this);
//		mAdapter = new BuyerCluesAdapter(GlobalData.context, allLeads);
        mListView.setAdapter(mAdapter);
        //开始加载数据
        mListView.refreshListener.onRefresh();
    }

    @Override
    public void onRefresh() {
        PAGE = 0;
        mloadEnd = false;
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getBuyerleadList(PAGE, PAGE_SIZE), this, HttpConstants.GET_LIST_REFRESH);
    }

    @Override
    public void onLoadMore() {

        if (mloadEnd || allLeads == null || allLeads.size() < PAGE_SIZE)
            return;

        mloadEnd = true;
        PAGE++;
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getBuyerleadList(PAGE, PAGE_SIZE), this, HttpConstants.GET_LIST_LOADMORE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("buyerCluesList");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("buyerCluesList");
    }

    @Override
    public void onHttpStart(String action, int requestId) {

    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        switch (requestId) {
            case HttpConstants.GET_LIST_REFRESH:
                mListView.onRefreshComplete();
                switch (response.getErrno()) {
                    case 0:
//						allLeads = new HCJsonParse().parseBuyerCluesResult(response.getData());
                        allLeads = JsonParseUtil.fromJsonArray(response.getData(), BuyerCluesEntity.class);
//						mAdapter.updateBuyerClues(allLeads, 0);
                        if (allLeads != null && allLeads.size() < PAGE_SIZE) {
                            mloadEnd = true;
                            mListView.setFooterInvisible();
                        }
                        break;
                }
                break;
            case HttpConstants.GET_LIST_LOADMORE:
                mListView.onLoadMoreComplete();
//				List<BuyerCluesEntity> tempLeads = new HCJsonParse().parseBuyerCluesResult(response.getData());
                List<BuyerCluesEntity> tempLeads = JsonParseUtil.fromJsonArray(response.getData(), BuyerCluesEntity.class);
                if (tempLeads != null) {
                    allLeads.addAll(tempLeads);
//					mAdapter.updateBuyerClues(allLeads, 0);
                }
                if (tempLeads == null || tempLeads.size() < PAGE_SIZE) {
                    mloadEnd = true;
                    mListView.setFooterLoadEnd();
                } else {
                    mloadEnd = false;
                }
                break;
        }

        mAdapter.notifyDataSetInvalidated();

        if (error != null)
            ToastUtil.showInfo(response.getErrmsg());
    }

    @Override
    public void onHttpProgress(String action, int requestId, long bytesWritten, long totalSize) {

    }

    @Override
    public void onHttpRetry(String action, int requestId, int retryNo) {

    }

    @Override
    public void onHttpFinish(String action, int requestId) {

    }

    @Override
    public void onDestroyView() {
        OKHttpManager.getInstance().cancelRequest(mCall);
        super.onDestroyView();
    }
}