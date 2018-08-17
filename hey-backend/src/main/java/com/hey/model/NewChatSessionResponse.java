package com.hey.model;

import java.io.Serializable;
import java.util.Date;

public class NewChatSessionResponse extends WsMessage implements Serializable {

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
