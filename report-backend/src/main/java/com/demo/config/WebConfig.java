package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@EnableWebMvc
@ComponentScan("com.demo")
@Import(HibernateConfig.class)
public class WebConfig {

    @Bean
    public InternalResourceViewResolver viewResolver() {

        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();

        resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");

        return resolver;
    }

}