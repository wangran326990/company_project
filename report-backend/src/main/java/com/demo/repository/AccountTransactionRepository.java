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


        Map<String,Object> params = new HashMap<>();


        if(request.getAccountId() != null){
            sql.append(" AND ACCOUNT_ID = :accountId ");
            params.put("accountId", request.getAccountId());
        }


        if(request.getStartDate() != null){
            sql.append(" AND DATETIME >= :startDate ");
            params.put("startDate", request.getStartDate());
        }


        if(request.getEndDate() != null){
            sql.append(" AND DATETIME <= :endDate ");
            params.put("endDate", request.getEndDate());
        }


        if(request.getTranType() != null){
            sql.append(" AND TRAN_TYPE = :tranType ");
            params.put("tranType", request.getTranType());
        }


        if(request.getGameId() != null){
            sql.append(" AND GAME_ID = :gameId ");
            params.put("gameId", request.getGameId());
        }


        // dynamic sorting
        sql.append(buildOrderBy(request));


        Query query = entityManager
                .createNativeQuery(sql.toString(), "TransactionReportDtoMapping");


        params.forEach(query::setParameter);


        // pagination
        query.setFirstResult(
                request.getPage() * request.getSize()
        );

        query.setMaxResults(
                request.getSize()
        );


        return (List<TransactionReportDto>) query.getResultList();
    }


    private String buildOrderBy(TransactionSearchRequestDto request){

        String sortColumn = switch (request.getSortBy()) {
            case "amount" -> "amount";
            case "balance" -> "balance";
            case "tranType"-> "TRAN_TYPE";
            case "PLATFORM_TRAN_ID"->"PLATFORM_TRAN_ID";
            case "GAME_TRAN_ID"->"GAME_TRAN_ID";
            case "ACCOUNT_ID"-> "ACCOUNT_ID";
            case "ID"->"ID";
            case "GAME_ID"->"GAME_ID";
            default -> "DATETIME";
        };


        String direction =
                "DESC".equalsIgnoreCase(request.getSortDirection())
                        ? "DESC"
                        : "ASC";


        return " ORDER BY " + sortColumn + " " + direction;
    }

    public long count(){

        return entityManager
                .createQuery(
                        "select count(t) from AccountTransactionEntity t",
                        Long.class
                )
                .getSingleResult();

    }
}
