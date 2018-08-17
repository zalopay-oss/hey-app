package com.hey.model;

import java.util.List;

public class AddressBookResponse {
    private List<AddressBookItem> items;

    public List<AddressBookItem> getItems() {
        return items;
    }

    public void setItems(List<AddressBookItem> items) {
        this.items = items;
    }
}
