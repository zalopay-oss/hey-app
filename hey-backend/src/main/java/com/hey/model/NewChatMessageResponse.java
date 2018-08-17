package com.hey.model;

import java.io.Serializable;

public class NewChatMessageResponse extends WsMessage implements Serializable {

    private String sessionId;
    private String lastMessage;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
