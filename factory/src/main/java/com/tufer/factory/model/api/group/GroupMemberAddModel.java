package com.tufer.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tufer Email:1126179195@qq.com
 * @version 1.0.0
 */
public class GroupMemberAddModel {
    private Set<String> users = new HashSet<>();

    public GroupMemberAddModel(Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
