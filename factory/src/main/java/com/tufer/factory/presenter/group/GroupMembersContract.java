package com.tufer.factory.presenter.group;

import com.tufer.factory.model.db.view.MemberUserModel;
import com.tufer.factory.presenter.BaseContract;

/**
 * 群成员的契约
 *
 * @author Tufer Email:1126179195@qq.com
 * @version 1.0.0
 */
public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter {
        // 具有一个刷新的方法
        void refresh();
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        // 获取群的ID
        String getGroupId();
    }
}
