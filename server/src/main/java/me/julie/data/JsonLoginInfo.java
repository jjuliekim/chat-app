package me.julie.data;

import java.util.Map;

public class JsonLoginInfo {
    private Map<String, User> logins;

    // <username, User>
    // given the username, can get the user's info
    public JsonLoginInfo(Map<String, User> logins) {
        this.logins = logins;
    }

    public Map<String, User> getLogins() {
        return logins;
    }

    public void setLogins(Map<String, User> logins) {
        this.logins = logins;
    }
}