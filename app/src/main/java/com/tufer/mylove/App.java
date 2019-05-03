package com.tufer.mylove;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.igexin.sdk.PushManager;
import com.mob.MobSDK;
import com.tufer.common.app.Application;
import com.tufer.factory.Factory;
import com.tufer.mylove.notification.NotificationChannels;

/**
 * Created by Tufer on 2018/4/10 0010.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化通知通道
        NotificationChannels.createAllNotificationChannels(this);

        Factory.setup();

        // 注册生命周期
        registerActivityLifecycleCallbacks(new PushInitializeLifecycle());

        MobSDK.init(this);
    }

    @Override
    protected void showAccountView(Context context) {

        // 登录界面的显示


    }

    /**
     * 个推服务在部分手机上极易容易回收，可放Resumed中唤起
     */
    private class PushInitializeLifecycle implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            // 推送进行初始化
            PushManager.getInstance().initialize(App.this, AppPushService.class);
            // 推送注册消息接收服务
            PushManager.getInstance().registerPushIntentService(App.this, AppMessageReceiverService.class);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
