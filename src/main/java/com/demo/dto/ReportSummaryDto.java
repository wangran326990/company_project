package com.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportSummaryDto {
    private Integer accountId;
    private BigDecimal betSum;
    private BigDecimal winSum;
    private BigDecimal net;

}
