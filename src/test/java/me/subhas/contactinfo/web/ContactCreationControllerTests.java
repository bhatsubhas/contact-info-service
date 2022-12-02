package me.subhas.contactinfo.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import me.subhas.contactinfo.business.exception.DuplicateContactNameException;
import me.subhas.contactinfo.business.impl.ContactInfoServiceImpl;
import me.subhas.contactinfo.business.model.ContactCreate;
import me.subhas.contactinfo.business.model.ContactResponse;

@WebMvcTest
class ContactCreationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactInfoServiceImpl contactInfoService;

    @Test
    @DisplayName("Create Contact when all the details are provdied")
    void testCreateContactSuccessfully() throws Exception {
        long id = 1L;
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";
        String phone = "123456";
        ContactResponse response = new ContactResponse(id, name, email, phone);
        when(contactInfoService.createContact(any(ContactCreate.class))).thenReturn(response);

        StringBuilder jsonContact = new StringBuilder();
        jsonContact.append("{ ");
        jsonContact.append("\"name\":").append("\"").append(name).append("\",");
        jsonContact.append("\"email\":").append("\"").append(email).append("\",");
        jsonContact.append("\"phone\":").append("\"").append(phone).append("\"");
        jsonContact.append(" }");

        mockMvc.perform(
                post("/api/v1/contacts").contentType(APPLICATION_JSON).content(jsonContact.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.phone").value(phone))
                .andDo(print());

        verify(contactInfoService).createContact(any(ContactCreate.class));
    }

    @Test
    @DisplayName("Create Contact when email is not provdied")
    void testCreateContactEmailNotRequired() throws Exception {
        long id = 1L;
        String name = "Abc Xyz";
        String email = null;
        String phone = "123456";
        ContactResponse response = new ContactResponse(id, name, email, phone);
        when(contactInfoService.createContact(any(ContactCreate.class))).thenReturn(response);

        StringBuilder jsonContact = new StringBuilder();
        jsonContact.append("{ ");
        jsonContact.append("\"name\":").append("\"").append(name).append("\",");
        jsonContact.append("\"phone\":").append("\"").append(phone).append("\"");
        jsonContact.append(" }");

        mockMvc.perform(
                post("/api/v1/contacts").contentType(APPLICATION_JSON).content(jsonContact.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").isEmpty())
                .andExpect(jsonPath("$.phone").value(phone))
                .andDo(print());

        verify(contactInfoService).createContact(any(ContactCreate.class));
    }

    @Test
    @DisplayName("Cannot create Contact when name is not provdied")
    void testCannotCreateContactWhenNameNotProvided() throws Exception {
        String phone = "123456";

        StringBuilder jsonContact = new StringBuilder();
        jsonContact.append("{ ");
        jsonContact.append("\"phone\":").append("\"").append(phone).append("\"");
        jsonContact.append(" }");

        mockMvc.perform(
                post("/api/v1/contacts").contentType(APPLICATION_JSON).content(jsonContact.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("'name' field is mandatory"))
                .andDo(print());

        verify(contactInfoService, times(0)).createContact(any(ContactCreate.class));
    }

    @Test
    @DisplayName("Cannot create Contact when phone is not provdied")
    void testCannotCreateContactWhenPhoneNotProvided() throws Exception {
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";

        StringBuilder jsonContact = new StringBuilder();
        jsonContact.append("{ ");
        jsonContact.append("\"name\":").append("\"").append(name).append("\",");
        jsonContact.append("\"email\":").append("\"").append(email).append("\"");
        jsonContact.append(" }");

        mockMvc.perform(
                post("/api/v1/contacts").contentType(APPLICATION_JSON).content(jsonContact.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("'phone' field is mandatory"))
                .andDo(print());

        verify(contactInfoService, times(0)).createContact(any(ContactCreate.class));
    }

    @Test
    @DisplayName("Cannot create Contact when only email is provdied")
    void testCannotCreateContactWhenOnlyEmailProvided() throws Exception {
        String email = "abc.xyz@gmail.com";

        StringBuilder jsonContact = new StringBuilder();
        jsonContact.append("{ ");
        jsonContact.append("\"phone\":").append("\"").append(email).append("\"");
        jsonContact.append(" }");

        mockMvc.perform(
                post("/api/v1/contacts").contentType(APPLICATION_JSON).content(jsonContact.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("'name' field is mandatory"))
                .andDo(print());
        verify(contactInfoService, times(0)).createContact(any(ContactCreate.class));
    }

    @Test
    @DisplayName("Do create Contact when already a contact with same name present")
    void testCannotCreateWhenNameIsDuplicate() throws Exception {
        String name = "Abc Xyz";
        String email = "abc.xyz@gmail.com";
        String phone = "123456";
        String errorMessage = String.format("Contact with with name '%s' already present", name);
        doThrow(new DuplicateContactNameException(errorMessage))
                .when(contactInfoService).createContact(any(ContactCreate.class));

        StringBuilder jsonContact = new StringBuilder();
        jsonContact.append("{ ");
        jsonContact.append("\"name\":").append("\"").append(name).append("\",");
        jsonContact.append("\"email\":").append("\"").append(email).append("\",");
        jsonContact.append("\"phone\":").append("\"").append(phone).append("\"");
        jsonContact.append(" }");

        mockMvc.perform(
                post("/api/v1/contacts").contentType(APPLICATION_JSON).content(jsonContact.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                .andDo(print());

        verify(contactInfoService).createContact(any(ContactCreate.class));
    }
}
