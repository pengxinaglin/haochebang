package com.haoche51.checker.fragment.approve;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haoche51.checker.DAO.DataObserver;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.adapter.RepurchaseApproveAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.PurchaseTaskShortEntity;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ACache;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 回购审批
 * Created by wfx on 2016/7/25.
 */
public class RepurchaseApproveFragment extends CommonBaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, HCPullToRefresh.OnRefreshCallback {

    private HCPullToRefresh mPullToRefresh;
    private ListView mListView;
    private List<PurchaseTaskShortEntity> mTasks = new ArrayList<>();
    private RepurchaseApproveAdapter mAdapter = null;
    private int pageIndex = 0;//当前页面索引
    private ACache mAcache;

    private DataObserver mDataObserver = new DataObserver(new Handler()) {
        @Override
        public void onChanged() {

        }

        @Override
        public void onChanged(Bundle data) {
            if (data.getBoolean(TaskConstants.BINDLE_IS_APPROVE, false)) {
                onPullDownRefresh();
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.fragment_trans_task;
    }

    @Override
    protected void initView(View view) {
        mPullToRefresh = (HCPullToRefresh) view.findViewById(R.id.pull_to_refresh);
        mPullToRefresh.setCanPull(true);
        mPullToRefresh.setOnRefreshCallback(this);
        mListView = mPullToRefresh.getListView();
        mListView.setDivider(getResources().getDrawable(R.color.hc_self_gray_bg));
        mListView.setDividerHeight(DisplayUtils.dip2px(getActivity(), 10));
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //初始化ACache
        if (mAcache == null) {
            mAcache = ACache.get(getActivity());
        }
        //读取ACache中的缓存记录
        String json = mAcache.getAsString(TaskConstants.ACACHE_REPURCHASE_APPROVE);
        if (!TextUtils.isEmpty(json)) {
            this.mTasks.clear();
//            List<PurchaseTaskShortEntity> tempList = new HCJsonParse().parsePurchaseShortEntitys(json);
            List<PurchaseTaskShortEntity> tempList = JsonParseUtil.fromJsonArray(json, PurchaseTaskShortEntity.class);
            if (tempList != null)
                this.mTasks.addAll(tempList);
        }
        if (mAdapter == null) {
            mAdapter = new RepurchaseApproveAdapter(getActivity(), mTasks, R.layout.item_repurchase_approve);
        }
        //设置数据
        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }
        //自动刷新
        mPullToRefresh.setFirstAutoRefresh();
        //设置监听数据变化
        HCTasksWatched.getInstance().registerDataObserver(mDataObserver);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (mTasks.size() == 0 || id == -1) {
            return;
        }

        Intent intent = new Intent(getActivity(), HCWebViewActivity.class);
        intent.putExtra(HCWebViewActivity.KEY_INTENT_EXTRA_URL, mTasks.get(position).getUrl());
        intent.putExtra(HCWebViewActivity.KEY_INTENT_EXTRA_APPROVE, true);
        startActivity(intent);
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (action.equals(HttpConstants.ACTION_GET_AUDIT_LIST)) {
            onListHttpResponse(response, requestId);
        }
    }

    /**
     * 列表页刷新返回解析
     *
     * @param response
     * @param requestId
     */
    private void onListHttpResponse(HCHttpResponse response, int requestId) {
        //停止刷新
        mPullToRefresh.finishRefresh();
        try {
//            dismissLoadingView();
            switch (response.getErrno()) {
                case 0:
//                    List<PurchaseTaskShortEntity> viewTaskList = new HCJsonParse().parsePurchaseShortEntitys(response.getData());
                    List<PurchaseTaskShortEntity> viewTaskList = JsonParseUtil.fromJsonArray(response.getData(), PurchaseTaskShortEntity.class);
                    //刷新
                    if (requestId == HttpConstants.GET_LIST_REFRESH) {
                        if (response.getData() != null)
                            mAcache.put(TaskConstants.ACACHE_REPURCHASE_APPROVE, response.getData());//只缓存刷新的数据
                        mTasks.clear();
                        if (viewTaskList != null)
                            mTasks.addAll(viewTaskList);
                    } else {
                        //把后来加载的添加进来
                        if (viewTaskList != null)
                            mTasks.addAll(viewTaskList);
                    }
                    //是否还有更多
                    boolean isNoMoreData = (viewTaskList == null || viewTaskList.isEmpty()) ? true : viewTaskList.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT;
                    mPullToRefresh.setFooterStatus(isNoMoreData);
                    //重置数据
                    mAdapter.notifyDataSetChanged();
                    //当没有数据时显示默认空白页
                    if (mTasks.size() == 0) {
                        mPullToRefresh.hideFooter();
                        showNoDataView(false, getString(R.string.hc_common_result_nodata), this);
                    } else {
                        dismissResultView();//有数据，显示出来数据列表页面
                    }
                    break;
                default:
                    mPullToRefresh.setFooterStatus(false);
                    ToastUtil.showInfo(response.getErrmsg());
                    //无网络视图
                    if (mTasks.size() == 0) {
                        showErrorView(false, this);
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        //当空数据列表时会回调此方法
        if (mPullToRefresh != null) {
            dismissResultView();
            onPullDownRefresh();//刷新重新拉取数据
        }
    }

    @Override
    public void onPullDownRefresh() {
        pageIndex = 0;
        OKHttpManager.getInstance().post(HCHttpRequestParam.getRepurchaseAproveList(pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT), this, HttpConstants.GET_LIST_REFRESH);//下拉刷新
    }

    @Override
    public void onLoadMoreRefresh() {
        OKHttpManager.getInstance().post(HCHttpRequestParam.getRepurchaseAproveList(++pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT), this, HttpConstants.GET_LIST_LOADMORE);//上拉刷新
    }

    @Override
    public void onDestroyView() {
        HCTasksWatched.getInstance().UnRegisterDataObserver(mDataObserver);
        super.onDestroyView();
    }
}