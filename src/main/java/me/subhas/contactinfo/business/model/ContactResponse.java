package me.subhas.contactinfo.business.model;

import me.subhas.contactinfo.data.entity.Contact;

public record ContactResponse(Long id, String name, String email, String phone) {
    public ContactResponse(Contact contact) {
        this(contact.getId(), contact.getName(), contact.getEmail(), contact.getPhone());
    }
}
