package com.unla.eventos.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.unla.eventos.helpers.ViewRouteHelper;

import jakarta.servlet.http.HttpSession;


@Controller
public class UserAccessController {

	@GetMapping("/login")
	public String login(Model model, HttpSession session) {
		String loginError = (String) session.getAttribute("loginError");
		if (loginError != null) {
			model.addAttribute("loginError", loginError);
			session.removeAttribute("loginError");
		}
		return ViewRouteHelper.USER_LOGIN;
	}

	@GetMapping("/loginsuccess")
	public String loginCheck(Model model) {
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			model.addAttribute("username", user.getUsername());
		} catch (Exception e) {
			// not authenticated
		}
		return ViewRouteHelper.INDEX;
	}
}
