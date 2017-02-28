package com.haoche51.checker.fragment.evaluate;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haoche51.checker.DAO.DataObserver;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.CheckTaskDetailActivity;
import com.haoche51.checker.adapter.CheckTaskAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ACache;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.IntentExtraMap;
import com.haoche51.checker.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * 验车任务待处理列表
 * Created by mac on 15/12/1.
 */
public class CheckTaskFragment extends CommonBaseFragment implements AdapterView.OnItemClickListener, HCPullToRefresh.OnRefreshCallback, View.OnClickListener {

    public static final String ONCHANGED_STATRCHECK = "statrCheck", ONCHANGED_COMPLETECHECK = "completeCheck",
            ONCHANGED_FINISHTASK = "finishtask", ONCHANGED_CHECKER_COMMENT = "checker_comment", ONCHANGED_REFRESH_DATA = "refresh_data", ONCHANGED_APPOINTMENT_PLACE = "app_place";
    private HCPullToRefresh mPullToRefresh;
    private ListView mListView;
    private List<CheckTaskEntity> mTasks = new ArrayList<>();
    private CheckTaskAdapter mAdapter = null;
    private int pageIndex = 0;//当前页面索引
    private ACache mAcache;
    private int mClickIndex;
    private Call mCall;
    DataObserver mDataObserver = new DataObserver(new Handler()) {
        @Override
        public void onChanged() {
            try {
                if (mTasks == null || mTasks.isEmpty() || mClickIndex >= mTasks.size()) return;
                // 最后一条数据被处理之后，会触发监听，下面一行会有数组越界异常，被catch住了，并会crash，在此标注。IndexOutOfBoundsException
                mTasks.remove(mClickIndex);
                if (mTasks.size() > TaskConstants.DEFAULT_SHOWTASK_COUNT)
//                    mAcache.put(TaskConstants.ACACHE_CHECK_TASK, new HCJsonParse().parseGetCheckTaskListToJson(mTasks.subList(0, TaskConstants.DEFAULT_SHOWTASK_COUNT)));//缓存删除过本条数据后的json
                    mAcache.put(TaskConstants.ACACHE_CHECK_TASK, JsonParseUtil.toJsonArray(mTasks.subList(0, TaskConstants.DEFAULT_SHOWTASK_COUNT)));//缓存删除过本条数据后的json
                else
//                    mAcache.put(TaskConstants.ACACHE_CHECK_TASK, new HCJsonParse().parseGetCheckTaskListToJson(mTasks));//缓存删除过本条数据后的json
                    mAcache.put(TaskConstants.ACACHE_CHECK_TASK, JsonParseUtil.toJsonArray(mTasks));//缓存删除过本条数据后的json
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onChanged(Bundle data) {
            try {
                if (data != null) {
                    if (mTasks == null || mTasks.isEmpty() || mClickIndex >= mTasks.size()) return;
                    if (data.getBoolean(TaskConstants.BINDLE_DETAIL_TASK)) return;
                    CheckTaskEntity entity = mTasks.get(mClickIndex);
                    if (data.getBoolean(ONCHANGED_STATRCHECK)) {
                        entity.setCheck_status(TaskConstants.CHECK_STATUS_ONGOING);//标识为验车中
                    } else if (data.getBoolean(ONCHANGED_COMPLETECHECK)) {
                        entity.setCheck_status(TaskConstants.CHECK_STATUS_TOUPLOAD);//标识为待上传
                    } else if (!data.getBoolean(ONCHANGED_FINISHTASK) && !TextUtils.isEmpty(data.getString(ONCHANGED_CHECKER_COMMENT, ""))) {
                        entity.setChecker_comment(data.getString(ONCHANGED_CHECKER_COMMENT, ""));//修改备注
                    } else if (data.getBoolean(ONCHANGED_REFRESH_DATA)) {
                        mPullToRefresh.autoRefresh();
                    } else if (data.getBoolean(ONCHANGED_APPOINTMENT_PLACE)) {//修改地点
                        entity.setAppointment_place(data.getString("appointment_place"));
                    }
                    if (mTasks.size() > TaskConstants.DEFAULT_SHOWTASK_COUNT)
//                        mAcache.put(TaskConstants.ACACHE_CHECK_TASK, new HCJsonParse().parseGetCheckTaskListToJson(mTasks.subList(0, TaskConstants.DEFAULT_SHOWTASK_COUNT)));//缓存删除过本条数据后的json
                        mAcache.put(TaskConstants.ACACHE_CHECK_TASK, JsonParseUtil.toJsonArray(mTasks.subList(0, TaskConstants.DEFAULT_SHOWTASK_COUNT)));//缓存删除过本条数据后的json
                    else
//                        mAcache.put(TaskConstants.ACACHE_CHECK_TASK, new HCJsonParse().parseGetCheckTaskListToJson(mTasks));//缓存删除过本条数据后的json
                        mAcache.put(TaskConstants.ACACHE_CHECK_TASK, JsonParseUtil.toJsonArray(mTasks));//缓存删除过本条数据后的json
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected int getContentView() {
        return R.layout.fragment_check_task;
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
        String json = mAcache.getAsString(TaskConstants.ACACHE_CHECK_TASK);
        if (!TextUtils.isEmpty(json)) {
            this.mTasks.clear();
//            List<CheckTaskEntity> tempList = new HCJsonParse().parseGetCheckTaskListResult(json);
            List<CheckTaskEntity> tempList = JsonParseUtil.fromJsonArray(json, CheckTaskEntity.class);
            if (tempList != null)
                this.mTasks.addAll(tempList);
        }
        if (mAdapter == null)
            mAdapter = new CheckTaskAdapter(getActivity(), mTasks, R.layout.layout_checktask_item);
        else
            mAdapter.filterUploadingTask(this.mTasks);
        //设置数据
        if (mListView != null)
            mListView.setAdapter(mAdapter);
        //自动刷新
        mPullToRefresh.setFirstAutoRefresh();
        //设置监听数据变化
        HCTasksWatched.getInstance().registerDataObserver(mDataObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HCTasksWatched.getInstance().UnRegisterDataObserver(mDataObserver);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        mClickIndex = position;
        if (mTasks == null || mTasks.size() == 0 || id == -1 || mClickIndex >= mTasks.size()) {
            return;
        }

        CheckTaskEntity eTask = mTasks.get(position);
        HCActionUtil.launchActivity(GlobalData.context, CheckTaskDetailActivity.class, IntentExtraMap.putId(eTask.getId()));
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (action.equals(HttpConstants.ACTION_GET_CHECKTASK_LIST_NEW)) {
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
//                    List<CheckTaskEntity> tempList = new HCJsonParse().parseGetCheckTaskListResult(response.getData());
                    List<CheckTaskEntity> tempList = JsonParseUtil.fromJsonArray(response.getData(), CheckTaskEntity.class);
                    //是否还有更多
                    boolean isNoMoreData = tempList == null ? true : tempList.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT;
                    mPullToRefresh.setFooterStatus(isNoMoreData);
                    //刷新
                    if (requestId == HttpConstants.GET_LIST_REFRESH) {
                        if (response.getData() != null)
                            mAcache.put(TaskConstants.ACACHE_CHECK_TASK, response.getData());//只缓存刷新的数据
                        mTasks.clear();
                        if (tempList != null) {
                            mAdapter.filterUploadingTask(tempList);
                            mTasks.addAll(tempList);
                        }
                    } else {
                        //把后来加载的添加进来
                        if (tempList != null) {
                            mAdapter.filterUploadingTask(tempList);
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
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckTaskListNew(pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT, TaskConstants.REQUEST_CHECK_STATUS_PENDING, null),
                this, HttpConstants.GET_LIST_REFRESH);
    }

    @Override
    public void onLoadMoreRefresh() {
        mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckTaskListNew(++pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT, TaskConstants.REQUEST_CHECK_STATUS_PENDING, null),
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
        super.onDestroyView();
    }
}