package me.julie.data;

import java.util.List;
import java.util.Map;

public class JsonChatInfo {
    private Map<List<String>, List<Chat>> chatLogs;

    // <(list of) usernames, (list of) chat messages>
    // every conversation created in this app
    public JsonChatInfo(Map<List<String>, List<Chat>> chatLogs) {
        this.chatLogs = chatLogs;
    }

    public Map<List<String>, List<Chat>> getChatLogs() {
        return chatLogs;
    }

    public void setChatLogs(Map<List<String>, List<Chat>> chatLogs) {
        this.chatLogs = chatLogs;
    }
}