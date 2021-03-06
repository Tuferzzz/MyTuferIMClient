package com.tufer.mylove.frags.message;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import com.tufer.common.app.Activity;
import com.tufer.common.app.Application;
import com.tufer.common.app.PresenterFragment;
import com.tufer.common.tools.AudioPlayHelper;
import com.tufer.common.widget.MessageLayout;
import com.tufer.common.widget.PortraitView;
import com.tufer.common.widget.adapter.TextWatcherAdapter;
import com.tufer.common.widget.recycler.RecyclerAdapter;
import com.tufer.face.Face;
import com.tufer.factory.data.helper.DbHelper;
import com.tufer.factory.model.db.Message;
import com.tufer.factory.model.db.User;
import com.tufer.factory.persistence.Account;
import com.tufer.factory.presenter.message.ChatContract;
import com.tufer.factory.utils.FileCache;
import com.tufer.mylove.R;
import com.tufer.mylove.activities.MainActivity;
import com.tufer.mylove.activities.MessageActivity;
import com.tufer.mylove.frags.panel.PanelFragment;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Tufer
 * @version 1.0.0
 */
public abstract class ChatFragment<InitModel>
        extends PresenterFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,
        ChatContract.View<InitModel>, PanelFragment.PanelCallback {

    protected String mReceiverId;
    protected Adapter mAdapter;
    private boolean isSoftKeyboardOpen = false;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingLayout;

    @BindView(R.id.edit_content)
    EditText mContent;

    @BindView(R.id.btn_submit)
    View mSubmit;

    // 控制顶部面板与软键盘过度的Boss控件
    private MessageLayout mPanelBoss;
    private PanelFragment mPanelFragment;
    private DisplayMetrics dm;

    // 语音的基础
    private FileCache<AudioHolder> mAudioFileCache;
    private AudioPlayHelper<AudioHolder> mAudioPlayer;

    private Activity.MyOnTouchListener onTouchListener = ev -> {
        if (ev.getAction() == KeyEvent.ACTION_DOWN) {
            if (mPanelBoss.isOpen() || isSoftKeyboardOpen) {
                dm = getActivity().getApplicationContext().getResources().getDisplayMetrics();
                if (ev.getY() < 420 * dm.density) {
                    mPanelBoss.closePanel();
                    Util.hideKeyboard(mContent);
                    return true;
                }
            }
        }
        return false;
    };

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected final int getContentLayoutId() {
        return R.layout.fragment_chat_common;
    }

    // 得到顶部布局的资源Id
    @LayoutRes
    protected abstract int getHeaderLayoutId();

    @Override
    protected void initWidget(View root) {
        ((Activity) getActivity()).registerMyOnTouchListener(onTouchListener);
        // 拿到占位布局
        // 替换顶部布局一定需要发生在super之前
        // 防止控件绑定异常
        ViewStub stub = (ViewStub) root.findViewById(R.id.view_stub_header);
        stub.setLayoutResource(getHeaderLayoutId());
        stub.inflate();

        // 在这里进行了控件绑定
        super.initWidget(root);

        // 初始化面板操作
        mPanelBoss = root.findViewById(R.id.lay_content);
        mPanelBoss.setup(() -> {
            // 请求隐藏软键盘
            Util.hideKeyboard(mContent);
        });
        mPanelBoss.setOnStateChangedListener(new AirPanel.OnStateChangedListener() {
            @Override
            public void onPanelStateChanged(boolean isOpen) {
                // 面板改变
                if (isOpen)
                    onBottomPanelOpened();
            }

            @Override
            public void onSoftKeyboardStateChanged(boolean isOpen) {
                // 软键盘改变
                if (isOpen) {
                    onBottomPanelOpened();
                    isSoftKeyboardOpen = true;
                } else {
                    isSoftKeyboardOpen = false;
                }
            }
        });
        mPanelFragment = (PanelFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);
        mPanelFragment.setup(this);

        initToolbar();
        initAppbar();
        initEditContent();

        // RecyclerView基本设置
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 进入界面的时候就进行初始化
        mAudioPlayer = new AudioPlayHelper<>(new AudioPlayHelper.RecordPlayListener<AudioHolder>() {
            @Override
            public void onPlayStart(AudioHolder audioHolder) {
                // 范型作用就在于此
                audioHolder.onPlayStart();
            }

            @Override
            public void onPlayStop(AudioHolder audioHolder) {
                // 直接停止
                audioHolder.onPlayStop();
            }

            @Override
            public void onPlayError(AudioHolder audioHolder) {
                // 提示失败
                Application.showToast(R.string.toast_audio_play_error);
            }
        });

        // 下载工具类
        mAudioFileCache = new FileCache<>("audio/cache", "mp3", new FileCache.CacheListener<AudioHolder>() {
            @Override
            public void onDownloadSucceed(final AudioHolder holder, final File file) {
                Run.onUiAsync(() -> {
                    // 主线程播放
                    mAudioPlayer.trigger(holder, file.getAbsolutePath());
                });
            }

            @Override
            public void onDownloadFailed(AudioHolder holder) {
                Application.showToast(R.string.toast_download_error);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAudioPlayer.destroy();
        ((Activity) getActivity()).unRegisterMyOnTouchListener(onTouchListener);
    }

    private void onBottomPanelOpened() {
        // 当底部面板或者软键盘打开时触发
        if (mAppBarLayout != null) {
            mAppBarLayout.setExpanded(false, true);
            mRecyclerView.scrollToPosition(getRecyclerAdapter().getItemCount() - 1);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mPanelBoss.isOpen()) {
            // 关闭面板并且返回true代表自己已经处理了消费了返回
            mPanelBoss.closePanel();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    protected void initData() {
        super.initData();
        // 开始进行初始化操作
        mPresenter.start();
    }

    // 初始化Toolbar
    protected void initToolbar() {
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            MainActivity.show(getContext());
            getActivity().finish();
        });
    }

    //  给界面的Appbar设置一个监听，得到关闭与打开的时候的进度
    private void initAppbar() {
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    // 初始化输入框监听
    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                // 设置状态，改变对应的Icon
                mSubmit.setActivated(needSendMsg);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

    @OnClick(R.id.btn_face)
    void onFaceClick() {
        // 仅仅只需请求打开即可
        if (!mPanelBoss.isOpen()) {
            mPanelBoss.openPanel();
        }
        mPanelFragment.showFace();
    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {
        if (!mPanelBoss.isOpen()) {
            mPanelBoss.openPanel();
        }
        mPanelFragment.showRecord();
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mSubmit.isActivated()) {
            // 发送
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        if (!mPanelBoss.isOpen()) {
            mPanelBoss.openPanel();
        }
        mPanelFragment.showGallery();
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 当数据第一次加载完毕回调此方法或者当数据发生改变时回调
        onBottomPanelOpened();
    }

    @Override
    public EditText getInputEditText() {
        // 返回输入框
        return mContent;
    }

    @Override
    public void onSendGallery(String[] paths) {
        // 图片回调回来
        mPresenter.pushImages(paths);
    }

    @Override
    public void onRecordDone(File file, long time) {
        // 语音回调回来
        mPresenter.pushAudio(file.getAbsolutePath(), time);
    }

    // 内容的适配器
    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            // 我发送的在右边，收到的在左边
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());

            switch (message.getType()) {
                // 文字内容
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;

                // 语音内容
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;

                // 图片内容
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;

                // 其他内容：文件
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                // 左右都是同一个
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);

                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);

                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);

                // 默认情况下，返回的就是Text类型的Holder进行处理
                // 文件的一些实现
                default:
                    return new TextHolder(root);
            }
        }
    }


    // Holder的基类
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        @BindView(R.id.txt_name)
        TextView mName;

        @Nullable
        @BindView(R.id.iv_tips)
        ImageView isSendFail;


        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            // 进行数据加载
            sender.load();
            // 头像加载
            mPortrait.setup(Glide.with(ChatFragment.this), sender);
            mName.setText(sender.getName());

            if (isSendFail != null) {
                int status = message.getStatus();
                if (status == Message.STATUS_FAILED) {
                    // 正常状态, 隐藏Loading
                    isSendFail.setVisibility(View.VISIBLE);
                } else {
                    isSendFail.setVisibility(View.GONE);
                }
                // 当状态是错误状态时才允许点击
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }

            if (message.getIsRead() == Message.ISREAD_FALSE) {
                message.setIsRead(Message.ISREAD_TRUE);
                DbHelper.save(Message.class, message);
            }

            if(isSendFail!=null){
                isSendFail.setOnClickListener(v->{
                    if (mPresenter.rePush(mData)) {
                        // 必须是右边的才有可能需要重新发送
                        // 状态改变需要重新刷新界面当前的信息
                        updateData(mData);
                    }
                });
            }
        }
    }

    // 文字的Holder
    class TextHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            Spannable spannable = new SpannableString(message.getContent());

            // 解析表情
            Face.decode(mContent, spannable, (int) Ui.dipToPx(getResources(), 20));

            // 把内容设置到布局上
            mContent.setText(spannable);
        }
    }

    // 语音的Holder
    class AudioHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;
        @BindView(R.id.im_audio_track)
        ImageView mAudioTrack;
        @BindView(R.id.ll_chat)
        LinearLayout mLinearLayout;
        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // long 30000
            String attach = TextUtils.isEmpty(message.getAttach()) ? "0" :
                    message.getAttach();
            mContent.setText(formatTime(attach));
            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (message.getType() == Message.TYPE_AUDIO ) {
                        // 权限的判断，当然权限已经全局申请了
                        mAudioFileCache.download(getAudioHolder(), message.getContent());
                    }
                }
            });
        }

        public AudioHolder getAudioHolder(){
            return this;
        }

        // 当播放开始
        void onPlayStart() {
            // 显示
            mAudioTrack.setVisibility(View.VISIBLE);
        }

        // 当播放停止
        void onPlayStop() {
            // 占位并隐藏
            mAudioTrack.setVisibility(View.INVISIBLE);
        }

        private String formatTime(String attach) {
            float time;
            try {
                // 毫秒转换为秒
                time = Float.parseFloat(attach) / 1000f;
            } catch (Exception e) {
                time = 0;
            }
            // 12000/1000f = 12.0000000
            // 取整一位小数点 1.234 -> 1.2 1.02 -> 1.0
            String shortTime = String.valueOf(Math.round(time * 10f) / 10f);
            // 1.0 -> 1     1.2000 -> 1.2
            shortTime = shortTime.replaceAll("[.]0+?$|0+?$", "");
            return String.format("%s″", shortTime);
        }
    }

    // 图片的Holder
    class PicHolder extends BaseHolder {
        @BindView(R.id.im_image)
        ImageView mContent;


        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // 当是图片类型的时候，Content中就是具体的地址
            String content = message.getContent();

            Glide.with(ChatFragment.this)
                    .load(content)
                    .fitCenter()
                    .into(mContent);

        }
    }


}
