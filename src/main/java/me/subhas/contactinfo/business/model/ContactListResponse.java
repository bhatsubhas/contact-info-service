package me.subhas.contactinfo.business.model;

import java.util.List;

public class ContactListResponse {
    private List<ContactResponse> contacts;

    public ContactListResponse(List<ContactResponse> contacts) {
        this.contacts = contacts;
    }

    public List<ContactResponse> getContacts() {
        return contacts;
    }

}
