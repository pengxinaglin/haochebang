package com.haoche51.checker.pager;

import android.app.Activity;
import android.view.View;
import com.haoche51.checker.adapter.CarTagAdapter;
import com.haoche51.checker.constants.PictureConstants;

import java.util.Map;

/**
 * 选择车身位置标签 内饰页面
 * Created by mac on 15/9/14.
 */
public class CarInnerTagPager extends BaseTagPager {

  public CarInnerTagPager(Activity mActivity) {
    super(mActivity);
  }

  @Override
  public void initData() {
    if (photoTags.size() == 0) {
      this.mAdapter = new CarTagAdapter(this.mActivity, this.photoTags, this);
      this.recycler.setAdapter(this.mAdapter);
    }
  }

  @Override
  public void onItemClick(View view, int position) {
    Map<String, Object> map = photoTags.get(position);
    map.put("isSelected", true);
    mAdapter.notifyDataSetChanged();
    if (this.onTagSelectedListener != null)
	    this.onTagSelectedListener.onTagSelected((String) map.get("tagName"), PictureConstants.INNER_PICTURE_CHOSE, position , (Integer) photoTags.get(position).get("enumeration"));
  }
}
