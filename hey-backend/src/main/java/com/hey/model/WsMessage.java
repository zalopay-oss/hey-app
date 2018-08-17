package com.hey.model;

public abstract class WsMessage implements IWsMessage {
    protected String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
