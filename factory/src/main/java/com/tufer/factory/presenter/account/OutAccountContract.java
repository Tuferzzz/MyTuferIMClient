package com.tufer.factory.presenter.account;

import com.tufer.factory.presenter.BaseContract;

/**
 * @author Tufer
 * @version 1.0.0
 */
public interface OutAccountContract {
    interface View extends BaseContract.View<Presenter> {
        // 退出成功
        void outAccountSuccess();
    }


    interface Presenter extends BaseContract.Presenter {
        // 发起一个请求
        void outAccount();
    }

}
