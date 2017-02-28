package com.haoche51.checker.fragment.channel;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.DAO.DataObserver;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.channel.ChannelMainActivity;
import com.haoche51.checker.activity.channel.VehicleSourceDetailActivity;
import com.haoche51.checker.adapter.VehicleSourceAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.LocalCheckerEntity;
import com.haoche51.checker.entity.VehicleSeriesEntity;
import com.haoche51.checker.entity.VehicleSourceShortEntity;
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
import com.haoche51.checker.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车源列表
 * Created by PengXianglin on 16/3/1.
 */
public class VehicleSourceFragment extends CommonBaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, HCPullToRefresh.OnRefreshCallback {

	private TextView tv_title_content;
	private HCPullToRefresh mPullToRefresh;
	private ListView mListView;
	private List<VehicleSourceShortEntity> mTasks = new ArrayList<VehicleSourceShortEntity>();
	private VehicleSourceAdapter mAdapter = null;

	private int pageIndex = 0;//当前页面索引
	private ACache mAcache;
	private int mClickIndex;
	private String keyword;
	private LocalCheckerEntity maintenancePersonnel;//筛选维护人
	private VehicleSeriesEntity vehicleSeries;//筛选品牌车系

	public static final String BUNDLE_OFFLINE_CAR = "offline_car", BUNDLE_CONFIRM_SELL = "confirm_sell", BUNDLE_CHANGE_PRICE = "change_price",
		BUNDLE_CHOOSE_VEHICLE_FILTER = "choose_vehicle_filter";

	@Override
	protected int getContentView() {
		return R.layout.fragment_businesslist;
	}

	@Override
	protected void initView(View view) {
		tv_title_content = (TextView) view.findViewById(R.id.tv_title_content);
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
		String json = mAcache.getAsString(TaskConstants.ACACHE_VEHICLE_SOURCE_LIST);
		if (!TextUtils.isEmpty(json)) {
			this.mTasks.clear();
//			List<VehicleSourceShortEntity> tempList = new HCJsonParse().parseVehicleSourceList(json);
			List<VehicleSourceShortEntity> tempList = JsonParseUtil.fromJsonArray(json, VehicleSourceShortEntity.class);
			if (tempList != null)
				this.mTasks.addAll(tempList);
		}
		if (mAdapter == null) {
			mAdapter = new VehicleSourceAdapter(getActivity(), mTasks, R.layout.layout_vehicle_source_item);
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

					VehicleSourceShortEntity entity = mTasks.get(mClickIndex);
					//下线
					if (data.getBoolean(VehicleSourceFragment.BUNDLE_OFFLINE_CAR) && data.getInt("id") == entity.getId()) {
						mTasks.remove(entity);
					}
					//在售
					else if (data.getBoolean(VehicleSourceFragment.BUNDLE_CONFIRM_SELL) && data.getInt("id") == entity.getId()) {
						entity.setUp_time(data.getString("up_time"));
					}
					//修改报价
					else if (data.getBoolean(VehicleSourceFragment.BUNDLE_CHANGE_PRICE) && data.getInt("id") == entity.getId()) {
						entity.setPrice(data.getString("price"));
					}

					if (mTasks.size() > TaskConstants.DEFAULT_SHOWTASK_COUNT)
//						mAcache.put(TaskConstants.ACACHE_VEHICLE_SOURCE_LIST, new HCJsonParse().parseVehicleSourceListToJson(mTasks.subList(0, TaskConstants.DEFAULT_SHOWTASK_COUNT)));//缓存删除过本条数据后的json
						mAcache.put(TaskConstants.ACACHE_VEHICLE_SOURCE_LIST, JsonParseUtil.toJsonArray(mTasks.subList(0, TaskConstants.DEFAULT_SHOWTASK_COUNT)));//缓存删除过本条数据后的json
					else
//						mAcache.put(TaskConstants.ACACHE_VEHICLE_SOURCE_LIST, new HCJsonParse().parseVehicleSourceListToJson(mTasks));//缓存删除过本条数据后的json
						mAcache.put(TaskConstants.ACACHE_VEHICLE_SOURCE_LIST, JsonParseUtil.toJsonArray(mTasks));//缓存删除过本条数据后的json
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
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		mClickIndex = position;
		if (mTasks == null || mTasks.size() <= position) {
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("vehicleSourceId", mTasks.get(position).getId());
		HCActionUtil.launchActivity(GlobalData.context, VehicleSourceDetailActivity.class, map);
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (action.equals(HttpConstants.ACTION_DEALCARAPI_GETLIST)) {
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
					JSONObject obj = new JSONObject(response.getData());
//					List<VehicleSourceShortEntity> viewTaskList = new HCJsonParse().parseVehicleSourceList(obj.getString("list"));
					List<VehicleSourceShortEntity> viewTaskList = JsonParseUtil.fromJsonArray(obj.getString("list"), VehicleSourceShortEntity.class);
					//刷新
					if (requestId == HttpConstants.GET_LIST_REFRESH) {
						if (obj.has("total") && obj.has("check_cn") && obj.has("online_cn") && obj.has("offline_cn")) {
							tv_title_content.setText("共" + obj.getString("total") + "辆车源，" +
								"在线" + obj.getString("online_cn") + "辆，" +
								"待审核" + obj.getString("check_cn") + "辆，" +
								"下线" + obj.getString("offline_cn") + "辆");
						}
						if (obj.getString("list") != null)
							mAcache.put(TaskConstants.ACACHE_VEHICLE_SOURCE_LIST, obj.getString("list"));//只缓存刷新的数据
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
	public void doingFilter(Bundle data) {
		//加载网络筛选
		if (data != null) {
			keyword = data.getString("keyword", null);
			//获取选择的维护人
			LocalCheckerEntity checkerEntity = (LocalCheckerEntity) data.getSerializable(ChannelMainActivity.BUNDLE_CHOOSE_MAINTENANCE_FILTER);
			if (checkerEntity != null)
				maintenancePersonnel = checkerEntity;

			VehicleSeriesEntity seriesEntity = (VehicleSeriesEntity) data.getSerializable(BUNDLE_CHOOSE_VEHICLE_FILTER);
			if (seriesEntity != null)
				vehicleSeries = seriesEntity;
		} else {
			//清除筛选
			maintenancePersonnel = null;
			vehicleSeries = null;
		}
		if (mPullToRefresh != null)
			mPullToRefresh.setFirstAutoRefresh();//刷新
	}

	public LocalCheckerEntity getMaintenancePersonnel() {
		return maintenancePersonnel;
	}

	public VehicleSeriesEntity getVehicleSeries() {
		return vehicleSeries;
	}

	@Override
	public void onPullDownRefresh() {
		pageIndex = 0;
		OKHttpManager.getInstance().post(HCHttpRequestParam.getVehicleSourceList(pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT, keyword,
			maintenancePersonnel == null ? 0 : maintenancePersonnel.getId(), vehicleSeries == null ? 0 : vehicleSeries.getBrand_id(), vehicleSeries == null ? 0 : vehicleSeries.getId()),
			this, HttpConstants.GET_LIST_REFRESH);//下拉刷新
	}

	@Override
	public void onLoadMoreRefresh() {
		OKHttpManager.getInstance().post(HCHttpRequestParam.getVehicleSourceList(++pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT, keyword,
			maintenancePersonnel == null ? 0 : maintenancePersonnel.getId(), vehicleSeries == null ? 0 : vehicleSeries.getBrand_id(), vehicleSeries == null ? 0 : vehicleSeries.getId()),
			this, HttpConstants.GET_LIST_LOADMORE);//上拉刷新
	}
}