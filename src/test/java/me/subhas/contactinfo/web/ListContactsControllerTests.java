package me.subhas.contactinfo.web;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import me.subhas.contactinfo.business.impl.ContactInfoServiceImpl;
import me.subhas.contactinfo.business.model.ContactListResponse;
import me.subhas.contactinfo.business.model.ContactResponse;

@WebMvcTest
class ListContactsControllerTests {

    @MockBean
    private ContactInfoServiceImpl contactInfoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Empty JSON Array when no contacts are present")
    void testEmptyResponse() throws Exception {
        when(contactInfoService.listContacts()).thenReturn(new ContactListResponse(List.of()));

        mockMvc.perform(get("/api/v1/contacts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.contacts", hasSize(0))).andDo(print());

        verify(contactInfoService).listContacts();
    }

    @Test
    @DisplayName("When contacts are present, JSON Array of contacts is returned")
    void testContactsListInResponse() throws Exception {

        List<ContactResponse> contactResponses = List.of(
                new ContactResponse(1L, "Abc U", "abc.u@gmail.com", "1234561"),
                new ContactResponse(1L, "Bbc U", "bbc.u@gmail.com", "1234562"),
                new ContactResponse(1L, "Cbc U", "cbc.u@gmail.com", "1234563"),
                new ContactResponse(1L, "Dbc U", "dbc.u@gmail.com", "1234564"),
                new ContactResponse(1L, "Ebc U", "ebc.u@gmail.com", "1234565"));

        when(contactInfoService.listContacts()).thenReturn(new ContactListResponse(contactResponses));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/contacts"));
        resultActions.andExpect(jsonPath("$.contacts", hasSize(contactResponses.size())));
        resultActions.andExpect(jsonPath("$.contacts[0].name").value("Abc U"));
        resultActions.andExpect(jsonPath("$.contacts[0].email").value("abc.u@gmail.com"));
        resultActions.andExpect(jsonPath("$.contacts[0].phone").value("1234561"));

        resultActions.andExpect(jsonPath("$.contacts[2].name").value("Cbc U"));
        resultActions.andExpect(jsonPath("$.contacts[2].email").value("cbc.u@gmail.com"));
        resultActions.andExpect(jsonPath("$.contacts[2].phone").value("1234563"));

        verify(contactInfoService).listContacts();
    }
}
