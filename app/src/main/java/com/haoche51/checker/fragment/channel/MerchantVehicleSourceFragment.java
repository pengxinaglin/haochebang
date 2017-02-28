package com.haoche51.checker.fragment.channel;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haoche51.checker.DAO.DataObserver;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.channel.VehicleSourceDetailActivity;
import com.haoche51.checker.adapter.MerchantVehicleSourceAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.MerchantVehicleSourceEntity;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家车源列表
 * Created by PengXianglin on 16/3/4.
 */
public class MerchantVehicleSourceFragment extends CommonBaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, HCPullToRefresh.OnRefreshCallback {

	private HCPullToRefresh mPullToRefresh;
	private ListView mListView;
	private List<MerchantVehicleSourceEntity> mTasks = new ArrayList<MerchantVehicleSourceEntity>();
	private MerchantVehicleSourceAdapter mAdapter = null;

	private int id;//商家id
	private int pageIndex = 0;//当前页面索引
	private String keyword;
	private int mClickIndex;

	@Override
	protected int getContentView() {
		return R.layout.fragment_trans_task;
	}

	@Override
	protected void initView(View view) {
		id = (int) getArguments().get("id");
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
		if (mAdapter == null) {
			mAdapter = new MerchantVehicleSourceAdapter(getActivity(), mTasks, R.layout.layout_merchant_vehicle_source_item);
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

	DataObserver mDataObserver = new DataObserver(new Handler()) {
		@Override
		public void onChanged() {
		}

		@Override
		public void onChanged(Bundle data) {
			try {
				if (data != null) {
					if (mTasks == null || mTasks.isEmpty() || mClickIndex >= mTasks.size()) return;
					if (data.getBoolean(TaskConstants.BINDLE_DETAIL_TASK)) return;

					MerchantVehicleSourceEntity entity = mTasks.get(mClickIndex);
					//下线
					if (data.getBoolean(VehicleSourceFragment.BUNDLE_OFFLINE_CAR) && data.getInt("id") == entity.getId()) {
						mTasks.remove(entity);
					}//在售
					else if (data.getBoolean(VehicleSourceFragment.BUNDLE_CONFIRM_SELL) && data.getInt("id") == entity.getId()) {
						entity.setUp_time(data.getString("up_time"));
					}
					//修改报价
					else if (data.getBoolean(VehicleSourceFragment.BUNDLE_CHANGE_PRICE) && data.getInt("id") == entity.getId()) {
						entity.setPrice(data.getString("price"));
					}

					mAdapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroyView();
		HCTasksWatched.getInstance().UnRegisterDataObserver(mDataObserver);
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (action.equals(HttpConstants.ACTION_DEALCARAPI_DEALCARLIST)) {
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
//					List<MerchantVehicleSourceEntity> viewTaskList = new HCJsonParse().parseMerchantVehicleSourceList(response.getData());
					List<MerchantVehicleSourceEntity> viewTaskList = JsonParseUtil.fromJsonArray(response.getData(), MerchantVehicleSourceEntity.class);
					//刷新
					if (requestId == HttpConstants.GET_LIST_REFRESH) {
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
//			showLoadingView(false);
			onPullDownRefresh();//刷新重新拉取数据
		}
	}

	@Override
	public void doingFilter(Bundle where) {
		//加载网络筛选
		if (where != null)
			keyword = where.getString("keyword", null);
		if (mPullToRefresh != null)
			mPullToRefresh.setFirstAutoRefresh();//刷新
	}

	@Override
	public void onPullDownRefresh() {
		pageIndex = 0;
		OKHttpManager.getInstance().post(HCHttpRequestParam.getMerchantVehicleSourceList(id, pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT, keyword),
			this, HttpConstants.GET_LIST_REFRESH);//下拉刷新
	}

	@Override
	public void onLoadMoreRefresh() {
		OKHttpManager.getInstance().post(HCHttpRequestParam.getMerchantVehicleSourceList(id, ++pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT, keyword),
			this, HttpConstants.GET_LIST_LOADMORE);//上拉刷新
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		if (mTasks == null || mTasks.size() <= position) {
			return;
		}
		mClickIndex = position;
		Map<String, Object> map = new HashMap<>();
		map.put("vehicleSourceId", mTasks.get(position).getId());
		HCActionUtil.launchActivity(GlobalData.context, VehicleSourceDetailActivity.class, map);
	}
}
