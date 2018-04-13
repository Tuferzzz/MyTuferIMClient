package com.tufer.mylove;

import com.igexin.sdk.PushManager;
import com.mob.MobSDK;
import com.tufer.common.app.Application;
import com.tufer.factory.Factory;
import com.tufer.mylove.service.GetuiIntentService;
import com.tufer.mylove.service.GetuiPushService;

/**
 * Created by Tufer on 2018/4/10 0010.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Factory.setup();
        PushManager.getInstance().initialize(this, GetuiPushService.class);
        PushManager.getInstance().registerPushIntentService(this, GetuiIntentService.class);//MobSDK初始化
        MobSDK.init(this);
    }
}
