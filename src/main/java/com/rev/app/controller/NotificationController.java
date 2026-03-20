package com.rev.app.controller;

import com.rev.app.dto.NotificationDTO;
import com.rev.app.entity.User;
import com.rev.app.service.INotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @GetMapping
    public String showNotificationsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<NotificationDTO> notifications = notificationService.getUserNotifications(user.getId());
        model.addAttribute("notifications", notifications);
        model.addAttribute("user", user);
        return "notifications";
    }
}
