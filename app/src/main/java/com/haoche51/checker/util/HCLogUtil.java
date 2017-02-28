package com.haoche51.checker.util;


import android.os.Environment;

import com.haoche51.checker.BuildConfig;
import com.haoche51.checker.constants.TaskConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @File:Log.java
 * @Package:com.haoche51.checker.tools
 * @desc:自定义Log接口
 * @author:zhuzuofei
 * @date:2015-1-24 下午2:11:33
 * @version:V1.0
 */
public class HCLogUtil {

  public static final String TAG = "HC-HaoChe51@572";

  public static void d(String tag, String msg) {
    android.util.Log.d(tag, msg);
//		LogUtils.setLogFile(tag);
//		LogUtils.writeLog2File(msg);
  }

  public static void log(String msg) {
    android.util.Log.d("hclogtag", msg);
  }

  public static void e(String tag, String msg) {
    if (BuildConfig.LOG_DEBUG) {
      android.util.Log.e(tag, msg);
    }
  }

  public static void i(String tag, String msg) {
    if (BuildConfig.LOG_DEBUG) {
      android.util.Log.i(tag, msg);
    }
  }

  public static void v(String tag, String msg) {
    if (BuildConfig.LOG_DEBUG) {
      android.util.Log.v(tag, msg);
    }
  }

  public static void w(String tag, String msg) {
    if (BuildConfig.LOG_DEBUG) {
      android.util.Log.w(tag, msg);
    }
  }

  public static void d(String msg) {
    android.util.Log.d(TAG, msg);
  }

  public static void e(String msg) {
    if (BuildConfig.LOG_DEBUG) {
      android.util.Log.e(TAG, msg);
    }
  }

  public static void i(String msg) {
    if (BuildConfig.LOG_DEBUG) {
      android.util.Log.i(TAG, msg);
    }
  }

  public static void v(String msg) {
    if (BuildConfig.LOG_DEBUG) {
      android.util.Log.v(TAG, msg);
    }
  }

  public static void w(String msg) {
    if (BuildConfig.LOG_DEBUG) {
      android.util.Log.w(TAG, msg);
    }
  }


  /**
   * 打印日志到指定文件中
   *
   * @param tag
   * @param msg
   * @author wfx
   */
  public static void printToFile(String tag, String msg) {
    StringBuilder sb = new StringBuilder();
    sb.append(Environment.getExternalStorageDirectory().getAbsolutePath())
      .append(File.separator).append(TaskConstants.TEMP_HOME_PATH)
      .append(File.separator).append(TaskConstants.TEMP_LOG_PATH);

    File destDir = new File(sb.toString());
    destDir.mkdirs();
    File destFile = new File(destDir, "checker.log");
    FileWriter fw = null;
    BufferedWriter bw = null;
    try {
      fw = new FileWriter(destFile, true);
      bw = new BufferedWriter(fw);
      bw.write(tag);
      bw.write(":");
      bw.write(msg);
      bw.newLine();
      bw.flush();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      HCLogUtil.e(tag, "找不到日志输出的目标文件");
    } catch (IOException e) {
      e.printStackTrace();
      HCLogUtil.e(tag, "日志输出遇到问题");
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (fw != null) {
        try {
          fw.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }
  }
}

