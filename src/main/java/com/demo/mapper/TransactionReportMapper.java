package com.demo.mapper;

import com.demo.dto.TransactionReportDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.vo.TransactionReportRowVo;

public class TransactionReportMapper {
    public static TransactionReportRowVo toVo(AccountTransactionEntity entity) {

        return TransactionReportRowVo.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .datetime(entity.getDateTime())
                .tranType(entity.getTranType())
                .platformTranId(entity.getPlatformTranId())
                .amount(entity.getAmountSum())
                .gameId(entity.getGameId())
                .gameTranId(entity.getGameTranId())
                .balance(entity.getBalanceSum()).build();
    }

    public static TransactionReportRowVo toVo(TransactionReportDto dto) {

        return TransactionReportRowVo.builder()
                .id(dto.getId())
                .accountId(dto.getAccountId())
                .datetime(dto.getDateTime())
                .tranType(dto.getTranType())
                .platformTranId(dto.getPlatformTranId())
                .amount(dto.getAmount())
                .gameId(dto.getGameId())
                .gameTranId(dto.getGameTranId())
                .balance(dto.getBalance()).build();
    }

    public static TransactionReportDto toDto(AccountTransactionEntity entity) {

        return TransactionReportDto.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .dateTime(entity.getDateTime())
                .tranType(entity.getTranType())
                .platformTranId(entity.getPlatformTranId())
                .amount(entity.getAmountSum())
                .gameId(entity.getGameId())
                .gameTranId(entity.getGameTranId())
                .balance(entity.getBalanceSum()).build();
    }

}
