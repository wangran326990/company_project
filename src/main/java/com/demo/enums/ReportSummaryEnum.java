package com.demo.enums;

public enum ReportSummaryEnum {

    ACCOUNT_ID("Account ID"),
    BET_SUM("Bet Sum"),
    WIN_SUM("Win Sum"),
    NET("Net");

    private final String displayName;

    ReportSummaryEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}