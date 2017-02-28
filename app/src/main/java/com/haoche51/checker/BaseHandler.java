package com.haoche51.checker;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public abstract class BaseHandler<T extends Activity> extends Handler {
	public static final int MESSAGE_TOAST = 10000;
	
	private WeakReference<T> reference = null;
	
	public BaseHandler(T activity) {
		this.reference = new WeakReference<T>(activity);
	}

	@Override
	public void handleMessage(Message msg) {
		T activity = reference.get();
		if (activity == null) return;
		handleMessage(msg, activity);
	}
	
	protected void handleMessage(Message msg, T activity) {
		switch (msg.what) {
		case MESSAGE_TOAST:
			Toast.makeText(activity, msg.obj.toString(), msg.arg1).show();
			break;
		}
	}

}
