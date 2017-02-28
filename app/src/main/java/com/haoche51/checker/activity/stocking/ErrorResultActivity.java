package com.haoche51.checker.activity.stocking;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by xuhaibo on 16/7/6.
 */
public class ErrorResultActivity extends CommonStateActivity {

    @ViewInject(R.id.tv_qrcode_content)
    private TextView tv_qrcode_content;
    @Override
    protected int getContentView() {
        return R.layout.activity_error_result;
    }

    @Override
    protected void initView() {
        super.initView();
        setScreenTitle(getString(R.string.hc_scan_result));
    }

    @Event(R.id.btn_repeat)
    private void repeatClick(View view){
        finish();
    }

    @Override
    protected void initData() {
        Bundle extras = getIntent().getExtras();
        String content = extras.getString("result");
        tv_qrcode_content.setText(content);
    }
}
