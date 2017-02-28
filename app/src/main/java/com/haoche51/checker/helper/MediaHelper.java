package com.haoche51.checker.helper;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.MediaEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaHelper {
    private Context context;

    public MediaHelper(Context context) {
        this.context = context;
    }

    /**
     * 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 转换成时长字符串
     *
     * @param duration 时长
     * @return 时长字符串
     */
    public static String changeToDurationStr(long duration) {
        StringBuilder sb = new StringBuilder();
        long hour = duration / 1000 / 60 / 60;
        long min = duration / 1000 / 60;
        long sec = duration / 1000 % 60;
        if (hour < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(hour)).append("时");
        if (min < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(min)).append("分");
        if (sec < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(sec)).append("秒");
        return sb.toString();
    }

    /**
     * 获取本地所有的视频
     *
     * @return
     */
    public List<MediaEntity> getLocalVideos() {
        List<MediaEntity> videoList = new ArrayList<>();
        if (context == null) {
            return videoList;
        }
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Video.Media.DATE_MODIFIED.concat(" desc"));
        StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator).append(TaskConstants.TEMP_HOME_PATH).append(File.separator).append(TaskConstants.TEMP_VIDEO_PATH);
        MediaEntity videoEntity;
        if (cursor == null) {
            return videoList;
        }
        while (cursor.moveToNext()) {
            videoEntity = new MediaEntity();
            String path = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            if (path.toLowerCase().endsWith(".mds")) {
                continue;
            }

            //过滤掉checker目录下的文件
            if (path.toLowerCase().contains(sb.toString().toLowerCase())) {
                continue;
            }

            //文件不存在，则不显示
            if (!new File(path).exists()) {
                continue;
            }

            videoEntity.setPath(path);

            String mimeType = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
            videoEntity.setMimeType(mimeType);

            int id = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            videoEntity.setId(id);
            String displayName = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            videoEntity.setDisplayName(displayName);

            long duration = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            videoEntity.setDuration(duration);
            long size = cursor
                    .getLong(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            videoEntity.setSize(size);
            long modifyDate = cursor
                    .getLong(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
            videoEntity.setModifyDate(modifyDate);
            videoList.add(videoEntity);
        }
        cursor.close();
        return videoList;
    }

    /**
     * 获取本地所有的音频
     *
     * @return
     */
    public ArrayList<MediaEntity> getLocalAudios() {
        ArrayList<MediaEntity> audioList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.Media.DATE_MODIFIED.concat(" desc"));

        if (context == null || cursor==null) {
            return audioList;
        }

        MediaEntity audioEntity;
        while (cursor.moveToNext()) {
            audioEntity = new MediaEntity();

            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            if (!filePath.toLowerCase().endsWith(".mp3")) {
                continue;
            }

            //文件不存在，则不显示
            if (!new File(filePath).exists()) {
                continue;
            }

            audioEntity.setPath(filePath);

            audioEntity.setDuration(cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION)));
            audioEntity.setDisplayName(cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));

            String mimeType = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
            audioEntity.setMimeType(mimeType);

            long size = cursor
                    .getLong(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            audioEntity.setSize(size);

            long modifyDate = cursor
                    .getLong(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED));
            audioEntity.setModifyDate(modifyDate);
            audioList.add(audioEntity);
        }
        cursor.close();
        return audioList;
    }
}