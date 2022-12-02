package me.subhas.contactinfo.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import me.subhas.contactinfo.business.exception.DuplicateContactNameException;
import me.subhas.contactinfo.business.impl.ContactInfoServiceImpl;
import me.subhas.contactinfo.business.model.ContactCreate;
import me.subhas.contactinfo.business.model.ContactResponse;
import me.subhas.contactinfo.data.ContactRepository;
import me.subhas.contactinfo.data.entity.Contact;

@ExtendWith(MockitoExtension.class)
class ContactCreationServiceTests {

    @Mock
    private ContactRepository contactRepository;

    @Autowired
    @InjectMocks
    private ContactInfoServiceImpl contactInfoService;

    @Test
    @DisplayName("Successfully create a contact")
    void testSuccessfulContactCreation() {
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";
        String phone = "+91 12345 67890";

        Contact contact = new Contact(1L, name, email, phone);
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        ContactCreate contactCreate = new ContactCreate(name, email, phone);
        ContactResponse response = contactInfoService.createContact(contactCreate);
        assertEquals(contact.getId(), response.id());
        assertEquals(contactCreate.name(), response.name());
        assertEquals(contactCreate.email(), response.email());
        assertEquals(contactCreate.phone(), response.phone());

        verify(contactRepository).findByName(name);
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    @DisplayName("Cannot create a contact when contact with same name is already present")
    void testCannotCreateContactWithDuplicateName() {
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";
        String phone = "123456";
        Contact contact = new Contact(1L, name, email, phone);
        when(contactRepository.findByName("Abc Xyz")).thenReturn(Optional.of(contact));

        ContactCreate contactCreate = new ContactCreate(name, email, phone);
        String exceptionMessage = assertThrows(DuplicateContactNameException.class,
                () -> contactInfoService.createContact(contactCreate)).getMessage();
        assertEquals(String.format("Contact with with name '%s' already present", name), exceptionMessage);

        verify(contactRepository).findByName(name);
        verify(contactRepository, times(0)).save(any(Contact.class));
    }
}
