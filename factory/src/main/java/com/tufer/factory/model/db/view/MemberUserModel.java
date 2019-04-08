package com.tufer.factory.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

import com.tufer.factory.model.db.AppDatabase;

/**
 * 群成员对应的用户的简单信息表
 *
 * @author Tufer Email:1126179195@qq.com
 * @version 1.0.0
 */
@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String userId; // User-id/Member-userId
    @Column
    public String name; // User-name
    @Column
    public String alias; // Member-alias
    @Column
    public String portrait; // User-portrait
}
