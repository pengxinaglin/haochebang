package com.haoche51.checker.test;

import junit.framework.AssertionFailedError;
import junit.framework.TestListener;
import android.app.Activity;
import android.os.Bundle;
import android.test.AndroidTestRunner;
import android.util.Log;

public class TestActivity extends Activity {
	private static final String LOG_TAG = "junit";
	private int errCounter = 0;
	private int failureCounter = 0;
	private int testCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				runTest();
			}
		}).start();
	}
	
	private void runTest() {
		AndroidTestRunner runner = new AndroidTestRunner();
		runner.setContext(getApplicationContext());
		runner.setTest(new com.haoche51.checker.test.TestSuite());
		runner.addTestListener(new TestListener() {

			@Override
			public void addError(junit.framework.Test test, Throwable throwable) {
				Log.e(LOG_TAG, "error:" + test.getClass().getName());
				Log.e(LOG_TAG, throwable.getMessage(), throwable);
				++errCounter;
			}

			@Override
			public void addFailure(junit.framework.Test test, AssertionFailedError error) {
				Log.w(LOG_TAG, "failure:" + test.getClass().getName());
				Log.w(LOG_TAG, error.getMessage(), error);
				++failureCounter;
			}

			@Override
			public void endTest(junit.framework.Test test) {
				Log.i(LOG_TAG, "end:" + test.getClass().getName());
			}

			@Override
			public void startTest(junit.framework.Test test) {
				Log.i(LOG_TAG, "start:" + test.getClass().getName());
				++testCounter;
			}
		});
		runner.runTest();
		Log.w(LOG_TAG, String.format("test:%d error:%d failure:%d", testCounter, errCounter, failureCounter));
	}
	
}
