package me.julie.data;

import java.util.List;
import java.util.Map;

public class JsonChatId {
    private Map<List<String>, Integer> chatIds;

    // <(list of) usernames, unique group chat id>
    public JsonChatId(Map<List<String>, Integer> chatIds) {
        this.chatIds = chatIds;
    }

    public Map<List<String>, Integer> getChatIds() {
        return chatIds;
    }

    public void setChatIds(Map<List<String>, Integer> chatIds) {
        this.chatIds = chatIds;
    }
}
