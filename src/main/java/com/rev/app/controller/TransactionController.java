package com.rev.app.controller;

import com.rev.app.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private com.rev.app.service.IPaymentMethodService paymentMethodService;


    @GetMapping("/send")
    public String showSendMoneyPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("paymentMethods", paymentMethodService.getPaymentMethodsByUserId(user.getId()));
        return "send_money";
    }

    @GetMapping("/requests")
    public String showMoneyRequestsPage() {
        return "redirect:/money-request/manage";
    }
}
