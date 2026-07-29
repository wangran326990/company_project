package com.demo.dto;

import com.demo.entity.AccountTransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionReportDto {
    private Long id;

    private Integer accountId;

    private LocalDateTime dateTime;

    private String tranType;

    private String platformTranId;

    private String gameTranId;

    private String gameId;

    private BigDecimal amount;

    private BigDecimal balance;

    public static TransactionReportDto from(AccountTransactionEntity entity) {
        return TransactionReportDto.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .dateTime(entity.getDateTime())
                .tranType(entity.getTranType())
                .platformTranId(entity.getPlatformTranId())
                .amount(entity.getAmountSum())
                .gameId(entity.getGameId())
                .gameTranId(entity.getGameTranId())
                .balance(entity.getBalanceSum()).build();
    }

}
