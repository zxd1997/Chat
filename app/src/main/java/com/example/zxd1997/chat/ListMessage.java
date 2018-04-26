package com.example.zxd1997.chat;

public class ListMessage {
    private String from;
    private String header;
    private String last;

    public ListMessage(String from, String last) {

        this.from = from;
        this.last = last;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
