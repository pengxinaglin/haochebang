package com.haoche51.checker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.haoche51.checker.R;
import com.haoche51.checker.pager.BaseTagPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 15/9/14.
 */
public class CarTagAdapter extends RecyclerView.Adapter<CarTagAdapter.CarTagViewHolder> {

	private List<Map<String, Object>> photoTags = new ArrayList<>();
	private LayoutInflater inflater;
	private BaseTagPager tagPager;
	private Context mContext;


	public CarTagAdapter(Context mContext, List<Map<String, Object>> photoTags, BaseTagPager tagPager) {
		this.photoTags = photoTags;
		this.mContext = mContext;
		this.tagPager = tagPager;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public CarTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = inflater.inflate(R.layout.item_car_tag, parent, false);
		return new CarTagViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final CarTagViewHolder holder, final int position) {
		Map<String, Object> item = this.photoTags.get(position);
		holder.car_tag.setText(item.get("tagName").toString());
		boolean isSelected = (boolean) item.get("isSelected");
		if (isSelected) {
			holder.car_tag.setBackgroundResource(R.drawable.shape_upload_report);
			holder.car_tag.setTextColor(this.mContext.getResources().getColor(R.color.self_white));
		} else {
			holder.car_tag.setBackgroundResource(R.drawable.shape_sava_report);
			if (item.get("tagName").toString().contains("*"))
				holder.car_tag.setTextColor(this.mContext.getResources().getColor(R.color.hc_self_red));
			else
				holder.car_tag.setTextColor(this.mContext.getResources().getColor(R.color.image_selected_color));
		}
	}


	@Override
	public int getItemCount() {
		return this.photoTags.size();
	}

	public class CarTagViewHolder extends RecyclerView.ViewHolder {
		private Button car_tag;

		public CarTagViewHolder(View itemView) {
			super(itemView);
			car_tag = (Button) itemView.findViewById(R.id.tag);
		}
	}
}
