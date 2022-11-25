package com.codeseek.test.service;

import com.codeseek.test.dto.UserContactDTO;
import com.codeseek.test.entity.Contact;
import com.codeseek.test.entity.User;
import com.codeseek.test.exception.OAuth2AuthenticationProcessingException;
import com.codeseek.test.exception.ResourceNotFoundException;
import com.codeseek.test.exception.UserAlreadyExistAuthenticationException;
import com.codeseek.test.model.GoogleOAuth2User;
import com.codeseek.test.repository.UserRepository;
import com.codeseek.test.service.google.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ContactService contactService;

    private final GoogleService googleService;

    @Transactional
    public GoogleOAuth2User processGoogleUser(OAuth2AccessToken oAuth2AccessToken, Map<String, Object> attributes) {
        GoogleOAuth2User googleOAuth2User = new GoogleOAuth2User(attributes);
        if (!StringUtils.hasLength(googleOAuth2User.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (!StringUtils.hasLength(googleOAuth2User.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User user = Optional.ofNullable(userRepository.findByEmail(googleOAuth2User.getEmail())).orElse(new User());

        if (user.getId() == null) {
            if (userRepository.existsByEmail(googleOAuth2User.getEmail())) {
                throw new UserAlreadyExistAuthenticationException("User with email " + googleOAuth2User.getEmail() + " already exists");
            }

            user.setEmail(googleOAuth2User.getEmail());
            user.setProviderUserId(googleOAuth2User.getId());
        }
        user.setName(googleOAuth2User.getName());

        List<Contact> contactList = googleService.getUserContactsFromGoogle(oAuth2AccessToken);

        userRepository.save(user);
        contactService.saveOrUpdateUserContacts(user, contactList);

        return googleOAuth2User;
    }

    public List<UserContactDTO> getUserContacts(String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return contactService.getUserContacts(user)
                .stream()
                .map(contact -> new UserContactDTO(contact.getEmail(), contact.getName(), contact.getPhoneNumber()))
                .collect(Collectors.toList());
    }
}
