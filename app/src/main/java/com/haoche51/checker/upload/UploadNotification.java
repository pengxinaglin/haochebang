package com.haoche51.checker.upload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;

/**
 * 上传通知
 */
public class UploadNotification {

    /**
     * 向通知栏发送通知
     *
     * @param title     标题
     * @param content   内容
     * @param isNetStop 网络是否断开
     * @param isUploadFinish 报告是否上传完成
     */
    public static void sendNotice(Context context, String title, String content, boolean isNetStop, boolean isUploadFinish) {
        int notifyId = TaskConstants.NOTICE_NOTIY_ID;
        PendingIntent pendingIntent;
        if (isNetStop) {
            // 跳转到网络设置界面
            Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notifyId = TaskConstants.NOTICE_NOTIY_ID_NET;
        } else {
            // 跳转到验车界面
            Intent intent = new Intent("com.haoche51.checker.action.CHECK_MAIN");
            intent.addFlags(Intent.FILL_IN_DATA);
            if (!isUploadFinish) {
                // 上传中
                intent.putExtra(TaskConstants.BINDLE_FRAGMENT_INDEX, TaskConstants.FRAGMENT_UPLOAD_TASK);
            } else {
                // 已完成
                intent.putExtra(TaskConstants.BINDLE_FRAGMENT_INDEX, TaskConstants.FRAGMENT_FINISH_TASK);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder buidler = new NotificationCompat.Builder(context);
        buidler.setTicker(TaskConstants.NOTICE_UPLOAD_TICKER)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher);
        Notification notification = buidler.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notifyId, notification);
    }
}
