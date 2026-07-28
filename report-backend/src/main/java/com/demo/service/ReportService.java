package com.demo.service;

import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.mapper.ReportSummaryMapper;
import com.demo.mapper.TransactionReportMapper;
import com.demo.repository.AccountTransactionRepository;
import com.demo.vo.PaginationResponseVo;
import com.demo.vo.ReportSummaryVo;
import com.demo.vo.TransactionReportRowVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        long total =
                accountTransactionRepository.count(request);


        int totalPages =
                (int)Math.ceil(
                        (double) total / size
                );
        if(page < 0) {
            page = 1;
            request.setPage(1);
        }

        if(page > totalPages){

            page = totalPages;
            request.setPage(totalPages);
        }


        List<TransactionReportDto> data =
                accountTransactionRepository.search(request);





        return PaginationResponseVo
                .<TransactionReportDto>builder()
                .data(data)
                .currentPage(page)
                .pageSize(size)
                .totalRecords(total)
                .totalPages(totalPages)
                .build();
    }


    public List<ReportSummaryVo> getReportSummary(TransactionSearchRequestDto searchRequest) {
        return accountTransactionRepository
                .getReportSummary(searchRequest)
                .stream()
                .map(ReportSummaryMapper::toVo)
                .collect(Collectors.toList());
    }

    public List<TransactionReportDto> getExcelData(TransactionSearchRequestDto searchRequest) {
        return accountTransactionRepository.findRangeByAccountId(
                searchRequest.getStartDate(),
                searchRequest.getEndDate(),
                searchRequest.getAccountId()).stream().map(TransactionReportMapper::toDto).collect(Collectors.toList());
    }
}
