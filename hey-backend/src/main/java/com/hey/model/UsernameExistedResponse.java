package com.hey.model;

import java.io.Serializable;

public class UsernameExistedResponse implements Serializable {
    private boolean existed;
    private String username;

    public boolean isExisted() {
        return existed;
    }

    public void setExisted(boolean existed) {
        this.existed = existed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
