package com.haoche51.checker.fragment.channel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.channel.FindCarDetailActivity;
import com.haoche51.checker.adapter.FindCarAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.FindCarEntity;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ACache;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 找车需求列表
 * Created by wfx@20161031
 */
public class FindCarFragment extends CommonBaseFragment implements AdapterView.OnItemClickListener, HCPullToRefresh.OnRefreshCallback, View.OnClickListener {

    private HCPullToRefresh mPullToRefresh;
    private ListView mListView;
    private List<FindCarEntity> mTasks = new ArrayList<>();
    private FindCarAdapter mAdapter = null;
    private int pageIndex = 0;//当前页面索引
    private ACache mAcache;
    private Call mCall;
    private int mClickIndex;


    @Override
    protected int getContentView() {
        return R.layout.fragment_find_car;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(FindCarEntity carEntity) {
        if (carEntity != null && mTasks != null && mTasks.size() >= mClickIndex + 1) {
            mTasks.remove(mClickIndex);
            if (mAdapter == null) {
                mAdapter = new FindCarAdapter(getActivity(), mTasks, R.layout.item_find_car);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
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
        if (mAcache == null)
            mAcache = ACache.get(getActivity());
        //读取ACache中的缓存记录
        String json = mAcache.getAsString(TaskConstants.ACACHE_FIND_CAR);
        if (!TextUtils.isEmpty(json)) {
            this.mTasks.clear();
            List<FindCarEntity> tempList = JsonParseUtil.fromJsonArray(json, FindCarEntity.class);
            if (tempList != null)
                this.mTasks.addAll(tempList);
        }
        if (mAdapter == null)
            mAdapter = new FindCarAdapter(getActivity(), mTasks, R.layout.item_find_car);
        //设置数据
        if (mListView != null)
            mListView.setAdapter(mAdapter);
        //自动刷新
        mPullToRefresh.setFirstAutoRefresh();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (mTasks == null || mTasks.size() == 0) {
            return;
        }
        mClickIndex = position;
        FindCarEntity findCarEntity = mTasks.get(position);
        Intent intent = new Intent(getActivity(), FindCarDetailActivity.class);
        intent.putExtra(TaskConstants.BINDLE_FIND_CAR, findCarEntity);
        startActivity(intent);
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (action.equals(HttpConstants.ACTION_GET_FIND_CAR_LIST)) {
            onListHttpResponse(response, requestId, error);
        }
    }

    /**
     * 列表页刷新返回解析
     *
     * @param response
     * @param requestId
     */
    private void onListHttpResponse(HCHttpResponse response, int requestId, Throwable error) {
        try {
            switch (response.getErrno()) {
                case 0:
                    List<FindCarEntity> tempList = JsonParseUtil.fromJsonArray(response.getData(), FindCarEntity.class);
                    //是否还有更多
                    boolean isNoMoreData = tempList == null ? true : tempList.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT;
                    mPullToRefresh.setFooterStatus(isNoMoreData);
                    //刷新
                    if (requestId == HttpConstants.GET_LIST_REFRESH) {
                        if (response.getData() != null)
                            mAcache.put(TaskConstants.ACACHE_FIND_CAR, response.getData());//只缓存刷新的数据
                        mTasks.clear();
                        if (tempList != null) {
                            mTasks.addAll(tempList);
                        }
                    } else {
                        //把后来加载的添加进来
                        if (tempList != null) {
                            mTasks.addAll(tempList);
                        }
                    }
                    //重置数据
                    mAdapter.notifyDataSetChanged();
                    //当没有数据时显示默认空白页
                    if (mTasks.size() == 0)
                        showNoDataView(false, getString(R.string.hc_common_result_nodata), this);
                    else
                        dismissResultView();//有数据，显示出来数据列表页面
                    break;
                case -100:
                    mPullToRefresh.setFooterStatus(false);
                    if (error != null) {
                        ToastUtil.showInfo(error.getMessage());
                    }
                    break;
                default:
                    mPullToRefresh.setFooterStatus(false);
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
            //停止刷新
            mPullToRefresh.finishRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPullDownRefresh() {
        pageIndex = 0;
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getFindCarList(pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT),
                this, HttpConstants.GET_LIST_REFRESH);
    }

    @Override
    public void onLoadMoreRefresh() {
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getFindCarList(++pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT),
                this, HttpConstants.GET_LIST_LOADMORE);
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
    public void onDestroyView() {
        OKHttpManager.getInstance().cancelRequest(mCall);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}