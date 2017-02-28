package com.haoche51.checker.item;

import java.util.ArrayList;
import java.util.List;

import com.haoche51.checker.adapter.ESearchListAdapter;
import com.haoche51.checker.entity.CheckTaskEntity;

import android.annotation.SuppressLint;
import android.widget.Filter;
/**
 * Evaluate Task Filter 
 * @author xuhaibo
 *
 */
public class EvaluateTasksFilter extends Filter {
	private List<CheckTaskEntity> source;
	private ESearchListAdapter  mAdapter = null;
	public EvaluateTasksFilter(List<CheckTaskEntity> source, ESearchListAdapter mAdapter) {
		this.source = source;
		this.mAdapter = mAdapter;
	}
	

	public void setSource(List<CheckTaskEntity> source) {
		this.source = source;
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults result= new FilterResults();
		if (constraint ==null || constraint.length() ==0) {
			result.values = source;
			result.count = source.size();
		}else {
			List<CheckTaskEntity> filters = new ArrayList<CheckTaskEntity>();
			for (CheckTaskEntity checkTask:source) {
				if (checkTask.getSeller_name().toUpperCase().startsWith(constraint.toString().toUpperCase()) ||
					checkTask.getSeller_name().toUpperCase().startsWith(constraint.toString().toUpperCase())){
						filters.add(checkTask);
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
		mAdapter.setAdapterTask((List<CheckTaskEntity>)results.values);
		mAdapter.notifyDataSetChanged();

	}

}
