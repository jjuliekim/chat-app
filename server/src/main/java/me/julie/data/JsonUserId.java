package me.julie.data;

import java.util.Map;

public class JsonUserId {
    Map<Integer, String> userIds;

    // <unique user id, username>
    public JsonUserId(Map<Integer, String> userIds) {
        this.userIds = userIds;
    }

    public Map<Integer, String> getUserIds() {
        return userIds;
    }

    public void setUserIds(Map<Integer, String> userIds) {
        this.userIds = userIds;
    }
}