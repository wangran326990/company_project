package com.demo.mapper;

import com.demo.dto.ReportSummaryDto;
import com.demo.vo.ReportSummaryVo;
import com.demo.vo.TransactionReportRowVo;

public class ReportSummaryMapper {
    public static ReportSummaryVo toVo(ReportSummaryDto dto) {

        return ReportSummaryVo.builder()
                .accountId(dto.getAccountId())
                .betSum(dto.getBetSum())
                .winSum(dto.getWinSum())
                .net(dto.getNet())
                .build();
    }
}
