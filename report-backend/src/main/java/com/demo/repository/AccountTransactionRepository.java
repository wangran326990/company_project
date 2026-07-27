package com.demo.repository;

import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
     * 	    + COALESCE(AMOUNT_RAW_LOYALTY, 0) / 100
     * 	) AS amount,
     *
     *     (
     *         COALESCE(BALANCE_REAL, 0)
     *         + COALESCE(BALANCE_RELEASED_BONUS, 0)
     *         + COALESCE(BALANCE_PLAYABLE_BONUS, 0)
     *         + COALESCE(BALANCE_RAW_LOYALTY, 0) / 100
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



    public List<TransactionReportDto> search(TransactionSearchRequestDto request) {


        StringBuilder sql = new StringBuilder("""
            SELECT
                                    ID as id,
                                    ACCOUNT_ID as accountId,
                                    DATETIME as datetime,
                                    TRAN_TYPE as tranType,
                                    PLATFORM_TRAN_ID as platformTranId,
                                    GAME_TRAN_ID as gameTranId,
                                    GAME_ID as gameId,
                
                                    ROUND (
                                        COALESCE(AMOUNT_REAL,0)
                                        + COALESCE(AMOUNT_RELEASED_BONUS,0)
                                        + COALESCE(AMOUNT_PLAYABLE_BONUS,0)
                                        + COALESCE(AMOUNT_UNDERFLOW,0)
                                        + COALESCE(AMOUNT_FREE_BET,0)
                                        + COALESCE(AMOUNT_RAW_LOYALTY,0)/100.00
                                    , 2) AS amount,
                
                                    ROUND (
                                        COALESCE(BALANCE_REAL,0)
                                        + COALESCE(BALANCE_RELEASED_BONUS,0)
                                        + COALESCE(BALANCE_PLAYABLE_BONUS,0)
                                        + COALESCE(BALANCE_RAW_LOYALTY,0)/100.00
                                    , 2) AS balance
                
                                FROM account_tran
            WHERE 1=1
        """);

        sql.append(getSearchQuery(request));
        Map<String, Object> params = getSearchQueryParams(request);
        // dynamic sorting
        sql.append(buildOrderBy(request));


        Query query = entityManager
                .createNativeQuery(sql.toString(), "TransactionReportDtoMapping");


        params.forEach(query::setParameter);


        // pagination
        query.setFirstResult(
                (request.getPage() - 1) * request.getSize()
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

        String sortColumn = switch (request.getSortBy()) {
            case "amount" -> "amount";
            case "balance" -> "balance";
            case "tranType"-> "TRAN_TYPE";
            case "platformTranId"->"PLATFORM_TRAN_ID";
            case "gameTranId"->"GAME_TRAN_ID";
            case "accountId"-> "ACCOUNT_ID";
            case "id"->"ID";
            case "gameId"->"GAME_ID";
            default -> "DATETIME";
        };


        String direction =
                "DESC".equalsIgnoreCase(request.getSortDirection())
                        ? "DESC"
                        : "ASC";


        return " ORDER BY " + sortColumn + " " + direction;
    }

    public long count(TransactionSearchRequestDto request){

        StringBuilder sql = new StringBuilder("""
                                    SELECT COUNT(*)
                                    FROM account_tran
                                    WHERE 1=1
                                """);


        sql.append(getSearchQuery(request));


        Map<String, Object> params =
                getSearchQueryParams(request);


        Query query = entityManager
                .createNativeQuery(sql.toString());


        params.forEach(query::setParameter);


        return ((Number) query.getSingleResult())
                .longValue();
    }
}
