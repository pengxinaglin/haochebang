package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.SameCityWorkerEntity;
import com.haoche51.checker.item.WorkerFilter;

import java.util.List;

/**
 * Created by mac on 16/1/21.
 */
public class LocalCityWorkerAdapter extends HCCommonAdapter<SameCityWorkerEntity> {

  public LocalCityWorkerAdapter(Context context, List<SameCityWorkerEntity> data, int layoutId) {
    super(context, data, layoutId);
  }

  @Override
  public void fillViewData(HCCommonViewHolder holder, int position) {
    TextView letter_title = holder.findTheView(R.id.letter_title);
    String currentFirstWord = mList.get(position).getFirst_char();
    if (position > 0) {
      String lastFirstWord = mList.get(position - 1).getFirst_char();
      if (currentFirstWord.equals(lastFirstWord)) {
        letter_title.setVisibility(View.GONE);
      } else {
        letter_title.setVisibility(View.VISIBLE);
        letter_title.setText(currentFirstWord);
      }
    } else {
      letter_title.setVisibility(View.VISIBLE);
      letter_title.setText(currentFirstWord);
    }

    TextView choose = holder.findTheView(R.id.choose);
    choose.setVisibility(mList.get(position).isChoose() ? View.VISIBLE : View.GONE);

    holder.setTextViewText(R.id.checker_name, mList.get(position).getName());
  }

  public Filter getFilter() {
    return new WorkerFilter(mList, this);
  }

  /**
   * 搜索
   */
  public void filter(String query, List<SameCityWorkerEntity> mLocalCityWorkerlist) {
    //全部置为未选中
    for (SameCityWorkerEntity entity : mLocalCityWorkerlist) {
      entity.setChoose(false);
    }
    //根据条件筛选
    if (TextUtils.isEmpty(query)) {
      setmList(mLocalCityWorkerlist);
      notifyDataSetChanged();
    } else {
      getFilter().filter(query);
    }
  }
}
