package me.subhas.contactinfo.business.model;

import me.subhas.contactinfo.data.entity.Contact;

public class ContactResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;

    public ContactResponse(Contact contact) {
        this.id = contact.getId();
        this.name = contact.getName();
        this.email = contact.getEmail();
        this.phone = contact.getPhone();
    }

    public ContactResponse(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
