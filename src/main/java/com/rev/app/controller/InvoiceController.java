package com.rev.app.controller;

import com.rev.app.dto.InvoiceDTO;
import com.rev.app.entity.User;
import com.rev.app.service.IInvoiceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private IInvoiceService invoiceService;

    @GetMapping("/manage")
    public String showInvoicesPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"BUSINESS".equals(user.getRole().name()) && !"ADMIN".equals(user.getRole().name())) {
            return "redirect:/dashboard";
        }

        List<InvoiceDTO> invoices = invoiceService.getInvoicesByBusinessUserId(user.getId());
        model.addAttribute("invoices", invoices);
        return "invoices";
    }

    @GetMapping("/create")
    public String showCreateInvoicePage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!"BUSINESS".equals(user.getRole().name()) && !"ADMIN".equals(user.getRole().name())) {
            return "redirect:/dashboard";
        }
        return "create_invoice";
    }
}
