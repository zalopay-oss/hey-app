package com.hey.model;

import java.util.List;

public class ChatContainerResponse extends WsMessage {
    private boolean isChatGroup;
    private String sessionId;
    private List<ChatItem> chatItems;

    public List<ChatItem> getChatItems() {
        return chatItems;
    }

    public void setChatItems(List<ChatItem> chatItems) {
        this.chatItems = chatItems;
    }

    public boolean isChatGroup() {
        return isChatGroup;
    }

    public void setChatGroup(boolean chatGroup) {
        isChatGroup = chatGroup;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
