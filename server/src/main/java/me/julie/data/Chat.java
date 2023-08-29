package me.julie.data;

public class Chat {
    private String sender;
    private String message;
    private String date;
    private String time;

    public Chat(String sender, String message, String date, String time) {
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    // date and time are not needed/recorded
    public Chat(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.date = "";
        this.time = "";
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
