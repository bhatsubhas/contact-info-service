package me.subhas.contactinfo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.subhas.contactinfo.data.ContactRepository;
import me.subhas.contactinfo.data.entity.Contact;

@Configuration
public class ContactInfoConfig {

    @Bean
    CommandLineRunner seedContacts(ContactRepository contactRepository) {
        return args -> {
            contactRepository.save(new Contact("Abc U", "abc.u@gmail.com", "123456"));
            contactRepository.save(new Contact("Bcd V", "bcd.v@gmail.com", "123457"));
            contactRepository.save(new Contact("Cde W", "cde.w@gmail.com", "123458"));
            contactRepository.save(new Contact("Def X", "def.x@gmail.com", "123459"));
            contactRepository.save(new Contact("Efg Y", "efg.y@gmail.com", "123450"));
        };
    }
}
