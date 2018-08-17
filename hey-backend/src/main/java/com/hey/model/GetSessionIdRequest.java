package com.hey.model;

import java.io.Serializable;

public class GetSessionIdRequest implements Serializable {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
