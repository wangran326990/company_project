package com.demo.service;

import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.mapper.TransactionReportMapper;
import com.demo.repository.AccountTransactionRepository;
import com.demo.vo.TransactionReportRowVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final AccountTransactionRepository accountTransactionRepository;

    public ReportService(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    public List<TransactionReportRowVo> search(
            TransactionSearchRequestDto form
    ){

        List<TransactionReportRowVo> result = accountTransactionRepository
                .search(form)
                .stream()
                .map(TransactionReportMapper::toVo)
                .toList();
        return result;

    }

}
