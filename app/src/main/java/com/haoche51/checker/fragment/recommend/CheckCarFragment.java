package com.haoche51.checker.fragment.recommend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.CheckCluesAdapter;
import com.haoche51.checker.entity.VehicleRecomEntity;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ACache;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.haoche51.checker.activity.recommend.RecommendMainActivity.REQUEST_ADD_CHECKER_CLUE;

/**
 * 验车推荐
 * Created by PengXianglin on 16/7/6.
 */
public class CheckCarFragment extends CommonBaseFragment implements HCPullToRefresh.OnRefreshCallback, View.OnClickListener {

	private HCPullToRefresh mPullToRefresh;
	private ListView mListView;
//	private TextView currMonthBonusTv, historyBonusTv;

	private List<VehicleRecomEntity> allLeads = new ArrayList<>();
	private CheckCluesAdapter mAdapter = null;
	private int pageIndex = 0;//当前页面索引
	private ACache mAcache;

	@Override
	protected int getContentView() {
		return R.layout.fragment_recommend_check;
	}

	@Override
	protected void initView(View view) {
		mPullToRefresh = (HCPullToRefresh) view.findViewById(R.id.pull_to_refresh);
		mPullToRefresh.setCanPull(true);
		mPullToRefresh.setOnRefreshCallback(this);
		mListView = mPullToRefresh.getListView();
		mListView.setDivider(getResources().getDrawable(R.color.hc_self_gray_bg));
		mListView.setDividerHeight(DisplayUtils.dip2px(getActivity(), 10));
//		currMonthBonusTv = (TextView) view.findViewById(R.id.tv_curr_month_bonus);
//		historyBonusTv = (TextView) view.findViewById(R.id.tv_history_bonus);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		//初始化ACache
		if (mAcache == null) {
			mAcache = ACache.get(getActivity());
		}
		//读取ACache中的缓存记录
		String json = mAcache.getAsString(TaskConstants.ACACHE_RECOMMEND_CHECK);
		if (!TextUtils.isEmpty(json)) {
			this.allLeads.clear();
//			this.allLeads.addAll(new HCJsonParse().parseGetCheckVehicleRecomEntitys(json));
			List<VehicleRecomEntity> tempList= JsonParseUtil.fromJsonArray(json, VehicleRecomEntity.class);
			if(tempList!=null){
			   this.allLeads.addAll(tempList);
			}
		}
		if (mAdapter == null) {
			mAdapter = new CheckCluesAdapter(getActivity(), allLeads, R.layout.item_vehicle_recom);
		}
		//设置数据
		if (mListView != null) {
			mListView.setAdapter(mAdapter);
		}
		//自动刷新
		mPullToRefresh.setFirstAutoRefresh();
//		getVehicleRecomBonus();
	}


	/**
	 * 获取车源推荐奖金
	 */
//	private void getVehicleRecomBonus() {
//		OKHttpManager.getInstance().post(HCHttpRequestParam.getVehicleRecomBonus(), this, 0);
//	}


	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (getActivity() != null && getActivity().isFinishing()) {
			return;
		}
		if (HttpConstants.ACTION_GET_VEHICLE_RECOM_LIST.equals(action)) {
			dismissLoadingView();
			onListHttpResponse(response, requestId);
		}
//		else if (HttpConstants.ACTION_GET_VEHICLE_RECOM_BONUS.equals(action)) {
//			parseVehicleRecomBonus(response);
//		}
	}

	/**
	 * 解析车源推荐奖金
	 *
	 * @param response
	 */
//	private void parseVehicleRecomBonus(HCHttpResponse response) {
//		switch (response.getErrno()) {
//			case 0:
////				VehicleRecomEntity vehicleRecomEntity = new HCJsonParse().parseVehicleRecomBonus(response.getData());
//				VehicleRecomEntity vehicleRecomEntity = JsonParseUtil.fromJsonObject(response.getData(), VehicleRecomEntity.class);
//				if (vehicleRecomEntity != null) {
//					currMonthBonusTv.setText(String.valueOf(vehicleRecomEntity.getMonth()));
//					historyBonusTv.setText(String.valueOf(vehicleRecomEntity.getTotal()));
//				}
//				break;
//			default:
//				ToastUtil.showInfo(response.getErrmsg());
//				break;
//		}
//	}

	/**
	 * 列表页刷新返回解析
	 *
	 * @param response
	 * @param requestId
	 */
	private void onListHttpResponse(HCHttpResponse response, int requestId) {
		switch (response.getErrno()) {
			case 0://请求成功
//				List<VehicleRecomEntity> tempList = new HCJsonParse().parseCheckCluesResult(response.getData());
				List<VehicleRecomEntity> tempList = JsonParseUtil.fromJsonArray(response.getData(), VehicleRecomEntity.class);
				if (requestId == HttpConstants.GET_LIST_REFRESH && tempList != null) { //刷新
					if (!TextUtils.isEmpty(response.getData())) {
						mAcache.put(TaskConstants.ACACHE_RECOMMEND_CHECK, response.getData());//只缓存刷新的数据
					}
					allLeads.clear();
					allLeads.addAll(tempList);
				} else if (requestId == HttpConstants.GET_LIST_LOADMORE && tempList != null) {//加载更多
					//把后来加载的添加进来
					allLeads.addAll(tempList);
				} else {//数据解析出错
					showErrorView(true, this);
				}
				//是否还有更多
				if (tempList != null) {
					boolean isNoMoreData = tempList.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT;
					mPullToRefresh.setFooterStatus(isNoMoreData);
				}
				//重置数据
				mAdapter.notifyDataSetChanged();
				//当没有数据时显示默认空白页
				if (allLeads.size() == 0) {
					mPullToRefresh.hideFooter();
					showNoDataView(true, GlobalData.resourceHelper.getString(R.string.hc_common_result_nodata), this);
				} else {
					dismissResultView();//有数据，显示出来数据列表页面
				}
				break;
			default:
				mPullToRefresh.setFooterStatus(false);
				ToastUtil.showInfo(response.getErrmsg());
				//无网络视图
				if (allLeads.size() == 0) {
					showErrorView(true, this);
				}
				break;
		}
		//停止刷新
		mPullToRefresh.finishRefresh();
	}

	@Override
	public void onClick(View view) {
		//当空数据列表时会回调此方法
		if (mPullToRefresh != null) {
			dismissResultView();
			showLoadingView(false);
			onPullDownRefresh();//刷新重新拉取数据
		}
	}

	@Override
	public void onPullDownRefresh() {
		pageIndex = 0;
		OKHttpManager.getInstance().post(HCHttpRequestParam.getVehicleRecomByPage(pageIndex,
				TaskConstants.DEFAULT_SHOWTASK_COUNT), this, HttpConstants.GET_LIST_REFRESH);
	}

	@Override
	public void onLoadMoreRefresh() {
		pageIndex++;
		OKHttpManager.getInstance().post(HCHttpRequestParam.getVehicleRecomByPage(pageIndex,
				TaskConstants.DEFAULT_SHOWTASK_COUNT), this, HttpConstants.GET_LIST_LOADMORE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ADD_CHECKER_CLUE) {
			// 重新刷新数据
			if (resultCode == Activity.RESULT_OK) {
				if (mListView != null) {
					mListView.setSelection(0);
				}
				if (mPullToRefresh != null) {
					mPullToRefresh.setFirstAutoRefresh();
				}
			}
		}
	}
}

