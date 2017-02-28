package com.haoche51.checker.pager;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.haoche51.checker.R;
import com.haoche51.checker.adapter.CarTagAdapter;
import com.haoche51.checker.listener.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 15/9/14.
 */
public abstract class BaseTagPager implements RecyclerItemClickListener.OnItemClickListener {
  public Activity mActivity;
  protected View rootView;

  public RecyclerView recycler;
  public RecyclerItemClickListener itemClickListener;
  public List<Map<String, Object>> photoTags = new ArrayList<>();
  public CarTagAdapter mAdapter;
  public OnTagSelectedListener onTagSelectedListener;

  public interface OnTagSelectedListener {
    void onTagSelected(String tagName, int type, int index, int enumeration);
  }

  public void setOnTagSelectedListener(OnTagSelectedListener onTagSelectedListener) {
    this.onTagSelectedListener = onTagSelectedListener;
  }

  public BaseTagPager(Activity mActivity) {
    this.mActivity = mActivity;
    rootView = initView();
    initData();
  }

  public View initView() {
    View view = View.inflate(this.mActivity, R.layout.popupwindow_base_car_tags, null);
    this.recycler = (RecyclerView) view.findViewById(R.id.recycler_contract_photo);
    initRecyclerView();
    return view;
  }

  private void initRecyclerView() {
    GridLayoutManager layoutManager = new GridLayoutManager(this.mActivity, 4);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    this.recycler.setLayoutManager(layoutManager);
    this.recycler.setHasFixedSize(true);
    this.itemClickListener = new RecyclerItemClickListener(this.mActivity, this);
    this.recycler.addOnItemTouchListener(this.itemClickListener);
  }

  public View getRootView() {
    return this.rootView;
  }

  public abstract void initData();

  public void onChange(String tagName) {
    if (this.mAdapter != null)
      for (Map<String, Object> e : this.photoTags) {
        if (e.get("tagName").equals(tagName)) {
          e.put("isSelected", false);
          this.mAdapter.notifyDataSetChanged();
          break;
        }
      }
  }

  @Override
  public abstract void onItemClick(View view, int position);

}