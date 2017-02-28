package com.haoche51.checker.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.constants.CameraConstants;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.util.PhotoComparator;
import com.haoche51.checker.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingHelper {
	private static final String SETTING_FILEE = "new_user_setting";
	// 拍摄位置
	private static final String STANDARD_INDEX = "standard_index";
	private static final String FREE_INDEX = "free_index";
	private static final String SCRATCH_INDEX = "scratch_index";
	// 拍摄模式
	private static final String CURRENT_MODE = "camera_mode";
	// 标准照顺序
	private static final String STANDARD_PHOTO = "standard_photo";
	// 自由拍摄列表
	private static final String FREE_PHOTO = "free_photo";
	// 瑕疵图列表
	private static final String SCRATCH_PHOTO = "scratch_photo";
	// 全局设置
	private static final String GOLABLE_SETTING = "camera_global_setting";
	private SharedPreferences mReader = null;
	private SharedPreferences.Editor mWriter = null;
	private Gson mGson;

	public SettingHelper(Context context) {
		mReader = context.getSharedPreferences(SETTING_FILEE,
			Context.MODE_PRIVATE);
		mWriter = mReader.edit();
		mGson = new Gson();
	}

	/**
	 * @param taskId 任务id
	 * @param mode   相机模式
	 * @param index  设置位置
	 * @return
	 */
	public SettingHelper setStandardIndex(String taskId, int mode, int index) {
		switch (mode) {
			case CameraConstants.CAMERA_STANDARD_MODE:
				mWriter.putInt(STANDARD_INDEX + taskId, index);
				break;
			case CameraConstants.CAMERA_FREE_MODE:
				mWriter.putInt(FREE_INDEX + taskId, index);
				break;
			case CameraConstants.CAMERA_SCRATCH_MODE:
				mWriter.putInt(SCRATCH_INDEX + taskId, index);
				break;
		}
		return this;
	}

	/**
	 * 清除
	 *
	 * @param taskId
	 * @param mode
	 * @return
	 */
	public SettingHelper clearIndex(String taskId, int mode) {
		switch (mode) {
			case CameraConstants.CAMERA_STANDARD_MODE:
				mWriter.remove(STANDARD_INDEX + taskId);
				break;
			case CameraConstants.CAMERA_FREE_MODE:
				mWriter.remove(FREE_INDEX + taskId);
				break;
			case CameraConstants.CAMERA_SCRATCH_MODE:
				mWriter.remove(SCRATCH_INDEX + taskId);
				break;
			default: // -1 clean all
				mWriter.remove(STANDARD_INDEX + taskId);
				mWriter.remove(FREE_INDEX + taskId);
				mWriter.remove(SCRATCH_INDEX + taskId);
				break;
		}
		return this;
	}

	/**
	 * 获取
	 *
	 * @param taskId
	 * @param mode
	 * @return
	 */
	public int getIndex(String taskId, int mode) {
		switch (mode) {
			case CameraConstants.CAMERA_STANDARD_MODE:
				return mReader.getInt(STANDARD_INDEX + taskId, 0);
			case CameraConstants.CAMERA_FREE_MODE:
				return mReader.getInt(FREE_INDEX + taskId, 0);
			case CameraConstants.CAMERA_SCRATCH_MODE:
				return mReader.getInt(SCRATCH_INDEX + taskId, 0);
		}
		return 0;
	}

	public SettingHelper setCameraMode(String taskId, int mode) {
		mWriter.putInt(CURRENT_MODE + taskId, mode);
		return this;
	}

	public SettingHelper clearCameraMode(String taskId) {
		mWriter.remove(CURRENT_MODE + taskId);
		return this;
	}

	public int getCameraMode(String taskId) {
		return mReader.getInt(CURRENT_MODE + taskId,
			CameraConstants.CAMERA_STANDARD_MODE);
	}

	public boolean commit() {
		return mWriter.commit();
	}

	/**
	 * 全局设置
	 *
	 * @return
	 */
	public List<PhotoEntity> getGlobalSetting() {
		List<PhotoEntity> globalList = mGson.fromJson(
			mReader.getString(GOLABLE_SETTING, ""),
			new TypeToken<List<PhotoEntity>>() {
			}.getType());
		if (globalList == null) {
			globalList = defaultSetting(); // 如果未设置过，读取默认设置
		}
		return globalList;
	}

	public SettingHelper setGobalSetting(List<PhotoEntity> golablesetting) {
		mWriter.putString(GOLABLE_SETTING, mGson.toJson(golablesetting));
		return this;
	}

	/**
	 * 初始化默认设置
	 *
	 * @return
	 */
	public static List<PhotoEntity> defaultSetting() {
		List<PhotoEntity> globalList = new ArrayList<>();
		for (int i = 0; i < CameraConstants.upload_order.length; i++) {
			int index = CameraConstants.upload_order[i];
			int type = 0;
			if (index < CameraConstants.OUT_INDEX) { // 外观12 张
				type = PictureConstants.OUTER_PICTURE_CHOSE;
			} else if (index < CameraConstants.INNER_INDEX) { // 内饰18张
				type = PictureConstants.INNER_PICTURE_CHOSE;
			} else { // 细节9张
				type = PictureConstants.DETAIL_PICTURE_CHOSE;
			}
			globalList.add(new PhotoEntity(CameraConstants.CAMERA_STANDARD_MODE, index, CameraConstants.PHOTO_ENNUM[i], type, CameraConstants.defaultindicatorText[i], null));
		}
		return globalList;
	}


	/**
	 * 获得默认的标签tag
	 */
	public List<Map<String, Object>> getDefaultStrings(String taskId) {
		List<PhotoEntity> standardList;
		standardList = mGson.fromJson(
			mReader.getString(STANDARD_PHOTO + taskId, ""),
			new TypeToken<List<PhotoEntity>>() {
			}.getType());

		//是否为空
		if (standardList == null) {
			//写入一个标准的模板
			String json = mGson.toJson(defaultSetting());
			PreferenceUtil.putString(GlobalData.context, STANDARD_PHOTO + taskId, json);
			standardList = mGson.fromJson(json, new TypeToken<List<PhotoEntity>>() {
			}.getType());
		}

		PhotoComparator comparator = new PhotoComparator();
		Collections.sort(standardList, comparator);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < standardList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", standardList.get(i).getName());
			map.put("enumeration", standardList.get(i).getEnumeration());

			list.add(map);
		}
		return list;
	}

	// 标准照设置
	public List<PhotoEntity> getStandardPhoto(String taskId) {
		List<PhotoEntity> standardList;
		standardList = mGson.fromJson(
			mReader.getString(STANDARD_PHOTO + taskId, ""),
			new TypeToken<List<PhotoEntity>>() {
			}.getType());
		return standardList;
	}

	public SettingHelper clearStandardPhoto(String taskId) {
		mWriter.remove(STANDARD_PHOTO + taskId);
		return this;
	}

	public SettingHelper setStandardPhoto(List<PhotoEntity> standard,
	                                      String taskId) {
		mWriter.putString(STANDARD_PHOTO + taskId, mGson.toJson(standard));
		return this;
	}

	// 瑕疵照设置
	public List<PhotoEntity> getScratchPhoto(String taskId) {
		List<PhotoEntity> scratchList;
		scratchList = mGson.fromJson(
			mReader.getString(SCRATCH_PHOTO + taskId, ""),
			new TypeToken<List<PhotoEntity>>() {
			}.getType());
		return scratchList;
	}

	public SettingHelper setScratchPhoto(List<PhotoEntity> scratchList,
	                                     String taskId) {
		mWriter.putString(SCRATCH_PHOTO + taskId, mGson.toJson(scratchList));
		return this;
	}

	public SettingHelper clearScratchPhoto(String taskId) {
		mWriter.remove(SCRATCH_PHOTO + taskId);
		return this;
	}

	// 自由拍照
	public List<PhotoEntity> getFreePhoto(String taskId) {
		List<PhotoEntity> freeList;
		freeList = mGson.fromJson(mReader.getString(FREE_PHOTO + taskId, ""),
			new TypeToken<List<PhotoEntity>>() {
			}.getType());
		return freeList;
	}

	public SettingHelper setFreePhoto(List<PhotoEntity> freeList, String taskId) {
		mWriter.putString(FREE_PHOTO + taskId, mGson.toJson(freeList));
		return this;
	}

	public SettingHelper clearFreePhoto(String taskId) {
		mWriter.remove(FREE_PHOTO + taskId);
		return this;
	}

}
