package karmanchik.chtotib.telegrambot.bot;


import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.enums.BotState;
import karmanchik.chtotib.models.enums.Role;
import karmanchik.chtotib.models.enums.UserState;
import karmanchik.chtotib.models.exception.ResourceNotFoundException;
import karmanchik.chtotib.telegrambot.bot.handler.Handler;
import karmanchik.chtotib.telegrambot.services.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class UpdateReceiver {
    private final List<Handler> handlers;
    private final ChatUserService userService;

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (isMessageWithText(update)) {
                Message message = update.getMessage();
                Long chatId = message.getChatId();
                String userName = message.getChat().getUserName();
                ChatUser chatUser = userService.findOrCreate(chatId, userName);
                chatUser.setUserName(userName);
                log.info("ChatUser - {}", chatUser);
                return getHandlerByUser(chatUser).handle(chatUser, message.getText());
            } else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                Long chatId = callbackQuery.getMessage().getChatId();
                String userName = callbackQuery.getMessage().getChat().getUserName();
                ChatUser chatUser = userService.findOrCreate(chatId, userName);
                chatUser.setUserName(userName);
                log.info("ChatUser - {}", chatUser);
                return getHandlerByUser(chatUser).handle(chatUser, callbackQuery.getData());
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException | ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Определяет обработчик для пользователя по его {@link Role}, {@link UserState} и {@link BotState}
     * @param chatUser Пользователь чата
     * @return Обработчик
     */
    private Handler getHandlerByUser(ChatUser chatUser) {
        return handlers.stream()
                .filter(handler -> handler.operatedBotState() == chatUser.getBotState())
                .filter(handler -> handler.operatedUserRoles().stream()
                        .anyMatch(role -> role == chatUser.getRole()))
                .filter(handler -> handler.operatedUserSate().stream()
                        .anyMatch(state -> state == chatUser.getUserState()))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    /**
     * Проверяет, является ли входящее сообщение пользователя текстовым
     * @param update
     * @return
     */
    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
