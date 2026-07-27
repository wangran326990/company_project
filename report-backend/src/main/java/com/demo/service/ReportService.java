package com.demo.service;

import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.mapper.TransactionReportMapper;
import com.demo.repository.AccountTransactionRepository;
import com.demo.vo.PaginationResponseVo;
import com.demo.vo.TransactionReportRowVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final AccountTransactionRepository accountTransactionRepository;

    public ReportService(AccountTransactionRepository accountTransactionRepository) {
        this.accountTransactionRepository = accountTransactionRepository;
    }

    public PaginationResponseVo<TransactionReportDto> search(
            TransactionSearchRequestDto request
    ){

        int page = request.getPage();
        int size = request.getSize();


        List<TransactionReportDto> data =
                accountTransactionRepository.search(request);


        long total =
                accountTransactionRepository.count(request);


        int totalPages =
                (int)Math.ceil(
                        (double) total / size
                );


        return PaginationResponseVo
                .<TransactionReportDto>builder()
                .data(data)
                .currentPage(page)
                .pageSize(size)
                .totalRecords(total)
                .totalPages(totalPages)
                .build();
    }



}
