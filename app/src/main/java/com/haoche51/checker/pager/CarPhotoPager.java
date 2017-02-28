package com.haoche51.checker.pager;

import android.app.Activity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.DAO.CompressedPhotoDAO;
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
 * 汽车照片
 */
public class CarPhotoPager extends TabBasePhotoPager {
  public CarPhotoPager(Activity mActivity) {
    super(mActivity);
    REQUEST_CODE = PictureConstants.SELECT_CAR_PICTURE;
    this.openAlbum.setText("选择汽车照片");
    this.photoType.setText("上传所有非疵瑕照片");
  }

  @Override
  public void openAlbum(View v) {
    if (!this.mAdapter.isDelete()) {//删除状态不能选择添加图片
      //读取24小时以内的图片记录
      ArrayList<CompressedPhotoEntity> compressedPhotoEntities = (ArrayList<CompressedPhotoEntity>) CompressedPhotoDAO.getInstance().getBetween24H();

      PhotoPickerIntent intent = new PhotoPickerIntent(mActivity);
      intent.setPhotoCount(50);//设置每次可以选着50张图片
      intent.setShowCamera(false);
      Type type = new TypeToken<ArrayList<CompressedPhotoEntity>>() {
      }.getType();
      intent.setFilterPhotoList(new Gson().toJson(compressedPhotoEntities, type));
      mActivity.startActivityForResult(intent, REQUEST_CODE);
    } else
      ToastUtil.showInfo("如需其他操作请先点击右上角按键");
  }

  @Override
  public void deletePhoto() {
    removeOnItemTouchListener();
    mAdapter.setDelete(true);
    mAdapter.notifyDataSetChanged();
  }

  @Override
  public void cancelDeletePhoto() {
    addOnitemClickListener();
    mAdapter.setDelete(false);
    mAdapter.notifyDataSetChanged();
  }

  @Override
  public void deletePhotoFinish() {
    removeOnItemTouchListener();
    addOnitemClickListener();
    mAdapter.setDelete(false);
    mAdapter.notifyDataSetChanged();
  }

  @Override
  public void onItemClick(View view, int position) {
    if (position == photoPaths.size()) {//点击“+”，打开相册
      openAlbum(view);
    } else {//给这张图加标签
      if (!mAdapter.isDelete()) {
        view.setTag(position);
        ((ImageUploadActivity) mActivity).setPhotoTag(REQUEST_CODE, view);
      }
    }
  }

  @Override
  public void initData() {
    if (this.mAdapter == null) {
      this.mAdapter = new PhotoAdapter(this.mActivity, this.photoPaths, this);
      this.recycler.setAdapter(this.mAdapter);
    }
  }

  @Override
  public void initUI() {
    if (photoPaths.size() > 0) {
      this.title.setVisibility(View.VISIBLE);
      emptyView.setVisibility(View.GONE);
      refresh.setVisibility(View.VISIBLE);
    } else {
      this.title.setVisibility(View.GONE);
      emptyView.setVisibility(View.VISIBLE);
      refresh.setVisibility(View.GONE);
    }
  }

  @Override
  public void notifyDataChanged(List<String> photos) {
    for (String s : photos) {
      PhotoEntity e = new PhotoEntity();
      e.setPath(s);
      photoPaths.add(e);
      mAdapter.notifyDataSetChanged();
    }

    initUI();
  }
}
