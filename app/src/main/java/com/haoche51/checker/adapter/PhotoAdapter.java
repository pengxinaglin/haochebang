package com.haoche51.checker.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.ImageUploadActivity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.pager.CarPhotoPager;
import com.haoche51.checker.pager.TabBasePhotoPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPagerActivity;


/**
 * Created by mac on 15/9/11.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<PhotoEntity> photoPaths = new ArrayList<>();
    private LayoutInflater inflater;
    private TabBasePhotoPager photoPager;
    private Context mContext;
    private boolean isDelete = false;


    public PhotoAdapter(Context mContext, List<PhotoEntity> photoPaths, TabBasePhotoPager photoPager) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        this.photoPager = photoPager;
        inflater = LayoutInflater.from(mContext);

        //先清理图片加载框架缓存的图片，保证图片显示正确
        File dir = Glide.getPhotoCacheDir(mContext);
        String[] list = dir.list();
        for (int i = 0; i < list.length; i++) {
            new File(dir, list[i]).delete();
        }
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_selected_photo, parent, false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        if (position == this.photoPaths.size()) {//显示一个“+”
            Glide.with(mContext)
                    .load(R.drawable.addpic)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.addpic)
                    .error(me.iwf.photopicker.R.drawable.ic_broken_image_black_48dp)
                    .into(holder.ivPhoto);

            holder.tagName.setVisibility(View.GONE);
            holder.position.setVisibility(View.GONE);
        } else {//加载普通照片
            String path = photoPaths.get(position).getPath();
            if (!TextUtils.isEmpty(path)) {
                Uri uri = Uri.fromFile(new File(path));
                Glide.with(mContext)
                        .load(uri)
                        .centerCrop()
                        .thumbnail(0.1f)
                        .placeholder(me.iwf.photopicker.R.drawable.ic_photo_black_48dp)
                        .error(me.iwf.photopicker.R.drawable.ic_broken_image_black_48dp)
                        .into(holder.ivPhoto);
            }

            if (photoPager.getClass().getName().equals(CarPhotoPager.class.getName())) {
                holder.tagName.setVisibility(View.VISIBLE);
                holder.position.setVisibility(View.GONE);
                holder.tagName.setText(this.photoPaths.get(position).getName());
            } else {
                holder.tagName.setVisibility(View.GONE);
                holder.position.setVisibility(View.VISIBLE);
                holder.position.setText(this.photoPaths.get(position).getIndex() + 1 + "");
            }
        }

        holder.vSelected.setVisibility(this.isDelete ? View.VISIBLE : View.GONE);

        //长按看大图
        holder.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (position != photoPaths.size()) {//不是“+”这张图
                    Intent intent = new Intent(mContext, PhotoPagerActivity.class);
                    intent.putExtra(PhotoPagerActivity.EXTRA_PHOTO_PATH, photoPaths.get(position).getPath());
                    if (mContext instanceof ImageUploadActivity) {
                        ((ImageUploadActivity) mContext).previewPhoto(intent);
                    }
                }
                return true;
            }
        });

        //点击红色“-”删除当前这张图片
        holder.vSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != photoPaths.size()) {//不是“+”这张图
                    String tagName = photoPaths.get(position).getName();
                    photoPaths.remove(position);
                    if (photoPager.getOnPhotoListChangedListener() != null) {
                        photoPager.getOnPhotoListChangedListener().onChange();
                        photoPager.getOnPhotoListChangedListener().onDeleteOrTagChange(tagName);
                    }

                    //刷新界面
                    PhotoAdapter.this.notifyDataSetChanged();
                    if (photoPaths.size() == 0)//没有图片了，该显示没有图片的提示页面
                        photoPager.initUI();
                }
            }
        });
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public int getItemCount() {
        return this.isDelete ? this.photoPaths.size() : this.photoPaths.size() + 1;//如果是删除，不显示最后的“+”号图片
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;
        private TextView tagName, position;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_photo);
            vSelected = itemView.findViewById(me.iwf.photopicker.R.id.v_selected);
            vSelected.setVisibility(PhotoAdapter.this.isDelete ? View.VISIBLE : View.GONE);
            tagName = (TextView) itemView.findViewById(R.id.tagName);
            position = (TextView) itemView.findViewById(R.id.position);
        }
    }
}