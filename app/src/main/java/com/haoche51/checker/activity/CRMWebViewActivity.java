package com.haoche51.checker.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
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
import com.haoche51.checker.entity.UserRightEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class CRMWebViewActivity extends CommonTitleBaseActivity {
    public static String KEY_INTENT_EXTRA_CAR_REVISIT = "key_intent_extra_car_revisit";
    public static String KEY_INTENT_EXTRA_SALARY = "key_intent_extra_salary";
    public static String KEY_INTENT_EXTRA_REPORT = "key_intent_extra_report";
    private String url = "";//报告url
    private String title = "";

    @ViewInject(R.id.view_for_progress)
    private View mProgressView;
    private LinearLayout.LayoutParams mProgParams;

    @ViewInject(R.id.web_content)
    private WebView webView;

    private int mWidth;

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_crm_web_view, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this); // 注入view和事件
        if (getIntent().hasExtra(KEY_INTENT_EXTRA_CAR_REVISIT)) {
            title = "车源回访";
            requestCRMURL(TaskConstants.CRM_URL_CAR_REVISIT);
        } else if (getIntent().hasExtra(KEY_INTENT_EXTRA_SALARY)) {
            title = "工资明细";
            requestCRMURL(TaskConstants.CRM_URL_CHECKER_SALARY);
        }else if (getIntent().hasExtra(KEY_INTENT_EXTRA_REPORT)) {
            title = "工作简报";
            requestCRMURL(TaskConstants.CRM_URL_PURCHASE_REPORT);
        }
        initView();

    }

    /**
     * 请求CRM的URL
     *
     * @param type
     */
    private void requestCRMURL(int type) {
        OKHttpManager.getInstance().post(HCHttpRequestParam.getCRMUrl(type), this, 0);
    }


    /**
     * HTTP 请求结果
     *
     * @param action    当前请求action
     * @param requestId
     * @param response  hc 请求结果
     * @param error     网络问题造成failed 的error
     */
    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        try {
            if (HttpConstants.ACTION_GET_CRM_URL.equals(action)) {
                switch (response.getErrno()) {
                    case 0:
                        UserRightEntity userRight = JsonParseUtil.fromJsonObject(response.getData(), UserRightEntity.class);
                        if (userRight != null) {
                            url = userRight.getUrl();
                            webView.loadUrl(url);
                            HCLogUtil.e("CRM URL 1 ========================" + userRight.getUrl());
                        }
                        break;
                    default:
                        ToastUtil.showInfo(response.getErrmsg());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                HCLogUtil.e("CRM onPageFinished url ========================" + url);
                super.onPageFinished(view, url);
            }

            // 页面加载前
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                HCLogUtil.e("CRM onPageStarted url ========================" + url);
                super.onPageStarted(view, url, favicon);
            }

            // 这里可以设置使用哪种类型打开连接，当前内置or外部浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                HCLogUtil.e("CRM shouldOverrideUrlLoading url ========================" + url);
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
//        HCLogUtil.e("CRM URL 2 ========================" + url);
//        webView.loadUrl(url);

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
}
