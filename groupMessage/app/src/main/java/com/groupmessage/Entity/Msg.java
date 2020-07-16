package com.groupmessage.Entity;

public class Msg {
    public static final int TYPE_RECEIVED = 0;

    public static final int TYPE_SENT = 1;

    public static final int TYPE_IMAGE_RECEIVED = 2;

    public static final int TYPE_IMAGE_SENT = 3;

    private String content;

    private int type;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

}
