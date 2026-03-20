package com.rev.app.controller;

import com.rev.app.entity.Transaction;
import com.rev.app.entity.User;
import com.rev.app.service.ITransactionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping
    public String showHistory(HttpSession session, Model model,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal amountMin,
            @RequestParam(required = false) BigDecimal amountMax) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        if ((dateFrom == null || dateFrom.isBlank()) && date != null && !date.isBlank()) {
            dateFrom = date;
        }
        if ((dateTo == null || dateTo.isBlank()) && date != null && !date.isBlank()) {
            dateTo = date;
        }

        List<Transaction> transactions = user.getRole() == User.Role.ADMIN
                ? transactionService.getAllTransactions()
                : transactionService.getTransactionHistory(user.getId());

        transactions = applyFilters(transactions, type, search, dateFrom, dateTo, status, amountMin, amountMax);

        model.addAttribute("activePage", "history");
        model.addAttribute("transactions", transactions);
        model.addAttribute("isAdmin", user.getRole() == User.Role.ADMIN);
        model.addAttribute("currentType", type);
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentDateFrom", dateFrom);
        model.addAttribute("currentDateTo", dateTo);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentAmountMin", amountMin);
        model.addAttribute("currentAmountMax", amountMax);

        return "history/index";
    }

    @GetMapping("/export/csv")
    public void exportCsv(HttpSession session, HttpServletResponse response,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal amountMin,
            @RequestParam(required = false) BigDecimal amountMax) throws Exception {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        if ((dateFrom == null || dateFrom.isBlank()) && date != null && !date.isBlank()) {
            dateFrom = date;
        }
        if ((dateTo == null || dateTo.isBlank()) && date != null && !date.isBlank()) {
            dateTo = date;
        }

        List<Transaction> transactions = user.getRole() == User.Role.ADMIN
                ? transactionService.getAllTransactions()
                : transactionService.getTransactionHistory(user.getId());
        transactions = applyFilters(transactions, type, search, dateFrom, dateTo, status, amountMin, amountMax);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=revpay_transactions.csv");

        PrintWriter writer = response.getWriter();
        writer.println("ID,Date,Type,Sender,Receiver,Amount,Status,Note");
        for (Transaction tx : transactions) {
            writer.println(String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                    tx.getId(),
                    tx.getTimestamp(),
                    tx.getType(),
                    tx.getSender() != null ? tx.getSender().getFullName() : "N/A",
                    tx.getRecipient() != null ? tx.getRecipient().getFullName() : "N/A",
                    tx.getAmount(),
                    tx.getStatus(),
                    tx.getNote() != null ? "\"" + tx.getNote().replace("\"", "\"\"") + "\"" : ""));
        }
        writer.flush();
    }

    private List<Transaction> applyFilters(List<Transaction> transactions, String type, String search,
            String dateFrom, String dateTo, String status,
            BigDecimal amountMin, BigDecimal amountMax) {
        if (type != null && !type.isEmpty() && !type.equals("ALL")) {
            transactions = transactions.stream()
                    .filter(t -> t.getType().name().equals(type))
                    .collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty() && !status.equals("ALL")) {
            transactions = transactions.stream()
                    .filter(t -> t.getStatus().name().equals(status))
                    .collect(Collectors.toList());
        }
        if (dateFrom != null && !dateFrom.isEmpty()) {
            LocalDateTime from = LocalDate.parse(dateFrom).atStartOfDay();
            transactions = transactions.stream()
                    .filter(t -> !t.getTimestamp().isBefore(from))
                    .collect(Collectors.toList());
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            LocalDateTime to = LocalDate.parse(dateTo).atTime(23, 59, 59);
            transactions = transactions.stream()
                    .filter(t -> !t.getTimestamp().isAfter(to))
                    .collect(Collectors.toList());
        }
        if (amountMin != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getAmount().compareTo(amountMin) >= 0)
                    .collect(Collectors.toList());
        }
        if (amountMax != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getAmount().compareTo(amountMax) <= 0)
                    .collect(Collectors.toList());
        }
        if (search != null && !search.isEmpty()) {
            String s = search.toLowerCase();
            transactions = transactions.stream()
                    .filter(t -> {
                        if (t.getId().toString().contains(s))
                            return true;
                        if (t.getSender() != null && t.getSender().getFullName().toLowerCase().contains(s))
                            return true;
                        if (t.getRecipient() != null && t.getRecipient().getFullName().toLowerCase().contains(s))
                            return true;
                        if (t.getNote() != null && t.getNote().toLowerCase().contains(s))
                            return true;
                        return false;
                    })
                    .collect(Collectors.toList());
        }
        return transactions;
    }
}
