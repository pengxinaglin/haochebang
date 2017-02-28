package com.haoche51.checker.DAO.migration.version;

import android.database.sqlite.SQLiteDatabase;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.migration.Migration;
import com.haoche51.checker.DAO.migration.MigrationImpl;

public class MigrateV18ToV19 extends MigrationImpl {

	@Override
	public int applyMigration(SQLiteDatabase db, int currentVersion) {
		prepareMigration(db, currentVersion);

		//评估报告表添加字段
		try {
			db.execSQL("ALTER TABLE " + CheckReportDAO.TABLE_NAME + " ADD COLUMN exclusive integer");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getMigratedVersion();
	}

	@Override
	public int getTargetVersion() {
		return 18;
	}

	@Override
	public int getMigratedVersion() {
		return 19;
	}

	@Override
	public Migration getPreviousMigration() {
		return new MigrateV17ToV18();
	}
}