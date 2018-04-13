package com.tufer.mylove.frags.account;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.tufer.common.Common;
import com.tufer.common.app.PresenterFragment;
import com.tufer.factory.presenter.account.RegisterContract;
import com.tufer.factory.presenter.account.RegisterPresenter;
import com.tufer.mylove.R;
import com.tufer.mylove.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册的界面
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {
    private static final int UPDATA_MCODE_ENABLED = 0;
    private static final int UPDATA_MCODE_TEXT = 1;
    private AccountTrigger mAccountTrigger;
    private RegisterFragment instance;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_verification)
    EditText mVerification;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;


    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.txt_send_code)
    TextView mCode;


    public RegisterFragment() {
        // Required empty public constructor
        instance=this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 拿到我们的Activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }


    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = mPhone.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        String code = mVerification.getText().toString();
        // 调用P层进行注册
        mPresenter.register(phone, code, name, password);
    }

    @OnClick(R.id.txt_send_code)
    void onSendCodeClick() {
        String phone = mPhone.getText().toString();
        mPresenter.obtainCode(Common.Constance.COUNTRY, phone);
    }

    @OnClick(R.id.txt_go_login)
    void onShowLoginClick() {
        // 让AccountActivity进行界面切换
        mAccountTrigger.triggerView();
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        // 当需要显示错误的时候触发，一定是结束了

        // 停止Loading
        mLoading.stop();
        // 让控件可以输入
        mPhone.setEnabled(true);
        mName.setEnabled(true);
        mPassword.setEnabled(true);
        // 提交按钮可以继续点击
        mSubmit.setEnabled(true);
        Log.e("TAG","showError()->"+Thread.currentThread().getName());
    }

    @Override
    public void showLoading() {
        super.showLoading();
            // 正在进行时，正在进行注册，界面不可操作
            // 开始Loading
            mLoading.start();
            // 让控件不可以输入
            mPhone.setEnabled(false);
            mName.setEnabled(false);
            mPassword.setEnabled(false);
            // 提交按钮不可以继续点击
            mSubmit.setEnabled(false);
        Log.e("TAG","showLoading()->"+Thread.currentThread().getName());
    }

    @Override
    public void registerSuccess() {
        // 注册成功，这个时候账户已经登录
        // 我们需要进行跳转到MainActivity界面
        MainActivity.show(getContext());
        // 关闭当前界面
        getActivity().finish();
        Log.e("TAG","registerSuccess()->"+Thread.currentThread().getName());
    }

    @Override
    public void obtainCodeSuccess() {
        Log.e("TAG","obtainCodeSuccess()->"+Thread.currentThread().getName());
        mCode.setEnabled(false);
        int i=60;
        while (0 != (i--)) {
            try {
                Message msg=new Message();
                msg.arg1=i;
                msg.what=UPDATA_MCODE_TEXT;
                uiHandler.sendMessage(msg);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        uiHandler.sendEmptyMessage(UPDATA_MCODE_ENABLED);
    }

    private Handler uiHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA_MCODE_TEXT:
                    if(instance.isAdded()) {
                        mCode.setText(msg.arg1+ getString(R.string.label_recapture));
                    }
                    break;
                case UPDATA_MCODE_ENABLED:
                    mCode.setEnabled(true);
                    mCode.setText(R.string.label_send_code);
                    break;
            }
        }
    };
}
