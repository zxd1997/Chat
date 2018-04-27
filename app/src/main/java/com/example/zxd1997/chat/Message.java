package com.example.zxd1997.chat;

import org.litepal.crud.DataSupport;

public class Message extends DataSupport {
    private String from;
    private String to;
    private String content;
    private int type;

    public Message(String from, String to, String content, int type) {

        this.from = from;
        this.to = to;
        this.content = content;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
