package com.demo.dto;

import com.demo.validation.ValidDateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@ValidDateRange
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSearchRequestDto {
    @Positive(message = "Account ID must be positive")
    private Integer accountId;
    @NotNull(message = "Start Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @NotNull(message = "End Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
    @Size(max = 100, message = "Transaction type must be 100 characters or fewer")
    private String tranType;
    @Size(max = 100, message = "Game Id must be 100 characters or fewer")
    private String gameId;
    @Size(max = 100, message = "platformTran Id must be 100 characters or fewer")
    private String platformTranId;
    @Size(max = 100, message = "gameTran Id must be 100 characters or fewer")
    private String gameTranId;

    @Size(max = 40, message = "sortBy must be 40 characters or fewer")
    private String sortBy = "";     // datetime, amount, balance
    @Size(max = 4, message = "sortDirection must be 4 characters or fewer")
    private String sortDirection; // ASC, DESC

    @Min(value = 1, message = "Page must be greater than or equal to 1")
    @Max(value = 10000000, message= "Page must lower than or equal to 10000000")
    private int page = 1;

    @Min(value = 0, message = "Page must be greater than or equal to 0")
    private int size = 50;

    @AssertTrue(message = "Size must be either 25 or 50")
    public boolean isSizeValid() {
        return size == 25 || size == 50;
    }
}
