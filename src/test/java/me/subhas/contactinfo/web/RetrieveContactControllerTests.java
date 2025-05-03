package me.subhas.contactinfo.web;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import me.subhas.contactinfo.business.exception.ContactNotFoundException;
import me.subhas.contactinfo.business.impl.ContactInfoServiceImpl;
import me.subhas.contactinfo.business.model.ContactResponse;

@WebMvcTest
class RetrieveContactControllerTests {

    @MockitoBean
    private ContactInfoServiceImpl contactInfoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Successfully retrieve a Contact")
    void testRetrieveContactSuccessfully() throws Exception {
        Long id = 456L;
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";
        String phone = "123456";
        ContactResponse response = new ContactResponse(id, name, email, phone);
        when(contactInfoService.retrieveContact(id)).thenReturn(response);

        mockMvc.perform(get(String.format("/api/v1/contacts/%d", id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.phone").value(phone))
                .andDo(print());

        verify(contactInfoService).retrieveContact(id);
    }

    @Test
    @DisplayName("Return Not Found with error message when Contact with specified id not found")
    void testNotFoundMessageWhenContactWithIdNotPresent() throws Exception {
        Long id = 456L;

        when(contactInfoService.retrieveContact(id))
                .thenThrow(new ContactNotFoundException(String.format("Could not find contact with id '%d'", id)));

        mockMvc.perform(get(String.format("/api/v1/contacts/%d", id)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(String.format("Could not find contact with id '%d'", id)))
                .andDo(print());

        verify(contactInfoService).retrieveContact(id);
    }

    @Test
    @DisplayName("Fail when id is not a number")
    void testFailureWhenIdIsNotNumber() throws Exception {

        String id = "abc";
        mockMvc.perform(get(String.format("/api/v1/contacts/%s", id)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage")
                        .value(String.format("'id' must be a valid number, provided '%s'", id)))
                .andDo(print());

        verify(contactInfoService, times(0)).retrieveContact(anyLong());
    }
}
