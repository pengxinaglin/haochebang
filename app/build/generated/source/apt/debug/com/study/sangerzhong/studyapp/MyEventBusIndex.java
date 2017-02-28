package com.study.sangerzhong.studyapp;

import org.greenrobot.eventbus.meta.SimpleSubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberMethodInfo;
import org.greenrobot.eventbus.meta.SubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/** This class is generated by EventBus, do not edit. */
public class MyEventBusIndex implements SubscriberInfoIndex {
    private static final Map<Class<?>, SubscriberInfo> SUBSCRIBER_INDEX;

    static {
        SUBSCRIBER_INDEX = new HashMap<Class<?>, SubscriberInfo>();

        putIndex(new SimpleSubscriberInfo(com.haoche51.checker.fragment.channel.FindCarFragment.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onMainEventBus", com.haoche51.checker.entity.FindCarEntity.class,
                    ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.haoche51.checker.activity.DownloadDialogActivity.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onMainEventBus", com.haoche51.checker.entity.UpdateVersionEntity.class,
                    ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.haoche51.checker.service.AutoUpdateVersionService.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onEventMainThread", com.haoche51.checker.entity.UpdateServiceEntity.class,
                    ThreadMode.MAIN),
        }));

    }

    private static void putIndex(SubscriberInfo info) {
        SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info);
    }

    @Override
    public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
        SubscriberInfo info = SUBSCRIBER_INDEX.get(subscriberClass);
        if (info != null) {
            return info;
        } else {
            return null;
        }
    }
}