package com.darya.compiment.telegram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final String BOT_NAME = "BotForMydDarlingGirlfriend_bot";
    private static final String DELIMETER = "\\|";
    @Value("${bot.chatId}")
    private Long daryaChatId;
    private static final Map<MoodType, String> MOOD_TYPE_STRING_MAP = Map.of(
            MoodType.GOOD, "good_mood.txt",
            MoodType.MAD, "mad_mood.txt",
            MoodType.TYPICAL, "typical_mood.txt"
    );

    public TelegramBot(@Value("${bot.token}") String token) {
        super(token);
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var msg = update.getMessage();
            var chatId = msg.getChatId();
            daryaChatId = chatId;
            try {
                checkMessage(msg.getText());
                sendNotification(String.valueOf(chatId), determineMod(msg.getText()));
            } catch (IllegalArgumentException e) {
                var request = new SendMessage(String.valueOf(chatId), e.getMessage());
                execute(request);
            }
        }
    }

    private void checkMessage(String message) {
        Arrays.stream(MoodType.values())
                .map(MoodType::getDisplayName)
                .filter(type -> type.equalsIgnoreCase(message.trim()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Sorry pussy I do not understand what u mean. If u want compliment just type your today mood) (Possible moods: " + Arrays.toString(MoodType.values())));
    }

    private MoodType determineMod(String message) {
        return Arrays.stream(MoodType.values())
                .filter(moodType -> moodType.getDisplayName().equalsIgnoreCase(message.trim()))
                .findFirst().get();
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void sendByScheduler() throws TelegramApiException {
        if (daryaChatId == null) {
            return;
        }
        String initialMessage = "Hi my darling Draya, how are u feeling today? (Possible answers: " + Arrays.toString(MoodType.values()) + ";";
        var request = new SendMessage(daryaChatId.toString(), initialMessage);
        execute(request);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    private void sendNotification(String chatId, MoodType moodType) throws TelegramApiException, IOException {
        String allContent = Files.readString(Path.of("src/main/resources/" + MOOD_TYPE_STRING_MAP.get(moodType)));
        String[] allCompliments = allContent.split(DELIMETER);
        Integer complimentNumber = allCompliments.length;
        var request = new SendMessage(chatId, allCompliments[RandomUtils.nextInt(0, complimentNumber)]);
        execute(request);
    }
}
