package com.hugo.xdaily;

/**
 * @auther Hugo
 * Created on 2016/6/2 14:15.
 */
public class MessageEvent {
    private String id;

    public MessageEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
