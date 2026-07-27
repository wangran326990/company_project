package com.demo.controller;

import com.demo.dto.ReportSummaryDto;
import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.service.AccountTransactionService;
import com.demo.service.ReportService;
import com.demo.vo.ReportSummaryVo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.IOException;
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

    @GetMapping(value = "/export", produces = "text/csv")
    public void exportCsv(
            @Valid TransactionSearchRequestDto request,
            HttpServletResponse response) throws IOException {

        List<TransactionReportDto> reports = reportService.getExcelData(request);

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"report.csv\"");

        CSVPrinter csvPrinter = new CSVPrinter(
                response.getWriter(),
                CSVFormat.DEFAULT.withHeader(
                        "ID",
                        "Account ID",
                        "Datetime",
                        "Tran Type",
                        "Platform Tran ID",
                        "Game Tran ID",
                        "Game ID",
                        "Amount",
                        "Balance"));

        for (TransactionReportDto report : reports) {
            csvPrinter.printRecord(
                    report.getId(),
                    report.getAccountId(),
                    report.getDateTime(),
                    report.getTranType(),
                    report.getPlatformTranId(),
                    report.getGameTranId(),
                    report.getGameId(),
                    report.getAmount(),
                    report.getBalance()
            );
        }

        csvPrinter.flush();
    }

}
