package me.subhas.contactinfo.business;

import me.subhas.contactinfo.business.model.ContactCreate;
import me.subhas.contactinfo.business.model.ContactListResponse;
import me.subhas.contactinfo.business.model.ContactResponse;

public interface ContactInfoService {
    ContactResponse createContact(ContactCreate contactCreate);

    ContactListResponse listContacts();

    ContactResponse retrieveContact(Long id);
}
