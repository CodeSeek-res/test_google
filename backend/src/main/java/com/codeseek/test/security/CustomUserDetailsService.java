package com.codeseek.test.security;

import com.codeseek.test.entity.User;
import com.codeseek.test.model.LocalUser;
import com.codeseek.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public LocalUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
        return new LocalUser(user.getProviderUserId(), user.getEmail(), user.getName());
    }
}
