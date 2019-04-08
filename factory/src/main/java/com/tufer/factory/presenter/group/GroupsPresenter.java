package com.tufer.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.tufer.factory.data.group.GroupsDataSource;
import com.tufer.factory.data.group.GroupsRepository;
import com.tufer.factory.data.helper.GroupHelper;
import com.tufer.factory.data.helper.UserHelper;
import com.tufer.factory.model.db.Group;
import com.tufer.factory.presenter.BaseSourcePresenter;
import com.tufer.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 我的群组Presenter
 *
 * @author Tufer Email:1126179195@qq.com
 * @version 1.0.0
 */
public class GroupsPresenter extends BaseSourcePresenter<Group, Group,
        GroupsDataSource, GroupsContract.View> implements GroupsContract.Presenter {

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        // 加载网络数据, 以后可以优化到下拉刷新中
        // 只有用户下拉进行网络请求刷新
        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if (view == null)
            return;

        // 对比差异
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 界面刷新
        refreshData(result, groups);
    }
}
