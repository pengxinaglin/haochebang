package com.haoche51.checker.activity.media;

import android.content.ContentValues;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.czt.mp3recorder.MP3Recorder;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.util.ToastUtil;

import java.io.File;
import java.io.IOException;


public class AudioRecordActivity extends CommonTitleBaseActivity {
  private static final String LOG_TAG = "AudioRecordActivity";
  private String mFileName = null;

  private Button mRecordButton = null;

  private Button mPlayButton = null;
  private MediaPlayer mPlayer = null;
  private boolean mStartRecording = true;
  private boolean mStartPlaying = true;

  private MP3Recorder mRecorder;
  private TextView tv_record;

  public AudioRecordActivity() {

  }

  private void onRecord(boolean start) {
    if (start) {
      startRecording();
    } else {
      stopRecording();
    }
  }

  private void onPlay(boolean start) {
    if (start) {
      startPlaying();
    } else {
      stopPlaying();
    }
  }

  private void startPlaying() {
    if (TextUtils.isEmpty(mFileName)) {
      ToastUtil.showInfo("请先录音，然后再播放");
      return;
    }
    mPlayer = new MediaPlayer();
    try {
      mPlayer.setDataSource(mFileName);
      mPlayer.prepare();
      mPlayer.start();
    } catch (IOException e) {
      Log.e(LOG_TAG, "prepare() failed");
    }
  }

  private void stopPlaying() {
    if (mPlayer != null) {
      mPlayer.release();
      mPlayer = null;
    }
  }

  private void startRecording() {
    StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
    sb.append(File.separator).append(TaskConstants.TEMP_HOME_PATH).append(File.separator).append(TaskConstants.TEMP_AUDIO_RECORD_PATH).append(File.separator);
    //创建目录
    File parentDir = new File(sb.toString());
    parentDir.mkdirs();
    File recordFile = new File(parentDir, "rc".concat(String.valueOf(System.currentTimeMillis())).concat(".mp3"));
    mRecorder = new MP3Recorder(recordFile);
    mFileName = recordFile.getAbsolutePath();
    try {
      mRecorder.start();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void stopRecording() {
    if (mRecorder == null) {
      return;
    }
    mRecorder.stop();
    /* 然后将录制的文件存储在MediaStore中 ，实现数据共享*/
    ContentValues values = new ContentValues();
    values.put(MediaStore.Audio.Media.TITLE, "评估师录音");
    values.put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis());
    values.put(MediaStore.Audio.Media.DATA, mFileName);
    MediaPlayer mediaPlayer = new MediaPlayer();
    try {
      mediaPlayer.setDataSource(mFileName);
      mediaPlayer.prepare();
      values.put(MediaStore.Audio.Media.DURATION, mediaPlayer.getDuration());
    } catch (IOException e) {
      e.printStackTrace();
    }
    /* 获取插入数据的Uri */
    getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
    mRecorder = null;
  }

  @Override
  public View getHCContentView() {
    View contentView = View.inflate(this, R.layout.activity_audio_record, null);
    tv_record = (TextView) contentView.findViewById(R.id.tv_record);
    mRecordButton = (Button) contentView.findViewById(R.id.btn_record);

    mRecordButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onRecord(mStartRecording);
        if (mStartRecording) {
          mRecordButton.setText("结束录音");
          tv_record.setVisibility(View.VISIBLE);
        } else {
          mRecordButton.setText("开始录音");
          tv_record.setVisibility(View.GONE);
        }
        mStartRecording = !mStartRecording;
      }
    });
    mPlayButton = (Button) contentView.findViewById(R.id.btn_play);
    mPlayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onPlay(mStartPlaying);
        if (mStartPlaying) {
          mPlayButton.setText("停止播放");
        } else {
          mPlayButton.setText("播放录音");
        }
        mStartPlaying = !mStartPlaying;
      }
    });
    return contentView;
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {

  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.hc_audio_record));
  }

  @Override
  public void onPause() {
    super.onPause();
    if (mRecorder != null) {
      mRecorder.stop();
      mRecorder = null;
    }

    if (mPlayer != null) {
      mPlayer.release();
      mPlayer = null;
    }
  }
}