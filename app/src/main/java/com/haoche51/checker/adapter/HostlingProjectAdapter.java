package com.haoche51.checker.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.hostling.SubmitHostlingActivity;
import com.haoche51.checker.entity.HostlingTaskEntity;
import com.haoche51.checker.helper.ImageLoaderHelper;
import com.haoche51.checker.widget.HCNoScrollGridView;

import java.util.List;

import me.iwf.photopicker.utils.PhotoPickerIntent;


/**
 * Created by mac on 16/1/28.
 */
public class HostlingProjectAdapter extends HCCommonAdapter<HostlingTaskEntity.ProjectEntity> {

	int index;

	public HostlingProjectAdapter(Context context, List<HostlingTaskEntity.ProjectEntity> data, int index, int layoutId) {
		super(context, data, layoutId);
		this.index = index;
	}

	@Override
	public int getCount() {
		return mList.size() + 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public void notifyDataSetChanged() {
		//重新排序Id
		for (int i = 1; i <= mList.size(); i++) {
			mList.get(i - 1).setRepair_id(i + index);
		}
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//显示加item
		if (position >= mList.size()) {
			convertView = View.inflate(mContext, R.layout.layout_hostling_add_item, null);
			return convertView;
		}

		HCCommonViewHolder holder;
		if (convertView == null) {
			holder = new HCCommonViewHolder(mContext, parent, mLayoutId, position);
		} else {
			holder = (HCCommonViewHolder) convertView.getTag();
		}
		if (holder == null) {
			holder = new HCCommonViewHolder(mContext, parent, mLayoutId, position);
		}
		fillViewData(holder, position);
		return holder.getConvertView();
	}

	@Override
	public void fillViewData(final HCCommonViewHolder holder, final int position) {
		final HostlingTaskEntity.ProjectEntity projectEntity = getItem(position);
		//项目编号
		holder.setTextViewText(R.id.tv_position, (position + 1 + index) + "");
		//项目名称
		final EditText tv_project_name = holder.findTheView(R.id.tv_project_name);
		if (tv_project_name.getTag() instanceof TextWatcher) {
			tv_project_name.removeTextChangedListener((TextWatcher) (tv_project_name.getTag()));
		}
		tv_project_name.setText(projectEntity.getName());
		tv_project_name.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					final boolean focus = projectEntity.isFocus();
					check(position);
					if (!focus && !tv_project_name.isFocused()) {
						tv_project_name.requestFocus();
						tv_project_name.onWindowFocusChanged(true);
					}
				}
				return false;
			}
		});

		final TextWatcher watcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					projectEntity.setName(null);
				} else {
					projectEntity.setName(s.toString());
				}
			}
		};
		tv_project_name.addTextChangedListener(watcher);
		tv_project_name.setTag(watcher);

		//项目金额
		final EditText tv_project_fee = holder.findTheView(R.id.tv_project_fee);
		if (tv_project_fee.getTag() instanceof TextWatcher) {
			tv_project_fee.removeTextChangedListener((TextWatcher) (tv_project_fee.getTag()));
		}

		tv_project_fee.setText(projectEntity.getExpect_price());
		tv_project_fee.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(final View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					final boolean focus = projectEntity.isFocus();
					check(position);
					if (!focus && !tv_project_fee.isFocused()) {
						tv_project_fee.requestFocus();
						tv_project_fee.onWindowFocusChanged(true);
					}
				}
				return false;
			}
		});

		final TextWatcher watcherFee = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				projectEntity.setExpect_price(s.toString());
			}
		};
		tv_project_fee.addTextChangedListener(watcherFee);
		tv_project_fee.setTag(watcherFee);

		//删除本项目
		holder.findTheView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mList != null && !mList.isEmpty()) {
					mList.remove(position);
					HostlingProjectAdapter.this.notifyDataSetChanged();
				}
			}
		});
		//整备前图片
		final List<String> pre_image = projectEntity.getPre_image();
		HCNoScrollGridView gridView = holder.findTheView(R.id.list_gridview);
		GridAdapter mAdapter = new GridAdapter(mContext, pre_image, position, R.layout.layout_hostling_photo_item);
		gridView.setAdapter(mAdapter);
	}

	private void check(int position) {
		if (mList == null || mList.isEmpty()) return;
		for (HostlingTaskEntity.ProjectEntity l : mList) {
			l.setFocus(false);
		}
		mList.get(position).setFocus(true);
	}

	private class GridAdapter extends HCCommonAdapter<String> {
		private int position;

		public GridAdapter(Context context, List<String> data, int position, int layoutId) {
			super(context, data, layoutId);
			this.position = position;
		}

		@Override
		public int getCount() {
			return mList.size() + 1;
		}

		@Override
		public void fillViewData(HCCommonViewHolder holder, int position) {
			//删除本张图片
			ImageView iv_delete = holder.findTheView(R.id.iv_delete);
			iv_delete.setTag(position);
			iv_delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mList.remove((int) view.getTag());
					HostlingProjectAdapter.this.notifyDataSetChanged();
				}
			});
			ImageView iv_image = holder.findTheView(R.id.iv_image);
			iv_image.setScaleType(ImageView.ScaleType.FIT_XY);
			if (position < mList.size())
				if (!TextUtils.isEmpty(mList.get(position))) {
					if (mList.get(position).contains("http"))
						ImageLoaderHelper.displayImage(mList.get(position), iv_image);
					else
						ImageLoaderHelper.displayImage("http://image1.haoche51.com/" + mList.get(position), iv_image);
				}
			//最后一张默认添加选图
			if (position == mList.size()) {
				holder.findTheView(R.id.tv_add_image).setVisibility(View.VISIBLE);
				iv_delete.setVisibility(View.GONE);
				iv_image.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//打开选择图片
						PhotoPickerIntent intent = new PhotoPickerIntent(mContext);
						intent.setPhotoCount(10);
						intent.setShowCamera(false);
						((Activity) mContext).startActivityForResult(intent, SubmitHostlingActivity.SELECT_PHOTO);
						//记录当前点击的位置
						((SubmitHostlingActivity) mContext).currentClick = GridAdapter.this.position;
					}
				});
			}
		}
	}
}
