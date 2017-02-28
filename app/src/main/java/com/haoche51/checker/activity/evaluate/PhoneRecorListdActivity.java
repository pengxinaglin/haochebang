package com.haoche51.checker.activity.evaluate;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.fragment.PhoneRecordListFragment;
import com.haoche51.checker.widget.FragmentItem;
import com.haoche51.checker.widget.HCSmartTabLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 车主录音、买家录音
 * Created by mac on 15/11/11.
 */
public class PhoneRecorListdActivity extends CommonBaseFragmentActivity {

  private static final String TAG = "PhoneRecorListdActivity";
  public static final String KEY_INTENT_EXTRA_BUYER_PHONE = "buyer_phone";
  public static final String KEY_INTENT_EXTRA_SALER_PHONE = "saler_phone";
  public static final String KEY_TASK_TYPE = "task_type";
  @ViewInject(R.id.hc_smarttab_layout_content_view)
  private HCSmartTabLayout hcSmartTabLayout;

  private String salerphone, buyerPhone;
  private String taskId;

  private int taskType;
  private boolean isSalerRecordShow = true;

  private PhoneRecordListFragment salerFragment;
  private PhoneRecordListFragment buyerFragment;


  @Override
  protected void initView() {
    super.initView();
    setContentView(R.layout.activity_common_smarttab);
    registerTitleBack();
    setScreenTitle(getString(R.string.hc_phone_record_list_activity_title));
    x.view().inject(this);

    if (getIntent().hasExtra(KEY_INTENT_EXTRA_BUYER_PHONE)) {
      buyerPhone = getIntent().getStringExtra(KEY_INTENT_EXTRA_BUYER_PHONE);
      isSalerRecordShow = true;
    } else {
      isSalerRecordShow = false;
    }
    salerphone = getIntent().getStringExtra(KEY_INTENT_EXTRA_SALER_PHONE);

    taskId = getIntent().getStringExtra("id");
    taskType = getIntent().getIntExtra(KEY_TASK_TYPE,0);

    salerFragment = PhoneRecordListFragment.newInstance(salerphone, taskId,taskType);
    buyerFragment = PhoneRecordListFragment.newInstance(buyerPhone, taskId,taskType);
  }

  @Override
  protected void initData() {
    super.initData();
    if (isSalerRecordShow) {
      List<FragmentItem> list = new ArrayList<>();
      list.add(new FragmentItem(getString(R.string.hc_phone_record_list_saler), salerFragment));
      list.add(new FragmentItem(getString(R.string.hc_phone_record_list_buyer), buyerFragment));
      //设置界面
      hcSmartTabLayout.setContentFragments(this, list);

      hcSmartTabLayout.addOnHCPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
          //tab 切换，播放停止
          salerFragment.pausePlay();
          buyerFragment.pausePlay();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
      });

    } else {
      hcSmartTabLayout.setVisibility(View.GONE);
      FragmentTransaction ft = this.getSupportFragmentManager()
        .beginTransaction();
      ft.add(R.id.fl_activity_common_smarttab, PhoneRecordListFragment.newInstance(salerphone, taskId,taskType));
      ft.commitAllowingStateLoss();
    }
  }


}
