package com.rev.app.controller;

import com.rev.app.entity.User;
import com.rev.app.rest.BusinessAnalyticsRestController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BusinessAnalyticsRestController analyticsRestController;

    @GetMapping("/analytics")
    public String showAnalyticsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRole() != User.Role.BUSINESS && user.getRole() != User.Role.ADMIN)) {
            return "redirect:/dashboard";
        }

        var analytics = analyticsRestController.getBusinessSummary(user.getId()).getBody();
        if (analytics != null) {
            model.addAllAttributes(analytics);
        }
        model.addAttribute("user", user);
        return "business-dashboard";
    }
}
