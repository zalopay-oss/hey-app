package com.hey.model;

import java.util.List;

public class ChatListResponse {
    private List<ChatListItem> items;

    public List<ChatListItem> getItems() {
        return items;
    }

    public void setItems(List<ChatListItem> items) {
        this.items = items;
    }
}
