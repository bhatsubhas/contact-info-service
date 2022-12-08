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
        checkDuplicateNameForCreate(contactCreate.name());
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
        if (exists) {
            contactRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public ContactResponse updateContact(Long id, ContactUpdate update) {
        checkDuplicateNameForUpdate(update.name(), id);
        Contact contact = getContact(id);
        if (isUpdatable(update.name(), contact.getName())) {
            contact.setName(update.name());
        }
        if (isUpdatable(update.email(), contact.getEmail())) {
            contact.setEmail(update.email());
        }
        ;
        if (isUpdatable(update.phone(), contact.getPhone())) {
            contact.setPhone(update.phone());
        }
        return new ContactResponse(contact);
    }

    private void checkDuplicateNameForCreate(String name) {
        Optional<Contact> existingContact = contactRepository.findByName(name);
        if (existingContact.isPresent()) {
            throw new DuplicateContactNameException(
                    String.format("Contact with with name '%s' already present", name));
        }
    }

    private void checkDuplicateNameForUpdate(String name, Long updateContactId) {
        Optional<Contact> existingContact = contactRepository.findByName(name);
        existingContact.ifPresent(contact -> {
            if (!contact.getId().equals(updateContactId)) {
                throw new DuplicateContactNameException(
                        String.format("Contact with with name '%s' already used", name));
            }
        });
    }

    private boolean isUpdatable(String newValue, String oldValue) {
        return Objects.nonNull(newValue) && !newValue.isBlank() && !newValue.equals(oldValue);
    }

    private Contact getContact(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isEmpty()) {
            throw new ContactNotFoundException(String.format("Could not find contact with id '%d'", id));
        }
        return contact.get();
    }
}
