package com.hey.model;

import java.io.Serializable;

public class AddFriendResponse implements Serializable {
    private AddressBookItem item;

    public AddressBookItem getItem() {
        return item;
    }

    public void setItem(AddressBookItem item) {
        this.item = item;
    }
}
