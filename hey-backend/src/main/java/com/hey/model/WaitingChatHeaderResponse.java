package com.hey.model;

import java.io.Serializable;

public class WaitingChatHeaderResponse implements Serializable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
