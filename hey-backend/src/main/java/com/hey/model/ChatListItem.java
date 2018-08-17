package com.hey.model;

import java.util.Date;

public class ChatListItem {
    private String name;
    private String sessionId;
    private String lastMessage;
    private int unread;
    private boolean groupChat;
    private Date updatedDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public boolean getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(boolean groupChat) {
        this.groupChat = groupChat;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
