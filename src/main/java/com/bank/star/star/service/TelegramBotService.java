package com.bank.star.star.service;

import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.DTO.RecommendationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TelegramBotService {

    private final RecommendationService recommendationService;
    private final JdbcTemplate jdbcTemplate;

    public TelegramBotService(RecommendationService recommendationService,
                              @Qualifier("recommendationsJdbcTemplate")
                              JdbcTemplate jdbcTemplate) {
        this.recommendationService = recommendationService;
        this.jdbcTemplate = jdbcTemplate;


        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            // Проверяем существование таблицы users
            String checkTableSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'USERS'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTableSql, Integer.class);

            if (tableCount == null || tableCount == 0) {
                System.out.println("Таблицы не найдены.");
            }
        } catch (Exception e) {
            System.err.println("База данных не инициализирована: " + e.getMessage());
            System.err.println("Пожалуйста, создайте таблицы вручную");
        }
    }

    public String getHelpMessage() {
        return """
            🏦 *Добро пожаловать в Star Bank Recommendations Bot!*
            
            *Доступные команды:*
            /start - Начало работы
            /help - Показать это сообщение
            /recommend <username> - Получить рекомендации
            
            *Примеры:*
            `/recommend sheron.berge`
            `/recommend test.user`
            `/recommend john.doe`
            
            *Тестовые пользователи:*
            • sheron.berge
            • test.user
            • john.doe
            """;
    }

    public String getRecommendationsForUser(String username) {
        try {

            try {
                jdbcTemplate.execute("SELECT 1 FROM users LIMIT 1");
            } catch (Exception e) {
                return "❌ *Ошибка:* База данных не инициализирована.\n\n" +
                        "Таблица users не найдена. Пожалуйста:\n" +
                        "1. Откройте H2 Console: http://localhost:8080/h2-console\n" +
                        "2. Подключитесь к базе: jdbc:h2:file:./data/recommendations\n";
            }


            String sql = "SELECT id FROM users WHERE username = ?";
            List<String> userIds = jdbcTemplate.queryForList(sql, String.class, username);

            if (userIds.isEmpty()) {
                return "❌ *Пользователь не найден*\n\n" +
                        "Пользователь с именем `" + username + "` не существует.\n" +
                        "Доступные пользователи:\n" +
                        "• sheron.berge\n" +
                        "• test.user\n" +
                        "• john.doe";
            }

            if (userIds.size() > 1) {
                return "⚠️ *Найдено несколько пользователей* с таким именем";
            }

            UUID userId = UUID.fromString(userIds.get(0));

            // Получаем имя пользователя
            String userSql = "SELECT first_name, last_name FROM users WHERE id = ?";
            String userName = jdbcTemplate.query(
                    userSql,
                    rs -> {
                        if (rs.next()) {
                            String firstName = rs.getString("first_name");
                            String lastName = rs.getString("last_name");
                            return (firstName != null ? firstName : "") + " " +
                                    (lastName != null ? lastName : "");
                        }
                        return "Клиент";
                    },
                    userId
            );


            RecommendationResponse response = recommendationService.getRecommendations(userId);


            StringBuilder message = new StringBuilder();
            message.append("👋 *Здравствуйте, ").append(userName.trim()).append("!*\n\n");
            message.append("🎯 *Персональные рекомендации для вас:*\n\n");

            if (response.getRecommendations().isEmpty()) {
                message.append("📭 *Пока нет рекомендаций*\n");
                message.append("Проверьте позже или обратитесь в отделение банка.");
            } else {
                int counter = 1;
                for (ProductRecommendation recommendation : response.getRecommendations()) {
                    message.append(counter++).append(". *").append(recommendation.getName()).append("*\n");
                    message.append("   📝 ").append(recommendation.getText()).append("\n");
                    message.append("   🆔 ID: `").append(recommendation.getId()).append("`\n\n");
                }
                message.append("\n✨ *Выберите продукт, который вам подходит!*");
            }

            return message.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ *Ошибка при обработке запроса*\n\n" +
                    "Детали: " + e.getMessage() + "\n" +
                    "Пожалуйста, попробуйте позже или обратитесь в поддержку.";
        }
    }

    public String processCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return getHelpMessage();
        }

        String trimmedCommand = command.trim().toLowerCase();

        if (trimmedCommand.startsWith("/start") || trimmedCommand.startsWith("/help")) {
            return getHelpMessage();
        } else if (trimmedCommand.startsWith("/recommend")) {
            String[] parts = command.split(" ", 2);
            if (parts.length < 2) {
                return "❌ *Использование:* /recommend <имя пользователя>\n\n" +
                        "*Пример:* `/recommend sheron.berge`";
            }
            String username = parts[1].trim();
            return getRecommendationsForUser(username);
        } else {
            return "❌ *Неизвестная команда*\n\n" +
                    "Используйте /help для списка команд";
        }
    }
}