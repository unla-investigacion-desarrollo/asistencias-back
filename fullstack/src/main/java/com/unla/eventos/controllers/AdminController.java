package com.unla.eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.User;
import com.unla.eventos.services.IUserService;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/list";
    }

    @GetMapping("/create")
    public String showCreateForm(User user) {
        return "admin/create";
    }

    @PostMapping("/create")
    public String createUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/create";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "admin/edit";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "admin/edit";
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
