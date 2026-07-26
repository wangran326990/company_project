package com.demo.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionReportRowVo {
    private Long id;

    private Integer accountId;

    private LocalDateTime datetime;

    private String tranType;

    private String platformTranId;

    private String gameTranId;

    private Long gameId;


    private BigDecimal amount;

    private BigDecimal balance;
}
