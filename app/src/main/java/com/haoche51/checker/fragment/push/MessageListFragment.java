package com.haoche51.checker.fragment.push;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.channel.ChannelMainActivity;
import com.haoche51.checker.activity.evaluate.CheckTaskDetailActivity;
import com.haoche51.checker.activity.hostling.HostlingDetailActivity;
import com.haoche51.checker.activity.offlinesold.StockAttentionActivity;
import com.haoche51.checker.activity.purchase.PurchaseBuyBackDetailActivity;
import com.haoche51.checker.activity.purchase.PurchaseTaskPendingDetailActivity;
import com.haoche51.checker.adapter.PushMessageAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.HCPullToRefresh;
import com.haoche51.checker.entity.BackPushDetailEntity;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.entity.MessageEntity;
import com.haoche51.checker.entity.push.PushTask;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.helper.UserDataHelper;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.HCCacheUtil;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.IntentExtraMap;
import com.haoche51.checker.util.PreferenceUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 15/11/19.
 */
public class MessageListFragment extends CommonBaseFragment {

	private HCPullToRefresh mPullToRefresh;
	private List<MessageEntity> mMessages;
	private int pageSize = 10;
	private int mPage = 0;
	private PushMessageAdapter mAdapter;
	private Gson mGson = new Gson();

	@Override
	protected int getContentView() {
		return R.layout.activity_message;
	}

	@Override
	protected void initView(View view) {
		mPullToRefresh = (HCPullToRefresh) view.findViewById(R.id.pull_to_refresh);
		mPullToRefresh.setCanPull(true);
		mPullToRefresh.setFirstAutoRefresh();

		final LinearLayout relEmpty = (LinearLayout) view.findViewById(R.id.ll_list_empty);
		TextView textView = (TextView) relEmpty.findViewById(R.id.result_txt);
		textView.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.common_button_nodata, 0, 0);
		textView.setText(getString(R.string.hc_common_result_nodata));
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPullToRefresh.setFirstAutoRefresh();//重新拉取数据
			}
		});
		mPullToRefresh.setEmptyView(relEmpty);
		mPullToRefresh.setOnRefreshCallback(new HCPullToRefresh.OnRefreshCallback() {
			@Override
			public void onPullDownRefresh() {
				mPage = 0;
				OKHttpManager.getInstance().post(HCHttpRequestParam.getMessageList(mPage, pageSize), MessageListFragment.this, 0); //下拉刷新
			}

			@Override
			public void onLoadMoreRefresh() {
				OKHttpManager.getInstance().post(HCHttpRequestParam.getMessageList(++mPage, pageSize), MessageListFragment.this, 1);//上划刷新
			}
		});
		final ListView mListView = mPullToRefresh.getListView();
		mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.hc_self_gray_bg)));
		mListView.setDividerHeight(DisplayUtils.dip2px(getActivity(), 15));
		mMessages = HCCacheUtil.getCacheMessages(); //
		mAdapter = new PushMessageAdapter(getActivity(), mMessages, R.layout.message_item);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mMessages == null || mMessages.isEmpty())
					return;

				MessageEntity entity = mMessages.get(position);
//				if (entity.getType() != 0 && entity.getType() != 1 && entity.getType() != 2 && entity.getType() != 3 && entity.getType() != TaskConstants.MESSAGE_BACK_TASK) {
//					return;
//				}

				PushTask task;
				try {
					task = mGson.fromJson(entity.getCustom_content(), PushTask.class);
					if (task == null) return;
				} catch (Exception e) {
					return;
				}

				ProgressDialogUtil.showProgressDialog(getActivity(), GlobalData.resourceHelper.getString(R.string.hc_loading));
				switch (entity.getType()) {
					case TaskConstants.MESSAGE_CHECK_TASK://验车
						OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckTask(task.getTask_id()), MessageListFragment.this, 0);
						break;
					case TaskConstants.MESSAGE_BACK_TASK://收车
						if (task.getIs_care() == 1) {
							Intent intent = new Intent(getActivity(), StockAttentionActivity.class);
							intent.putExtra("stock_id", task.getStock_id());
							startActivity(intent);
							return;
						}
						OKHttpManager.getInstance().post(HCHttpRequestParam.getBackPushDetail(task.getTask_id()), MessageListFragment.this, 0);
						break;
					case TaskConstants.MESSAGE_FINE_NOTIFY://罚款
						lookFine(entity.getCustom_content());
						break;
					case TaskConstants.MESSAGE_FIND_CAR://找车需求
						Intent intent = new Intent(getActivity(), ChannelMainActivity.class);
						intent.putExtra(TaskConstants.BINDLE_FRAGMENT_INDEX, 2);
						startActivity(intent);
					default:
						break;
				}
			}
		});
	}

	/**
	 * 跳转查看罚款
	 */
	private void lookFine(String custom_content) {
		try {
			ProgressDialogUtil.closeProgressDialog();
			JSONObject object = new JSONObject(custom_content);
			String url = object.getString("url");
			if (TextUtils.isEmpty(url)) {
				ToastUtil.showInfo("查询结果为空");
				return;
			}
			Map params = new HashMap();
			params.put(HCWebViewActivity.KEY_INTENT_EXTRA_URL, url);
			HCActionUtil.launchActivity(getActivity(), HCWebViewActivity.class, params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (action.equals(HttpConstants.ACTION_GET_MESSAGE)) {
			//记录更新最后一次刷新成功的时间
			try {
				PreferenceUtil.putInt(GlobalData.mContext, UserDataHelper.LAST_UPDATE_TIME, (int) (new Date().getTime() / 1000));
				onListHttpResponse(response, requestId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (action.equals(HttpConstants.ACTION_GET_CHECKTASK)) {
			onCheckTaskResponse(response);
		}
		if (action.equals(HttpConstants.ACTION_BACK_PUSH_DETAIL)) {
			onBackPushDetailResponse(response);
		}

		ProgressDialogUtil.closeProgressDialog();
	}

	/**
	 * 列表页刷新返回解析
	 *
	 * @param response
	 * @param requestId
	 */
	private void onListHttpResponse(HCHttpResponse response, int requestId) {
		switch (response.getErrno()) {
			case 0:
//				List<MessageEntity> list = new HCJsonParse().parseMessageLists(response.getData());
				List<MessageEntity> list = JsonParseUtil.fromJsonArray(response.getData(), MessageEntity.class);
				//解析返回值，缓存首屏
				if (requestId == 0) { //下拉刷新
					if (mMessages != null && list != null) {
						mMessages.clear();
						mMessages.addAll(list);
						HCCacheUtil.saveCacheMessages(mMessages);
					}
				} else { //上拉加载更多
					if (mMessages != null) {
						mMessages.addAll(mergeConflict(mMessages, list));
					}
					if (list != null) {
						boolean isNoMoreData = list.size() < pageSize;
						mPullToRefresh.setFooterStatus(isNoMoreData);
					}
				}
				mAdapter.notifyDataSetChanged();
				int count = mAdapter.getCount();
				HCLogUtil.e("response message", "result count:" + count);
				break;
			default:
				mPullToRefresh.setFooterStatus(false);
				if (getActivity() != null && !getActivity().isFinishing())
					Toast.makeText(getActivity(), response.getErrmsg(), Toast.LENGTH_SHORT).show();
				break;
		}
		mPullToRefresh.finishRefresh();
	}

	/**
	 * 解析返回的单个验车任务信息
	 *
	 * @param response
	 */
	private void onCheckTaskResponse(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				CheckTaskEntity entity;
				try {
					Type type = new TypeToken<CheckTaskEntity>() {
					}.getType();
					entity = response.getData(type);
				} catch (Exception e) {
					return;
				}
				//start evaluateTaskActivity
				if (entity != null) {
					HCActionUtil.launchActivity(getActivity(), CheckTaskDetailActivity.class, IntentExtraMap.putId(entity.getId()));
				}
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
		}
	}

	/**
	 * 解析收车相关推送类型
	 */
	private void onBackPushDetailResponse(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				if (response.getData() != null) {
					Gson gson = new Gson();
					BackPushDetailEntity mTaskEntity = gson.fromJson(response.getData(), BackPushDetailEntity.class);
					if (mTaskEntity != null) {
						//1:待跟进任务、2、已预约收车任务、3：收车其他详情页、4：整备详情页、5：付款申请详情页)
						Map<String, Object> map = new HashMap<>();
						map.put("id", mTaskEntity.getTask_id());
						switch (mTaskEntity.getPage_type()) {
							case 1:
								HCActionUtil.launchActivity(GlobalData.context, PurchaseTaskPendingDetailActivity.class, map);
								break;
							case 2:
								HCActionUtil.launchActivity(GlobalData.context, PurchaseTaskPendingDetailActivity.class, map);
								break;
							case 3:
								HCActionUtil.launchActivity(GlobalData.context, PurchaseBuyBackDetailActivity.class, map);
//								HCActionUtil.launchActivity(GlobalData.context, PurchaseTaskDetailActivity.class, map);
								break;
							case 4://待整备
								//1待整备、3整备中、2 4 已完成
//								switch (mTaskEntity.getRepair_status()) {
//									case 1:
//										map.put("type", TaskConstants.REQUEST_HOSTLING_DEFAULT);
//										break;
//									case 3:
//										map.put("type", TaskConstants.REQUEST_HOSTLING_UNDERWAY);
//										break;
//									case 2:
//									case 4:
//										map.put("type", TaskConstants.REQUEST_HOSTLING_COMPLETE);
//										break;
//								}
								map.put(HostlingDetailActivity.INTENT_PUSH_SEND, true);
//								map.put("status", mTaskEntity.getRepair_status());
								HCActionUtil.launchActivity(GlobalData.context, HostlingDetailActivity.class, map);
								break;
							case 7://付款申请
								Intent intent = new Intent(getActivity(), PurchaseBuyBackDetailActivity.class);
								intent.putExtra("task_id", mTaskEntity.getTask_id());
								startActivity(intent);
								break;
						}
					}
				}
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	/**
	 * 本地合并message 列表
	 *
	 * @return
	 */
	private List<MessageEntity> mergeConflict
	(List<MessageEntity> mMessages, List<MessageEntity> newMessages) {
		List<MessageEntity> result = new ArrayList<>();
		if (mMessages == null || newMessages == null) {
			return result;
		}
		for (MessageEntity pushEntity : newMessages) {
			if (!mMessages.contains(pushEntity)) {
				result.add(pushEntity);
			}
		}
		return result;
	}
}
