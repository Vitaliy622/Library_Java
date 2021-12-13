package com.team3.controllers;

import com.team3.entity.User;
import com.team3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/registration")
public class RegistrationController {

    private final UserService userService;
    private static final String REGISTRATION_FORM = "users/registration";

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return REGISTRATION_FORM;
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return REGISTRATION_FORM;
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Password is not equals!");
            return REGISTRATION_FORM;
        }
        if (!userService.addUser(user)) {
            model.addAttribute("emailError", "User already exist!");
            return REGISTRATION_FORM;
        }
        return "redirect:/login";
    }
}