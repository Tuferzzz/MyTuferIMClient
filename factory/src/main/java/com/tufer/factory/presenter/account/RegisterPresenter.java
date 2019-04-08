package com.tufer.factory.presenter.account;

import android.text.TextUtils;

import com.tufer.common.Common;
import com.tufer.common.app.Application;
import com.tufer.factory.Factory;
import com.tufer.factory.R;
import com.tufer.factory.data.DataSource;
import com.tufer.factory.data.helper.AccountHelper;
import com.tufer.factory.model.api.account.RegisterModel;
import com.tufer.factory.model.db.User;
import com.tufer.factory.persistence.Account;
import com.tufer.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * @author Tufer
 * @version 1.0.0
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String code, String name, String password) {
        // 调用开始方法，在start中默认启动了Loading
        start();

        // 得到View接口
        RegisterContract.View view = getView();

        // 校验
        if (!checkMobile(phone)) {
            // 提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            // 姓名需要大于2位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            // 密码需要大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            checkVerificationCode(phone, code, name, password);
        }
    }

    @Override
    public void obtainCode(String country, String phone) {
        // 得到View接口
        final RegisterContract.View view = getView();
        // 校验
        if (!checkMobile(phone)) {
            // 提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else {
            // 注册一个事件回调，用于处理发送验证码操作的结果
            SMSSDK.registerEventHandler(new EventHandler() {
                public void afterEvent(int event, int result, Object data) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        // 处理成功得到验证码的结果
                        // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                            Application.showToast(R.string.label_verifying_code_sent);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功
                            view.obtainCodeSuccess();
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                        }
                    } else {
                        // 处理错误的结果
                        ((Throwable) data).printStackTrace();
                        view.showError(R.string.data_account_register_invalid_send_code);
                    }

                    // 用完回调要注销，否则会造成泄露
                    SMSSDK.unregisterEventHandler(this);

                }
            });
            // 触发操作
            SMSSDK.getVerificationCode(country, phone);
        }
    }

    /**
     * 检查手机号是否合法
     *
     * @param phone 手机号码
     * @return 合法为True
     */
    public boolean checkMobile(String phone) {
        // 手机号不为空，并且满足格式
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    private void checkVerificationCode(final String phone, final String code, final String name, final String password) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        // 得到View接口

        RegisterModel model = new RegisterModel(phone, password, name, Account.getPushId());
        // 进行网络请求，并设置回送接口为自己
        AccountHelper.register(model, RegisterPresenter.this);
//        final RegisterContract.View view = getView();
//        SMSSDK.registerEventHandler(new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    // 处理验证成功的结果
//                    // 进行网络请求
//                    // 构造Model，进行请求调用
//                    RegisterModel model = new RegisterModel(phone, password, name, Account.getPushId());
//                    // 进行网络请求，并设置回送接口为自己
//                    AccountHelper.register(model, RegisterPresenter.this);
//                } else {
//                    // 处理错误的结果
//                    view.showError(R.string.data_account_register_invalid_verification_code);
//                }
//                // 用完回调要注销，否则会造成泄露
//                SMSSDK.unregisterEventHandler(this);
//            }
//        });
//        // 触发操作
//        SMSSDK.submitVerificationCode(Common.Constance.COUNTRY, phone, code);
    }

    @Override
    public void onDataLoaded(User user) {
        // 当网络请求成功，注册好了，回送一个用户信息回来
        // 告知界面，注册成功
        final RegisterContract.View view = getView();
        if (view == null)
            return;
        // 此时是从网络回送回来的，并不保证处于主现场状态
        // 强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 调用主界面注册成功
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 网络请求告知注册失败
        final RegisterContract.View view = getView();
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
}
