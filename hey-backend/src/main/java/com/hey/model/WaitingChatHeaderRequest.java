package com.hey.model;

import java.io.Serializable;

public class WaitingChatHeaderRequest implements Serializable {
    private String[] usernames;

    public String[] getUsernames() {
        return usernames;
    }

    public void setUsernames(String[] usernames) {
        this.usernames = usernames;
    }
}
