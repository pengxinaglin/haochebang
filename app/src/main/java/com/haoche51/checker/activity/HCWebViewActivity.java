package com.haoche51.checker.activity;


import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.util.DownloadUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class HCWebViewActivity extends CommonTitleBaseActivity {
    public static String KEY_INTENT_EXTRA_DOWNLOAD_ENABLE = "key_intent_extra_download_enable";
    public static String KEY_INTENT_EXTRA_DOWNLOAD_URL = "key_intent_extra_download_url";
    public static String KEY_INTENT_EXTRA_DOWNLOAD_CHECKER_REPORT_OR_CJD = "key_intent_extra_download_checker_report_or_cjd";
    public static String KEY_INTENT_EXTRA_DOWNLOAD_CHECKER_REPORT = "key_intent_extra_download_checker_report";
    public static String KEY_INTENT_EXTRA_DOWNLOAD_CJD = "key_intent_extra_download_cjd";
    public static String KEY_INTENT_EXTRA_VEHICLE_SOURCE_ESTIMATE = "key_intent_extra_vehicle_source_estimate";
    public static String KEY_INTENT_EXTRA_PURCHASE = "key_intent_extra_purchase";
    public static String KEY_INTENT_EXTRA_APPROVE = "key_intent_extra_approve";
    public static String KEY_INTENT_EXTRA_DANGER = "key_intent_extra_danger";
    public static String KEY_INTENT_EXTRA_URL = "url";
    public static String KEY_INTENT_EXTRA_ID = "id";
    private String EVALUATION_URL = "";//报告url
    private String DOWNLOAD_URL = "";//下载url
    private String filename = "";//报告文件名
    private boolean downloadEnable = false;//是否下载页面的标识
    private String title = "";

    @ViewInject(R.id.view_for_progress)
    private View mProgressView;
    private LinearLayout.LayoutParams mProgParams;

    @ViewInject(R.id.web_content)
    private WebView webView;

    @ViewInject(R.id.ll_activity_evaluation_download_path)
    private LinearLayout ll_activity_evaluation_download_path;
    @ViewInject(R.id.tv_activity_evaluation_download_path)
    private TextView tv_activity_evaluation_download_path;
    private int mWidth;
    private String str;
    private String taskID;

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_evaluation, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this); // 注入view和事件
        EVALUATION_URL = getIntent().getStringExtra(KEY_INTENT_EXTRA_URL);
        if (getIntent().hasExtra(KEY_INTENT_EXTRA_ID)) {
            taskID = getIntent().getStringExtra(KEY_INTENT_EXTRA_ID);
        }
        if (getIntent().hasExtra(KEY_INTENT_EXTRA_DOWNLOAD_URL)) {
            DOWNLOAD_URL = getIntent().getStringExtra(KEY_INTENT_EXTRA_DOWNLOAD_URL);
        }
        if (getIntent().hasExtra(KEY_INTENT_EXTRA_DOWNLOAD_ENABLE)) {
            downloadEnable = getIntent().getBooleanExtra(KEY_INTENT_EXTRA_DOWNLOAD_ENABLE, false);
        }
        if (getIntent().hasExtra(KEY_INTENT_EXTRA_DOWNLOAD_CHECKER_REPORT_OR_CJD)) {
            str = getIntent().getStringExtra(KEY_INTENT_EXTRA_DOWNLOAD_CHECKER_REPORT_OR_CJD);
            if (str.equals(KEY_INTENT_EXTRA_DOWNLOAD_CHECKER_REPORT)) {
                title = "车检报告";
                showDownloadTip(DOWNLOAD_URL, 0);
            } else if (str.equals(KEY_INTENT_EXTRA_DOWNLOAD_CJD)) {
                title = "车鉴定报告";
                showDownloadTip(DOWNLOAD_URL, 1);
            }
        } else if (getIntent().hasExtra(KEY_INTENT_EXTRA_VEHICLE_SOURCE_ESTIMATE)) {
            title = "车源估价";
        } else if (getIntent().hasExtra(KEY_INTENT_EXTRA_APPROVE)) {
            title = "审批详情";
        } else if (getIntent().hasExtra(KEY_INTENT_EXTRA_DANGER)) {
            title = "出险记录";
        } else if (getIntent().hasExtra(KEY_INTENT_EXTRA_PURCHASE)) {
            title = "任务详情";
        } else {
            title = "车源详情";
        }

        initView();

    }

    /**
     * @param downloadUrl
     * @param flag        0:车检报告  1：车鉴定报告
     */
    private void showDownloadTip(String downloadUrl, Integer flag) {
        if (downloadEnable) {
            filename = DownloadUtil.getFileName(downloadUrl, flag);
            tv_activity_evaluation_download_path.setText("保存路径:" + DownloadUtil.MRECORDDIR_NAME
                    + "/" + DownloadUtil.getFileName(downloadUrl, flag));//+ filename
            ll_activity_evaluation_download_path.setVisibility(View.VISIBLE);
        } else {
            ll_activity_evaluation_download_path.setVisibility(View.GONE);
        }
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(title);
        mRightFaction.setText("刷新");
        mRightFaction.setVisibility(View.VISIBLE);
        mRightFaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    webView.reload();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void initView() {
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient() {
            // 页面家在完成后
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            // 页面加载前
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            // 这里可以设置使用哪种类型打开连接，当前内置or外部浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //调用拨号程序
                if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            // for support https web
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        mWidth = GlobalData.mContext.getResources().getDisplayMetrics().widthPixels;

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                final int width = mWidth * newProgress / 100;
                if (mProgParams == null) {
                    mProgParams = (LinearLayout.LayoutParams) mProgressView.getLayoutParams();
                    mProgParams.height = 10;
                    mProgressView.setVisibility(View.VISIBLE);
                }
                mProgParams.width = width;
                mProgressView.setLayoutParams(mProgParams);
                if (newProgress == 100) {
                    mProgressView.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        webView.loadUrl(EVALUATION_URL);

    }

    @Event(R.id.btn_report_download)
    private void downloadReport(View v) {
        if (TextUtils.isEmpty(DOWNLOAD_URL)) {
            ToastUtil.showText("未生成可下载的报告，无法下载");
            return;
        }

        int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            //无法调用download manager ,需要启用它
            ToastUtil.showText("下载管理器已停用，请启用");
            String packageName = "com.android.providers.downloads";
            try {
                //Open the specific App Info page:
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);

            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                startActivity(intent);
            }
        } else {
            ToastUtil.showText("开始下载");
            DownloadUtil.downloadUseDownloadManager(HCWebViewActivity.this, DOWNLOAD_URL, title);
        }
    }

    /**
     * 返回按钮
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(DownloadUtil.mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(DownloadUtil.mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return false;
    }

    @Event(R.id.tv_common_back)
    private void back(View view) {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(TaskConstants.BINDLE_IS_ASSIGN, true);
        HCTasksWatched.getInstance().notifyWatchers(bundle);
        super.finish();
    }
}
