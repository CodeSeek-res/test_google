package com.codeseek.test.service;

import com.codeseek.test.entity.Contact;
import com.codeseek.test.entity.User;
import com.codeseek.test.repository.ContactsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService {

    private final ContactsRepository contactsRepository;

    public List<Contact> getUserContacts(User user) {
        return contactsRepository.findAllByUser(user);
    }

    @Transactional
    public void saveOrUpdateUserContacts(User user, List<Contact> contactList) {
        contactsRepository.deleteAllByUser(user);
        contactList.forEach(contact -> contact.setUser(user));
        contactsRepository.saveAll(contactList);
    }
}
