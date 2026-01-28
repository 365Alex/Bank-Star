package com.bank.star.star.service;

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
    }

    public String getHelpMessage() {
        return """
            Добро пожаловать в бот рекомендаций банка Star!
            
            Доступные команды:
            /recommend <username> - получить персональные рекомендации
            
            Пример: /recommend sheron.berge
            """;
    }

    public String getRecommendationsForUser(String username) {
        try {
            // Ищем пользователя по имени
            String sql = "SELECT id FROM users WHERE username = ?";
            List<String> userIds = jdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> rs.getString("id"),
                    username
            );

            if (userIds.isEmpty()) {
                return "Пользователь не найден";
            }

            if (userIds.size() > 1) {
                return "Найдено несколько пользователей с таким именем";
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
                        return "Пользователь";
                    },
                    userId
            );

            // Получаем рекомендации
            RecommendationResponse response = recommendationService.getRecommendations(userId);

            // Форматируем ответ
            StringBuilder message = new StringBuilder();
            message.append("Здравствуйте, ").append(userName.trim()).append("!\n\n");
            message.append("Новые продукты для вас:\n\n");

            if (response.getRecommendations().isEmpty()) {
                message.append("Пока нет персональных рекомендаций. Проверьте позже!");
            } else {
                int counter = 1;
                for (var recommendation : response.getRecommendations()) {
                    message.append(counter++).append(". ").append(recommendation.getName()).append("\n");
                    message.append("   ").append(recommendation.getText()).append("\n\n");
                }
            }

            return message.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Произошла ошибка при обработке запроса";
        }
    }

    public String processCommand(String command) {
        if (command.startsWith("/start") || command.startsWith("/help")) {
            return getHelpMessage();
        } else if (command.startsWith("/recommend")) {
            String[] parts = command.split(" ", 2);
            if (parts.length < 2) {
                return "Использование: /recommend <имя пользователя>";
            }
            String username = parts[1].trim();
            return getRecommendationsForUser(username);
        } else {
            return "Неизвестная команда. Используйте /help для справки.";
        }
    }
}