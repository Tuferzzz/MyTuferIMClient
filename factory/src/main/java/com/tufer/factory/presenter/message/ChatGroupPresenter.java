package com.tufer.factory.presenter.message;

import com.tufer.factory.data.helper.GroupHelper;
import com.tufer.factory.data.message.MessageGroupRepository;
import com.tufer.factory.model.db.Group;
import com.tufer.factory.model.db.Message;
import com.tufer.factory.model.db.view.MemberUserModel;
import com.tufer.factory.persistence.Account;

import java.util.List;

/**
 * Ⱥ������߼�
 *
 * @author Tufer Email:1126179195@qq.com
 * @version 1.0.0
 */
public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView>
        implements ChatContract.Presenter {

    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        // ����Դ��View�������ߣ������ߵ�����
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();

        // ��Ⱥ����Ϣ
        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            // ��ʼ������
            ChatContract.GroupView view = getView();

            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);

            // ������Ϣ��ʼ��
            view.onInit(group);

            // ��Ա��ʼ��
            List<MemberUserModel> models = group.getLatelyGroupMembers();
            final long memberCount = group.getGroupMemberCount();
            // û����ʾ�ĳ�Ա������
            long moreCount = memberCount - models.size();
            view.onInitGroupMembers(models, moreCount);
        }

    }
}
