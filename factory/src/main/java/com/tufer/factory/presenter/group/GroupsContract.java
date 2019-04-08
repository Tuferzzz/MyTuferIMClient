package com.tufer.factory.presenter.group;

import com.tufer.factory.model.db.Group;
import com.tufer.factory.presenter.BaseContract;

/**
 * 我的群列表契约
 *
 * @author Tufer Email:1126179195@qq.com
 * @version 1.0.0
 */
public interface GroupsContract {
    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Group> {

    }
}
