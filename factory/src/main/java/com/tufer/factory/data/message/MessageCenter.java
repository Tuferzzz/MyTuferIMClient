package com.tufer.factory.data.message;

import com.tufer.factory.model.card.MessageCard;

/**
 * 消息中心，进行消息卡片的消费
 *
 * @author Tufer
 * @version 1.0.0
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
