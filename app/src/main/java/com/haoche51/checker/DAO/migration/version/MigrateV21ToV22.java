package com.haoche51.checker.DAO.migration.version;

import android.database.sqlite.SQLiteDatabase;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.CheckUploadTaskDAO;
import com.haoche51.checker.DAO.CompressedPhotoDAO;
import com.haoche51.checker.DAO.migration.Migration;
import com.haoche51.checker.DAO.migration.MigrationImpl;

/**
 * Created by mac on 15/11/25.
 */
public class MigrateV21ToV22 extends MigrationImpl {

	@Override
	public int applyMigration(SQLiteDatabase db, int currentVersion) {
		prepareMigration(db, currentVersion);
		try {
			db.execSQL(CheckUploadTaskDAO.CREATE_TABLE);//创建上传中任务表
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL(CompressedPhotoDAO.CREATE_TABLE);//创建任务使用过图片记录表
		} catch (Exception e) {
			e.printStackTrace();
		}
		//评估报告表添加字段
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN check_source text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN complete_check integer");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getMigratedVersion();
	}

	@Override
	public int getTargetVersion() {
		return 21;
	}

	@Override
	public int getMigratedVersion() {
		return 22;
	}

	@Override
	public Migration getPreviousMigration() {
		return new MigrateV20ToV21();
	}
}
