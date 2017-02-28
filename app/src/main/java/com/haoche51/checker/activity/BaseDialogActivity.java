package com.haoche51.checker.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.haoche51.checker.constants.TaskConstants;

/**
 * 基础对话框Activity
 * Created by wfx on 2016/9/26.
 */
public class BaseDialogActivity extends Activity{
    /**
     * 广播接收者--接收关闭页面的广播--finsh
     */
    private BroadcastReceiver finishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(TaskConstants.ACTION_FINISH_MAIN.equals(intent.getAction())){
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerReceiver(finishReceiver, new IntentFilter(TaskConstants.ACTION_FINISH_MAIN));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(finishReceiver);
        super.onDestroy();
    }
}
