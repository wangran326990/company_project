package com.demo.repository;

import com.demo.config.TestHibernateConfig;
import com.demo.dto.ReportSummaryDto;
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
                .size(0)
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
            assertEquals( "SPORTSBOOK2.0", record.getGameId());
        });

    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void reportSummeryBySelectedRangeAndAccount_returnExpectedSummary() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .accountId(2)
                .build();
        List<ReportSummaryDto> result = accountTransactionRepository.getReportSummary(transactionSearchRequestDto);
        assertEquals(1,result.size());
        assertEquals(2, result.get(0).getAccountId());
        assertEquals(new BigDecimal("-28025.54"), result.get(0).getNet());
        assertEquals(new BigDecimal("-29374.90"), result.get(0).getBetSum());
        assertEquals(new BigDecimal("1349.36"), result.get(0).getWinSum());
    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void reportSummeryBySelectedRange_returnExpectedSummary() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2025, 7,29,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .build();
        List<ReportSummaryDto> result = accountTransactionRepository.getReportSummary(transactionSearchRequestDto);
        assertEquals(1,result.size());
        assertEquals(2166, result.get(0).getAccountId());
        assertEquals(new BigDecimal("-24.00"), result.get(0).getNet());
        assertEquals(new BigDecimal("-24.00"), result.get(0).getBetSum());
        assertEquals(new BigDecimal("0.00"), result.get(0).getWinSum());
    }


    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void reportSummeryBySelectedRange_withTranType_returnExpectedSummary() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2025, 7,29,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .tranType("GAME_BET")
                .endDate(end)
                .build();
        List<ReportSummaryDto> result = accountTransactionRepository.getReportSummary(transactionSearchRequestDto);
        assertEquals(1,result.size());
        assertEquals(2166, result.get(0).getAccountId());
        assertEquals(new BigDecimal("-24.00"), result.get(0).getNet());
        assertEquals(new BigDecimal("-24.00"), result.get(0).getBetSum());
        assertEquals(new BigDecimal("0.00"), result.get(0).getWinSum());
    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void reportSummeryBySelectedRange_withPlatformTranId_returnExpectedSummary() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 7,29,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .platformTranId("510000096174")
                .endDate(end)
                .build();
        List<ReportSummaryDto> result = accountTransactionRepository.getReportSummary(transactionSearchRequestDto);
        assertEquals(1,result.size());
        assertEquals(2166, result.get(0).getAccountId());
        assertEquals(new BigDecimal("-10.00"), result.get(0).getNet());
        assertEquals(new BigDecimal("-10.00"), result.get(0).getBetSum());
        assertEquals(new BigDecimal("0.00"), result.get(0).getWinSum());
    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void reportSummeryBySelectedRange_withGameTranId_returnExpectedSummary() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2025, 7,29,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .gameTranId("10000011300")
                .endDate(end)
                .build();
        List<ReportSummaryDto> result = accountTransactionRepository.getReportSummary(transactionSearchRequestDto);
        assertEquals(1,result.size());
        assertEquals(2166, result.get(0).getAccountId());
        assertEquals(new BigDecimal("-10.00"), result.get(0).getNet());
        assertEquals(new BigDecimal("-10.00"), result.get(0).getBetSum());
        assertEquals(new BigDecimal("0.00"), result.get(0).getWinSum());
    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void reportSummeryBySelectedRange_withGameId_returnExpectedSummary() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2025, 7,29,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .gameId("SPORTSBOOK2.0")
                .endDate(end)
                .build();
        List<ReportSummaryDto> result = accountTransactionRepository.getReportSummary(transactionSearchRequestDto);
        assertEquals(1,result.size());
        assertEquals(2166, result.get(0).getAccountId());
        assertEquals(new BigDecimal("-24.00"), result.get(0).getNet());
        assertEquals(new BigDecimal("-24.00"), result.get(0).getBetSum());
        assertEquals(new BigDecimal("0.00"), result.get(0).getWinSum());
    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void countRangeByAccountId_returnCorrectCount() {
        Integer accountId = 2166;
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .accountId(accountId)
                .build();
        long count = accountTransactionRepository.count(transactionSearchRequestDto);
        assertEquals(1056, count);

    }



    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void count_withGameTranId_returnsExpectedNumberOfRecords() {
        Integer accountId = 2166;
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .gameTranId("10000011300")
                .build();
        long count = accountTransactionRepository.count(transactionSearchRequestDto);
        assertEquals(1, count);

    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )
    void count_withPlatformTranId_returnsExpectedNumberOfRecords() {
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
        long count = accountTransactionRepository.count(transactionSearchRequestDto);
        assertEquals(1, count);

    }

    @Test
    @Sql(
            scripts={
                    "/sql/schema.sql",
                    "/sql/data.sql"
            }
    )

    void count_withGameId_returnsExpectedNumberOfRecords() {
        LocalDateTime start = LocalDateTime.of(2025, 1,1,0,0);
        LocalDateTime end = LocalDateTime.of(2026, 1,1,1,0,0);
        TransactionSearchRequestDto transactionSearchRequestDto = TransactionSearchRequestDto
                .builder()
                .startDate(start)
                .endDate(end)
                .gameId("SPORTSBOOK2.0")
                .build();
        long result = accountTransactionRepository.count(transactionSearchRequestDto);
        assertEquals(270, result);

    }

}
