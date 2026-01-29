package com.bank.star.star.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DataSourceInitializer {

    @Bean
    public CommandLineRunner initPrimaryDatabase(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                // Выполняем скрипты для primary базы (JPA)
                ScriptUtils.executeSqlScript(connection,
                        new ClassPathResource("schema-primary.sql"));
                System.out.println("Primary database schema created successfully");

                // Вставляем тестовые данные
                ScriptUtils.executeSqlScript(connection,
                        new ClassPathResource("data.sql"));
                System.out.println("Primary database data initialized successfully");
            } catch (Exception e) {
                System.err.println("Error initializing primary database: " + e.getMessage());
                // Не прерываем запуск приложения
            }
        };
    }

    @Bean
    public CommandLineRunner initSecondaryDatabase(
            @Qualifier("recommendationsDataSource") DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                // Выполняем скрипты для второй базы
                ScriptUtils.executeSqlScript(connection,
                        new ClassPathResource("schema-racommindation.sql"));
                ScriptUtils.executeSqlScript(connection,
                        new ClassPathResource("data-recommendation.sql"));
                System.out.println("Secondary database initialized successfully");
            } catch (Exception e) {
                System.err.println("Error initializing secondary database: " + e.getMessage());
                // Не прерываем запуск приложения
            }
        };
    }
}