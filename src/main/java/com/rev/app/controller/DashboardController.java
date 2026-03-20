package com.rev.app.controller;

import com.rev.app.entity.User;
import com.rev.app.service.*;
import com.rev.app.rest.BusinessAnalyticsRestController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.rev.app.repository.IUserRepository;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final IWalletService walletService;
    private final ITransactionService transactionService;
    private final INotificationService notificationService;
    private final IMoneyRequestService moneyRequestService;
    private final BusinessAnalyticsRestController businessAnalyticsRestController;
    private final IPaymentMethodService paymentMethodService;
    private final IUserRepository userRepository;

    @Autowired
    public DashboardController(IWalletService walletService, 
                               ITransactionService transactionService,
                               INotificationService notificationService,
                               IMoneyRequestService moneyRequestService,
                               BusinessAnalyticsRestController businessAnalyticsRestController,
                               IPaymentMethodService paymentMethodService,
                               IUserRepository userRepository) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.notificationService = notificationService;
        this.moneyRequestService = moneyRequestService;
        this.businessAnalyticsRestController = businessAnalyticsRestController;
        this.paymentMethodService = paymentMethodService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        
        try {
            model.addAttribute("walletBalance", walletService.getWalletByUserId(user.getId()).getBalance());
        } catch (com.rev.app.exception.ResourceNotFoundException e) {
            // Retroactively provision empty wallet for older admins or default admin
            if (user.getRole() == User.Role.ADMIN) {
                walletService.createWallet(user.getId());
                model.addAttribute("walletBalance", java.math.BigDecimal.ZERO);
                
                // Also retroactively patch a transaction PIN if missing
                if (user.getTransactionPin() == null || user.getTransactionPin().isEmpty()) {
                    User dbUser = userRepository.findById(user.getId()).orElse(null);
                    if (dbUser != null) {
                        dbUser.setTransactionPin(dbUser.getPassword());
                        userRepository.save(dbUser);
                        user.setTransactionPin(dbUser.getPassword());
                        session.setAttribute("user", user); // Update session with new PIN
                    }
                }
            } else {
                model.addAttribute("walletBalance", java.math.BigDecimal.ZERO);
            }
        }
        model.addAttribute("recentTransactions", transactionService.getTransactionHistory(user.getId()).stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("notificationCount", notificationService.getUnreadNotifications(user.getId()).size());
        
        // Use IUserService or IMoneyRequestService directly ideally, but can check size
        // model.addAttribute("pendingRequestsCount", moneyRequestService.getIncomingRequests(user.getId()).size());

        if (user.getRole() == User.Role.BUSINESS || user.getRole() == User.Role.ADMIN) {
            var analytics = businessAnalyticsRestController.getBusinessSummary(user.getId()).getBody();
            model.addAllAttributes(analytics);
        }

        return "dashboard"; 
    }

    @GetMapping("/business")
    public String showBusinessDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRole() != User.Role.BUSINESS && user.getRole() != User.Role.ADMIN)) {
            return "redirect:/dashboard";
        }

        var analytics = businessAnalyticsRestController.getBusinessSummary(user.getId()).getBody();
        model.addAllAttributes(analytics);
        model.addAttribute("user", user);

        return "business-dashboard";
    }



}
