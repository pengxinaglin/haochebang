package com.haoche51.checker.activity.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.haoche51.checker.R;

import org.xutils.x;

/**
 * Created by yangming on 2015/11/30.
 */
public abstract class CommonStateActivity extends CommonBaseActivity {

  public final int RESULT_NODATA = 0;
  public final int RESULT_ERROR = 1;
  //  private RelativeLayout stateLayout;
  private FrameLayout mContentViewTitle;
  private FrameLayout mContentViewContainer;
  private View mContentView;
  private View mLoadingView;
  private View mResultView;

  protected abstract int getContentView();

  protected int getTitleView() {
    return 0;
  }

  @Override
  protected void initView() {
//    stateLayout = (RelativeLayout) findViewById(R.id.state_layout);
    setContentView(R.layout.activity_common_status);

    mContentViewTitle = (FrameLayout) findViewById(R.id.fl_common_content_title);
    mContentViewContainer = (FrameLayout) findViewById(R.id.fl_common_content_container);

    if (getTitleView() == 0) {
      mContentViewTitle.addView(View.inflate(this, R.layout.layout_common_titlebar, null));
    } else {
      mContentViewTitle.addView(View.inflate(this, getTitleView(), null));
    }

    mContentView = View.inflate(this, getContentView(), null);
    mContentViewContainer.addView(mContentView);
    x.view().inject(this);
    registerTitleBack();

    super.initView();
  }

  /**
   * 页面中显示加载进度圈圈
   *
   * @param contentVisible 页面原内容是否显示 true 显示原页面内容； false 隐藏原页面内容
   */
  public void showLoadingView(boolean contentVisible) {
    showLoadingView(contentVisible, getString(R.string.loading));
  }

  /**
   * 页面中显示加载进度圈圈
   *
   * @param contentVisible 页面原内容是否显示 true 显示原页面内容； false 隐藏原页面内容
   * @param text
   */
  public void showLoadingView(boolean contentVisible, String text) {
    if (mLoadingView == null) {
      mLoadingView = LayoutInflater.from(this).inflate(
        R.layout.layout_common_loading_view, null);
    }
    final TextView textView = (TextView) mLoadingView.findViewById(R.id.loading_txt);
    textView.setText(text);
    mContentViewContainer.removeView(mLoadingView);
    mContentViewContainer.addView(mLoadingView, new LayoutParams(
      LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    if (!contentVisible) {
      mContentView.setVisibility(View.INVISIBLE);
    }
  }

  /**
   * 显示页面结果视图
   *
   * @param contentVisible 页面原内容是否显示 true 显示原页面内容； false 隐藏原页面内容
   * @param result         结果类型
   * @param text           显示文案
   * @param l              点击监听
   */
  public void showResultView(boolean contentVisible, int result, String text, View.OnClickListener l) {
    if (mResultView == null) {

      mResultView = LayoutInflater.from(this).inflate(
        R.layout.layout_common_result_view, null);
    }
    final TextView textView = (TextView) mResultView
      .findViewById(R.id.result_txt);
    textView.setText(text);

    if (result == RESULT_NODATA) {
      textView.setCompoundDrawablesWithIntrinsicBounds(0,
        R.drawable.common_button_nodata, 0, 0);

      TextView textViewNoData = (TextView) mResultView
        .findViewById(R.id.click_txt);
      textViewNoData.setVisibility(View.GONE);
    } else if (result == RESULT_ERROR) {
      textView.setCompoundDrawablesWithIntrinsicBounds(0,
        R.drawable.common_button_offline, 0, 0);
    }
    mContentViewContainer.removeView(mResultView);
    mContentViewContainer.addView(mResultView, new LayoutParams(
      LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    if (!contentVisible) {
      mContentView.setVisibility(View.INVISIBLE);
    }

    textView.setOnClickListener(l);
  }

  /**
   * 显示无数据视图
   *
   * @param contentVisible 页面原内容是否显示 true 显示原页面内容； false 隐藏原页面内容
   * @param l              点击监听
   */
  public void showNoDataView(boolean contentVisible, View.OnClickListener l) {
    showResultView(contentVisible, RESULT_NODATA, getString(R.string.hc_common_result_nodata), l);
  }

  /**
   * 显示无数据视图
   *
   * @param contentVisible 页面原内容是否显示 true 显示原页面内容； false 隐藏原页面内容
   * @param text           显示文案
   * @param l              点击事件
   */
  public void showNoDataView(boolean contentVisible, String text, View.OnClickListener l) {
    showResultView(contentVisible, RESULT_NODATA, text, l);
  }

  /**
   * 显示页面错误视图
   *
   * @param contentVisible 页面原内容是否显示 true 显示原页面内容； false 隐藏原页面内容
   * @param text           显示文案
   * @param l              点击事件
   */
  public void showErrorView(boolean contentVisible, String text, View.OnClickListener l) {
    showResultView(contentVisible, RESULT_ERROR, text, l);
  }

  /**
   * 显示页面错误视图
   *
   * @param contentVisible 页面原内容是否显示 true 显示原页面内容； false 隐藏原页面内容
   * @param l              点击事件
   */
  public void showErrorView(boolean contentVisible, View.OnClickListener l) {
    showResultView(contentVisible, RESULT_ERROR, getString(R.string.hc_common_result_offline), l);
  }

  /**
   * 取消页面加载视图
   */
  public void dismissLoadingView() {
    if (mLoadingView != null) {
      mContentViewContainer.removeView(mLoadingView);
    }
    mContentView.setVisibility(View.VISIBLE);
  }

  /**
   * 取消页面结果视图
   *
   * @param contentVisible 页面原内容是否显示 true 显示原页面内容； false 隐藏原页面内容
   */
  public void dismissResultView(boolean contentVisible) {
    if (mResultView != null) {
      mContentViewContainer.removeView(mResultView);
    }
    if (!contentVisible) {
      mContentView.setVisibility(View.INVISIBLE);
    } else {
      mContentView.setVisibility(View.VISIBLE);
    }
  }

}
