package me.subhas.contactinfo.web;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.springframework.test.web.servlet.ResultActions;

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
    @DisplayName("When no contacts are present, returns empty contats, page number and size will be zero")
    void testEmptyResponse() throws Exception {
        when(contactInfoService.listContacts(anyInt(),anyInt())).thenReturn(new ContactListResponse(List.of(), 0,0));

        mockMvc.perform(get("/api/v1/contacts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.contacts", hasSize(0)))
        .andExpect(jsonPath("$.pageNumber").value(0))
        .andExpect(jsonPath("$.pageSize").value(0))
        .andDo(print());

        verify(contactInfoService).listContacts(0,5);
    }

    @Test
    @DisplayName("When contacts are present, successfully access contacts with default page number and size")
    void testContactsListInResponse() throws Exception {

        List<ContactResponse> contactResponses = List.of(
                new ContactResponse(1L, "Abc U", "abc.u@gmail.com", "1234561"),
                new ContactResponse(1L, "Bbc U", "bbc.u@gmail.com", "1234562"),
                new ContactResponse(1L, "Cbc U", "cbc.u@gmail.com", "1234563"),
                new ContactResponse(1L, "Dbc U", "dbc.u@gmail.com", "1234564"),
                new ContactResponse(1L, "Ebc U", "ebc.u@gmail.com", "1234565"));

        when(contactInfoService.listContacts(anyInt(), anyInt())).thenReturn(new ContactListResponse(contactResponses, 1, 5));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/contacts"));
        resultActions.andExpect(jsonPath("$.contacts", hasSize(contactResponses.size())));
        resultActions.andExpect(jsonPath("$.pageNumber").value(1));
        resultActions.andExpect(jsonPath("$.pageSize").value(contactResponses.size()));
        resultActions.andExpect(jsonPath("$.contacts[0].name").value("Abc U"));
        resultActions.andExpect(jsonPath("$.contacts[0].email").value("abc.u@gmail.com"));
        resultActions.andExpect(jsonPath("$.contacts[0].phone").value("1234561"));

        resultActions.andExpect(jsonPath("$.contacts[2].name").value("Cbc U"));
        resultActions.andExpect(jsonPath("$.contacts[2].email").value("cbc.u@gmail.com"));
        resultActions.andExpect(jsonPath("$.contacts[2].phone").value("1234563"));

        verify(contactInfoService).listContacts(0, 5);
    }

    @Test
    @DisplayName("When contacts are present, successfully return second page of contacts")
    void testContactsListSecondPageResponse() throws Exception {

        List<ContactResponse> contactResponses = List.of(
                new ContactResponse(1L, "Dbc U", "dbc.u@gmail.com", "1234564"),
                new ContactResponse(1L, "Ebc U", "ebc.u@gmail.com", "1234565"));

        when(contactInfoService.listContacts(anyInt(), anyInt())).thenReturn(new ContactListResponse(contactResponses, 2, 2));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/contacts?pageNumber=2&pageSize=3"));
        resultActions.andExpect(jsonPath("$.contacts", hasSize(contactResponses.size())));
        resultActions.andExpect(jsonPath("$.pageNumber").value(2));
        resultActions.andExpect(jsonPath("$.pageSize").value(contactResponses.size()));
        resultActions.andExpect(jsonPath("$.contacts[0].name").value("Dbc U"));
        resultActions.andExpect(jsonPath("$.contacts[0].email").value("dbc.u@gmail.com"));
        resultActions.andExpect(jsonPath("$.contacts[0].phone").value("1234564"));

        resultActions.andExpect(jsonPath("$.contacts[1].name").value("Ebc U"));
        resultActions.andExpect(jsonPath("$.contacts[1].email").value("ebc.u@gmail.com"));
        resultActions.andExpect(jsonPath("$.contacts[1].phone").value("1234565"));

        resultActions.andDo(print());

        verify(contactInfoService).listContacts(1, 3);
    }
}
