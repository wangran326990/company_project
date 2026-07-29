package com.demo.repository;

import com.demo.dto.ReportSummaryDto;
import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountTransactionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * This is the query I need to build
     * SELECT
     *     ID,
     *     ACCOUNT_ID,
     *     DATETIME,
     *     TRAN_TYPE,
     *     PLATFORM_TRAN_ID,
     *     GAME_TRAN_ID,
     *     GAME_ID,
     *
     *     (
     * 	    COALESCE(AMOUNT_REAL, 0)
     * 	    + COALESCE(AMOUNT_RELEASED_BONUS, 0)
     * 	    + COALESCE(AMOUNT_PLAYABLE_BONUS, 0)
     * 	    + COALESCE(AMOUNT_UNDERFLOW, 0)
     * 	    + COALESCE(AMOUNT_FREE_BET, 0)
     * 	) AS amount,
     *
     *     (
     *         COALESCE(BALANCE_REAL, 0)
     *         + COALESCE(BALANCE_RELEASED_BONUS, 0)
     *         + COALESCE(BALANCE_PLAYABLE_BONUS, 0)
     *     ) AS balance
     *
     * FROM account_tran WHERE ID = 71;
     *
     *
     */

    public List<AccountTransactionEntity> findAll() {
        return entityManager
                .createQuery("select accountTransaction " +
                        "from AccountTransactionEntity accountTransaction",
                        AccountTransactionEntity.class).getResultList();

    }

    public List<AccountTransactionEntity> findRangeByAccountId(LocalDateTime startDate, LocalDateTime endDate, Integer accountId) {
        StringBuilder jpql = new StringBuilder(
                "SELECT ate FROM AccountTransactionEntity ate " +
                        "WHERE ate.dateTime BETWEEN :startDate AND :endDate"
        );

        if (accountId != null) {
            jpql.append(" AND ate.accountId = :accountId");
        }
        TypedQuery<AccountTransactionEntity> query = entityManager.createQuery(jpql.toString(), AccountTransactionEntity.class);
        if(startDate == null) {
            startDate = LocalDateTime.now();
        }
        if(endDate == null) {
            endDate = LocalDateTime.now();
        }
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        if(accountId != null) {
            query.setParameter("accountId", accountId);
        }
        return query.getResultList();
    }



    public List<TransactionReportDto> search(TransactionSearchRequestDto request) {
        if(request.getSize() == 0) return new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("ID as id, ")
                .append("ACCOUNT_ID as accountId, ")
                .append("DATETIME as datetime, ")
                .append("TRAN_TYPE as tranType, ")
                .append("PLATFORM_TRAN_ID as platformTranId, ")
                .append("GAME_TRAN_ID as gameTranId, ")
                .append("GAME_ID as gameId, ")

                .append("ROUND( ")
                .append("COALESCE(AMOUNT_REAL,0) ")
                .append("+ COALESCE(AMOUNT_RELEASED_BONUS,0) ")
                .append("+ COALESCE(AMOUNT_PLAYABLE_BONUS,0) ")
                .append("+ COALESCE(AMOUNT_UNDERFLOW,0) ")
                .append("+ COALESCE(AMOUNT_FREE_BET,0) ")
                //.append("+ COALESCE(AMOUNT_RAW_LOYALTY,0)/100.00 ")
                .append(", 2) AS amount, ")

                .append("ROUND( ")
                .append("COALESCE(BALANCE_REAL,0) ")
                .append("+ COALESCE(BALANCE_RELEASED_BONUS,0) ")
                .append("+ COALESCE(BALANCE_PLAYABLE_BONUS,0) ")
                //.append("+ COALESCE(BALANCE_RAW_LOYALTY,0)/100.00 ")
                .append(", 2) AS balance ")

                .append("FROM account_tran ")
                .append("WHERE 1=1 ");
        sql.append(getSearchQuery(request));
        Map<String, Object> params = getSearchQueryParams(request);
        // dynamic sorting
        sql.append(buildOrderBy(request));


        Query query = entityManager
                .createNativeQuery(sql.toString(), "TransactionReportDtoMapping");


        params.forEach(query::setParameter);


        // pagination

        query.setFirstResult(
                (request.getPage() - 1) < 0 ? 0: (request.getPage() - 1)  * request.getSize()
        );

        query.setMaxResults(
                request.getSize()
        );


        return (List<TransactionReportDto>) query.getResultList();
    }

    private static Map<String, Object> getSearchQueryParams(TransactionSearchRequestDto request) {
        Map<String,Object> params = new HashMap<>();


        if(request.getAccountId() != null){
            params.put("accountId", request.getAccountId());
        }


        if(request.getStartDate() != null){
            params.put("startDate", request.getStartDate());
        }


        if(request.getEndDate() != null){
            params.put("endDate", request.getEndDate());
        }


        if(request.getTranType() != null && !request.getTranType().isEmpty()){
            params.put("tranType", request.getTranType());
        }


        if(request.getGameId() != null && !request.getGameId().isEmpty()){
            params.put("gameId", request.getGameId());
        }

        if(request.getGameTranId() != null && !request.getGameTranId().isEmpty()){
            params.put("gameTranId", request.getGameTranId());
        }

        if(request.getPlatformTranId() != null && !request.getPlatformTranId().isEmpty()){
            params.put("platformTranId", request.getPlatformTranId());
        }
        return params;
    }

    private String getSearchQuery(TransactionSearchRequestDto request) {
        StringBuilder sql = new StringBuilder();
        if(request.getAccountId() != null){
            sql.append(" AND ACCOUNT_ID = :accountId ");

        }


        if(request.getStartDate() != null){
            sql.append(" AND DATETIME >= :startDate ");

        }


        if(request.getEndDate() != null){
            sql.append(" AND DATETIME <= :endDate ");

        }


        if(request.getTranType() != null && !request.getTranType().isEmpty()){
            sql.append(" AND TRAN_TYPE = :tranType ");

        }


        if(request.getGameId() != null && !request.getGameId().isEmpty()){
            sql.append(" AND GAME_ID = :gameId ");

        }

        if(request.getGameTranId() != null && !request.getGameTranId().isEmpty()){
            sql.append(" AND GAME_TRAN_ID = :gameTranId ");
        }

        if(request.getPlatformTranId() != null && !request.getPlatformTranId().isEmpty()){
            sql.append(" AND PLATFORM_TRAN_ID = :platformTranId ");
        }
        return sql.toString();
    }


    private String buildOrderBy(TransactionSearchRequestDto request){
        if(request.getSortBy() == null) request.setSortBy("");
        String sortColumn;
        switch (request.getSortBy()) {
            case "amount":
                sortColumn = "amount";
                break;
            case "balance":
                sortColumn = "balance";
                break;
            case "tranType":
                sortColumn = "TRAN_TYPE";
                break;
            case "platformTranId":
                sortColumn = "PLATFORM_TRAN_ID";
                break;
            case "gameTranId":
                sortColumn = "GAME_TRAN_ID";
                break;
            case "accountId":
                sortColumn = "ACCOUNT_ID";
                break;
            case "id":
                sortColumn = "ID";
                break;
            case "gameId":
                sortColumn = "GAME_ID";
                break;
            default:
                sortColumn = "DATETIME";
                break;
        }

        String direction =
                "DESC".equalsIgnoreCase(request.getSortDirection())
                        ? "DESC"
                        : "ASC";


        return " ORDER BY " + sortColumn + " " + direction;
    }

    public long count(TransactionSearchRequestDto request){

        StringBuilder sql = new StringBuilder()
                .append("SELECT COUNT(*)\n")
                .append("FROM account_tran\n")
                .append("WHERE 1=1\n");


        sql.append(getSearchQuery(request));


        Map<String, Object> params =
                getSearchQueryParams(request);


        Query query = entityManager
                .createNativeQuery(sql.toString());


        params.forEach(query::setParameter);


        return ((Number) query.getSingleResult())
                .longValue();
    }


    public List<ReportSummaryDto> getReportSummary(TransactionSearchRequestDto request) {
        StringBuilder jpql = new StringBuilder();

        jpql.append("SELECT new com.demo.dto.ReportSummaryDto( ")
                .append("a.accountId, ")
                .append("SUM(CASE WHEN a.tranType = 'GAME_BET' THEN a.amountReal ELSE 0 END), ")
                .append("SUM(CASE WHEN a.tranType = 'GAME_WIN' THEN a.amountReal ELSE 0 END), ")
                .append("SUM(CASE WHEN a.tranType = 'GAME_WIN' THEN a.amountReal ELSE 0 END) ")
                .append("+ ")
                .append("SUM(CASE WHEN a.tranType = 'GAME_BET' THEN a.amountReal ELSE 0 END) ")
                .append(") ")
                .append("FROM AccountTransactionEntity a ")
                .append("WHERE a.tranType IN ('GAME_BET', 'GAME_WIN') ");

        Map<String, Object> params = new HashMap<>();
        if(request.getAccountId() != null) {
            jpql.append(" AND a.accountId = :accountId ");
            params.put("accountId", request.getAccountId());
        }

        if (request.getStartDate() != null) {
            jpql.append(" AND a.dateTime >= :startDate ");
            params.put("startDate", request.getStartDate());
        }

        if (request.getEndDate() != null) {
            jpql.append(" AND a.dateTime <= :endDate ");
            params.put("endDate", request.getEndDate());
        }

        if(request.getTranType() != null && !request.getTranType().isEmpty()){
            jpql.append(" AND a.tranType = :tranType ");
            params.put("tranType", request.getTranType());
        }


        if(request.getGameId() != null && !request.getGameId().isEmpty()){
            jpql.append(" AND a.gameId = :gameId ");
            params.put("gameId", request.getGameId());
        }

        if(request.getGameTranId() != null && !request.getGameTranId().isEmpty()){
            jpql.append(" AND a.gameTranId = :gameTranId ");
            params.put("gameTranId", request.getGameTranId());
        }

        if(request.getPlatformTranId() != null && !request.getPlatformTranId().isEmpty()){
            jpql.append(" AND a.platformTranId = :platformTranId ");
            params.put("platformTranId", request.getPlatformTranId());
        }

        jpql.append(" GROUP BY a.accountId");

        TypedQuery<ReportSummaryDto> query =
                entityManager.createQuery(
                        jpql.toString(),
                        ReportSummaryDto.class
                );


        params.forEach(query::setParameter);


        return query.getResultList();
    }
}
