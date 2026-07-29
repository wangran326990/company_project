package com.demo.service;

import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.repository.AccountTransactionRepository;
import com.demo.vo.PaginationResponseVo;
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
