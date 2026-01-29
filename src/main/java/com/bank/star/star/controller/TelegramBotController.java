package com.bank.star.star.controller;

import com.bank.star.star.DTO.ProductRecommendation;
import com.bank.star.star.DTO.RecommendationResponse;
import com.bank.star.star.service.RecommendationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bot")
public class TelegramBotController {

    private final RecommendationService recommendationService;
    private final JdbcTemplate jdbcTemplate;

    public TelegramBotController(RecommendationService recommendationService,
                                 @Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.recommendationService = recommendationService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/help")
    public String getHelp() {
        return "Добро пожаловать в бот рекомендаций банка Star!\n\n" +
                "Доступные команды:\n" +
                "/recommend <имя пользователя> - получить персональные рекомендации\n\n" +
                "Пример: /recommend sheron.berge\n\n" +
                "Для получения рекомендаций вам нужно быть зарегистрированным пользователем нашего банка.";
    }

    @GetMapping("/recommend/{username}")
    public String getRecommendationsForUser(@PathVariable String username) {
        try {
            // Ищем пользователя по имени
            String sql = "SELECT id FROM users WHERE username = ?";
            List<String> userIds = jdbcTemplate.queryForList(sql, String.class, username);

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
                    userId.toString()  // конвертируем UUID в строку
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
                for (ProductRecommendation recommendation : response.getRecommendations()) {
                    message.append(counter++).append(". ").append(recommendation.getName()).append("\n");
                    message.append("   ").append(recommendation.getText()).append("\n\n");
                }
            }

            return message.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Произошла ошибка при обработке запроса: " + e.getMessage();
        }
    }
}