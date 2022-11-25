package com.codeseek.test.service.google;

import com.codeseek.test.entity.Contact;
import com.codeseek.test.exception.ContactsNotFoundException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GoogleService {

    public List<Contact> getUserContactsFromGoogle(OAuth2AccessToken oAuth2AccessToken) {
        List<Contact> contactList = new ArrayList<>();

        try {
            AccessToken accessToken = new AccessToken(oAuth2AccessToken.getTokenValue(),
                    Optional.ofNullable(oAuth2AccessToken.getExpiresAt()).map(Date::from).orElse(null));
            GoogleCredentials googleCredentials = new GoogleCredentials(accessToken);
            HttpCredentialsAdapter httpCredentialsAdapter = new HttpCredentialsAdapter(googleCredentials);

            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            PeopleService service =
                    new PeopleService.Builder(httpTransport, jsonFactory, httpCredentialsAdapter)
                            .setApplicationName("testProject")
                            .build();


            ListConnectionsResponse listConnectionsResponse = service.people().connections()
                    .list("people/me")
                    .setPersonFields("names,emailAddresses,phoneNumbers")
                    .execute();

            List<Person> connections = listConnectionsResponse.getConnections();
            if (!CollectionUtils.isEmpty(connections)) {
                for (Person person : connections) {
                    Contact contact = new Contact();
                    List<Name> names = person.getNames();

                    if (names != null && names.size() > 0) {
                        contact.setName(person.getNames().get(0).getDisplayName());
                    } else {
                        log.error("No names available for connection.");
                    }

                    List<EmailAddress> emailAddresses = person.getEmailAddresses();
                    if (emailAddresses != null && emailAddresses.size() > 0) {
                        contact.setEmail(person.getEmailAddresses().get(0).getValue());
                    } else {
                        log.error("No emails available for connection.");
                    }

                    List<PhoneNumber> phoneNumbers = person.getPhoneNumbers();
                    if (phoneNumbers != null && phoneNumbers.size() > 0) {
                        contact.setPhoneNumber(person.getPhoneNumbers().get(0).getValue());
                    } else {
                        log.error("No emails available for connection.");
                    }

                    contactList.add(contact);
                }
            } else {
                log.error("No connections found.");
            }

        } catch (Exception ex) {
            throw new ContactsNotFoundException(ex.getMessage(), ex.getCause());
        }
        return contactList;
    }

}
