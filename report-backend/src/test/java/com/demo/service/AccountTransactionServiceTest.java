package com.demo.service;

import com.demo.entity.AccountTransactionEntity;
import com.demo.repository.AccountTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountTransactionServiceTest {
    @Mock
    private AccountTransactionRepository accountTransactionRepository;


    @InjectMocks
    private AccountTransactionService accountTransactionService;

    @Test
    void shouldReturnAllTransactions() {

        // Arrange
        AccountTransactionEntity transaction1 =
                new AccountTransactionEntity();

        AccountTransactionEntity transaction2 =
                new AccountTransactionEntity();


        List<AccountTransactionEntity> mockTransactions = new ArrayList<>();
        mockTransactions.add(transaction1);
        mockTransactions.add(transaction2);


        Mockito.when(accountTransactionRepository.findAll())
                .thenReturn(mockTransactions);


        // Act
        List<AccountTransactionEntity> result =
                accountTransactionService.list();


        // Assert
        assertNotNull(result);

        assertEquals(2, result.size());

        assertSame(mockTransactions, result);


        verify(accountTransactionRepository, times(1))
                .findAll();
    }
}
