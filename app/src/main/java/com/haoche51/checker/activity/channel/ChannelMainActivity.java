package com.haoche51.checker.activity.channel;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.fragment.channel.BusinessListFragment;
import com.haoche51.checker.fragment.channel.FilterFragment;
import com.haoche51.checker.fragment.channel.FindCarFragment;
import com.haoche51.checker.fragment.channel.VehicleSourceFragment;
import com.haoche51.checker.widget.FragmentItem;
import com.haoche51.checker.widget.HCSmartTabLayout;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 渠寄商家、车源列表页面
 * Created by PengXianglin on 16/3/1.
 */
public class ChannelMainActivity extends CommonBaseFragmentActivity implements TextView.OnEditorActionListener, ViewPager.OnPageChangeListener, FilterFragment.OnBackClickListener, View.OnTouchListener {
    public static final String BUNDLE_CHOOSE_MAINTENANCE_FILTER = "choose_maintenance_filter";
    public static boolean filterShowing = false;
    private final String FILTER_FRAGMENT_TAG = "filter_fragment";
    private final int REQUESTCODE_ADDDEALER = 212;
    List<FragmentItem> mFragmentItemList;
    @ViewInject(R.id.content_view)
    private HCSmartTabLayout hcSmartTabLayout;
    @ViewInject(R.id.rl_titlebar)
    private RelativeLayout rl_titlebar;
    @ViewInject(R.id.et_trans_search)
    private EditText et_trans_search;//搜索内容
    @ViewInject(R.id.view_mask)
    private View view_mask;//遮罩
    @ViewInject(R.id.tv_title_add)
    private TextView addBtn;
    @ViewInject(R.id.tv_right_fuction)
    private TextView tv_right_fuction;
    @ViewInject(R.id.iv_right_filter)
    private ImageView iv_right_filter;
    @ViewInject(R.id.fl_filter)
    private FrameLayout fl_filter;
    private FilterFragment mFilterFragment;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_channel_main);
        x.view().inject(this);
        setScreenTitle(R.string.hc_channel_main_title);
        registerTitleBack();//设置返回
        //监听键盘点击搜索
        et_trans_search.setOnEditorActionListener(this);
        et_trans_search.setHint(getString(R.string.hc_channel_business_search));
        addBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 展示标题工具栏
     */
    public void showTitleBar(){
        tv_right_fuction.setVisibility(View.VISIBLE);
        iv_right_filter.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏标题工具栏
     */
    public void hideTitleBar(){
        tv_right_fuction.setVisibility(View.GONE);
        iv_right_filter.setVisibility(View.GONE);
    }


    @Override
    protected void initData() {
        mFragmentItemList = new ArrayList<>();
        mFragmentItemList.add(new FragmentItem(getString(R.string.hc_channel_business), new BusinessListFragment()));
        mFragmentItemList.add(new FragmentItem(getString(R.string.hc_channel_source), new VehicleSourceFragment()));
        mFragmentItemList.add(new FragmentItem(getString(R.string.hc_channel_find_car), new FindCarFragment()));
        //设置界面
        hcSmartTabLayout.setContentFragments(this, mFragmentItemList);
        hcSmartTabLayout.addOnHCPageChangeListener(this);
        //获取默认展示界面的标签索引，默认值：商家界面
        int index = getIntent().getIntExtra(TaskConstants.BINDLE_FRAGMENT_INDEX, 0);
        if (index != 0) {
            try {
                hcSmartTabLayout.getViewPager().setCurrentItem(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterShowing = false;
    }

    /**
     * 显示搜索框
     */
    @Event(R.id.tv_right_fuction)
    private void showSearchBox(final View v) {
        int width = getWindowManager().getDefaultDisplay().getWidth();
        doAnimator(width, 0, true);
        //关闭筛选
        if (filterShowing)
            hideFilterFragment();
    }

    /**
     * 添加商家
     */
    @Event(R.id.tv_title_add)
    private void showAddTaskPage(final View v) {
        Intent intent = new Intent(this, AddDealerActivity.class);
        intent.putExtra(AddDealerActivity.INTENT_ADD_DEALER, true);
        startActivityForResult(intent, REQUESTCODE_ADDDEALER);
    }

    /**
     * 筛选
     */
    @Event(R.id.iv_right_filter)
    private void rightFilter(final View v) {
        if (!filterShowing)
            showFilterFragment();
    }

    /**
     * 返回关闭筛选
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (filterShowing) {
                hideFilterFragment();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 隐藏搜索框
     */
    @Event(R.id.view_mask)
    private void clickMask(View v) {
        hideSearchBox(v);
    }

    /**
     * 隐藏搜索框
     */
    @Event(R.id.tv_search_cancel)
    private void hideSearchBox(View v) {
        //清空搜索
        if (et_trans_search != null)
            et_trans_search.setText("");
        if (mFragmentItemList != null)
            for (FragmentItem item : mFragmentItemList) {
                item.getFragment().doingFilter(new Bundle());
            }
        //隐藏搜索框
        int width = getWindowManager().getDefaultDisplay().getWidth();
        doAnimator(0, width, false);
    }

    @Event(R.id.iv_search)
    private void onSearchClick(View v) {
        if (!TextUtils.isEmpty(et_trans_search.getText().toString().trim())) {
            commit(et_trans_search.getText().toString());
        }
    }

    private void doAnimator(int targetWidth, int startWdith, final boolean enable) {
        final ViewGroup.LayoutParams layoutParams = rl_titlebar.getLayoutParams();
        ValueAnimator va = ValueAnimator.ofInt(targetWidth, startWdith);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator va) {
                layoutParams.width = (Integer) va.getAnimatedValue();
                rl_titlebar.setLayoutParams(layoutParams);
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //激活输入框
                if (enable) {
                    //弹出键盘
                    et_trans_search.setFocusable(true);
                    et_trans_search.setFocusableInTouchMode(true);
                    et_trans_search.requestFocus();
                    //让软键盘延时弹出，以更好的加载Activity
                    et_trans_search.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(et_trans_search, 0);
                        }
                    }, 100);
                    //弹出半透明效果
                    view_mask.setVisibility(View.VISIBLE);
                } else {
                    //关闭遮罩
                    view_mask.setVisibility(View.GONE);
                    //关闭键盘
                    et_trans_search.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(et_trans_search.getWindowToken(), 0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        va.setDuration(400);
        va.start();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        //监听点击输入法的“搜索”
        if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) {
            //当前页面进行搜索
            commit(et_trans_search.getText().toString());
            return true;
        }
        return false;
    }

    private void commit(String search_field) {
        //当前页面进行搜索
        if (mFragmentItemList != null) {
            FragmentItem item = mFragmentItemList.get(hcSmartTabLayout.getCurrentPagerPosition());
            Bundle where = new Bundle();
            where.putString("keyword", search_field);
            item.getFragment().doingFilter(where);
        }
        //关闭遮罩
        view_mask.setVisibility(View.GONE);
        //关闭键盘
        et_trans_search.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(et_trans_search.getWindowToken(), 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                addBtn.setVisibility(View.VISIBLE);
                et_trans_search.setHint(getString(R.string.hc_channel_business_search));
                if (((BusinessListFragment) mFragmentItemList.get(0).getFragment()).getMaintenancePersonnel() == null)
                    iv_right_filter.setImageResource(R.drawable.ic_channel_filter_normal);
                else
                    iv_right_filter.setImageResource(R.drawable.ic_channel_filter_display);
                showTitleBar();
                break;
            case 1:
                addBtn.setVisibility(View.GONE);
                et_trans_search.setHint(getString(R.string.hc_channel_vehicle_source_search));
                if (((VehicleSourceFragment) mFragmentItemList.get(1).getFragment()).getMaintenancePersonnel() != null ||
                        ((VehicleSourceFragment) mFragmentItemList.get(1).getFragment()).getVehicleSeries() != null)
                    iv_right_filter.setImageResource(R.drawable.ic_channel_filter_display);
                else
                    iv_right_filter.setImageResource(R.drawable.ic_channel_filter_normal);
                showTitleBar();
                break;
            case 2:
                hideTitleBar();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 显示筛选
     */
    public void showFilterFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        if (fragmentManager.findFragmentByTag(FILTER_FRAGMENT_TAG) == null) {
            mFilterFragment = new FilterFragment(this);
        } else {
            mFilterFragment = (FilterFragment) fragmentManager.findFragmentByTag(FILTER_FRAGMENT_TAG);
        }
        if (!mFilterFragment.isAdded()) {
            ft.add(R.id.fl_filter, mFilterFragment, FILTER_FRAGMENT_TAG);
            fl_filter.setOnTouchListener(this);
        }

        ft.show(mFilterFragment);
        //设置是否只筛选维护人
        mFilterFragment.setOnlyFilterMerchantDetail(hcSmartTabLayout.getCurrentPagerPosition() == 0 ? true : false);
        //回显上一次选择的筛选结果
        if (hcSmartTabLayout.getCurrentPagerPosition() == 0) {
            mFilterFragment.setMaintenance(((BusinessListFragment) mFragmentItemList.get(0).getFragment()).getMaintenancePersonnel());
        } else {
            mFilterFragment.setMaintenance(((VehicleSourceFragment) mFragmentItemList.get(1).getFragment()).getMaintenancePersonnel());
            mFilterFragment.setBrand(((VehicleSourceFragment) mFragmentItemList.get(1).getFragment()).getVehicleSeries());
        }
        ft.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
        filterShowing = true;
        fl_filter.setBackgroundColor(getResources().getColor(R.color.black_80));
    }

    /**
     * 隐藏筛选
     */
    public void hideFilterFragment() {
        if (getSupportFragmentManager().findFragmentByTag(FILTER_FRAGMENT_TAG) != null && mFilterFragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
            ft.hide(mFilterFragment);
            ft.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            filterShowing = false;
            fl_filter.setBackgroundColor(getResources().getColor(R.color.hc_self_transparent));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_ADDDEALER:
                    FragmentItem item = mFragmentItemList.get(hcSmartTabLayout.getCurrentPagerPosition());
                    item.getFragment().doingFilter(null);
                    break;
                default:
                    //调用筛选Fragment的onActivityResult方法更新筛选结果
                    Fragment f = getSupportFragmentManager().findFragmentByTag(FILTER_FRAGMENT_TAG);
                    if (f != null)
                        f.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }

    @Override
    public void onbackClick(Bundle bundle) {
        CommonBaseFragment fragment = mFragmentItemList.get(hcSmartTabLayout.getCurrentPagerPosition()).getFragment();
        Bundle data = new Bundle();
        //筛选
        if (bundle != null && !bundle.isEmpty()) {
            //存在有效的筛选结果
            if (bundle.getSerializable(VehicleSourceFragment.BUNDLE_CHOOSE_VEHICLE_FILTER) != null || bundle.getSerializable(BUNDLE_CHOOSE_MAINTENANCE_FILTER) != null)
                iv_right_filter.setImageResource(R.drawable.ic_channel_filter_display);
            //传递筛选车系
            data.putSerializable(VehicleSourceFragment.BUNDLE_CHOOSE_VEHICLE_FILTER, bundle.getSerializable(VehicleSourceFragment.BUNDLE_CHOOSE_VEHICLE_FILTER));
            //传递筛选维护人
            data.putSerializable(BUNDLE_CHOOSE_MAINTENANCE_FILTER, bundle.getSerializable(BUNDLE_CHOOSE_MAINTENANCE_FILTER));
            fragment.doingFilter(data);
        }
        //清除筛选
        else {
            iv_right_filter.setImageResource(R.drawable.ic_channel_filter_normal);
            fragment.doingFilter(null);
        }

        hideFilterFragment();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return filterShowing;
    }
}