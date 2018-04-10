package com.tufer.mylove;


import com.igexin.sdk.PushManager;
import com.tufer.common.app.Application;
import com.tufer.factory.Factory;
import com.tufer.mylove.service.GetuiIntentService;
import com.tufer.mylove.service.GetuiPushService;

/**
 * @author Tufer
 * @version 1.0.0
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Factory.setup();
        PushManager.getInstance().initialize(this, GetuiPushService.class);
        PushManager.getInstance().registerPushIntentService(this, GetuiIntentService.class);
    }
}
