package com.demo.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum TransactionColumnEnum {
    ID("id", "ID", false),
    ACCOUNT_ID("accountId", "Account ID", true),
    DATE_TIME("dateTime", "Datetime", false),
    TRAN_TYPE("tranType", "Tran Type", true),
    PLATFORM_TRAN_ID("platformTranId", "Platform Tran ID", true),
    GAME_TRAN_ID("gameTranId", "Game Tran ID", true),
    GAME_ID("gameId", "Game ID", true),
    AMOUNT("amount", "Amount", false),
    BALANCE("balance", "Balance", false);

    private final String value;
    private final String label;
    private final boolean filterable;

    TransactionColumnEnum(String value, String label, boolean filterable) {
        this.value = value;
        this.label = label;
        this.filterable = filterable;
    }

    public String getSortIndicator(String sortBy, String sortDirection) {
        if (!value.equals(sortBy)) {
            return "↕";
        }

        return "ASC".equals(sortDirection) ? "↑" : "↓";
    }

   public static TransactionColumnEnum getSortedColumn(String column) {
        Optional<TransactionColumnEnum> optionalTransactionColumnEnum = Arrays.stream(values())
                .filter(transactionColumnEnum -> transactionColumnEnum.value.equals(column))
                .findFirst();
        return optionalTransactionColumnEnum.orElse(DATE_TIME);
    }
}