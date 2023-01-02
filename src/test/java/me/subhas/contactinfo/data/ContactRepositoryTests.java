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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    @Test
    @DisplayName("Repository returns Contacts of specified page no. and page size.")
    void testFindByPage_returnsContacts() {
        entityManager.persistFlushFind(new Contact("Abc U", "abc.u@gmail.com", "123456"));
        entityManager.persistFlushFind(new Contact("Bcd V", "bcd.v@gmail.com", "123457"));
        entityManager.persistFlushFind(new Contact("Cde W", "cde.w@gmail.com", "123458"));
        entityManager.persistFlushFind(new Contact("Def X", "def.x@gmail.com", "123459"));
        entityManager.persistFlushFind(new Contact("Efg Y", "efg.y@gmail.com", "123450"));
        entityManager.persistFlushFind(new Contact("Fgh U", "fgh.u@gmail.com", "123451"));
        entityManager.persistFlushFind(new Contact("Ghi V", "ghi.v@gmail.com", "123452"));
        entityManager.persistFlushFind(new Contact("Hij W", "hij.w@gmail.com", "123453"));
        entityManager.persistFlushFind(new Contact("Ijk X", "ijk.x@gmail.com", "123454"));
        entityManager.persistFlushFind(new Contact("Jkl Y", "jkl.y@gmail.com", "123455"));

        int pageNumber = 0;
        int pageSize = 7;
        PageRequest pageRequest = PageRequest.of(pageNumber,pageSize);
        Page<Contact> page = contactRepository.findAll(pageRequest);
        assertEquals(pageNumber, page.getNumber());
        assertEquals(pageSize, page.getContent().size());

        pageNumber = 1;
        pageRequest = PageRequest.of(pageNumber,pageSize);
        page = contactRepository.findAll(pageRequest);
        assertEquals(pageNumber, page.getNumber());
        assertEquals(3, page.getContent().size());

        pageNumber = 2;
        pageRequest = PageRequest.of(pageNumber,pageSize);
        page = contactRepository.findAll(pageRequest);
        assertEquals(pageNumber, page.getNumber());
        assertEquals(0, page.getContent().size());
    }
}
