package com.haoche51.checker.item;

import android.annotation.SuppressLint;
import android.widget.Filter;

import com.haoche51.checker.adapter.LocalCityWorkerAdapter;
import com.haoche51.checker.entity.SameCityWorkerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/1/21.
 */
public class WorkerFilter extends Filter {
  private List<SameCityWorkerEntity> source;
  private LocalCityWorkerAdapter mAdapter = null;

  public WorkerFilter(List<SameCityWorkerEntity> mList, LocalCityWorkerAdapter LocalCityWorkerAdapter) {
    this.source = mList;
    this.mAdapter = LocalCityWorkerAdapter;
  }

  public void setSource(List<SameCityWorkerEntity> source) {
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
      List<SameCityWorkerEntity> filters = new ArrayList<SameCityWorkerEntity>();
      for (SameCityWorkerEntity entity : source) {
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
    mAdapter.setmList((List<SameCityWorkerEntity>) results.values);
    mAdapter.notifyDataSetChanged();

  }
}
