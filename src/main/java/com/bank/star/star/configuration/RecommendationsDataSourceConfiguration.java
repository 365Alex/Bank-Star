package com.bank.star.star.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class RecommendationsDataSourceConfiguration {

    @Value("${application.recommendations-db.url}")
    private String url;

    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
                .url(url)
                .driverClassName("org.h2.Driver")
                .type(HikariDataSource.class)
                .build();
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(
            @Qualifier("recommendationsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource defaultDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "defaultJdbcTemplate")
    public JdbcTemplate defaultJdbcTemplate(@Qualifier("defaultDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @PostConstruct
    public void initTransactionDatabase() {
        try {
            // Создаем отдельный DataSource для инициализации
            HikariDataSource dataSource = DataSourceBuilder.create()
                    .url(url)
                    .driverClassName("org.h2.Driver")
                    .type(HikariDataSource.class)
                    .build();

            try (Connection connection = dataSource.getConnection()) {
                // Запускаем SQL скрипт для инициализации тестовых данных
                ScriptUtils.executeSqlScript(connection,
                        new ClassPathResource("init-transaction-db.sql"));
            } finally {
                dataSource.close();
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize transaction database: " + e.getMessage());
            e.printStackTrace();
            // Игнорируем ошибку, так как база может быть уже инициализирована
        }
    }
}