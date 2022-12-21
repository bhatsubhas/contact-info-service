package me.subhas.contactinfo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import me.subhas.contactinfo.data.entity.Contact;

@DataJpaTest
class ContactRepositoryTests {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Repository returns empty list when no contacts are present in the DB")
    void testFindAll_returnsNoContacts() {
        List<Contact> contacts = contactRepository.findAll();
        assertEquals(0, contacts.size());
    }

    @Test
    @DisplayName("Repository returns empty optional when contact with passed name is not found in the DB")
    void testFindByName_returnsEmptyOptional() {
        Optional<Contact> contactOpt = contactRepository.findByName("Aaa Bbb");
        assertTrue(contactOpt.isEmpty());
    }

    @Test
    @DisplayName("Repository returns Contact when contact with passed name is found in the DB")
    void testFindByName_returnsContact() {
        String name = "Aaa Bbb";
        String email = "aaa.bbb@gmail.com";
        String phone = "1234567890";
        Contact savedContact = entityManager.persistFlushFind(new Contact(name, email, phone));
        Optional<Contact> contactOpt = contactRepository.findByName("Aaa Bbb");

        assertTrue(contactOpt.isPresent());
        Contact contact = contactOpt.get();
        assertEquals(name, contact.getName());
        assertEquals(email, contact.getEmail());
        assertEquals(phone, contact.getPhone());

        assertEquals(savedContact.getName(), contact.getName());
        assertEquals(savedContact.getEmail(), contact.getEmail());
        assertEquals(savedContact.getPhone(), contact.getPhone());
    }
}
