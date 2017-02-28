package com.haoche51.checker.activity.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;

/**
 * Created by xuhaibo on 15/9/11.
 */
public abstract class CommonTitleBaseFragmentActivity extends FragmentActivity {
	/* titlebar return*/
	private TextView mReturn;
	/* title content */
	public TextView mTitle;
	/* haoche51 content view*/
	/* titleBar rightFaction */
	private TextView mRightFaction;
	private FrameLayout mContentViewContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置禁止横屏
		setContentView(R.layout.activity_common);
		mReturn = (TextView) findViewById(R.id.tv_common_back);
		mTitle = (TextView) findViewById(R.id.tv_common_title);
		mRightFaction = (TextView) findViewById(R.id.tv_right_fuction);
		initTitleBar(mReturn, mTitle, mRightFaction);
		onHCReturn(mReturn);
		mContentViewContainer = (FrameLayout) findViewById(R.id.content_container);
		mContentViewContainer.addView(getHCContentView());
		initContentView(savedInstanceState);

		//注册广播，用于退出时关闭页面
		registerReceiver(finishReceiver, new IntentFilter(TaskConstants.ACTION_FINISH_MAIN));

	}


	/**
	 * 广播--接收关闭页面的广播--finsh
	 */
	private BroadcastReceiver finishReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (TaskConstants.ACTION_FINISH_MAIN.equals(intent.getAction())) {
				try {
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};


	@Override
	protected void onDestroy() {
		unregisterReceiver(finishReceiver);
		super.onDestroy();
	}


	/**
	 * @param saveInstanceState activity onCreate saveInstanceState
	 */
	public abstract void initContentView(Bundle saveInstanceState);

	/**
	 * @param back  return btn
	 * @param title title content
	 */
	public abstract void initTitleBar(TextView back, TextView title, TextView mRightFaction);

	/**
	 * @return view haoche51 content view
	 */
	public abstract View getHCContentView();

	void onHCReturn(TextView mReturn) {
		mReturn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public FrameLayout getRootView() {
		return mContentViewContainer;
	}
}
