package com.haoche51.checker.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.haoche51.checker.R;
import com.haoche51.checker.custom.RoundImageView;
import com.haoche51.checker.helper.ImageLoaderHelper;

import java.util.List;

public class OfflineSoldConfirmAdapter extends HCCommonAdapter<String> {

	public OfflineSoldConfirmAdapter(Context context, List<String> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public void fillViewData(HCCommonViewHolder holder, int position) {
		RoundImageView iv_photo = holder.findTheView(R.id.iv_upload_photo);
		ImageView iv_delete = holder.findTheView(R.id.iv_delete);
		ImageLoaderHelper.displayImage("file://" + mList.get(position), iv_photo);
		setClick(iv_delete, position);
	}

	private void setClick(final ImageView iv_delete, final int position) {
		iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mList.remove(position);
					notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
