package com.demo.controller;

import com.demo.dto.ReportSummaryDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.service.AccountTransactionService;
import com.demo.service.ReportService;
import com.demo.vo.ReportSummaryVo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@AllArgsConstructor
public class ReportRestController {
    private final AccountTransactionService accountTransactionService;
    private final ReportService reportService;


    @GetMapping("/list")
    public List<AccountTransactionEntity> list() {
         return accountTransactionService.list();
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ReportSummaryVo>> summary(@Valid TransactionSearchRequestDto request) {
        List<ReportSummaryVo> summary = this.reportService.getReportSummary(request);
        return ResponseEntity.ok(summary);
    }

}
