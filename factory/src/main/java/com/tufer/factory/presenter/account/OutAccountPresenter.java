package com.tufer.factory.presenter.account;

import com.tufer.factory.data.DataSource;
import com.tufer.factory.data.helper.AccountHelper;
import com.tufer.factory.model.db.User;
import com.tufer.factory.persistence.Account;
import com.tufer.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 退出登陆的逻辑实现
 *
 * @author Tufer
 * @version 1.0.0
 */
public class OutAccountPresenter extends BasePresenter<OutAccountContract.View>
        implements OutAccountContract.Presenter, DataSource.Callback<User> {
    public OutAccountPresenter(OutAccountContract.View view) {
        super(view);
    }

    @Override
    public void onDataLoaded(User user) {
        final OutAccountContract.View view = getView();
        if (view == null)
            return;
        // 强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.outAccountSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final OutAccountContract.View view = getView();
        if (view == null)
            return;
        // 此时是从网络回送回来的，并不保证处于主现场状态
        // 强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 调用主界面注册失败显示错误
                view.showError(strRes);
            }
        });
    }

    @Override
    public void outAccount() {
        start();
        if(Account.isLogin()){
            AccountHelper.outAccount(this);
        }
    }
}
