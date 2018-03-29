package com.tufer.mvptest;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class Presenter implements IPresenter {
    private IView view;
    public Presenter(IView view) {
        this.view=view;
    }

    @Override
    public void search() {
        String s = view.getData();
        IUser user=new User();
        if(TextUtils.isEmpty(s)){
            return ;
        }
        String result = "Result:" + s + "-" + user.search(s);
        view.setData(result);
    }
}
