package karmanchik.chtotib.telegrambot.bot;


import karmanchik.chtotib.entityservice.entity.ChatUser;
import karmanchik.chtotib.entityservice.enums.BotState;
import karmanchik.chtotib.entityservice.enums.Role;
import karmanchik.chtotib.entityservice.enums.UserState;
import karmanchik.chtotib.entityservice.exception.ResourceNotFoundException;
import karmanchik.chtotib.entityservice.repositories.JpaChatUserRepository;
import karmanchik.chtotib.telegrambot.bot.handler.Handler;
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
    private final JpaChatUserRepository userRepository;

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (isMessageWithText(update)) {
                Message message = update.getMessage();
                Long chatId = message.getChatId();
                String userName = message.getChat().getUserName();
                ChatUser chatUser = userRepository.findByChatIdAndUserName(chatId, userName)
                        .orElseGet(() -> userRepository.save(ChatUser.builder()
                                .chatId(chatId)
                                .userName(userName)
                                .botState(BotState.START)
                                .userState(UserState.NONE)
                                .role(Role.NONE)
                                .build()));
                log.info("ChatUser - {}", chatUser);
                return getHandlerByUser(chatUser).handle(chatUser, message.getText());
            } else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                Long chatId = callbackQuery.getMessage().getChatId();
                String userName = callbackQuery.getMessage().getChat().getUserName();
                ChatUser chatUser = userRepository.findByChatIdAndUserName(chatId, userName)
                        .orElseGet(() -> userRepository.save(ChatUser.builder()
                                .chatId(chatId)
                                .userName(userName)
                                .botState(BotState.START)
                                .userState(UserState.NONE)
                                .role(Role.NONE)
                                .build()));
                log.info("ChatUser - {}", chatUser);
                return getHandlerByUser(chatUser).handle(chatUser, callbackQuery.getData());
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException | ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

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

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
