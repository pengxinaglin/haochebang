package com.haoche51.checker.DAO.migration.version;

import android.database.sqlite.SQLiteDatabase;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.LiYangVehicleDAO;
import com.haoche51.checker.DAO.migration.Migration;
import com.haoche51.checker.DAO.migration.MigrationImpl;

public class MigrateV19ToV20 extends MigrationImpl {

	@Override
	public int applyMigration(SQLiteDatabase db, int currentVersion) {
		prepareMigration(db, currentVersion);

		//评估报告表添加字段
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN liyang_model_id text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN liyang_brand_name text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN liyang_series_name text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN liyang_model_name text");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//新建表
		try {
			db.execSQL("DROP TABLE IF EXISTS " + LiYangVehicleDAO.TABLE_NAME);
			db.execSQL(LiYangVehicleDAO.CREATE_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getMigratedVersion();
	}

	@Override
	public int getTargetVersion() {
		return 19;
	}

	@Override
	public int getMigratedVersion() {
		return 20;
	}

	@Override
	public Migration getPreviousMigration() {
		return new MigrateV18ToV19();
	}
}