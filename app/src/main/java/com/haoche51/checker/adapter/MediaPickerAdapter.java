package com.haoche51.checker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.MediaEntity;
import com.haoche51.checker.helper.MediaHelper;
import com.haoche51.checker.util.UnixTimeUtil;

import java.util.List;

/**
 * 多媒体数据适配器
 * @author wfx
 */
public class MediaPickerAdapter extends BaseAdapter {

  private List<MediaEntity> listVideos;
  private Context mContext;
  private int layoutId;

  public MediaPickerAdapter(Context context, List<MediaEntity> listVideos, int layoutId) {
    this.mContext = context;
    this.listVideos = listVideos;
    this.layoutId=layoutId;
  }

  public void setListVideos(List<MediaEntity> listVideos) {
    this.listVideos = listVideos;
  }

  @Override
  public int getCount() {
    return listVideos.size();
  }

  @Override
  public Object getItem(int position) {
    return position;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = LayoutInflater.from(mContext).inflate(layoutId, null);
      holder.iv_video = (ImageView) convertView.findViewById(R.id.iv_video);
      holder.tv_video_date = (TextView) convertView.findViewById(R.id.tv_video_date);
      holder.tv_video_name = (TextView) convertView.findViewById(R.id.tv_video_name);
      holder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
      holder.tv_video_size = (TextView) convertView.findViewById(R.id.tv_video_size);
      holder.cb_check = (CheckBox) convertView.findViewById(R.id.cb_check);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    final MediaEntity videoEntity = listVideos.get(position);
    holder.tv_video_date.setText(UnixTimeUtil.format(videoEntity.getModifyDate(), UnixTimeUtil.DEFAULT_PATTERN_2));//ms
    holder.tv_video_name.setText(videoEntity.getDisplayName());
    holder.tv_video_duration.setText(MediaHelper.changeToDurationStr(videoEntity.getDuration()));
    holder.tv_video_size.setText(android.text.format.Formatter.formatFileSize(mContext, videoEntity.getSize()));
    holder.iv_video.setImageBitmap(videoEntity.getBitmap());
    holder.cb_check.setChecked(videoEntity.isChecked());
    holder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        videoEntity.setChecked(isChecked);
      }
    });
    return convertView;
  }


  private class ViewHolder {
    private ImageView iv_video;
    private TextView tv_video_date;
    private TextView tv_video_name;
    private TextView tv_video_duration;
    private TextView tv_video_size;
    private CheckBox cb_check;
  }

}
