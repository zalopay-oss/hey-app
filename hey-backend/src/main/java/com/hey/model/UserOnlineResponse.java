package com.hey.model;

import java.io.Serializable;

public class UserOnlineResponse extends WsMessage implements Serializable {
    private String userId;
    private String fullName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
