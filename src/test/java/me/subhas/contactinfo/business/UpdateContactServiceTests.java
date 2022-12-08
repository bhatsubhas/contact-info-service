package me.subhas.contactinfo.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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

import me.subhas.contactinfo.business.exception.ContactNotFoundException;
import me.subhas.contactinfo.business.exception.DuplicateContactNameException;
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

    @Test
    @DisplayName("Throw exception when name to be udpated is already used")
    void testThrowsExceptionWhenUpdateNameIsAlreadyUsed() {
        Long existingId = 345L;
        String name = "Abc Xyz";
        String email = "xyz.abc@gmail.com";
        String phone = "98765432";
        Contact contact = new Contact(existingId, name, email, phone);

        Long updateId = 343L;
        String updateName = "Abc Xyz";
        ContactUpdate update = new ContactUpdate(updateName, null, null);
        when(contactRepository.findByName(updateName)).thenReturn(Optional.of(contact));

        String exceptionMessage = assertThrows(DuplicateContactNameException.class,
                () -> contactInfoService.updateContact(updateId, update)).getMessage();

        assertEquals(String.format("Contact with with name '%s' already used", updateName), exceptionMessage);

        verify(contactRepository).findByName(updateName);
        verify(contactRepository, times(0)).findById(updateId);

    }

    @Test
    @DisplayName("Nothing will be updated when Name, Email and Phone Number are same as before")
    void testNothingToUpdateWhenSameNameEmailAndPhoneIsPassed() {
        Long id = 345L;
        String name = "Xyz Abc";
        String email = "xyz.abc@gmail.com";
        String phone = "98765432";
        Contact contact = mock(Contact.class);
        when(contact.getId()).thenReturn(id);
        when(contact.getName()).thenReturn(name);
        when(contact.getEmail()).thenReturn(email);
        when(contact.getPhone()).thenReturn(phone);
        when(contactRepository.findByName(name)).thenReturn(Optional.of(contact));
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        ContactUpdate update = new ContactUpdate(name, email, phone);

        ContactResponse response = contactInfoService.updateContact(id, update);

        assertEquals(id, response.id());
        assertEquals(name, response.name());
        assertEquals(email, response.email());
        assertEquals(phone, response.phone());

        verify(contactRepository).findById(id);
        verify(contact, times(2)).getName();
        verify(contact, times(2)).getEmail();
        verify(contact, times(2)).getPhone();
        verify(contact, times(0)).setName(anyString());
        verify(contact, times(0)).setEmail(anyString());
        verify(contact, times(0)).setPhone(anyString());
    }

}
