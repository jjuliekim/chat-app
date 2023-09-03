package me.julie.data;

import java.util.List;
import java.util.Map;

public class JsonChatInfo {
    private Map<Integer, List<Message>> chatLogs;

    // <unique chat id, (list of) chat messages>
    // every conversation created in this app
    public JsonChatInfo(Map<Integer, List<Message>> chatLogs) {
        this.chatLogs = chatLogs;
    }

    public Map<Integer, List<Message>> getChatLogs() {
        return chatLogs;
    }

    public void setChatLogs(Map<Integer, List<Message>> chatLogs) {
        this.chatLogs = chatLogs;
    }
}