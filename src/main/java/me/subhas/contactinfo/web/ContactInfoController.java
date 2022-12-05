package me.subhas.contactinfo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import me.subhas.contactinfo.business.ContactInfoService;
import me.subhas.contactinfo.business.model.ContactCreate;
import me.subhas.contactinfo.business.model.ContactListResponse;
import me.subhas.contactinfo.business.model.ContactResponse;
import me.subhas.contactinfo.web.exception.IllegalIdException;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactInfoController {

    @Autowired
    private ContactInfoService contactInfoService;

    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@Valid @RequestBody ContactCreate contactCreate) {
        ContactResponse response = contactInfoService.createContact(contactCreate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ContactListResponse> listContacts() {
        return ResponseEntity.ok().body(contactInfoService.listContacts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> retrieveContact(@PathVariable(name = "id") String id) {
        try {
            return ResponseEntity.ok().body(contactInfoService.retrieveContact(Long.valueOf(id)));
        } catch(NumberFormatException ex) {
            throw new IllegalIdException(String.format("'id' must be a valid number, provided '%s'", id));
        }
    }

}
