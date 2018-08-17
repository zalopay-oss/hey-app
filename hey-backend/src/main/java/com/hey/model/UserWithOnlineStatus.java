package com.hey.model;

public class UserWithOnlineStatus extends UserLite {
    boolean isOnline;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
