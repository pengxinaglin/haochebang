package com.haoche51.checker.activity.notice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.NetInfoUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class QujiVehicleActivity extends CommonStateActivity {

  private int mWidth;

  @ViewInject(R.id.view_for_progress)
  private View mProgressView;
  private LinearLayout.LayoutParams mProgParams;
  private View contentView;
  @ViewInject(R.id.web_content)
  private WebView webView;

  @Override
  protected int getContentView() {
    return R.layout.layout_quji_vehicles;
  }

  @Override
  protected int getTitleView() {
    return R.layout.layout_quji_vehicle_title;
  }

  @Override
  protected void initView() {
    super.initView();
    x.view().inject(this);
    showLoadingView(true);
    OKHttpManager.getInstance().post(HCHttpRequestParam.getQujiVehicleSource(), this, 0);
  }

  /**
   * 处理web 页返回
   */
  @Override
  protected void registerTitleBack() {
    View backBtn = findViewById(R.id.tv_common_back);
    if (backBtn != null) {
      backBtn.setVisibility(View.VISIBLE);
      backBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
          if (webView != null && webView.canGoBack()) {
            webView.goBack();
          } else {
            finish();
          }
        }
      });
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (webView != null && webView.canGoBack()) {
        webView.goBack();
        return true;
      } else {
        finish();
      }
    }
    return super.onKeyDown(keyCode, event);
  }

  @Event(R.id.tv_right_fuction)
  private void onCloseClick(View view) {
    finish();
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    super.onHttpComplete(action, requestId, response, error);
    dismissLoadingView();
    switch (response.getErrno()) {
      case 0:// 正常返回
        initWebView(response.getData());
        break;
      case -100://网络不给力
        showErrorView(true, "网络不给力", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            OKHttpManager.getInstance().post(HCHttpRequestParam.getQujiVehicleSource(), QujiVehicleActivity.this, 0);
            showLoadingView(true);
          }
        });
        break;
      default:
        showErrorView(true, response.getErrmsg(), new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            OKHttpManager.getInstance().post(HCHttpRequestParam.getQujiVehicleSource(), QujiVehicleActivity.this, 0);
            showLoadingView(true);
          }
        });
        break;
    }

  }

  /**
   * 初始化 webView
   */
  private void initWebView(String url) {
    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
    webView.getSettings().setAllowFileAccess(true);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setSupportZoom(true);
    webView.getSettings().setUseWideViewPort(true);
    webView.getSettings().setLoadWithOverviewMode(true);
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
        if (url.startsWith("tel:")) {
          Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
          startActivity(intent);
        } else {
          if (!NetInfoUtil.isNetAvaliable()) {
            showErrorView(true, "网络不给力", new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                OKHttpManager.getInstance().post(HCHttpRequestParam.getQujiVehicleSource(), QujiVehicleActivity.this, 0);
                showLoadingView(true);
              }
            });
          } else {
            webView.loadUrl(url);
          }

        }
        return true;
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
        }
        mProgParams.width = width;
        mProgressView.setVisibility(View.VISIBLE);
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
    webView.loadUrl(url);
  }

}
