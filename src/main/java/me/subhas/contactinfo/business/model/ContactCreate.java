package me.subhas.contactinfo.business.model;

import jakarta.validation.constraints.NotNull;
import me.subhas.contactinfo.data.entity.Contact;

public record ContactCreate(@NotNull(message = "'name' field is mandatory") String name, String email,
        @NotNull(message = "'phone' field is mandatory") String phone) {
    public Contact toContactEntity() {
        return new Contact(name, email, phone);
    }
}
