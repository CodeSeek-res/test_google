package com.codeseek.test.repository;

import com.codeseek.test.entity.Contact;
import com.codeseek.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactsRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByUser(User user);

    void deleteAllByUser(User user);
}
