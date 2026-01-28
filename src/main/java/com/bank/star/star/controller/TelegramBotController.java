package com.bank.star.star.controller;

import com.bank.star.star.service.TelegramBotService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
public class TelegramBotController {

    private final TelegramBotService telegramBotService;

    public TelegramBotController(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @GetMapping("/help")
    public String getHelp() {
        return telegramBotService.getHelpMessage();
    }

    @GetMapping("/recommend/{username}")
    public String getRecommendationsForUser(@PathVariable String username) {
        return telegramBotService.getRecommendationsForUser(username);
    }

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody String updateJson) {
        // Здесь можно парсить JSON и обрабатывать команды
        // Для MVP достаточно простых REST эндпоинтов
        return "Webhook received";
    }
}