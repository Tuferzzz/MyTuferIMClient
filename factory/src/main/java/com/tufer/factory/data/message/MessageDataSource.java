package com.tufer.factory.data.message;

import com.tufer.factory.data.DbDataSource;
import com.tufer.factory.model.db.Message;

/**
 * 消息的数据源定义，他的实现是：MessageRepository
 * 关注的对象是Message表
 *
 * @author Tufer
 * @version 1.0.0
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
