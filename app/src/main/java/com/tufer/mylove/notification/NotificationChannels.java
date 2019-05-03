package com.tufer.mylove.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.tufer.common.R;

import java.util.Arrays;
import java.util.List;

public class NotificationChannels {
    public final static String NEWMESSAGE = "NEWMESSAGE";//有声音提示，状态栏有图标显示,有弹出框
    public final static String IMPORTANCE = "IMPORTANCE";//有声音提示，状态栏有图标显示
    public final static String DEFAULT = "DEFAULT";//没有声音提示状态栏有图标显示

    public static void createAllNotificationChannels(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (nm == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            List<NotificationChannel> notificationChannels = nm.getNotificationChannels();
            if (notificationChannels==null||notificationChannels.size()==0){
                NotificationChannel newMessage = new NotificationChannel(NEWMESSAGE, context.getString(R.string.label_channel_new_message),NotificationManager.IMPORTANCE_HIGH);
                newMessage.enableLights(true);//是否在桌面icon右上角展示小红点
                newMessage.setLightColor(Color.RED);//小红点颜色
                newMessage.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                nm.createNotificationChannels(Arrays.asList(
                        newMessage,
                        new NotificationChannel(
                                IMPORTANCE,
                                context.getString(R.string.label_channel_importance),
                                NotificationManager.IMPORTANCE_DEFAULT),
                        new NotificationChannel(
                                DEFAULT,
                                context.getString(R.string.label_channel_default),
                                NotificationManager.IMPORTANCE_LOW)
                ));
            }
        }
    }
}