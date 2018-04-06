package com.tufer.mylove.activities;

import android.content.Context;
import android.content.Intent;

import com.tufer.common.app.Activity;
import com.tufer.common.app.Fragment;
import com.tufer.mylove.R;
import com.tufer.mylove.frags.account.UpdateInfoFragment;


public class AccountActivity extends Activity {
    private Fragment mCurFragment;

    /**
     * 账户Activity显示的入口
     *
     * @param context Context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    // Activity中收到剪切图片成功的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }
}
