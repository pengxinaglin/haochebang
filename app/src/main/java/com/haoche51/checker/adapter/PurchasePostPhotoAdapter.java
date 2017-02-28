package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.custom.RoundImageView;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.helper.ImageLoaderHelper;

import java.util.List;

/**
 * 收车发帖照片
 *
 * @author wfx
 */
public class PurchasePostPhotoAdapter extends BaseAdapter {
    private List<PhotoEntity> mData;
    //带标签的条目的数量
    private int tagSize;
    private Context mContext;
    private PhotoEntity entity;
    private int photoLimit;

    public PurchasePostPhotoAdapter(Context context, List<PhotoEntity> data, int tagSize, int photoLimit) {
        this.mContext = context;
        this.mData = data;
        this.tagSize = tagSize;
        this.photoLimit = photoLimit;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {

        if (position < tagSize) {
            return 0;
        }
        if (position >= tagSize && position < mData.size() - 1) {
            return 1;
        }
        entity = mData.get(position);
        if (position == mData.size() - 1 && !TextUtils.isEmpty(entity.getPath())) {
            return 1;
        }

        return 2;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        //第3类添加符号
        if (type == 2) {
            convertView = View.inflate(mContext, R.layout.item_plus_tag, null);
            return convertView;
        }
        entity = mData.get(position);
        //第1类带标签、不带删除符号的
        if (type == 0) {
            ViewHolder holder;
            if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof ViewHolder)) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_purchase_success, null);
                holder.iv_photo = (RoundImageView) convertView.findViewById(R.id.riv_purchase_photo);
                holder.tv_photo_tag = (TextView) convertView.findViewById(R.id.tv_photo_tag);
                holder.tv_photo_tag_new = (TextView) convertView.findViewById(R.id.tv_photo_tag_new);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            if (!TextUtils.isEmpty(entity.getPath())) {
                //清除imageview的背景
                holder.iv_photo.setBackgroundResource(0);
                ImageLoaderHelper.displayImage("file://" + entity.getPath(), holder.iv_photo);
                holder.tv_photo_tag.setVisibility(View.GONE);
                holder.tv_photo_tag_new.setVisibility(View.VISIBLE);
                holder.tv_photo_tag_new.setText(entity.getName());
            } else {
                holder.tv_photo_tag.setVisibility(View.VISIBLE);
                holder.tv_photo_tag_new.setVisibility(View.INVISIBLE);
                holder.tv_photo_tag.setText(entity.getName());
            }
            return convertView;
        }

        //第2类不带标签、带删除符号的
        if (type == 1) {
            ImageHolder holder;
            if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof ImageHolder)) {
                holder = new ImageHolder();
                convertView = View.inflate(mContext, R.layout.item_no_tag, null);
                holder.iv_photo = (RoundImageView) convertView.findViewById(R.id.riv_purchase_photo);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mData.remove(position);
                        int size = mData.size();
                        if (size < photoLimit && !TextUtils.isEmpty(mData.get(size - 1).getPath())) {
                            mData.add(new PhotoEntity());
                        }
                        notifyDataSetChanged();
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ImageHolder) convertView.getTag();
            }

            if (!TextUtils.isEmpty(entity.getPath())) {
                //清除imageview的背景
                holder.iv_photo.setBackgroundResource(0);
                ImageLoaderHelper.displayImage("file://" + entity.getPath(), holder.iv_photo);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        private RoundImageView iv_photo;
        private TextView tv_photo_tag;
        private TextView tv_photo_tag_new;
    }

    private class ImageHolder {
        private RoundImageView iv_photo;
        private ImageView iv_delete;
    }

}
