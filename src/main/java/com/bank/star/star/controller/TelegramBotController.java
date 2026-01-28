package com.bank.star.star.controller;

import com.bank.star.star.service.TelegramBotService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> handleWebhook(@RequestBody TelegramUpdate update) {
        if (update.getMessage() != null && update.getMessage().getText() != null) {
            String response = telegramBotService.processCommand(update.getMessage().getText());
            return ResponseEntity.ok(new TelegramResponse(response));
        }
        return ResponseEntity.ok().build();
    }

    // Для тестирования через GET
    @GetMapping("/process")
    public String processCommand(@RequestParam String command) {
        return telegramBotService.processCommand(command);
    }

    // DTO классы для Telegram API
    static class TelegramUpdate {
        private Message message;

        public Message getMessage() { return message; }
        public void setMessage(Message message) { this.message = message; }
    }

    static class Message {
        private String text;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    static class TelegramResponse {
        private String text;

        public TelegramResponse(String text) { this.text = text; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}