package com.bank.star.star.configuration;

import com.bank.star.star.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {

    @Value("${telegram.bot.token:8253067700:AAE-jj3vJOvuCwYMDTXQo6fFEZby4ogsEhs}")
    private String botToken;

    @Value("${telegram.bot.username:star_bank_recommendations_bot}")
    private String botUsername;

    @Value("${telegram.bot.enabled:true}")
    private boolean botEnabled;

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBotService telegramBotService) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        if (botEnabled) {
            botsApi.registerBot(new StarBankRecommendationsBot(botToken, botUsername, telegramBotService));
        }
        return botsApi;
    }

    public static class StarBankRecommendationsBot extends TelegramLongPollingBot {
        private final String botUsername;
        private final TelegramBotService telegramBotService;

        public StarBankRecommendationsBot(String botToken, String botUsername, TelegramBotService telegramBotService) {
            super(botToken);
            this.botUsername = botUsername;
            this.telegramBotService = telegramBotService;
        }

        @Override
        public void onUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                String chatId = update.getMessage().getChatId().toString();

                // Обработка команды
                String response = telegramBotService.processCommand(messageText);

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(response);
                message.enableMarkdown(true);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public String getBotUsername() {
            return botUsername;
        }
    }

}