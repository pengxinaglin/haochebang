package com.haoche51.checker.activity.media;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.List;

/**
 * 选择音频文件
 * Created by PengXianglin on 16/3/10.
 */
public class AudioPickerActivity extends CommonTitleBaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public final static String KEY_SELECTED_AUDIO = "SELECTED_AUDIO";
    @ViewInject(R.id.tv_no_data)
    private TextView tv_no_data;

    @ViewInject(R.id.lv_video)
    private ListView videoListView;

    private MediaPickerAdapter listViewAdapter;
    private List<MediaEntity> listAudios;
    private int audioSize;
    private MediaEntity checkedAudio;

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
        mTitle.setText(getString(R.string.hc_audio_list));
        mRightFaction.setVisibility(View.VISIBLE);
        mRightFaction.setText(getResources().getString(R.string.button_ok));
        mRightFaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(KEY_SELECTED_AUDIO, checkedAudio);
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
        listAudios = provider.getLocalAudios();
        audioSize = listAudios.size();
        if (audioSize == 0) {
            tv_no_data.setVisibility(View.VISIBLE);
            videoListView.setVisibility(View.GONE);
        }
        listViewAdapter = new MediaPickerAdapter(this, listAudios, R.layout.item_audio_list);
        videoListView.setAdapter(listViewAdapter);
        videoListView.setOnItemClickListener(this);
        videoListView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MediaEntity videoEntity = listAudios.get(position);
        boolean isChecked = !videoEntity.isChecked();
        //限制其只能选中一项
        if (isChecked) {
            checkedAudio = videoEntity;
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
        MediaEntity tempAudio;
        for (int i = 0; i < audioSize; i++) {
            if (i == curPosition) {
                continue;
            }
            tempAudio = listAudios.get(i);
            tempAudio.setChecked(false);
        }
        listViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MediaEntity mediaEntity = listAudios.get(position);
        Uri uri = Uri.parse("file:///".concat(mediaEntity.getPath()));
        intent.setDataAndType(uri, mediaEntity.getMimeType());
        startActivity(intent);
        return false;
    }

}
