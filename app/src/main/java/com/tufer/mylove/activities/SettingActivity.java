package com.tufer.mylove.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.LinearLayout;

import com.tufer.common.app.ToolbarActivity;
import com.tufer.common.tools.SingleClickHelper;
import com.tufer.factory.Factory;
import com.tufer.factory.persistence.Account;
import com.tufer.mylove.R;
import com.tufer.mylove.notification.NotificationChannels;
import com.tufer.mylove.notification.NotificationUtil;

import butterknife.BindView;

public class SettingActivity extends ToolbarActivity {

    private static final int NOTIFICATION_REQUESTCODE =1;

    @BindView(R.id.lay_account)
    LinearLayout account;

    @BindView(R.id.lay_new_message_notification)
    LinearLayout newMessageNotification;

    @BindView(R.id.lay_wurao_pattern)
    LinearLayout wuraoPattern;

    @BindView(R.id.lay_chat)
    LinearLayout chat;

    @BindView(R.id.lay_privacy)
    LinearLayout privacy;

    @BindView(R.id.lay_currency)
    LinearLayout currency;

    @BindView(R.id.lay_about)
    LinearLayout about;

    @BindView(R.id.lay_help)
    LinearLayout help;

    @BindView(R.id.lay_switch_account)
    LinearLayout switchAccount;

    @BindView(R.id.lay_exit)
    LinearLayout exit;

    public static void show(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initTitleNeedBack() {
        super.initTitleNeedBack();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.nav_menu_setting);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        SingleClickHelper.click(newMessageNotification,v -> NotificationUtil.gotoNotificationChannelSetting(this, NotificationChannels.NEWMESSAGE,NOTIFICATION_REQUESTCODE));
        SingleClickHelper.click(switchAccount,v->{
            if(Account.isLogin()){
                Account.clearAccount();
                AccountActivity.show(this);
                Factory.app().finishAll();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==NOTIFICATION_REQUESTCODE){
            Account.setNotification(Account.getNotificationUri(this));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
