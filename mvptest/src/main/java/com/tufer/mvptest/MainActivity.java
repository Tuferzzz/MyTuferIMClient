package com.tufer.mvptest;

import android.widget.EditText;
import android.widget.TextView;

import com.tufer.common.app.Activity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements IView{
    @BindView(R.id.et_input)
    EditText editText;

    @BindView(R.id.tv_result)
    TextView textView;

    private IPresenter presenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        presenter=new Presenter(this);
    }

    @OnClick(R.id.btn_submit)
    void onSubmit(){
        presenter.search();

    }

    @Override
    public String getData() {
        return editText.getText().toString();
    }

    @Override
    public void setData(String s) {
        textView.setText(s);
    }
}
