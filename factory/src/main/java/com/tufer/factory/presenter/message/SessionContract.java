package com.tufer.factory.presenter.message;

import com.tufer.factory.model.db.Session;
import com.tufer.factory.presenter.BaseContract;

/**
 * @author Tufer
 * @version 1.0.0
 */
public interface SessionContract {
    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Session> {

    }
}