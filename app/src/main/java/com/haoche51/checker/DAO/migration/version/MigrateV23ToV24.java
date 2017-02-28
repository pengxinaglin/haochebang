package com.haoche51.checker.DAO.migration.version;

import android.database.sqlite.SQLiteDatabase;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.CheckUploadTaskDAO;
import com.haoche51.checker.DAO.migration.Migration;
import com.haoche51.checker.DAO.migration.MigrationImpl;

/**
 * Created by mac on 15/11/25.
 */
public class MigrateV23ToV24 extends MigrationImpl {

	@Override
	public int applyMigration(SQLiteDatabase db, int currentVersion) {
		prepareMigration(db, currentVersion);
		//评估报告表添加字段
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN is_channel integer");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN audio_url text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN video_url text");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//上传中任务表添加字段
		try {
			db.execSQL("ALTER TABLE " + CheckUploadTaskDAO.TABLE_NAME + " ADD COLUMN max integer");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE " + CheckUploadTaskDAO.TABLE_NAME + " ADD COLUMN audio_json text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE " + CheckUploadTaskDAO.TABLE_NAME + " ADD COLUMN video_json text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getMigratedVersion();
	}

	@Override
	public int getTargetVersion() {
		return 23;
	}

	@Override
	public int getMigratedVersion() {
		return 24;
	}

	@Override
	public Migration getPreviousMigration() {
		return new MigrateV22ToV23();
	}
}
