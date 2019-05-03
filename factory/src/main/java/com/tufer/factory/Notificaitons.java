package com.tufer.factory;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.tufer.common.R;
import com.tufer.factory.model.db.Message;
import com.tufer.utils.BitmapUtil;

/**
 * @author peter
 * @date 2018/7/4
 */
public class Notificaitons {
    public final static int NOTIFICATION_NEW_MESSAGE = 0;

    // 接收者Id，可以是群，也可以是人的Id
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    // 是否是群
    public static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    public final static String NEWMESSAGE = "NEWMESSAGE";//有声音提示，状态栏有图标显示,有弹出框
    public final static String IMPORTANCE = "IMPORTANCE";//有声音提示，状态栏有图标显示
    public final static String DEFAULT = "DEFAULT";//没有声音提示状态栏有图标显示

    private static volatile Notificaitons sInstance = null;

    private Notificaitons() {
    }

    public static Notificaitons getInstance() {
        if (sInstance == null) {
            synchronized (Notificaitons.class) {
                if (sInstance == null) {
                    sInstance = new Notificaitons();
                }
            }
        }
        return sInstance;
    }

    /**
     * 发送简单通知
     *
     * @param context
     * @param nm
     */
    public void sendNewMessageNotification(Context context, NotificationManager nm, Message message) {
        String contentTitle,receiverId,pictureUri,contentText;
        boolean isGroup;
        String messageActivity = context.getPackageName()+".activities.MessageActivity";
        if(message.getReceiver()==null){
            isGroup=true;
            contentTitle=message.getGroup().getName();
            pictureUri=message.getGroup().getPicture();
            contentText=message.getSender().getName()+":"+message.getSampleContent();
            receiverId=message.getGroup().getId();
        }else{
            isGroup=false;
            contentTitle=message.getSender().getName();
            pictureUri=message.getSender().getPortrait();
            contentText=message.getSampleContent();
            receiverId=message.getSender().getId();
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context.getPackageName(),messageActivity));
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, receiverId);
        bundle.putBoolean(KEY_RECEIVER_IS_GROUP, isGroup);
        intent = intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //创建通知
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context,NEWMESSAGE)
                    //设置通知左侧的小图标
                    .setSmallIcon(R.mipmap.ic_notification)
                    //设置通知标题
                    .setContentTitle(contentTitle)
                    //设置通知内容
                    .setContentText(contentText)
                    //设置点击通知后自动删除通知
                    .setAutoCancel(true)
                    //设置显示通知时间
                    .setShowWhen(true)
                    //设置点击通知时的响应事件
                    .setContentIntent(pi)
                    .setLargeIcon(BitmapUtil.netUrlPicToBmp(pictureUri))
                    .build();
        }else{
            notification = new NotificationCompat.Builder(context)
                    //设置通知左侧的小图标
                    .setSmallIcon(R.mipmap.ic_notification)
                    //设置通知标题
                    .setContentTitle(contentTitle)
                    //设置通知内容
                    .setContentText(contentText)
                    //设置点击通知后自动删除通知
                    .setAutoCancel(true)
                    //设置显示通知时间
                    .setShowWhen(true)
                    //设置点击通知时的响应事件
                    .setContentIntent(pi)
                    .setLargeIcon(BitmapUtil.netUrlPicToBmp(pictureUri))
                    .build();
        }
        //发送通知
        nm.notify(receiverId,NOTIFICATION_NEW_MESSAGE,notification);
    }
}