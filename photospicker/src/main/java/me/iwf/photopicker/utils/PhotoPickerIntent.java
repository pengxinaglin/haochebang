package me.iwf.photopicker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.entity.CompressedPhotoEntity;

/**
 * Created by donglua on 15/7/2.
 */
public class PhotoPickerIntent extends Intent {

	private PhotoPickerIntent() {
	}

	private PhotoPickerIntent(Intent o) {
		super(o);
	}

	private PhotoPickerIntent(String action) {
		super(action);
	}

	private PhotoPickerIntent(String action, Uri uri) {
		super(action, uri);
	}

	private PhotoPickerIntent(Context packageContext, Class<?> cls) {
		super(packageContext, cls);
	}

	public PhotoPickerIntent(Context packageContext) {
		super(packageContext, PhotoPickerActivity.class);
	}

	public void setPhotoCount(int photoCount) {
		this.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, photoCount);
	}

	public void setShowCamera(boolean showCamera) {
		this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
	}

	public void setFilterPhotoList(String compressedPhotoEntities) {
		Type type = new TypeToken<ArrayList<CompressedPhotoEntity>>() {
		}.getType();
		this.putExtra(PhotoPickerActivity.KEY_FILTER_PHOTOS, (ArrayList<CompressedPhotoEntity>) new Gson().fromJson(compressedPhotoEntities, type));
	}
}
