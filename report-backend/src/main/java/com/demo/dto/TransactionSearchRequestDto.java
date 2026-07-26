package com.demo.dto;

import com.demo.validation.ValidDateRange;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ValidDateRange
public class TransactionSearchRequestDto {
    private Integer accountId;
    @NotNull(message = "Start Date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End Date is required")
    private LocalDateTime endDate;

    private String tranType;

    private String gameId;

    private String platformTranId;

    private String gameTranId;


    private String sortBy = "";     // datetime, amount, balance

    private String sortDirection; // ASC, DESC

    private int page = 1;

    private int size = 50;
}
