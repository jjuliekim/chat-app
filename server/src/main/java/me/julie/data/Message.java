package me.julie.data;

public class Message {
    private String sender;
    private String text;
    private String date;
    private String time;

    public Message(String sender, String text, String date, String time) {
        this.sender = sender;
        this.text = text;
        this.date = date;
        this.time = time;
    }

    // date and time are not needed/recorded
    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.date = "";
        this.time = "";
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
