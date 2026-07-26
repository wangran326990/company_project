package com.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="account_tran")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccountTransactionEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private Long id;

        @Column(name = "ACCOUNT_ID", nullable = false)
        private Integer accountId;

        @Column(name = "DATETIME", nullable = false)
        private LocalDateTime datetime;

        @Column(name = "LOGDATETIME", nullable = false)
        private LocalDateTime logDatetime;

        @Column(name = "TRAN_TYPE", length = 10)
        private String tranType;

        @Column(name = "AMOUNT_REAL", precision = 10, scale = 2)
        private BigDecimal amountReal;

        @Column(name = "BALANCE_REAL", precision = 10, scale = 2)
        private BigDecimal balanceReal;

        @Column(name = "PLATFORM_TRAN_ID", length = 100)
        private String platformTranId;

        @Column(name = "GAME_TRAN_ID", length = 100)
        private String gameTranId;

        @Column(name = "GAME_ID", length = 100)
        private String gameId;

        @Column(name = "PLATFORM_ID")
        private Integer platformId;

        @Column(name = "payment_id")
        private Integer paymentId;

        @Column(name = "ROLLED_BACK")
        private Integer rolledBack;

        @Column(name = "ROLLBACK_TRAN_ID")
        private Long rollbackTranId;

        @Column(name = "AMOUNT_RELEASED_BONUS", precision = 10, scale = 2)
        private BigDecimal amountReleasedBonus;

        @Column(name = "AMOUNT_PLAYABLE_BONUS", precision = 10, scale = 2)
        private BigDecimal amountPlayableBonus;

        @Column(name = "BALANCE_RELEASED_BONUS", precision = 10, scale = 2)
        private BigDecimal balanceReleasedBonus;

        @Column(name = "BALANCE_PLAYABLE_BONUS", precision = 10, scale = 2)
        private BigDecimal balancePlayableBonus;

        @Column(name = "AMOUNT_UNDERFLOW", precision = 10, scale = 2)
        private BigDecimal amountUnderflow;

        @Column(name = "AMOUNT_RAW_LOYALTY")
        private Long amountRawLoyalty;

        @Column(name = "BALANCE_RAW_LOYALTY")
        private Long balanceRawLoyalty;

        @Column(name = "AMOUNT_FREE_BET", precision = 10, scale = 2)
        private BigDecimal amountFreeBet;

        @Column(name = "GAME_INSTANCE_ID")
        private Long gameInstanceId;

        @Column(name = "TRANSACTION_ON_HOLD_ID")
        private Long transactionOnHoldId;

        @Column(name = "CHANNEL", length = 25)
        private String channel;

        @Column(name = "GAME_SESSION_ID", length = 255)
        private String gameSessionId;

        @Column(name = "EXTERNAL_GAME_SESSION_ID", length = 255)
        private String externalGameSessionId;

    }
