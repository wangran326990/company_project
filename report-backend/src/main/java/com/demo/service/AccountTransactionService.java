package com.demo.service;

import com.demo.dto.TransactionReportDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.repository.AccountTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountTransactionService {
    private final AccountTransactionRepository accountTransactionRepository;

    public AccountTransactionService(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    public List<AccountTransactionEntity> list(){

        return accountTransactionRepository.findAll();

    }


}
