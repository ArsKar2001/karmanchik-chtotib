package karmanchik.chtotib.telegrambot.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    @Value("${bot.token}")
    @Getter
    private String botToken;

    @Value("${bot.name}")
    @Getter
    private String botUsername;

    private final UpdateReceiver updateReceiver;

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Получили новый update: {}", update.toString());
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);
        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(this::executeWithExceptionCheck);
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        updates.parallelStream().forEach(update -> {
            log.info("Получили новый update: {}", update.toString());
            List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);
            if (messagesToSend != null && !messagesToSend.isEmpty()) {
                messagesToSend.forEach(this::executeWithExceptionCheck);
            }
        });
    }

    private void executeWithExceptionCheck(Object response) {
        log.info("Новый объект для отправки: {}", response.toString());
        try {
            execute((BotApiMethod<? extends Serializable>) response);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }
}
