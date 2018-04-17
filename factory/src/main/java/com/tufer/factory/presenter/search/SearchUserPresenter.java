package com.tufer.factory.presenter.search;

import com.tufer.common.app.Application;
import com.tufer.factory.Factory;
import com.tufer.factory.model.api.RspModel;
import com.tufer.factory.model.card.UserCard;
import com.tufer.factory.net.Network;
import com.tufer.factory.presenter.BasePresenter;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索人的实现
 *
 * @author Tufer
 * @version 1.0.0
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter {

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        Network.remote().userSearch(content)
                .map(new Function<RspModel<List<UserCard>>, List<UserCard>>() {
                    @Override
                    public List<UserCard> apply(RspModel<List<UserCard>> listRspModel) throws Exception {
                        if(listRspModel.success()){
                            return listRspModel.getResult();
                        }
                        throw new Exception(Factory.app().getString(Factory.decodeRspCode(listRspModel)));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserCard>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(List<UserCard> users) {
                        // 返回数据
                        final SearchContract.UserView view = getView();
                        if (view != null) {
                            view.onSearchDone(users);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        final SearchContract.UserView view = getView();
                        if (view != null) {
                            Application.showToast(e.getMessage());
                        }
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }
}
