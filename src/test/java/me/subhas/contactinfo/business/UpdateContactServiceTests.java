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
import me.subhas.contactinfo.business.model.ContactUpdate;
import me.subhas.contactinfo.data.ContactRepository;
import me.subhas.contactinfo.data.entity.Contact;

@ExtendWith(MockitoExtension.class)
class UpdateContactServiceTests {

    @Mock
    private ContactRepository contactRepository;

    @Autowired
    @InjectMocks
    private ContactInfoServiceImpl contactInfoService;

    @Test
    @DisplayName("Successfully Update Name, Email and Phone Number")
    void testUpdateOfNameEmailAndPhone() {
        Long id = 345L;
        String name = "Xyz Abc";
        String email = "xyz.abc@gmail.com";
        String phone = "98765432";
        Contact contact = new Contact(id, name, email, phone);

        String updateName = "Abc Xyz";
        String updateEmail = "abc.xyz@gmail.com";
        String updatePhone = "9845123456";
        ContactUpdate update = new ContactUpdate(updateName, updateEmail, updatePhone);
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        ContactResponse response = contactInfoService.updateContact(id, update);

        assertEquals(id, response.id());
        assertEquals(updateName, response.name());
        assertEquals(updateEmail, response.email());
        assertEquals(updatePhone, response.phone());

        verify(contactRepository).findById(id);
    }

    @Test
    @DisplayName("Throw exception when contact to update is not found")
    void testThorwsExceptionWhenContactWithIdNotFound() {
        Long id = 345L;
        String updateName = "Abc Xyz";
        String updateEmail = "abc.xyz@gmail.com";
        String updatePhone = "9845123456";
        ContactUpdate update = new ContactUpdate(updateName, updateEmail, updatePhone);
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        String exceptionMessage = assertThrows(ContactNotFoundException.class,
                () -> contactInfoService.updateContact(id, update)).getMessage();

        assertEquals(String.format("Could not find contact with id '%d'", id), exceptionMessage);

        verify(contactRepository).findById(id);
    }

    @Test
    @DisplayName("Don't update anything when null value passed in update object")
    void testNothingUpdatedWhenNullValuesArePassed() {
        Long id = 345L;
        String name = "Xyz Abc";
        String email = "xyz.abc@gmail.com";
        String phone = "98765432";
        Contact contact = new Contact(id, name, email, phone);

        String updateName = null;
        String updateEmail = null;
        String updatePhone = null;
        ContactUpdate update = new ContactUpdate(updateName, updateEmail, updatePhone);
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        ContactResponse response = contactInfoService.updateContact(id, update);

        assertEquals(id, response.id());
        assertEquals(name, response.name());
        assertEquals(email, response.email());
        assertEquals(phone, response.phone());

        verify(contactRepository).findById(id);
    }

    @Test
    @DisplayName("Don't update anything when empty value passed in update object")
    void testNothingUpdatedWhenEmptyValuesArePassed() {
        Long id = 345L;
        String name = "Xyz Abc";
        String email = "xyz.abc@gmail.com";
        String phone = "98765432";
        Contact contact = new Contact(id, name, email, phone);

        String updateName = "";
        String updateEmail = "";
        String updatePhone = "";
        ContactUpdate update = new ContactUpdate(updateName, updateEmail, updatePhone);
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        ContactResponse response = contactInfoService.updateContact(id, update);

        assertEquals(id, response.id());
        assertEquals(name, response.name());
        assertEquals(email, response.email());
        assertEquals(phone, response.phone());

        verify(contactRepository).findById(id);
    }

    @Test
    @DisplayName("Successfully Update only Email and Phone Number")
    void testUpdateOnlyEmailAndPhone() {
        Long id = 345L;
        String name = "Xyz Abc";
        String email = "xyz.abc@gmail.com";
        String phone = "98765432";
        Contact contact = new Contact(id, name, email, phone);

        String updateEmail = "abc.xyz@gmail.com";
        String updatePhone = "9845123456";
        ContactUpdate update = new ContactUpdate(null, updateEmail, updatePhone);
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        ContactResponse response = contactInfoService.updateContact(id, update);

        assertEquals(id, response.id());
        assertEquals(contact.getName(), response.name());
        assertEquals(updateEmail, response.email());
        assertEquals(updatePhone, response.phone());

        verify(contactRepository).findById(id);
    }


}
