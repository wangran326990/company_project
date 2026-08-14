package com.demo.repository;

import com.demo.dto.ReportSummaryDto;
import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.entity.AccountTransactionEntity;
import com.demo.enums.TransactionColumnEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.demo.enums.TransactionColumnEnum.AMOUNT;

@Repository
public class AccountTransactionRepository {
    /**
     *  It does not reuse the same EntityManager
     *  It usually creates a temporary one for the operation
     *  The persistence context is not kept open
     */
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
                "SELECT new AccountTransactionEntity(" +
                        "ate.id, " +
                        "ate.accountId, " +
                        "ate.dateTime, " +
                        "ate.tranType, " +
                        "ate.platformTranId, " +
                        "ate.gameTranId, " +
                        "ate.gameId, " +
                        "ate.amountReleasedBonus, " +
                        "ate.amountPlayableBonus, " +
                        "ate.balanceReleasedBonus, " +
                        "ate.balancePlayableBonus, " +
                        "ate.amountUnderflow, " +
                        "ate.amountFreeBet," +
                        "ate.amountReal," +
                        "ate.balanceReal" +
                        ") " +
                        "FROM AccountTransactionEntity ate " +
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

        StringBuilder getIdJPQL = new StringBuilder();
        getIdJPQL.append("SELECT ");
        getIdJPQL.append("    t.id ");
        getIdJPQL.append("FROM AccountTransactionEntity t ");


        JPQLQueryConditions jpqlQueryConditions =  buildSearchQueryCondition(request);
        getIdJPQL.append(jpqlQueryConditions.getJpql());
        getIdJPQL.append(buildJPQLOrderBy(request));


        TypedQuery<Long> query =
                entityManager.createQuery(
                        getIdJPQL.toString(),
                        Long.class
                );

        jpqlQueryConditions.getParameters().forEach(query::setParameter);

        query.setFirstResult(
                (request.getPage() - 1) < 0 ? 0: (request.getPage() - 1)  * request.getSize()
        );

        query.setMaxResults(
                request.getSize()
        );
        List<Long> ids = query.getResultList();
        if(ids.isEmpty()) return new ArrayList<>();

        StringBuilder jpql = new StringBuilder();

        jpql.append("SELECT new com.demo.dto.TransactionReportDto(");
        jpql.append("    t.id, ");
        jpql.append("    t.accountId, ");
        jpql.append("    t.dateTime, ");
        jpql.append("    t.tranType, ");
        jpql.append("    t.platformTranId, ");
        jpql.append("    t.gameTranId, ");
        jpql.append("    t.gameId, ");

        jpql.append("    COALESCE(t.amountReal, 0) + ");
        jpql.append("    COALESCE(t.amountReleasedBonus, 0) + ");
        jpql.append("    COALESCE(t.amountPlayableBonus, 0) + ");
        jpql.append("    COALESCE(t.amountUnderflow, 0) + ");
        jpql.append("    COALESCE(t.amountFreeBet, 0), ");

        jpql.append("    COALESCE(t.balanceReal, 0) + ");
        jpql.append("    COALESCE(t.balanceReleasedBonus, 0) + ");
        jpql.append("    COALESCE(t.balancePlayableBonus, 0) ");
        jpql.append(") ");
        jpql.append("FROM AccountTransactionEntity t ");
        jpql.append("WHERE t.id IN :ids");
        jpql.append(buildJPQLOrderBy(request));

        TypedQuery<TransactionReportDto> transactionReportQuery = entityManager.createQuery(
                jpql.toString(),
                TransactionReportDto.class
        );
        transactionReportQuery.setParameter("ids", ids);
        return transactionReportQuery.getResultList();
    }
    private JPQLQueryConditions buildSearchQueryCondition(
            TransactionSearchRequestDto request) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("WHERE 1 = 1 ");

        if (request.getAccountId() != null) {
            jpql.append("AND t.accountId = :accountId ");
            params.put("accountId", request.getAccountId());
        }

        if (request.getStartDate() != null) {
            jpql.append("AND t.dateTime >= :startDate ");
            params.put("startDate", request.getStartDate());
        }

        if (request.getEndDate() != null) {
            jpql.append("AND t.dateTime <= :endDate ");
            params.put("endDate", request.getEndDate());
        }

        if (request.getTranType() != null &&
                !request.getTranType().isEmpty()) {

            jpql.append("AND t.tranType = :tranType ");
            params.put("tranType", request.getTranType());
        }

        if (request.getGameId() != null &&
                !request.getGameId().isEmpty()) {

            jpql.append("AND t.gameId = :gameId ");
            params.put("gameId", request.getGameId());
        }

        if (request.getGameTranId() != null &&
                !request.getGameTranId().isEmpty()) {

            jpql.append("AND t.gameTranId = :gameTranId ");
            params.put("gameTranId", request.getGameTranId());
        }

        if (request.getPlatformTranId() != null &&
                !request.getPlatformTranId().isEmpty()) {

            jpql.append("AND t.platformTranId = :platformTranId ");
            params.put(
                    "platformTranId",
                    request.getPlatformTranId()
            );
        }

        return new JPQLQueryConditions(jpql.toString(), params);
    }



    private String buildJPQLOrderBy(TransactionSearchRequestDto request) {
        if(request.getSortBy() == null) request.setSortBy("");

        String sortColumn = "";
        TransactionColumnEnum sortedColumn = TransactionColumnEnum.getSortedColumn(request.getSortBy());
        switch (sortedColumn) {
            case AMOUNT:
                sortColumn =
                        " (COALESCE(t.amountReal, 0) " +
                                "+ COALESCE(t.amountReleasedBonus, 0) " +
                                "+ COALESCE(t.amountPlayableBonus, 0) " +
                                "+ COALESCE(t.amountUnderflow, 0) " +
                                "+ COALESCE(t.amountFreeBet, 0)) ";
                break;

            case BALANCE:
                sortColumn =
                        " (COALESCE(t.balanceReal, 0) " +
                                "+ COALESCE(t.balanceReleasedBonus, 0) " +
                                "+ COALESCE(t.balancePlayableBonus, 0)) ";
                break;

            case TRAN_TYPE:
                sortColumn = "t.tranType";
                break;

            case PLATFORM_TRAN_ID:
                sortColumn = "t.platformTranId";
                break;

            case GAME_TRAN_ID:
                sortColumn = "t.gameTranId";
                break;

            case ACCOUNT_ID:
                sortColumn = "t.accountId";
                break;

            case ID:
                sortColumn = "t.id";
                break;

            case GAME_ID:
                sortColumn = "t.gameId";
                break;

            default:
                sortColumn = "t.dateTime";
                break;
        }

        String direction =
                "DESC".equalsIgnoreCase(request.getSortDirection())
                        ? "DESC"
                        : "ASC";

        return " ORDER BY " + sortColumn + " " + direction;
    }



    public long count(TransactionSearchRequestDto request){

        StringBuilder jpql = new StringBuilder()
                .append("SELECT COUNT(t.id)\n")
                .append("FROM AccountTransactionEntity t\n");

        JPQLQueryConditions jpqlQueryConditions = buildSearchQueryCondition(request);
        jpql.append(jpqlQueryConditions.getJpql());



        TypedQuery<Long> query =
                entityManager.createQuery(
                        jpql.toString(),
                        Long.class
                );


        jpqlQueryConditions.getParameters().forEach(query::setParameter);


        return query.getSingleResult();
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
