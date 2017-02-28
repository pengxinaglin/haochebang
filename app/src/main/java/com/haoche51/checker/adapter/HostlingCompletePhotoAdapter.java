package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.RoundImageView;
import com.haoche51.checker.helper.ImageLoaderHelper;

import java.util.List;

/**
 * 完成整备后照片
 */
public class HostlingCompletePhotoAdapter extends BaseAdapter {
  private List<String> photoPathList;
  private Context context;
  private ViewHolder holder;
  private View view;

  public HostlingCompletePhotoAdapter(Context context, List<String> list) {
    this.context = context;
    this.photoPathList = list;
  }

  @Override
  public int getCount() {
    return photoPathList.size();
  }

  @Override
  public Object getItem(int position) {
    return photoPathList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      view = LayoutInflater.from(context).inflate(R.layout.item_offlinesold_confirm_photo, parent, false);
      holder = new ViewHolder();
      holder.iv_photo = (RoundImageView) view.findViewById(R.id.iv_upload_photo);
      holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
      view.setTag(holder);
    } else {
      view = convertView;
      holder = (ViewHolder) view.getTag();
    }

    String path = photoPathList.get(position);
    if (!TextUtils.isEmpty(path)) {
      ImageLoaderHelper.displayImage(TaskConstants.PHOTO_PREFIX + path, holder.iv_photo);
      setClick(holder.iv_delete, position);
    }
    return view;
  }

  private void setClick(final ImageView iv_delete, final int position) {
    iv_delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(android.view.View v) {
        try {
          photoPathList.remove(position);
          notifyDataSetChanged();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private static class ViewHolder {
    private RoundImageView iv_photo;
    private ImageView iv_delete;
  }

}
