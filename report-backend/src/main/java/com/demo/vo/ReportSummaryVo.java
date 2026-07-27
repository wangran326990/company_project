package com.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportSummaryVo {
    private Integer accountId;
    private BigDecimal betSum;
    private BigDecimal winSum;
    private BigDecimal net;

}
