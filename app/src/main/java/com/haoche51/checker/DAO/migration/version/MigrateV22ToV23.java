package com.haoche51.checker.DAO.migration.version;

import android.database.sqlite.SQLiteDatabase;
import com.haoche51.checker.DAO.migration.Migration;
import com.haoche51.checker.DAO.migration.MigrationImpl;

/**
 * Created by mac on 15/11/25.
 */
public class MigrateV22ToV23 extends MigrationImpl {

  @Override
  public int applyMigration(SQLiteDatabase db, int currentVersion) {
    prepareMigration(db, currentVersion);
    return getMigratedVersion();
  }

  @Override
  public int getTargetVersion() {
    return 22;
  }

  @Override
  public int getMigratedVersion() {
    return 23;
  }

  @Override
  public Migration getPreviousMigration() {
    return new MigrateV21ToV22();
  }
}
