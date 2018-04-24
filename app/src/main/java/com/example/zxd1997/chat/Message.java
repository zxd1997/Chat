package com.example.zxd1997.chat;

public class Message {
    private String name;
    private String content;
    private int type;

    public Message(String name, String content, int type) {
        this.name = name;
        this.content = content;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
