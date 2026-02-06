package com.bank.star.star.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
public class DataSourceInitializer {

    @Bean
    @ConfigurationProperties("application.recommendations-db")
    public DataSourceProperties recommendationsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "recommendationsDataSource")
    public HikariDataSource recommendationsDataSource(
            @Qualifier("recommendationsDataSourceProperties")
            DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public JdbcTemplate recommendationsJdbcTemplate(
            @Qualifier("recommendationsDataSource") HikariDataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }
}
