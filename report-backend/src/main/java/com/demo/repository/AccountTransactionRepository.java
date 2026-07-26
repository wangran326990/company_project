package com.demo.repository;

import com.demo.entity.AccountTransactionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountTransactionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<AccountTransactionEntity> findAll() {
        return entityManager
                .createQuery("select accountTransaction " +
                        "from AccountTransactionEntity accountTransaction",
                        AccountTransactionEntity.class).getResultList();

    }
}
