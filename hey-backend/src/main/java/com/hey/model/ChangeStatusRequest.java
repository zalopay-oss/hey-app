package com.hey.model;

import java.io.Serializable;

public class ChangeStatusRequest implements Serializable {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
