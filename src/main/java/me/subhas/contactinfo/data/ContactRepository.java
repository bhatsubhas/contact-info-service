package me.subhas.contactinfo.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.subhas.contactinfo.data.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByName(String name);
}
