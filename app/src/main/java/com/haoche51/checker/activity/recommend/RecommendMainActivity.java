package com.haoche51.checker.activity.recommend;

import android.content.Intent;
import android.view.View;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.purchase.PurchaseAddClueActivity;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.fragment.recommend.BuyerRecommendFragment;
import com.haoche51.checker.fragment.recommend.CheckCarFragment;
import com.haoche51.checker.fragment.recommend.PurchaseRecommendFragment;
import com.haoche51.checker.widget.FragmentItem;
import com.haoche51.checker.widget.HCSmartTabLayout;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐主页
 * Created by pengxianglin on 16/6/8.
 */
public class RecommendMainActivity extends CommonBaseFragmentActivity {

	public static final int REQUEST_ADD_BUYER_CLUE = 100;
	public static final int REQUEST_ADD_PURCHASE_CLUE = 101;
	public static final int REQUEST_ADD_CHECKER_CLUE = 102;

	@ViewInject(R.id.content_view)
	private HCSmartTabLayout hcSmartTabLayout;
	List<FragmentItem> mFragmentItemList;


	@Override
	protected void initView() {
		setContentView(R.layout.activity_recommend_main);
		x.view().inject(this);
		super.initView();
		setScreenTitle(R.string.buyer_clues);
		registerTitleBack();
	}

	@Override
	protected void initData() {
		mFragmentItemList = new ArrayList<>();

		mFragmentItemList.add(new FragmentItem(getString(R.string.p_buyer), new BuyerRecommendFragment()));
		mFragmentItemList.add(new FragmentItem(getString(R.string.hc_repurchase), new PurchaseRecommendFragment()));
		mFragmentItemList.add(new FragmentItem(getString(R.string.hc_check_car), new CheckCarFragment()));

		//设置界面
		hcSmartTabLayout.setContentFragments(this, mFragmentItemList);
	}

	/**
	 * 添加推荐
	 *
	 * @param view
	 */
	@Event(R.id.fab_show_data)
	private void addClue(View view){
		int position = hcSmartTabLayout.getCurrentPagerPosition();
		Intent intent;
        switch (position){
			case 0:
				//添加推荐买家
				intent = new Intent(this, BuyerAddCluesActivity.class);
				this.startActivityForResult(intent, REQUEST_ADD_BUYER_CLUE);
				break;
			case 1:
				//添加回购推荐
				intent = new Intent(this, PurchaseAddClueActivity.class);
				this.startActivityForResult(intent, REQUEST_ADD_PURCHASE_CLUE);
				break;
			case 2:
				//添加验车推荐
				intent = new Intent(this, CheckAddCluesActivity.class);
				this.startActivityForResult(intent, REQUEST_ADD_CHECKER_CLUE);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
			case REQUEST_ADD_BUYER_CLUE:
				//添加买家线索
				mFragmentItemList.get(0).getFragment().onActivityResult(requestCode, resultCode, data);
				break;
			case REQUEST_ADD_PURCHASE_CLUE:
				//添加回购线索
				mFragmentItemList.get(1).getFragment().onActivityResult(requestCode, resultCode, data);
				break;
			case REQUEST_ADD_CHECKER_CLUE:
				//添加验车线索
				mFragmentItemList.get(2).getFragment().onActivityResult(requestCode, resultCode, data);
				break;
		}
	}
}