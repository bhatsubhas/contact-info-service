package me.subhas.contactinfo.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import me.subhas.contactinfo.business.model.ContactResponse;
import me.subhas.contactinfo.business.model.ContactUpdate;

@WebMvcTest
class UpdateContactControllerTests {
    @MockBean
    private ContactInfoServiceImpl contactInfoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Success when name, email and phone are requested for update")
    void testSuccessWhenNameEmailAndPhoneIsUpdated() throws Exception {
        Long id = 343L;
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";
        String phone = "123456";

        ContactResponse response = new ContactResponse(id, name, email, phone);
        when(contactInfoService.updateContact(anyLong(), any(ContactUpdate.class))).thenReturn(response);

        StringBuilder requestBodyBuilder = new StringBuilder();
        requestBodyBuilder.append("{ ");
        requestBodyBuilder.append("\"name\":").append("\"").append(name).append("\",");
        requestBodyBuilder.append("\"email\":").append("\"").append(email).append("\",");
        requestBodyBuilder.append("\"phone\":").append("\"").append(phone).append("\"");
        requestBodyBuilder.append(" }");

        mockMvc.perform(put(String.format("/api/v1/contacts/%d", id)).contentType(APPLICATION_JSON)
                .content(requestBodyBuilder.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.phone").value(phone))
                .andDo(print());

        verify(contactInfoService).updateContact(anyLong(), any(ContactUpdate.class));

    }

    @Test
    @DisplayName("Bad request when no request body is provided for update")
    void testBadReqeustWhenNoRequestBody() throws Exception {
        Long id = 343L;
        mockMvc.perform(put(String.format("/api/v1/contacts/%d", id)).contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Success when only email is requested for update ")
    void testSuccessWhenRequestBodyHasOnlyEmail() throws Exception {
        Long id = 343L;
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";
        String phone = "123456";

        ContactResponse response = new ContactResponse(id, name, email, phone);
        when(contactInfoService.updateContact(anyLong(), any(ContactUpdate.class))).thenReturn(response);

        StringBuilder requestBodyBuilder = new StringBuilder();
        requestBodyBuilder.append("{ ");
        requestBodyBuilder.append("\"email\":").append("\"").append(email).append("\"");
        requestBodyBuilder.append(" }");

        mockMvc.perform(put(String.format("/api/v1/contacts/%d", id)).contentType(APPLICATION_JSON)
                .content(requestBodyBuilder.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.phone").value(phone))
                .andDo(print());

        verify(contactInfoService).updateContact(anyLong(), any(ContactUpdate.class));
    }
}
