package com.demo.controller;


import com.demo.dto.TransactionSearchRequestDto;
import com.demo.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String showPage(Model model) {

        model.addAttribute(
                "searchForm",
                new TransactionSearchRequestDto());

        return "report";
    }

    @PostMapping
    public String generateReport(
            @Valid
            @ModelAttribute("searchForm")
            TransactionSearchRequestDto form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "report";
        }

        model.addAttribute(
                "transactions",
                reportService.search(form));

        return "report";
    }

}