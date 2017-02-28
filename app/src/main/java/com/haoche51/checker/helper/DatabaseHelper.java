package com.haoche51.checker.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.CheckUploadTaskDAO;
import com.haoche51.checker.DAO.CompressedPhotoDAO;
import com.haoche51.checker.DAO.LiYangVehicleDAO;
import com.haoche51.checker.DAO.NoticeDAO;
import com.haoche51.checker.DAO.migration.version.MigrateV17ToV18;
import com.haoche51.checker.DAO.migration.version.MigrateV18ToV19;
import com.haoche51.checker.DAO.migration.version.MigrateV19ToV20;
import com.haoche51.checker.DAO.migration.version.MigrateV20ToV21;
import com.haoche51.checker.DAO.migration.version.MigrateV21ToV22;
import com.haoche51.checker.DAO.migration.version.MigrateV22ToV23;
import com.haoche51.checker.DAO.migration.version.MigrateV23ToV24;

public class DatabaseHelper extends SQLiteOpenHelper {
  public static String DB_NAME = "checker.db";
  public static int DB_VERSION = 24;

  public DatabaseHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  protected void finalize() throws Throwable {
    getWritableDatabase().close();
    super.finalize();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CheckReportDAO.CREATE_TABLE);
    db.execSQL(NoticeDAO.CREATE_TABLE);
    db.execSQL(LiYangVehicleDAO.CREATE_TABLE);
    db.execSQL(CheckUploadTaskDAO.CREATE_TABLE);//创建上传任务表
    db.execSQL(CompressedPhotoDAO.CREATE_TABLE);//创建创建已经压缩的图片信息表
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    switch (newVersion) {
      case 24:
        new MigrateV23ToV24().applyMigration(db, oldVersion);
        break;
      case 23:
        new MigrateV22ToV23().applyMigration(db, oldVersion);
        break;
      case 22:
        new MigrateV21ToV22().applyMigration(db, oldVersion);
        break;
      case 21:
        new MigrateV20ToV21().applyMigration(db, oldVersion);
        break;
      case 20:
        new MigrateV19ToV20().applyMigration(db, oldVersion);
        break;
      case 19:
        new MigrateV18ToV19().applyMigration(db, oldVersion);
        break;
      case 18:
        new MigrateV17ToV18().applyMigration(db, oldVersion);
        break;
      //TODO ...
    }
  }
}
