package com.haoche51.checker.listener;

/**
 * Created by mac on 15/9/29.
 */
//watched：被观察
public interface Watched {
	void addWatcher(Watcher watcher);

	void removeWatcher(Watcher watcher);

	void notifyWatchers(int min, int max);

}
