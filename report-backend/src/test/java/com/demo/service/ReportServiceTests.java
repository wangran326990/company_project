package com.demo.service;

import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.repository.AccountTransactionRepository;
import com.demo.vo.PaginationResponseVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ReportServiceTests {
    @Mock
    private AccountTransactionRepository accountTransactionRepository;


    @InjectMocks
    private ReportService reportService;


    @Test
    void search_shouldReturnPaginationResponse() {
        TransactionSearchRequestDto request = new TransactionSearchRequestDto();
        request.setPage(1);
        request.setSize(25);

        List<TransactionReportDto> data = Arrays.asList(
                new TransactionReportDto(),
                new TransactionReportDto()
        );
        when(accountTransactionRepository.count(request)).thenReturn(2L);
        when(accountTransactionRepository.search(request)).thenReturn(data);
        PaginationResponseVo<TransactionReportDto> result =
                reportService.search(request);

        assertEquals(1, result.getCurrentPage());
        assertEquals(25, result.getPageSize());
        assertEquals(2L, result.getTotalRecords());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getData().size());

        verify(accountTransactionRepository).count(request);
        verify(accountTransactionRepository).search(request);
    }

    @Test
    void search_whenPageLessThanOne_shouldResetToOne() {

        TransactionSearchRequestDto request = new TransactionSearchRequestDto();
        request.setPage(0);
        request.setSize(25);

        when(accountTransactionRepository.count(request)).thenReturn(50L);
        when(accountTransactionRepository.search(request))
                .thenReturn(Collections.emptyList());

        PaginationResponseVo<TransactionReportDto> result =
                reportService.search(request);

        assertEquals(1, result.getCurrentPage());
        assertEquals(1, request.getPage());
        verify(accountTransactionRepository).count(request);

        verify(accountTransactionRepository).search(request);
    }


    @Test
    void search_whenPageGreaterThanTotalPages_shouldResetToLastPage() {

        TransactionSearchRequestDto request = new TransactionSearchRequestDto();
        request.setPage(10);
        request.setSize(25);

        when(accountTransactionRepository.count(request)).thenReturn(60L);
        when(accountTransactionRepository.search(request))
                .thenReturn(Collections.emptyList());

        PaginationResponseVo<TransactionReportDto> result =
                reportService.search(request);

        assertEquals(3, result.getCurrentPage());
        assertEquals(3, request.getPage());

        verify(accountTransactionRepository).search(request);
        verify(accountTransactionRepository).count(request);
    }

    @Test
    void search_whenNoRecords_shouldReturnZeroTotalPages() {

        TransactionSearchRequestDto request = new TransactionSearchRequestDto();
        request.setPage(1);
        request.setSize(25);

        when(accountTransactionRepository.count(request)).thenReturn(0L);
        when(accountTransactionRepository.search(request))
                .thenReturn(Collections.emptyList());

        PaginationResponseVo<TransactionReportDto> result =
                reportService.search(request);

        assertEquals(0, result.getTotalRecords());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getData().size());

        verify(accountTransactionRepository).search(request);
        verify(accountTransactionRepository).count(request);
    }

}
