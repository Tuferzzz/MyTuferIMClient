package com.tufer.factory.presenter.contact;

import com.tufer.common.app.Application;
import com.tufer.factory.Factory;
import com.tufer.factory.data.helper.DbHelper;
import com.tufer.factory.model.api.RspModel;
import com.tufer.factory.model.card.UserCard;
import com.tufer.factory.model.db.User;
import com.tufer.factory.net.Network;
import com.tufer.factory.presenter.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 关注的逻辑实现
 *
 * @author Tufer
 * @version 1.0.0
 */
public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter {

    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();
        Network.remote().userFollow(id)
                .map(new Function<RspModel<UserCard>, UserCard>() {
                    @Override
                    public UserCard apply(RspModel<UserCard> rspModel) throws Exception {
                        if (rspModel.success()) {
                            UserCard userCard = rspModel.getResult();
                            return userCard;
                        }
                        throw new Exception(Factory.app().getString(Factory.decodeRspCode(rspModel)));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<UserCard>() {
                    @Override
                    public void accept(UserCard userCard) throws Exception {
                        // 进行数据库存储，并分发通知, 异步的操作
                        DbHelper.save(User.class, userCard.build());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserCard>() {
                    @Override
                    public void accept(final UserCard userCard) throws Exception {
                        // 成功
                        final FollowContract.View view = getView();
                        if (view != null) {
                            view.onFollowSucceed(userCard);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        final FollowContract.View view = getView();
                        if (view != null) {
                            Application.showToast(throwable.getMessage());
                            view.showError(0);
                        }
                    }
                });
    }
}
