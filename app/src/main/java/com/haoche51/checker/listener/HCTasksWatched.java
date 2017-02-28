package com.haoche51.checker.listener;

import android.os.Bundle;

import com.haoche51.checker.DAO.DataObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 15/9/29.
 */
public class HCTasksWatched {

	private static HCTasksWatched taskWatched;

	public static HCTasksWatched getInstance() {
		if (taskWatched == null) {
			synchronized (HCTasksWatched.class) {
				if (taskWatched == null) {
					taskWatched = new HCTasksWatched();
				}
			}
		}
		return taskWatched;
	}

	private HCTasksWatched() {
	}

	private List<DataObserver> mObList = null;

	public void registerDataObserver(DataObserver mDataObserver) {
		if (mObList == null) mObList = new ArrayList<DataObserver>();
		if (!mObList.contains(mDataObserver))
			mObList.add(mDataObserver);
	}

	public void UnRegisterDataObserver(DataObserver mDataObserver) {
		if (mObList != null && mDataObserver != null) {
			mObList.remove(mDataObserver);
		}
	}

	public void notifyWatchers() {
		if (mObList == null || mObList.size() == 0) return;
		for (DataObserver observer : mObList) {
			observer.onChanged();
		}
	}

	public void notifyWatchers(Bundle data) {
		if (mObList == null || mObList.size() == 0) return;
		for (DataObserver observer : mObList) {
			observer.onChanged(data);
		}
	}
}
