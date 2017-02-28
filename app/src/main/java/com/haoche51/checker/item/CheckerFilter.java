package com.haoche51.checker.item;

import android.annotation.SuppressLint;
import android.widget.Filter;

import com.haoche51.checker.adapter.LocalCityCheckerAdapter;
import com.haoche51.checker.entity.LocalCheckerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 15/11/24.
 */
public class CheckerFilter extends Filter {
	private List<LocalCheckerEntity> source;
	private LocalCityCheckerAdapter mAdapter = null;

	public CheckerFilter(List<LocalCheckerEntity> mList, LocalCityCheckerAdapter localCityCheckerAdapter) {
		this.source = mList;
		this.mAdapter = localCityCheckerAdapter;
	}

	public void setSource(List<LocalCheckerEntity> source) {
		this.source = source;
	}

	@SuppressLint("DefaultLocale")
	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults result = new FilterResults();
		if (constraint == null || constraint.length() == 0) {
			result.values = source;
			result.count = source.size();
		} else {
			List<LocalCheckerEntity> filters = new ArrayList<LocalCheckerEntity>();
			for (LocalCheckerEntity entity : source) {
				if (entity.getName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
					filters.add(entity);
				}
			}
			result.values = filters;
			result.count = filters.size();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		mAdapter.setmList((List<LocalCheckerEntity>) results.values);
		mAdapter.notifyDataSetChanged();

	}
}
