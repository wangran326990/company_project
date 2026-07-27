package com.demo.controller;

import com.demo.entity.AccountTransactionEntity;
import com.demo.service.AccountTransactionService;
import com.demo.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class ReportControllerTest {


    private MockMvc mockMvc;


    @Mock
    private AccountTransactionService accountTransactionService;
    @Mock
    private ReportService reportService;


    @BeforeEach
    void setup() {

        ReportRestController controller =
                new ReportRestController(accountTransactionService,reportService);


        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }


    @Test
    void shouldReturnTransactionList() throws Exception {


        AccountTransactionEntity transaction =
                new AccountTransactionEntity();

        transaction.setId(1L);
        transaction.setAmountReal(new BigDecimal("100.00"));


        when(accountTransactionService.list())
                .thenReturn(List.of(transaction));


        mockMvc.perform(
                        get("/api/v1/report/list")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amountReal").value(100.0));


        verify(accountTransactionService)
                .list();
    }
}