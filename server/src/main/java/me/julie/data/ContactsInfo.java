package me.julie.data;

public class ContactsInfo {
    private String username;
    private String displayName;

    // for each user's own view of their contacts
    public ContactsInfo(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
