package com.haoche51.checker.DAO;

import android.content.ContentValues;
import android.database.Cursor;

import com.haoche51.checker.entity.BaseEntity;
import com.haoche51.checker.entity.NoticeEntity;

public class NoticeDAO extends BaseDAO {
	private static NoticeDAO dao = new NoticeDAO();
	public static NoticeDAO getInstance() {
		return dao;
	}
	
    public static final String TABLE_NAME = "notice";
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + "id            integer primary key,"
            + "title         text not null default '',"
            + "description   text not null default '',"
            + "notification  text not null default '',"
            + "create_time   integer not null default 0,"
            + "is_read       integer not null default 1"
            + ");";

    private static final String[] COLUMNS = {
        "id",
        "title",
        "description",
        "notification",
        "create_time",
        "is_read"
    };
    private static final String DEFAULT_ORDER_BY = "`create_time` DESC";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected ContentValues getContentValues(BaseEntity entity) {
        NoticeEntity noticeEntity = (NoticeEntity) entity;
        ContentValues mValues = new ContentValues();
        mValues.put(COLUMNS[0], noticeEntity.getId());
        mValues.put(COLUMNS[1], noticeEntity.getTitle());
        mValues.put(COLUMNS[2], noticeEntity.getDescription());
        mValues.put(COLUMNS[3], noticeEntity.getNotification());
        mValues.put(COLUMNS[4], noticeEntity.getCreate_time());
        mValues.put(COLUMNS[5], noticeEntity.getIs_read());
        return mValues;
    }

    @Override
    protected BaseEntity getEntityFromCursor(Cursor mCursor) {
    	NoticeEntity noticeEntity = new NoticeEntity();
    	noticeEntity.setId(mCursor.getInt(0));
    	noticeEntity.setTitle(mCursor.getString(1));
    	noticeEntity.setDescription(mCursor.getString(2));
    	noticeEntity.setNotification(mCursor.getString(3));
    	noticeEntity.setCreate_time(mCursor.getInt(4));
    	noticeEntity.setIs_read(mCursor.getInt(5));
        return noticeEntity;
    }

    @Override
    protected String[] getColumns() {
        return COLUMNS;
    }

    @Override
    protected String getDefaultOrderby() {
        return DEFAULT_ORDER_BY;
    }


}
