package me.subhas.contactinfo.business;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import me.subhas.contactinfo.business.impl.ContactInfoServiceImpl;
import me.subhas.contactinfo.data.ContactRepository;

@ExtendWith(MockitoExtension.class)
class DeleteContactServiceTests {
    @Mock
    private ContactRepository contactRepository;

    @Autowired
    @InjectMocks
    private ContactInfoServiceImpl contactInfoService;

    @Test
    @DisplayName("Successfully delete a contact when contact id is known")
    void testRetrieveContactSuccessfully() {
        Long id = 235L;
        when(contactRepository.existsById(id)).thenReturn(true);
        contactInfoService.deleteContact(id);
        verify(contactRepository).existsById(id);
        verify(contactRepository).deleteById(id);
    }

    @Test
    @DisplayName("Return without deleting when contact with id does not exists")
    void testReturnSilentlyWhenContacNotFound() {
        Long id = 235L;
        when(contactRepository.existsById(id)).thenReturn(false);
        contactInfoService.deleteContact(id);
        verify(contactRepository).existsById(id);
        verify(contactRepository, times(0)).deleteById(id);
    }
}
