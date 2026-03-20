package com.rev.app.controller;

import com.rev.app.entity.User;
import com.rev.app.service.IPaymentMethodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment-method")
public class PaymentMethodController {

    @Autowired
    private IPaymentMethodService paymentMethodService;

    @GetMapping("/manage")
    public String showPaymentMethodsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("paymentMethods", paymentMethodService.getPaymentMethodsByUserId(user.getId()));
        return "payment_methods";
    }
}
