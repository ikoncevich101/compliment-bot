package com.darya.compiment.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final String BOT_NAME = "BotForMydDarlingGirlfriend_bot";

    public TelegramBot(@Value("${bot.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var msg = update.getMessage();
            var chatId = msg.getChatId();
            try {
                var reply = msg.getText();
                sendNotification(String.valueOf(chatId), reply);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    private void sendNotification(String chatId, String msg) throws TelegramApiException {
        var request = new SendMessage(chatId, msg);
        execute(request);
    }
}
