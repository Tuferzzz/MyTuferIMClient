package com.tufer.mylove;

import android.os.Bundle;

import com.tufer.common.app.Activity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }
}
