package com.haoche51.checker.activity.approve;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.fragment.approve.DealApproveFragment;
import com.haoche51.checker.fragment.approve.HostlingApproveFragment;
import com.haoche51.checker.fragment.approve.RepurchaseApproveFragment;
import com.haoche51.checker.widget.FragmentItem;
import com.haoche51.checker.widget.HCSmartTabLayout;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批
 * Created by wfx on 2016/7/25.
 */
public class ApproveMainActivity extends CommonBaseFragmentActivity implements TextView.OnEditorActionListener,ViewPager.OnPageChangeListener{
    private List<FragmentItem> mFragmentItemList;

    @ViewInject(R.id.content_view)
    private HCSmartTabLayout hcSmartTabLayout;

    @ViewInject(R.id.et_trans_search)
    private EditText et_trans_search;//搜索内容

    @ViewInject(R.id.rl_titlebar)
    private RelativeLayout rl_titlebar;

    @ViewInject(R.id.view_mask)
    private View view_mask;//遮罩

    @ViewInject(R.id.tv_right_fuction)
    private TextView tv_right_fuction;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_approve);
        x.view().inject(this);
        et_trans_search.setHint(getString(R.string.hc_approve_search_hint));
        //监听键盘点击搜索
        et_trans_search.setOnEditorActionListener(this);
        hideTitleBar();
        setScreenTitle(R.string.approve);
        registerTitleBack();//设置返回
    }

    @Override
    protected void initData() {
        mFragmentItemList = new ArrayList<>();
        mFragmentItemList.add(new FragmentItem(getString(R.string.repurchase_approve), new RepurchaseApproveFragment()));
        mFragmentItemList.add(new FragmentItem(getString(R.string.deal_approve), new DealApproveFragment()));
        mFragmentItemList.add(new FragmentItem(getString(R.string.hostling_approve), new HostlingApproveFragment()));
        hcSmartTabLayout.setContentFragments(this, mFragmentItemList);
        hcSmartTabLayout.addOnHCPageChangeListener(this);
    }

    /**
     * 显示搜索框
     */
    @Event(R.id.tv_right_fuction)
    private void showSearchBox(final View v) {
        int width = getWindowManager().getDefaultDisplay().getWidth();
        doAnimator(width, 0, true);
    }


    /**
     * 隐藏搜索框
     */
    @Event(R.id.view_mask)
    private void clickMask(View v) {
        hideSearchBox();
    }

    /**
     * 隐藏搜索框
     */
    @Event(R.id.tv_search_cancel)
    private void clickCancel(View v) {
        hideSearchBox();
    }

    private void hideSearchBox(){
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
            where.putString("search_field", search_field);
            item.getFragment().doingFilter(where);
        }
        //关闭遮罩
        view_mask.setVisibility(View.GONE);
        //关闭键盘
        et_trans_search.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(et_trans_search.getWindowToken(), 0);
    }

    /**
     * 隐藏标题工具栏
     */
    public void hideTitleBar(){
        tv_right_fuction.setVisibility(View.GONE);
    }

    /**
     * 展示标题工具栏
     */
    public void showTitleBar(){
        tv_right_fuction.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
            case 2:
                hideTitleBar();
                break;
            case 1:
                showTitleBar();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
