package com.haoche51.checker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haoche51.checker.fragment.BaseReportFragment;

import java.util.List;

/**
 * 填写检查报告数据适配器
 * Created by wfx on 2016/6/29.
 */
public class FillExamReportPageAdapter extends FragmentPagerAdapter {
    private List<BaseReportFragment> mFragments;

    public FillExamReportPageAdapter(FragmentManager fm, List<BaseReportFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
