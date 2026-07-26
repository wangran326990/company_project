package com.demo.repository;

import com.demo.config.TestHibernateConfig;
import com.demo.entity.AccountTransactionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestHibernateConfig.class)
public class AccountTransactionRepositoryTests {

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;
    @Test
    @Sql(
        scripts={
                "/sql/schema.sql",
                "/sql/data.sql"
        }
    )
    @Transactional
    void testList() {
        List<AccountTransactionEntity> transactions =
                accountTransactionRepository.findAll();
        assertNotNull(transactions);
        assertEquals(6977, transactions.size());
    }

}
