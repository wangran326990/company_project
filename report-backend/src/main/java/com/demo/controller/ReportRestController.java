package com.demo.controller;

import com.demo.entity.AccountTransactionEntity;
import com.demo.service.AccountTransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportRestController {
    private final AccountTransactionService accountTransactionService;

    public ReportRestController(
            AccountTransactionService accountTransactionService
    ){
        this.accountTransactionService = accountTransactionService;
    }

    @GetMapping("/transaction/list")
    public List<AccountTransactionEntity> list() {
         return accountTransactionService.list();
    }

}
