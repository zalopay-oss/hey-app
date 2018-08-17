package com.hey.util;

public enum ErrorCode {
    AUTHORIZED_FAILED("error.unauthorized"),
    REGISTER_USERNAME_UNIQUED("username.uniqued"),
    REGISTER_USERNAME_EMPTY("username.empty"),
    REGISTER_FULLNAME_EMPTY("fullName.empty"),
    REGISTER_PASSWORD_EMPTY("password.empty"),

    ADD_FRIEND_USERNAME_EMPTY("username.empty"),
    ADD_FRIEND_USERNAME_NOT_EXISTED("username.notexist"),
    ADD_FRIEND_USERNAME_ALREADY("username.already"),

    START_GROUP_CHAT_USERNAME_NOT_EXISTED("username.notexist"),
    START_GROUP_CHAT_USERNAME_NOT_FRIEND("username.notfriend");



    private String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}


