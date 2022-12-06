package me.subhas.contactinfo.business.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import me.subhas.contactinfo.business.ContactInfoService;
import me.subhas.contactinfo.business.exception.ContactNotFoundException;
import me.subhas.contactinfo.business.exception.DuplicateContactNameException;
import me.subhas.contactinfo.business.model.ContactCreate;
import me.subhas.contactinfo.business.model.ContactListResponse;
import me.subhas.contactinfo.business.model.ContactResponse;
import me.subhas.contactinfo.business.model.ContactUpdate;
import me.subhas.contactinfo.data.ContactRepository;
import me.subhas.contactinfo.data.entity.Contact;

@Service
public class ContactInfoServiceImpl implements ContactInfoService {

    private ContactRepository contactRepository;

    public ContactInfoServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactResponse createContact(ContactCreate contactCreate) {
        String name = contactCreate.name();
        Optional<Contact> existingContact = contactRepository.findByName(name);
        if (existingContact.isPresent()) {
            throw new DuplicateContactNameException(
                    String.format("Contact with with name '%s' already present", name));
        }
        Contact contact = contactRepository.save(contactCreate.toContactEntity());
        return new ContactResponse(contact);
    }

    @Override
    public ContactListResponse listContacts() {
        List<ContactResponse> contacts = contactRepository.findAll().stream()
                .map(ContactResponse::new).toList();
        return new ContactListResponse(contacts);
    }

    @Override
    public ContactResponse retrieveContact(Long id) {
        Contact contact = getContact(id);
        return new ContactResponse(contact);
    }

    @Override
    public void deleteContact(Long id) {
        boolean exists = contactRepository.existsById(id);
        if(exists) {
            contactRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public ContactResponse updateContact(Long id, ContactUpdate update) {
        Contact contact = getContact(id);
        if(isUpdatable(update.name())) {
            contact.setName(update.name());
        }
        if(isUpdatable(update.email())) {
            contact.setEmail(update.email());
        }
        ;
        if(isUpdatable(update.phone())) {
            contact.setPhone(update.phone());
        }
        return new ContactResponse(contact);
    }

    private boolean isUpdatable(String value) {
        return Objects.nonNull(value) && !value.isBlank();
    }

    private Contact getContact(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if(contact.isEmpty()) {
            throw new ContactNotFoundException(String.format("Could not find contact with id '%d'", id));
        }
        return contact.get();
    }
}
