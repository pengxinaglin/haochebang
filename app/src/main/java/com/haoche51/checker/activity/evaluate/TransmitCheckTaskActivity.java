package com.haoche51.checker.activity.evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.LocalCityCheckerAdapter;
import com.haoche51.checker.entity.LocalCheckerEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.widget.SideBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 15/11/24.
 */
public class TransmitCheckTaskActivity extends CommonTitleBaseActivity implements AdapterView.OnItemClickListener,
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	@ViewInject(R.id.checker_list)
	private ListView mListView;

	@ViewInject(R.id.select_letter)
	private TextView select_letter;

	@ViewInject(R.id.side_bar)
	private SideBar side_bar;

	@ViewInject(R.id.search_box)
	private SearchView search_box;

	private int taskId;
	private List<LocalCheckerEntity> mLocalCityCheckerlist = new ArrayList<>();//本地的评估师 用于存放所有的评估师
	private LocalCityCheckerAdapter mAdapter;
	private LocalCheckerEntity chooseChecker;//当前选中的评估师

	public static final String INTENT_KEY_ONLY_CHOOSE_CHECKER = "only_choose_checker";

	@Override
	public View getHCContentView() {
		return View.inflate(this, R.layout.activity_transmit_checktask, null);
	}

	private Handler handler = new Handler();

	private void showIndex(String word) {
		select_letter.setVisibility(View.VISIBLE);
		select_letter.setText(word);
		//每次显示前先取出让它消失的任务
		handler.removeCallbacksAndMessages(null);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				select_letter.setVisibility(View.GONE);
			}
		}, 1500);
	}

	@Override
	public void initContentView(Bundle saveInstanceState) {
		x.view().inject(this);
		search_box.setOnQueryTextListener(this);
		search_box.setOnCloseListener(this);
		side_bar.setTextView(select_letter);
		side_bar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				showIndex(s);//显示当前触摸滑动的位置
				for (int i = 0; i < mLocalCityCheckerlist.size(); i++) {
					String firstWord = mLocalCityCheckerlist.get(i).getFirst_char();
					if (firstWord.equals(s)) {
						mListView.setSelection(i);//只需要第一个首字母为当前触摸字母的索引
						break;
					}
				}
			}
		});
		mAdapter = new LocalCityCheckerAdapter(this
				, mLocalCityCheckerlist, R.layout.item_checker);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		taskId = getIntent().getIntExtra("taskId", 0);
		ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
		//需要显示自己
		if (getIntent().getBooleanExtra(INTENT_KEY_ONLY_CHOOSE_CHECKER, false)) {
			//请求获取本地评估师
			OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckerList(0, true), this, 0);
		} else {
			//请求获取本地评估师
			OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckerList(0, false), this, 0);
		}
	}

	@Override
	public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
		mTitle.setText(getString(R.string.checker_list));
		mRightFaction.setCompoundDrawables(null, null, null, null);
		mRightFaction.setVisibility(View.VISIBLE);
		mRightFaction.setText(R.string.hc_common_save);
		mRightFaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//判断是否已选择
				if (chooseChecker == null) {
					ToastUtil.showInfo(getString(R.string.choose_acceptor));
					return;
				}

				//只选择评估师
				if (getIntent().getBooleanExtra(INTENT_KEY_ONLY_CHOOSE_CHECKER, false)) {
					Intent data = new Intent();
					data.putExtra("chooseChecker", chooseChecker);
					setResult(RESULT_OK, data);
					finish();
				} else {
					//二次确认
					AlertDialogUtil.showStandardTitleMessageDialog(TransmitCheckTaskActivity.this, getString(R.string.transmit_ok),
							getString(R.string.transmit_to_choose_checker, chooseChecker.getName()), getString(R.string.transmit_cancel),
							getString(R.string.transmit_confirm), new AlertDialogUtil.OnDismissListener() {
								@Override
								public void onDismiss(Bundle data) {
									//确定转单
									OKHttpManager.getInstance().post(HCHttpRequestParam.changeChecker(taskId, chooseChecker.getId(), chooseChecker.getName()), TransmitCheckTaskActivity.this, 0);
								}
							});
				}
			}
		});
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (action.equals(HttpConstants.ACTION_GET_CHECKERLIST)) {
			responseGetCheckerList(response);
		} else if (action.equals(HttpConstants.ACTION_CHANGE_CHECKER)) {
			responseChangeChecker(response);
		}
		ProgressDialogUtil.closeProgressDialog();
	}

	/**
	 * 处理请求转单
	 */
	private void responseChangeChecker(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				ToastUtil.showInfo(getString(R.string.successful));
				HCTasksWatched.getInstance().notifyWatchers();
				setResult(RESULT_OK);
				finish();
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	/**
	 * 解析员工列表信息
	 */
	private void responseGetCheckerList(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				if (!TextUtils.isEmpty(response.getData())) {
					//解析评估师list
//					List<LocalCheckerEntity> entities = new HCJsonParse().parseCheckerList(response.getData());
					List<LocalCheckerEntity> entities = JsonParseUtil.fromJsonArray(response.getData(), LocalCheckerEntity.class);
					if (entities != null)
						mLocalCityCheckerlist.addAll(entities);
					mAdapter.notifyDataSetChanged();
				}
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		//获得当前adapter中的list数据
		List<LocalCheckerEntity> entities = ((LocalCityCheckerAdapter) adapterView.getAdapter()).getmList();
		//先将其与同事全部置为false 未选择
		for (LocalCheckerEntity entity : entities) {
			entity.setIsChoose(false);
		}
		//设置当前点击项为已选中
		entities.get(i).setIsChoose(true);
		mAdapter.notifyDataSetChanged();
		chooseChecker = entities.get(i);
	}

	@Override
	public boolean onQueryTextSubmit(String s) {
		chooseChecker = null;//将已选置为空
		mAdapter.filter(s, mLocalCityCheckerlist);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String s) {
		chooseChecker = null;//将已选置为空
		mAdapter.filter(s, mLocalCityCheckerlist);
		return true;
	}

	@Override
	public boolean onClose() {
		mAdapter.setmList(mLocalCityCheckerlist);
		mAdapter.notifyDataSetChanged();
		return true;
	}
}
