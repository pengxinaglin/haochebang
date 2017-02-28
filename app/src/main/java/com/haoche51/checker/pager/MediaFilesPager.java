package com.haoche51.checker.pager;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.media.AudioPickerActivity;
import com.haoche51.checker.activity.media.VideoPickerActivity;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.entity.MediaEntity;

import java.io.File;


/**
 * 选择视频音频
 * Created by PengXianglin on 16/3/10.
 */
public class MediaFilesPager extends TabBasePhotoPager {

	private TextView tv_video;
	private Button bt_video, bt_video_del;
	private TextView tv_audio;
	private Button bt_audio, bt_audio_del;

	public MediaFilesPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public View initView() {
		View view = View.inflate(this.mActivity, R.layout.tab_media_files_pager, null);
		tv_video = (TextView) view.findViewById(R.id.tv_video);
		bt_video = (Button) view.findViewById(R.id.bt_video);
		bt_video_del = (Button) view.findViewById(R.id.bt_video_del);
		tv_audio = (TextView) view.findViewById(R.id.tv_audio);
		bt_audio = (Button) view.findViewById(R.id.bt_audio);
		bt_audio_del = (Button) view.findViewById(R.id.bt_audio_del);

		bt_video.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAlbum(view);
			}
		});
		bt_audio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAlbum(view);
			}
		});
		bt_video_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAlbum(view);
			}
		});
		bt_audio_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openAlbum(view);
			}
		});
		return view;
	}

	public void initData() {
		try {
			if (videoEntity != null) {
				tv_video.setText(videoEntity.getDisplayName());
				bt_video.setText("重新选择");
			}
			if (audioEntity != null) {
				tv_audio.setText(audioEntity.getDisplayName());
				bt_audio.setText("重新选择");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void openAlbum(View v) {
		//打开文件管理器
		switch (v.getId()) {
			case R.id.bt_video://选视频
				Intent video = new Intent(mActivity, VideoPickerActivity.class);
				mActivity.startActivityForResult(video, PictureConstants.SELECT_VIDEO);
				break;
			case R.id.bt_audio://选音频
				Intent audio = new Intent(mActivity, AudioPickerActivity.class);
				mActivity.startActivityForResult(audio, PictureConstants.SELECT_AUDIO);
				break;
			case R.id.bt_video_del://删除视频
				removeCopyedVideo(videoEntity);
				videoEntity = null;
				tv_video.setText("请选择");
				bt_video.setText("选择视频");
				break;
			case R.id.bt_audio_del://删除音频
				audioEntity = null;
				tv_audio.setText("请选择");
				bt_audio.setText("选择音频");
				break;
		}
	}

	/**
	 * 删除已复制的视频
	 * @param mediaEntity
   */
	private void removeCopyedVideo(MediaEntity mediaEntity){
		if(mediaEntity!=null && !TextUtils.isEmpty(mediaEntity.getCopyedPath()) && new File(mediaEntity.getCopyedPath()).exists()){
			new File(mediaEntity.getCopyedPath()).delete();
		}
	}
	/**
	 * 选择音频
	 *
	 * @param audioInfo
	 */
	@Override
	public void chooseAudio(MediaEntity audioInfo) {
		//音频
		if (audioInfo != null) {
			audioEntity = audioInfo;
			tv_audio.setText(audioInfo.getDisplayName());
			bt_audio.setText("重新选择");
		}
	}

	/**
	 * 选择视频
	 *
	 * @param videoEntity
	 */
	@Override
	public void chooseVideo(MediaEntity videoEntity) {
		if (videoEntity != null) {
			//删除之前复制好的视频
			removeCopyedVideo(this.videoEntity);
			this.videoEntity = videoEntity;
			tv_video.setText(videoEntity.getDisplayName());
			bt_video.setText("重新选择");
		}
	}
}
