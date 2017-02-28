package com.haoche51.checker.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.DAO.DataObserver;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.RevisitRecordAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.RevisitRecordEntity;
import com.haoche51.checker.listener.HCTasksWatched;
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
 * 回访记录列表
 * Created by wfx on 2016/7/18.
 */
public class RevisitRecordActivity extends CommonTitleBaseActivity implements HCPullToRefresh.OnRefreshCallback {
    private HCPullToRefresh mPullToRefresh;
    private ListView mListView;
    private View contentView;
    private ACache mAcache;
    private List<RevisitRecordEntity> revisitRecordEntities = new ArrayList<>();
    private RevisitRecordAdapter mAdapter;
    private Call mCall;
    private int taskId;
    private DataObserver mDataObServer = new DataObserver(new Handler()) {
        @Override
        public void onChanged() {
        }

        @Override
        public void onChanged(Bundle data) {
            if (data != null && data.getBoolean(TaskConstants.BINDLE_ADD_REVISIT_RECORD)) {
                onPullDownRefresh();
            }
        }
    };

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
        HCTasksWatched.getInstance().registerDataObserver(mDataObServer);
        //加载本地缓存数据
        loadCacheData();
    }


    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.revisit_record));
        mRightFaction.setVisibility(View.VISIBLE);
        mRightFaction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_top_add, 0, 0, 0);
        mRightFaction.setText(getString(R.string.vehicle_subscribe_condition_add));
        mRightFaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RevisitRecordActivity.this, AddRevisitRecordActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);
            }
        });
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
        String json = mAcache.getAsString(TaskConstants.ACACHE_VISIT_RECORD);
        if (!TextUtils.isEmpty(json)) {
            this.revisitRecordEntities.clear();
//            List<RevisitRecordEntity> tempList = new HCJsonParse().parseRevisitRecordToList(json);
            List<RevisitRecordEntity> tempList = JsonParseUtil.fromJsonArray(json, RevisitRecordEntity.class);
            if (tempList != null && tempList.size() > 0) {
                this.revisitRecordEntities.addAll(tempList);
            }
        }
        if (mAdapter == null) {
            mAdapter = new RevisitRecordAdapter(this, revisitRecordEntities, R.layout.item_revisit_record);
        }
        //设置数据
        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }
        if (NetInfoUtil.isNetAvaliable()) {
            onPullDownRefresh();
            return;
        }
    }


    @Override
    public void onPullDownRefresh() {
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getRevisitRecords(taskId),
                this, HttpConstants.GET_LIST_REFRESH);
    }

    @Override
    public void onLoadMoreRefresh() {
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        //停止刷新
        mPullToRefresh.finishRefresh();
        if (HttpConstants.ACTION_GET_REVISIT_LIST.equals(action)) {
            parseRevisitRecordsToList(response, requestId);
        }
    }

    /**
     * 解析回访记录列表
     *
     * @param response
     * @param requestId
     */
    private void parseRevisitRecordsToList(HCHttpResponse response, int requestId) {
        try {
            switch (response.getErrno()) {
                case 0:
//                    List<RevisitRecordEntity> tempList = new HCJsonParse().parseRevisitRecordToList(response.getData());
                    List<RevisitRecordEntity> tempList = JsonParseUtil.fromJsonArray(response.getData(), RevisitRecordEntity.class);
                    mPullToRefresh.setFooterStatus(true);
                    //刷新
                    if (requestId == HttpConstants.GET_LIST_REFRESH) {
                        if (response.getData() != null) {
                            mAcache.put(TaskConstants.ACACHE_VISIT_RECORD, response.getData());//只缓存刷新的数据
                        }
                        revisitRecordEntities.clear();
                    }
                    //把后来加载的添加进来
                    if (tempList != null) {
                        revisitRecordEntities.addAll(tempList);
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
        HCTasksWatched.getInstance().UnRegisterDataObserver(mDataObServer);
        super.onDestroy();
    }
}
