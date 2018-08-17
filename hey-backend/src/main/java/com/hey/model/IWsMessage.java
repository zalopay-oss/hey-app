package com.hey.model;

public interface IWsMessage {
    public static final String TYPE_CHAT_ITEM_REQUEST = "CHAT_ITEMS_REQUEST";
    public static final String TYPE_CHAT_ITEM_RESPONSE = "CHAT_ITEMS_RESPONSE";
    public static final String TYPE_CHAT_MESSAGE_REQUEST = "CHAT_MESSAGE_REQUEST";
    public static final String TYPE_CHAT_MESSAGE_RESPONSE = "CHAT_MESSAGE_RESPONSE";
    public static final String TYPE_CHAT_NEW_SESSION_RESPONSE = "CHAT_NEW_SESSION_RESPONSE";
    public static final String TYPE_NOTIFICATION_NEW_CHAT = "NEW_CHAT_RESPONSE";
    public static final String TYPE_NOTIFICATION_FRIEND_ONLINE = "USER_ONLINE_RESPONSE";
    public static final String TYPE_NOTIFICATION_FRIEND_OFFLINE = "USER_OFFLINE_RESPONSE";
}
