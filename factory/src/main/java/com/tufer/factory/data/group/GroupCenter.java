package com.tufer.factory.data.group;

import com.tufer.factory.model.card.GroupCard;
import com.tufer.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 *
 * @author Tufer
 * @version 1.0.0
 */
public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}
