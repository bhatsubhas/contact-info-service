package me.subhas.contactinfo.web;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import me.subhas.contactinfo.business.impl.ContactInfoServiceImpl;

@WebMvcTest
class DeleteContactControllerTests {

    @MockBean
    private ContactInfoServiceImpl contactInfoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Successfully delete a Contact")
    void testRetrieveContactSuccessfully() throws Exception {
        Long id = 456L;
        mockMvc.perform(delete(String.format("/api/v1/contacts/%d", id)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(contactInfoService).deleteContact(id);
    }

    @Test
    @DisplayName("Fail when id is not a number")
    void testFailureWhenIdIsNotNumber() throws Exception {

        String id = "abc";
        mockMvc.perform(delete(String.format("/api/v1/contacts/%s", id)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(String.format("'id' must be a valid number, provided '%s'", id)))
                .andDo(print());

        verify(contactInfoService, times(0)).deleteContact(anyLong());
    }
}
