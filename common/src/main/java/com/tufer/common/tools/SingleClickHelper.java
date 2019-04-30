package com.tufer.common.tools;

import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class SingleClickHelper {
    private static final int windowDuration = 1;

    public static void click(View view, Runnable r) {
        click(view, r, windowDuration);
    }

    public static void click(View view, Runnable r, int durationSeconds) {
        if (view == null || r == null) {
            return;
        }
        Observable.create(new ClickObservable(view)).throttleFirst(durationSeconds, TimeUnit.SECONDS).subscribe(o -> r.run());
    }

    public static void click(View view, View.OnClickListener listener) {
        click(view, listener, windowDuration);
    }

    public static void click(View view, View.OnClickListener listener, int durationSeconds) {
        if (view == null || listener == null) {
            return;
        }
        Observable.create(new ClickObservable(view)).throttleFirst(durationSeconds, TimeUnit.SECONDS).subscribe(o -> listener.onClick(view));
    }

    private SingleClickHelper() {
    }
}

class ClickObservable implements ObservableOnSubscribe<View> {
    private ObservableEmitter mEmitter;

    public ClickObservable(View view) {
        view.setOnClickListener(view1 -> {
            if (mEmitter != null) {
                mEmitter.onNext(view1);
            }
        });
    }

    @Override
    public void subscribe(ObservableEmitter<View> e) throws Exception {
        mEmitter = e;
    }
}