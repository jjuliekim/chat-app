package me.julie.data;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String displayName;
    private List<ContactsInfo> contacts;

    public User(String username, String password, String displayName, List<ContactsInfo> contacts) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
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

    public List<ContactsInfo> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsInfo> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<String> getContactsUsernames() {
        ArrayList<String> contactUsernames = new ArrayList<>();
        for (ContactsInfo contact : contacts) {
            contactUsernames.add(contact.getUsername());
        }
        return contactUsernames;
    }

    // names this user has set for each of their contacts
    public ArrayList<String> getContactsDisplayNames() {
        ArrayList<String> contactDisplayNames = new ArrayList<>();
        for (ContactsInfo contact : contacts) {
            contactDisplayNames.add(contact.getDisplayName());
        }
        return contactDisplayNames;
    }
}
