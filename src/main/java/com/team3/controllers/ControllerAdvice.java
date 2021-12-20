package com.team3.controllers;

import com.team3.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    private final UserService userService;

    public ControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public String getUser(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserByEmail(authentication.getName()).getName();
        } else return "anonim";
    }
}
