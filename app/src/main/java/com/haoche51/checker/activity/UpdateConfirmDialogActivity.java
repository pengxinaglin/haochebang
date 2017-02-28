package com.haoche51.checker.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.UpdateServiceEntity;
import com.haoche51.checker.util.HCLogUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 提示更新
 * Created by wfx on 2016/9/7.
 */
public class UpdateConfirmDialogActivity extends BaseDialogActivity {
    private int updateType;
    private String tag = "UpdateConfirmDialogActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_confirm_dialog);
        initData();
    }

    private void initData(){

        String versionName = getIntent().getStringExtra(TaskConstants.BINDLE_VERSION_NAME);
        String updateContent = getIntent().getStringExtra(TaskConstants.BINDLE_UPDATE_CONTENT);
        updateType = getIntent().getIntExtra(TaskConstants.SP_UPDATE_TYPE, TaskConstants.UPDATE_TYPE_NORMAL);
        TextView tv_latest_version = (TextView) findViewById(R.id.tv_latest_version);
        if (!TextUtils.isEmpty(versionName)) {
            tv_latest_version.setText("最新版本：V" + versionName);
        }
        TextView tv_update_content = (TextView) findViewById(R.id.tv_update_content);
        if (!TextUtils.isEmpty(updateContent)) {
            tv_update_content.setText(updateContent);
        }
        Button btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EventBus.getDefault().post(new UpdateServiceEntity());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (updateType == TaskConstants.UPDATE_TYPE_NORMAL) {
            Button btnCancel = (Button) findViewById(R.id.btn_cancel);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        HCLogUtil.e(tag,"onCreate  initData==========================");
    }

    /**
     * 点击返回键不关闭activity
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (updateType != TaskConstants.UPDATE_TYPE_NORMAL) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                return false;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void finish() {
        super.finish();
        HCLogUtil.e(tag,"finish==========================");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HCLogUtil.e(tag,"onDestroy==========================");
    }
}
