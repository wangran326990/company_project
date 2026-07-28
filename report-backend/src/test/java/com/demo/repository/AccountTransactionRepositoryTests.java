package com.demo.repository;

import com.demo.config.TestHibernateConfig;
import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestHibernateConfig.class)
public class AccountTransactionRepositoryTests {

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;
    @Test
    @Sql(
        scripts={
                "/sql/schema.sql",
                "/sql/data.sql"
        }
    )
    @Transactional
    void list_returnCount() {
        List<AccountTransactionEntity> transactions =
                accountTransactionRepository.findAll();
        assertNotNull(transactions);
        assertEquals(6977, transactions.size());
    }


    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    @Transactional
    void findRangeByAccountId_returnRecordFromRange() {
        Integer accountId = 2166;
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .accountId(accountId)
                .page(1)
                .size(25)
                .build();
        List<TransactionReportDto> result = accountTransactionRepository.search(transactionSearchRequestDto);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        result.forEach(t -> {
            assertEquals(accountId, t.getAccountId());
            assertFalse(t.getDateTime().isBefore(start));
            assertFalse(t.getDateTime().isAfter(end));
        });
    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    @Transactional
    void findRangeByAccountIdNoPageSet_returnRecordFromRange() {
        Integer accountId = 2166;
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .accountId(accountId)
                .page(1)
                .build();
        List<TransactionReportDto> result = accountTransactionRepository.search(transactionSearchRequestDto);
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    @Transactional
    void findRange_withPageSize_returnsExpectedNumberOfRecords() {
        Integer accountId = 2166;
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .accountId(accountId)
                .page(1)
                .size(50)
                .build();
        List<TransactionReportDto> result = accountTransactionRepository.search(transactionSearchRequestDto);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(50, result.size());

    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    @Transactional
    void findRange_withGameTranId_returnsExpectedNumberOfRecords() {
        Integer accountId = 2166;
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .gameTranId("10000011300")
                .page(1)
                .size(50)
                .build();
        List<TransactionReportDto> result = accountTransactionRepository.search(transactionSearchRequestDto);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        result.forEach(record->{
            assertEquals( "10000011300", record.getGameTranId());
        });

    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    @Transactional
    void findRange_withPlatformTranId_returnsExpectedNumberOfRecords() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .platformTranId("510000096174")
                .page(1)
                .size(50)
                .build();
        List<TransactionReportDto> result = accountTransactionRepository.search(transactionSearchRequestDto);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        result.forEach(record->{
            assertEquals( "510000096174", record.getPlatformTranId());
        });

    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    @Transactional
    void findRange_withGameId_returnsExpectedNumberOfRecords() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .gameId("SPORTSBOOK2.0")
                .page(1)
                .size(50)
                .build();
        List<TransactionReportDto> result = accountTransactionRepository.search(transactionSearchRequestDto);
        assertNotNull(result);
        assertFalse(result.isEmpty());

        result.forEach(record->{
            assertEquals( "SPORTSBOOK2.0", record.getPlatformTranId());
        });

    }
}
