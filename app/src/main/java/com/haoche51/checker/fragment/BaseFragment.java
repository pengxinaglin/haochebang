package com.haoche51.checker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.fragment.lead.BuyerLeadFragment;
import com.haoche51.checker.widget.ElasticListView;

public abstract class BaseFragment extends Fragment implements OnItemClickListener, OnQueryTextListener, OnCloseListener, OnCheckedChangeListener, ElasticListView.OnRefreshListener, ElasticListView.OnLoadMoreListener, View.OnClickListener {
    //	protected ScrollOverListView mListView;
    private static String tabTag;
    //下拉刷新listview
    protected ElasticListView mListView;
    // 搜索框
    protected SearchView searchBox;

    public static Fragment getInstance(String tag) {
        Fragment fragment = null;
        tabTag = tag;
        if (tag.equals(TaskConstants.MY_BUYERCLUES_PAGE)) {
            fragment = new BuyerLeadFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View baseView;
        baseView = inflater.inflate(R.layout.fragment_task, container, false);

        searchBox = (SearchView) baseView.findViewById(R.id.search_box);
        searchBox.setOnQueryTextListener(this);
        searchBox.setOnCloseListener(this);
        mListView = (ElasticListView) baseView.findViewById(R.id.mElasticListView);
        this.mListView.setonRefreshListener(this);
        this.mListView.setonLoadMoreListener(this);
        View emptyView = baseView.findViewById(R.id.empty_text);
        mListView.setEmptyView(emptyView);
        return baseView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }

    @Override
    public abstract void onRefresh();

    @Override
    public abstract void onLoadMore();

    @Override
    public void onClick(View view) {
    }

}
