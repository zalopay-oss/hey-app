package com.hey.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ChatMessage implements Serializable {
    private String sessionId;
    private UserHash userHash;
    private String message;
    private Date createdDate;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserHash getUserHash() {
        return userHash;
    }

    public void setUserHash(UserHash userHash) {
        this.userHash = userHash;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
