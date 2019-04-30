package com.tufer.mylove.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;

import com.tufer.common.app.ToolbarActivity;
import com.tufer.mylove.R;

public class SettingActivity extends ToolbarActivity {

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
}
