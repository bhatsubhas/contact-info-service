package me.subhas.contactinfo.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import me.subhas.contactinfo.business.impl.ContactInfoServiceImpl;
import me.subhas.contactinfo.business.model.ContactListResponse;
import me.subhas.contactinfo.business.model.ContactResponse;
import me.subhas.contactinfo.data.ContactRepository;
import me.subhas.contactinfo.data.entity.Contact;

@ExtendWith(MockitoExtension.class)
class ListContactsServiceTests {
    @Mock
    private ContactRepository contactRepository;

    @Autowired
    @InjectMocks
    private ContactInfoServiceImpl contactInfoService;

    @Test
    @DisplayName("When no contacts are present then return empty array")
    void testReturnsEmptyArray() {
        when(contactRepository.findAll()).thenReturn(List.of());
        ContactListResponse contacts = contactInfoService.listContacts();
        assertEquals(0, contacts.contacts().size());
        verify(contactRepository).findAll();
    }

    @Test
    @DisplayName("Returns more array of contacts")
    void testResturnArrayOfContacts() {

        List<Contact> contacts = List.of(
                new Contact(1L, "Abc U", "abc.u@gmail.com", "12345671"),
                new Contact(2L, "Bcd U", "bcd.u@gmail.com", "12345672"),
                new Contact(3L, "Cde U", null, "12345673"),
                new Contact(4L, "Efg U", "efg.u@gmail.com", "12345674"),
                new Contact(5L, "Fgh U", "Fgh.u@gmail.com", "12345675"));

        when(contactRepository.findAll()).thenReturn(contacts);
        ContactListResponse contactListResponse = contactInfoService.listContacts();
        assertEquals(5, contactListResponse.contacts().size());
        for (ContactResponse contactResponse : contactListResponse.contacts()) {
            Contact contact = contacts.get(contactResponse.id().intValue() - 1);
            assertEquals(contact.getName(), contactResponse.name());
            assertEquals(contact.getEmail(), contactResponse.email());
            assertEquals(contact.getPhone(), contactResponse.phone());
        }
        verify(contactRepository).findAll();
    }
}
