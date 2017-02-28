package com.haoche51.checker.DAO;

import android.content.ContentValues;
import android.database.Cursor;

import com.haoche51.checker.entity.BaseEntity;
import com.haoche51.checker.entity.CompressedPhotoEntity;

import java.util.List;

/**
 * 已经压缩过的图片的DAO
 * Created by wfx on 2015/12/29.
 */
public class CompressedPhotoDAO extends BaseDAO {
	public static final String TABLE_NAME = "check_compressed_photo";
	public static final String CREATE_TABLE = "create table " + TABLE_NAME
		+ "(" + "id integer primary key autoincrement,"
		+ "sd_photo_name text unique not null default '',"
//    + "sd_photo_name text not null default '',"
		+ "local_photo_name text unique not null default '',"
		+ "create_mills text not null default '0')";
	private static final String[] COLUMNS = {
		"id",
		"sd_photo_name",
		"local_photo_name",
		"create_mills"
	};
	private static final String DEFAULT_ORDER_BY = "create_mills desc";
	private static CompressedPhotoDAO dao = new CompressedPhotoDAO();

	public static CompressedPhotoDAO getInstance() {
		return dao;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected ContentValues getContentValues(BaseEntity entity) {
		CompressedPhotoEntity compressedPhoto = (CompressedPhotoEntity) entity;
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMNS[1], compressedPhoto.getSd_photo_name());
		contentValues.put(COLUMNS[2], compressedPhoto.getLocal_photo_name());
		contentValues.put(COLUMNS[3], compressedPhoto.getCreate_mills());
		return contentValues;
	}

	@Override
	protected BaseEntity getEntityFromCursor(Cursor mCursor) {
		CompressedPhotoEntity compressedPhoto = new CompressedPhotoEntity();
		compressedPhoto.setId(mCursor.getInt(0));
		compressedPhoto.setSd_photo_name(mCursor.getString(1));
		compressedPhoto.setLocal_photo_name(mCursor.getString(2));
		compressedPhoto.setCreate_mills(Long.parseLong(mCursor.getString(3)));
		return compressedPhoto;
	}

	@Override
	protected String[] getColumns() {
		return COLUMNS;
	}

	@Override
	protected String getDefaultOrderby() {
		return DEFAULT_ORDER_BY;
	}

	/**
	 * 根据条件查询已经压缩过的图片列表
	 *
	 * @param where 查询条件：null表示查询全部
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CompressedPhotoEntity> get(String where) {
		return (List<CompressedPhotoEntity>) super.get(where);
	}

	/**
	 * 查询24小时内的图片的相关记录
	 */
	public List<CompressedPhotoEntity> getBetween24H() {
		long current = System.currentTimeMillis() - 1000 * 60 * 60 * 24;
		String where = "create_mills>" + current;
		return (List<CompressedPhotoEntity>) super.get(where);
	}
}
