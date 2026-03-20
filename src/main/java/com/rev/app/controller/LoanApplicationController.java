package com.rev.app.controller;

import com.rev.app.dto.LoanApplicationDTO;
import com.rev.app.entity.User;
import com.rev.app.service.ILoanApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/loan")
public class LoanApplicationController {

    @Autowired
    private ILoanApplicationService loanService;

    @GetMapping("/apply")
    public String showLoanApplicationPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"BUSINESS".equals(user.getRole().name()) && !"ADMIN".equals(user.getRole().name())) {
            return "redirect:/dashboard";
        }
        return "apply_loan";
    }

    @GetMapping("/status")
    public String showLoanStatusPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"BUSINESS".equals(user.getRole().name()) && !"ADMIN".equals(user.getRole().name())) {
            return "redirect:/dashboard";
        }

        List<LoanApplicationDTO> loans = loanService.getLoanApplicationsByBusinessUserId(user.getId());
        model.addAttribute("loans", loans);
        return "loan_status";
    }
}
