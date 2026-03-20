package com.rev.app.controller;

import com.rev.app.dto.MoneyRequestDTO;
import com.rev.app.entity.User;
import com.rev.app.service.IMoneyRequestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/money-request")
public class MoneyRequestController {

    @Autowired
    private IMoneyRequestService moneyRequestService;

    @GetMapping("/manage")
    public String showMoneyRequestsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<MoneyRequestDTO> incoming = moneyRequestService.getIncomingRequests(user.getId());
        List<MoneyRequestDTO> outgoing = moneyRequestService.getOutgoingRequests(user.getId());

        model.addAttribute("incomingRequests", incoming);
        model.addAttribute("outgoingRequests", outgoing);
        model.addAttribute("user", user);
        return "money_requests";
    }
}
