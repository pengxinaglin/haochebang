package com.haoche51.checker.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 15/9/29.
 */
public class ConcreteWatched implements Watched {
	// 存放观察者
	private List<Watcher> list = new ArrayList<Watcher>();

	private static ConcreteWatched concreteWatched;

	public static ConcreteWatched getInstance() {
		if (concreteWatched == null) {
			synchronized (ConcreteWatched.class) {
				if (concreteWatched == null) {
					concreteWatched = new ConcreteWatched();
				}
			}
		}
		return concreteWatched;
	}

	private ConcreteWatched() {
	}

	@Override
	public void addWatcher(Watcher watcher) {
		list.add(watcher);
	}

	@Override
	public void removeWatcher(Watcher watcher) {
		list.remove(watcher);
	}

	@Override
	public void notifyWatchers(int min, int max) {
		try {
			for (Watcher watcher : list) {
				watcher.update(min, max);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
