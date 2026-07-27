package com.demo.dto;

import com.demo.validation.ValidDateRange;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @Min(value = 1, message = "Page must be greater than or equal to 1")
    private int page = 1;

    @Min(value = 0, message = "Page must be greater than or equal to 0")
    private int size = 50;
}
