package com.codeseek.test.controller;

import com.codeseek.test.dto.UserContactDTO;
import com.codeseek.test.dto.UserInfoDTO;
import com.codeseek.test.model.LocalUser;
import com.codeseek.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserInfoDTO getCurrentUser(@AuthenticationPrincipal LocalUser localUser) {
        return new UserInfoDTO(localUser.getName(), localUser.getEmail());
    }

    @GetMapping("/me/contacts")
    public List<UserContactDTO> getUserContacts(@AuthenticationPrincipal LocalUser localUser) {
        return userService.getUserContacts(localUser.getEmail());
    }
}
