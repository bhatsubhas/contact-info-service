package me.subhas.contactinfo.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import me.subhas.contactinfo.business.exception.ContactNotFoundException;
import me.subhas.contactinfo.business.impl.ContactInfoServiceImpl;
import me.subhas.contactinfo.business.model.ContactResponse;
import me.subhas.contactinfo.data.ContactRepository;
import me.subhas.contactinfo.data.entity.Contact;

@ExtendWith(MockitoExtension.class)
class RetrieveContactServiceTests {
    @Mock
    private ContactRepository contactRepository;

    @Autowired
    @InjectMocks
    private ContactInfoServiceImpl contactInfoService;

    @Test
    @DisplayName("Successfully retrieve a contact when contact id is known")
    void testRetrieveContactSuccessfully() {
        Long id = 235L;
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";
        String phone = "123456";
        Contact contact = new Contact(id, name, email, phone);
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        ContactResponse response = contactInfoService.retrieveContact(id);

        assertEquals(contact.getId(), response.id());
        assertEquals(contact.getName(), response.name());
        assertEquals(contact.getEmail(), response.email());
        assertEquals(contact.getPhone(), response.phone());

        verify(contactRepository).findById(id);

    }

    @Test
    @DisplayName("Throw exception when contact with specified id not present")
    void testRetrieveContactFailure() {
        Long id = 235L;
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        String message = assertThrows(ContactNotFoundException.class, () -> contactInfoService.retrieveContact(id)).getMessage();
        assertEquals(String.format("Could not find contact with id '%d'", id), message);

        verify(contactRepository).findById(id);

    }
}
