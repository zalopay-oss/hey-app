package com.hey.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ChatList implements Serializable {
    private List<UserHash> userHashes;
    private String sessionId;
    private Date updatedDate;
    private String lastMessage;

    public List<UserHash> getUserHashes() {
        return userHashes;
    }

    public void setUserHashes(List<UserHash> userHashes) {
        this.userHashes = userHashes;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
