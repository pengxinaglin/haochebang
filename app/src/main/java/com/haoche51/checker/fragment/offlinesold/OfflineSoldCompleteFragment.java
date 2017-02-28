package com.haoche51.checker.fragment.offlinesold;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.offlinesold.OfflineSoldSaleDetailActivity;
import com.haoche51.checker.adapter.OfflineSoldStockAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.OfflineShortEntity;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ACache;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线下售出—已完成列表
 * Created by mac on 16/01/11.
 */
public class OfflineSoldCompleteFragment extends CommonBaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, HCPullToRefresh.OnRefreshCallback {

  private HCPullToRefresh mPullToRefresh;
  private ListView mListView;
  private List<OfflineShortEntity> mTasks = new ArrayList<>();
  private OfflineSoldStockAdapter mAdapter = null;

  private int pageIndex = 0;//当前页面索引
  private ACache mAcache;
  private int mClickIndex;
  private String search;

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
    String json = mAcache.getAsString(TaskConstants.ACACHE_OFFLINESOLD_COMPLETE);
    if (!TextUtils.isEmpty(json)) {
      this.mTasks.clear();
//      List<OfflineShortEntity> tempList = new HCJsonParse().parseOfflineShortEntitys(json);
      List<OfflineShortEntity> tempList = JsonParseUtil.fromJsonArray(json, OfflineShortEntity.class);
      if (tempList != null)
        this.mTasks.addAll(tempList);
    }
    if (mAdapter == null) {
      mAdapter = new OfflineSoldStockAdapter(getActivity(), mTasks, R.layout.layout_offlinesold_stock_item, TaskConstants.REQUEST_OFFLINESOLD_COMPLETE);
    }
    //设置数据
    if (mListView != null) {
      mListView.setAdapter(mAdapter);
    }
    //自动刷新
    mPullToRefresh.setFirstAutoRefresh();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    mClickIndex = position;
    if (mTasks.size() == 0 || id == -1)
      return;
    Map<String, Object> map = new HashMap<>();
    map.put("id", mTasks.get(position).getStock_id());
    HCActionUtil.launchActivity(GlobalData.context, OfflineSoldSaleDetailActivity.class, map);
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    if (action.equals(HttpConstants.ACTION_GET_BACK_STOCK_LIST)) {
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
    try {
      dismissLoadingView();
      switch (response.getErrno()) {
        case 0:
//          List<OfflineShortEntity> viewTaskList = new HCJsonParse().parseOfflineShortEntitys(response.getData());
          List<OfflineShortEntity> viewTaskList = JsonParseUtil.fromJsonArray(response.getData(), OfflineShortEntity.class);
          //刷新
          if (requestId == HttpConstants.GET_LIST_REFRESH) {
            if (response.getData() != null)
              mAcache.put(TaskConstants.ACACHE_OFFLINESOLD_COMPLETE, response.getData());//只缓存刷新的数据
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
      //停止刷新
      mPullToRefresh.finishRefresh();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClick(View view) {
    //当空数据列表时会回调此方法
    if (mPullToRefresh != null) {
      dismissResultView();
//      showLoadingView(false);
      onPullDownRefresh();//刷新重新拉取数据
    }
  }

  @Override
  public void doingFilter(Bundle where) {
    search = where.getString("search_field", null);
    onPullDownRefresh();
  }


  @Override
  public void onPullDownRefresh() {
    pageIndex = 0;
    OKHttpManager.getInstance().post(HCHttpRequestParam.getBackStockList(pageIndex, TaskConstants.REQUEST_OFFLINESOLD_COMPLETE, TaskConstants.DEFAULT_SHOWTASK_COUNT, search), this, HttpConstants.GET_LIST_REFRESH);//下拉刷新
  }

  @Override
  public void onLoadMoreRefresh() {
    OKHttpManager.getInstance().post(HCHttpRequestParam.getBackStockList(++pageIndex, TaskConstants.REQUEST_OFFLINESOLD_COMPLETE, TaskConstants.DEFAULT_SHOWTASK_COUNT, search), this, HttpConstants.GET_LIST_LOADMORE);//上拉刷新
  }
}