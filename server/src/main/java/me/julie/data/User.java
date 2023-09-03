package me.julie.data;

import java.util.List;

public class User {
    private String username;
    private String password;
    private String displayName;
    private int id;
    private List<ContactsInfo> contacts;

    public User(String username, String password, String displayName, int id, List<ContactsInfo> contacts) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.id = id;
        this.contacts = contacts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public List<ContactsInfo> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsInfo> contacts) {
        this.contacts = contacts;
    }
}
