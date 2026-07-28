package com.demo.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.demo.repository"
})
public class TestHibernateConfig {


    @Autowired
    private Environment env;

    private static final Logger logger =
            LoggerFactory.getLogger(WebConfig.class);

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource ds =
                new DriverManagerDataSource();
        String driverClassName = env.getProperty("hibernate.connection.driver_class");
        driverClassName = driverClassName != null
                ? driverClassName
                : "com.mysql.cj.jdbc.Driver";
        logger.info("hibernate.connection.driver_class:{}", driverClassName);

        ds.setDriverClassName(driverClassName);

        String url = "jdbc:mysql://localhost:3306/demo_test";


        logger.info("hibernate.connection.url:{}", url);
        ds.setUrl(url);

        String username = env.getProperty("hibernate.connection.username");
        username = username != null
                ? username
                : "root";

        logger.info("hibernate.connection.username:{}", username);
        ds.setUsername(username);

        String password = env.getProperty("hibernate.connection.password");
        password = password != null
                ? username
                : "root";

        logger.info("hibernate.connection.password:{}", password);
        ds.setPassword(password);


        return ds;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource) {


        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();


        emf.setDataSource(dataSource);


        emf.setPackagesToScan(
                "com.demo.entity"
        );


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