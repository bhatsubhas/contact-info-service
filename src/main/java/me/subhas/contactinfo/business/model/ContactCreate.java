package me.subhas.contactinfo.business.model;

import jakarta.validation.constraints.NotNull;
import me.subhas.contactinfo.data.entity.Contact;

public class ContactCreate {

    @NotNull(message = "'name' field is mandatory")
    private String name;
    private String email;
    @NotNull(message = "'phone' field is mandatory")
    private String phone;

    public ContactCreate() {

    }

    public ContactCreate(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public Contact toContactEntity() {
        Contact contact = new Contact();
        contact.setName(name);
        contact.setEmail(email);
        contact.setPhone(phone);
        return contact;
    }

}
