package com.rev.app.controller;

import com.rev.app.entity.User;
import com.rev.app.service.IPaymentMethodService;
import com.rev.app.service.IWalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    private final IWalletService walletService;
    private final IPaymentMethodService paymentMethodService;

    @Autowired
    public WalletController(IWalletService walletService, IPaymentMethodService paymentMethodService) {
        this.walletService = walletService;
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public String showWalletPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("walletBalance", walletService.getWalletByUserId(user.getId()).getBalance());
        model.addAttribute("paymentMethods", paymentMethodService.getPaymentMethodsByUserId(user.getId()));
        
        return "wallet";
    }
}
