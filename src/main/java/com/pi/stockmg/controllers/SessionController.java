package com.pi.stockmg.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SessionController {
    private static final String STATIC_USERNAME = "wedamossab@gmail.com";
    private static final String STATIC_PASSWORD = "123456";

    @GetMapping("/signin")
    public ModelAndView signInPage() {
        return new ModelAndView("signin");
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        if (STATIC_USERNAME.equals(username) && STATIC_PASSWORD.equals(password)) {
            // Stocker l'utilisateur connecté dans la session
            request.getSession().setAttribute("authenticatedUser", STATIC_USERNAME);
            return "redirect:/produits";
        } else {
            return "redirect:/signin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/signin";
    }
}


