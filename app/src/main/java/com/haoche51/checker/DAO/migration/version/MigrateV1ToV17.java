package com.haoche51.checker.DAO.migration.version;

import android.database.sqlite.SQLiteDatabase;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.NoticeDAO;
import com.haoche51.checker.DAO.migration.Migration;
import com.haoche51.checker.DAO.migration.MigrationImpl;

/**
 * DB Version 17 之前的版本都执行该脚本
 */
public class MigrateV1ToV17 extends MigrationImpl {

  @Override
  public int applyMigration(SQLiteDatabase db, int currentVersion) {
    //不执行prepare检查，此为第一个脚本
    //prepareMigration(db, currentVersion);
    //删除表（其中VehicleBrandDAO、VehicleSeriesDAO、VehicleModelDAO 不必删除，这三个表基本不变，不动他们）
    //新建表
    db.execSQL("DROP TABLE IF EXISTS " + CheckReportDAO.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + NoticeDAO.TABLE_NAME);

    db.execSQL(CheckReportDAO.CREATE_TABLE);
    db.execSQL(NoticeDAO.CREATE_TABLE);

    return getMigratedVersion();
  }

  @Override
  public int getTargetVersion() {
    return 1;
  }

  @Override
  public int getMigratedVersion() {
    return 17;
  }

  @Override
  public Migration getPreviousMigration() {
    return null;
  }
}