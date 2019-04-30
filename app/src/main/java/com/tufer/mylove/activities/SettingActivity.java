package com.tufer.mylove.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.widget.LinearLayout;

import com.tufer.common.app.Application;
import com.tufer.common.app.ToolbarActivity;
import com.tufer.mylove.R;
import com.tufer.utils.NotificationUtil;

import butterknife.BindView;

public class SettingActivity extends ToolbarActivity {

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
        newMessageNotification.setOnClickListener(v -> NotificationUtil.gotoNotificationChannelSetting(this, Application.PUSH_MESSAGE_CHANNEL_ID,10));
    }
}
