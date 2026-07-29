package com.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * enable spring transactional support
 */
@EnableTransactionManagement
public class HibernateConfig {
    @Autowired
    private Environment env;

    private static final Logger logger =
            LoggerFactory.getLogger(WebConfig.class);

    /**
     * A DataSource represents your database connection provider.
     * @return
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
//        DriverManagerDataSource ds =
//                new DriverManagerDataSource();
        String driverClassName = env.getProperty("hibernate.connection.driver_class");
        driverClassName = driverClassName != null
                ? driverClassName
                : "com.mysql.cj.jdbc.Driver";
        logger.info("hibernate.connection.driver_class:{}", driverClassName);

        ds.setDriverClassName(driverClassName);

        String url = env.getProperty("MYSQL_CONNECT_URL");
        String name = env.getProperty("MYSQL_DB_NAME");
        url = url != null
                ? url
                : "jdbc:mysql://localhost:3306/";
        name = name != null ? name: "demo";
        url = url + name;

        logger.info("hibernate.connection.url:{}", url);
        ds.setJdbcUrl(url);

        String username = env.getProperty("hibernate.connection.username");
        username = (username != null)
                ? username
                : "root";

        logger.info("hibernate.connection.username:{}", username);
        ds.setUsername(username);

        String password = env.getProperty("hibernate.connection.password");
        password = (password != null)
                ? password
                : "root";

        logger.info("hibernate.connection.password:{}", password);
        ds.setPassword(password);

        // Maximum number of connections in the pool
        ds.setMaximumPoolSize(10);


        // Minimum number of idle connections kept ready 5 connect minimal
        ds.setMinimumIdle(5);


        // Maximum time to wait for a connection
        // 30 seconds
        ds.setConnectionTimeout(30000);


        // Close idle connections after 10 minutes
        ds.setIdleTimeout(600000);


        // Recycle connections after 30 minutes
        // Prevent stale MySQL connections
        ds.setMaxLifetime(1800000);


        logger.info(
                "HikariCP connection pool initialized"
        );

        return ds;
    }

    /**
     * This creates Hibernate's EntityManagerFactory which will assign entity manager instance to each thread
     * @param dataSource
     * @return
     */

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource) {


        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();


        emf.setDataSource(dataSource);


        emf.setPackagesToScan(
                "com.demo.entity"
        );

        // set hibernate to the JPA implementation provider
        emf.setJpaVendorAdapter(
                new HibernateJpaVendorAdapter()
        );


        Properties props = new Properties();

        //Can read from the environment variables
        props.put(
                "hibernate.show_sql", "true"
        );


        props.put(
                "hibernate.format_sql", "true"
        );

        props.put("hibernate.dialect","org.hibernate.dialect.MySQLDialect");

        props.put(
                "hibernate.hbm2ddl.auto",
                "none"
        );


        emf.setJpaProperties(props);


        return emf;
    }


    @Bean
    public JpaTransactionManager transactionManager(
            EntityManagerFactory emf) {

        return new JpaTransactionManager(emf);
    }
}
