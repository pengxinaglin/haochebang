package com.haoche51.checker.pager;

import android.app.Activity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.DAO.CompressedPhotoDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.ImageUploadActivity;
import com.haoche51.checker.adapter.PhotoAdapter;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.entity.CompressedPhotoEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.util.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by mac on 15/9/11.
 * 瑕疵照片
 */
public class DefectsPhotoPager extends TabBasePhotoPager {

    public DefectsPhotoPager(Activity mActivity) {
        super(mActivity);
        this.REQUEST_CODE = PictureConstants.SELECT_DEFECT_PICTURE;
        this.openAlbum.setText(GlobalData.resourceHelper.getString(R.string.select_defects));
        this.photoType.setText(GlobalData.resourceHelper.getString(R.string.upload_defects));
    }

    @Override
    public void openAlbum(View v) {
        if (!this.mAdapter.isDelete()) {//删除状态不能选择添加图片
            //读取24小时以内的图片记录
            ArrayList<CompressedPhotoEntity> compressedPhotoEntities = (ArrayList<CompressedPhotoEntity>) CompressedPhotoDAO.getInstance().getBetween24H();

            PhotoPickerIntent intent = new PhotoPickerIntent(this.mActivity);
            intent.setPhotoCount(50);//设置每次可以选着50张图片
            intent.setShowCamera(false);
            Type type = new TypeToken<ArrayList<CompressedPhotoEntity>>() {
            }.getType();
            intent.setFilterPhotoList(new Gson().toJson(compressedPhotoEntities, type));
            this.mActivity.startActivityForResult(intent, this.REQUEST_CODE);
        } else
            ToastUtil.showInfo(GlobalData.resourceHelper.getString(R.string.other_operating));
    }

    @Override
    public void deletePhoto() {
        removeOnItemTouchListener();
        this.mAdapter.setDelete(true);
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void cancelDeletePhoto() {
        addOnitemClickListener();
        this.mAdapter.setDelete(false);
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deletePhotoFinish() {
        removeOnItemTouchListener();
        addOnitemClickListener();
        //重新设置图片的Index值
        for (int i = 0; i < this.photoPaths.size(); i++) {
            this.getPhotoPaths().get(i).setIndex(i);
        }
        this.mAdapter.setDelete(false);
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == this.photoPaths.size()) {//点击“+”，打开相册
            openAlbum(view);
        } else if (!mAdapter.isDelete()) {//给这张图加瑕疵位置
            view.setTag(position);
            ((ImageUploadActivity) this.mActivity).setPhotoTag(this.REQUEST_CODE, view);
        }
    }

    @Override
    public void initData() {
        if (this.mAdapter == null) {
            this.mAdapter = new PhotoAdapter(this.mActivity, this.photoPaths, this);
            this.recycler.setAdapter(this.mAdapter);
        }
    }

    public void initUI() {
        if (this.photoPaths.size() > 0) {
            this.title.setVisibility(View.VISIBLE);
            this.emptyView.setVisibility(View.GONE);
            this.refresh.setVisibility(View.VISIBLE);
        } else {
            this.title.setVisibility(View.GONE);
            this.emptyView.setVisibility(View.VISIBLE);
            this.refresh.setVisibility(View.GONE);
        }
    }

    @Override
    public void notifyDataChanged(List<String> photos) {
        for (String s : photos) {
            PhotoEntity e = new PhotoEntity();
            e.setPath(s);
            e.setIndex(this.photoPaths.size());
            e.setType(PictureConstants.DEFECT_PICTURE_CHOSE);
            this.photoPaths.add(e);
            this.mAdapter.notifyDataSetChanged();
        }

        initUI();
    }
}
