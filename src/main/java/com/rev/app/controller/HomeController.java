package com.rev.app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping({"", "/", "/home"})
    public String showHomePage(HttpSession session) {
        if (session.getAttribute("user") != null) return "redirect:/dashboard";
        return "index"; 
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        if (session.getAttribute("user") != null) return "redirect:/dashboard";
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(HttpSession session) {
        if (session.getAttribute("user") != null) return "redirect:/dashboard";
        return "register";
    }

    @GetMapping("/admin")
    public String showAdminDashboard(HttpSession session) {
        // Simple role check for admin
        var user = (com.rev.app.entity.User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole().name())) return "redirect:/login";
        return "admin";
    }
}
