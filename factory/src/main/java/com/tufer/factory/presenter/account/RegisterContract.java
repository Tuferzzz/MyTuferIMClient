package com.tufer.factory.presenter.account;

import com.tufer.factory.presenter.BaseContract;

/**
 * @author Tufer
 * @version 1.0.0
 */
public interface RegisterContract {
    interface View extends BaseContract.View<Presenter> {
        // 注册成功
        void registerSuccess();
        //获取验证码成功
        void obtainCodeSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        // 发起一个注册
        void register(String phone, String code , String name, String password);

        // 获取验证码
        void obtainCode(String country,String phone);

    }

}
