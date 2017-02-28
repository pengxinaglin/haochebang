package com.haoche51.checker.activity.media;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.MediaPickerAdapter;
import com.haoche51.checker.entity.MediaEntity;
import com.haoche51.checker.helper.MediaHelper;

import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
import java.util.List;

/**
 * 选择视频
 * Created by wufx on 2016/3/9.
 */
public class VideoPickerActivity extends CommonTitleBaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public final static String KEY_SELECTED_VIDEO = "SELECTED_VIDEO";
    @ViewInject(R.id.tv_no_data)
    private TextView tv_no_data;

    @ViewInject(R.id.lv_video)
    private ListView videoListView;

    private MediaPickerAdapter listViewAdapter;
    private List<MediaEntity> listVideos;
    private int videoSize;
    private MediaEntity checkedVideo;

    @Override
    public View getHCContentView() {
        View contentView = View.inflate(this, R.layout.activity_video_list, null);
        videoListView = (ListView) contentView.findViewById(R.id.lv_video);
        tv_no_data = (TextView) contentView.findViewById(R.id.tv_no_data);
        return contentView;
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        initData();
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.hc_video_list));
        mRightFaction.setVisibility(View.VISIBLE);
        mRightFaction.setText(getResources().getString(R.string.button_ok));
        mRightFaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(KEY_SELECTED_VIDEO, checkedVideo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        MediaHelper provider = new MediaHelper(this);
        listVideos = provider.getLocalVideos();
        videoSize = listVideos.size();
        if (videoSize == 0) {
            tv_no_data.setVisibility(View.VISIBLE);
            videoListView.setVisibility(View.GONE);
        }
        listViewAdapter = new MediaPickerAdapter(this, listVideos, R.layout.item_video_list);
        videoListView.setAdapter(listViewAdapter);
        videoListView.setOnItemClickListener(this);
        videoListView.setOnItemLongClickListener(this);
        //加载视频缩略图
        loadImages();
    }

    /**
     * 加载视频缩略图
     */
    private void loadImages() {
        @SuppressWarnings("deprecation")
        final Object data = getLastNonConfigurationInstance();
        if (data == null) {
            new LoadImagesFromSDCard().execute();
        } else {
            final MediaEntity[] videoEntities = (MediaEntity[]) data;
            if (videoEntities.length == 0) {
                new LoadImagesFromSDCard().execute();
            }
            listViewAdapter.setListVideos(Arrays.asList(videoEntities));
            listViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MediaEntity videoEntity = listVideos.get(position);
        boolean isChecked = !videoEntity.isChecked();
        //限制其只能选中一项
        if (isChecked) {
            checkedVideo = videoEntity;
            checkOnlyOne(position);
        }
        videoEntity.setChecked(isChecked);
        CheckBox cb_check = (CheckBox) view.findViewById(R.id.cb_check);
        cb_check.setChecked(isChecked);
    }

    /**
     * 只选择一项
     *
     * @param curPosition 当前位置
     */
    private void checkOnlyOne(int curPosition) {
        MediaEntity tempVideo;
        for (int i = 0; i < videoSize; i++) {
            if (i == curPosition) {
                continue;
            }
            tempVideo = listVideos.get(i);
            tempVideo.setChecked(false);
        }
        listViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MediaEntity mediaEntity = listVideos.get(position);
        Uri uri = Uri.parse("file:///".concat(mediaEntity.getPath()));
        intent.setDataAndType(uri, mediaEntity.getMimeType());
        startActivity(intent);
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class LoadImagesFromSDCard extends AsyncTask<Object, MediaEntity, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            Bitmap bitmap;
            MediaEntity videoEntity;
            for (int i = 0; i < videoSize; i++) {
                videoEntity = listVideos.get(i);
                bitmap = getVideoThumbnail(videoEntity.getPath(), 120, 120, MediaStore.Video.Thumbnails.MINI_KIND);
                videoEntity.setBitmap(bitmap);
                if (bitmap != null) {
                    publishProgress(videoEntity);
                }
            }
            return null;
        }

        @Override
        public void onProgressUpdate(MediaEntity... value) {
            listViewAdapter.notifyDataSetChanged();
        }
    }
}
