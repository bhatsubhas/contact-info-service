package me.subhas.contactinfo.business;

import me.subhas.contactinfo.business.model.ContactCreate;
import me.subhas.contactinfo.business.model.ContactListResponse;
import me.subhas.contactinfo.business.model.ContactResponse;
import me.subhas.contactinfo.business.model.ContactUpdate;

public interface ContactInfoService {
    ContactResponse createContact(ContactCreate contactCreate);

    ContactListResponse listContacts(Integer pageNo, Integer pageSize);

    ContactResponse retrieveContact(Long id);

    void deleteContact(Long id);

    ContactResponse updateContact(Long id, ContactUpdate update);
}
