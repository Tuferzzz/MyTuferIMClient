package com.tufer.factory.presenter.group;

import com.tufer.factory.Factory;
import com.tufer.factory.data.helper.GroupHelper;
import com.tufer.factory.model.db.view.MemberUserModel;
import com.tufer.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * @author Tufer Email:1126179195@qq.com
 * @version 1.0.0
 */
public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMembersContract.View>
        implements GroupMembersContract.Presenter {

    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        // 显示Loading
        start();

        // 异步加载
        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null)
                return;

            String groupId = view.getGroupId();

            // 传递数量为-1 代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);

            refreshData(models);
        }
    };
}
