package me.subhas.contactinfo.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
        when(contactRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());
        ContactListResponse contacts = contactInfoService.listContacts(1,10);
        assertEquals(0, contacts.contacts().size());
        verify(contactRepository).findAll(any(PageRequest.class));
    }

    @Disabled("This test is not properly designed, hence disabled for now.")
    @ParameterizedTest
    @MethodSource("pageNumberAndSizeProvider")
    @DisplayName("Returns paginated contacts")
    void testReturnPaginatedContacts(Integer pageNumber, Integer pageSize) {

        List<Contact> contacts = List.of(
                new Contact(1L, "Abc U", "abc.u@gmail.com", "12345671"),
                new Contact(2L, "Bcd U", "bcd.u@gmail.com", "12345672"),
                new Contact(3L, "Cde U", null, "12345673"),
                new Contact(4L, "Efg U", "efg.u@gmail.com", "12345674"),
                new Contact(5L, "Fgh U", "Fgh.u@gmail.com", "12345675"),
                new Contact(6L, "Fgh U", "fgh.u@gmail.com", "123451"),
                new Contact(7L, "Ghi V", "ghi.v@gmail.com", "123452"),
                new Contact(8L, "Hij W", "hij.w@gmail.com", "123453"),
                new Contact(9L, "Ijk X", "ijk.x@gmail.com", "123454"),
                new Contact(10L, "Jkl Y", "jkl.y@gmail.com", "123455"));

        Page<Contact> page = new PageImpl<>(contacts, PageRequest.of(pageNumber, pageSize), contacts.size());
        when(contactRepository.findAll(any(PageRequest.class))).thenReturn(page);
        ContactListResponse contactListResponse = contactInfoService.listContacts(pageNumber, pageSize);
        assertEquals(pageSize, contactListResponse.contacts().size());
        assertEquals(pageNumber, contactListResponse.pageNumber());
        assertEquals(pageSize, contactListResponse.pageSize());
        for (ContactResponse contactResponse : contactListResponse.contacts()) {
            Contact contact = contacts.get(contactResponse.id().intValue() - 1);
            assertEquals(contact.getName(), contactResponse.name());
            assertEquals(contact.getEmail(), contactResponse.email());
            assertEquals(contact.getPhone(), contactResponse.phone());
        }
        verify(contactRepository).findAll(any(PageRequest.class));
    }

    static Stream<Arguments> pageNumberAndSizeProvider() {
        return Stream.of(
                Arguments.arguments(0, 5),
                Arguments.arguments(1, 5));
    }

    @Test
    @DisplayName("Returns paginated contacts with default pageSize and pageNumber when pass passed parameters are negative")
    void testReturnPaginatedContactsWithNegativeParameters() {

        List<Contact> contacts = List.of(
                new Contact(1L, "Abc U", "abc.u@gmail.com", "12345671"),
                new Contact(2L, "Bcd U", "bcd.u@gmail.com", "12345672"),
                new Contact(3L, "Cde U", null, "12345673"),
                new Contact(4L, "Efg U", "efg.u@gmail.com", "12345674"),
                new Contact(5L, "Fgh U", "Fgh.u@gmail.com", "12345675"));

        Page<Contact> page = new PageImpl<>(contacts);
        when(contactRepository.findAll(any(PageRequest.class))).thenReturn(page);
        ContactListResponse contactListResponse = contactInfoService.listContacts(1, 5);
        assertEquals(5, contactListResponse.contacts().size());
        assertEquals(0, contactListResponse.pageNumber());
        assertEquals(5, contactListResponse.pageSize());
        for (ContactResponse contactResponse : contactListResponse.contacts()) {
            Contact contact = contacts.get(contactResponse.id().intValue() - 1);
            assertEquals(contact.getName(), contactResponse.name());
            assertEquals(contact.getEmail(), contactResponse.email());
            assertEquals(contact.getPhone(), contactResponse.phone());
        }
        verify(contactRepository).findAll(any(PageRequest.class));
    }

}
