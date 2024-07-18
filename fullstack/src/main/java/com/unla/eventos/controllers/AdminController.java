package com.unla.eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.User;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IUserService;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return ViewRouteHelper.ADMIN_INDEX;
    }

    @GetMapping("/create")
    public String showCreateForm(User user, Model model) {
        model.addAttribute("roles", userService.getAllRoles());
        return ViewRouteHelper.ADMIN_CREATE;
    }

    @PostMapping("/create")
    public String createUser(@Valid User user, BindingResult result, @RequestParam String role, Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("roles", userService.getAllRoles());
            return ViewRouteHelper.ADMIN_CREATE;
        }
        try {
            userService.save(user, role);
		} catch (Exception e) {
			//TODO: Add errors on view
		}
        return ViewRouteHelper.REDIRECT_ADMIN_USERS_CRUD;
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
        	model.addAttribute("roles", userService.getAllRoles());
            model.addAttribute("user", user.get());
            return ViewRouteHelper.ADMIN_EDIT;
        } else {
            return ViewRouteHelper.REDIRECT_ADMIN_USERS_CRUD;
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid User user, BindingResult result, @RequestParam String role, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
        	model.addAttribute("roles", userService.getAllRoles());
            return ViewRouteHelper.ADMIN_EDIT;
        }
        try {
            userService.save(user, role);
		} catch (Exception e) {
			//TODO: Add errors on view
		}
        return ViewRouteHelper.REDIRECT_ADMIN_USERS_CRUD;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
    	try {
            userService.deleteById(id);
		} catch (Exception e) {
			//TODO: Add errors on view
		}
        return ViewRouteHelper.REDIRECT_ADMIN_USERS_CRUD;
    }
}
