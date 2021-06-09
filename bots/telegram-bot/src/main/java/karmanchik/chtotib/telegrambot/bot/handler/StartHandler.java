package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.entityservice.entity.ChatUser;
import karmanchik.chtotib.entityservice.enums.BotState;
import karmanchik.chtotib.entityservice.enums.Role;
import karmanchik.chtotib.entityservice.enums.UserState;
import karmanchik.chtotib.entityservice.repositories.JpaChatUserRepository;
import karmanchik.chtotib.telegrambot.bot.Const;
import karmanchik.chtotib.telegrambot.util.HelperUtils;
import karmanchik.chtotib.telegrambot.util.TelegramUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Обработчик для новых пользователей чат-бота
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {
    private final JpaChatUserRepository userRepository;

    @Value("${bot.name}")
    private String botUsername;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(ChatUser chatUser, String message) {
        UserState state = chatUser.getUserState();
        switch (state) {
            case NONE:
                return welcomeMessage(chatUser);
            case START:
                return startMessage(chatUser);
            default:
                return Collections.emptyList();
        }
    }

    private List<PartialBotApiMethod<? extends Serializable>> startMessage(ChatUser chatUser) {
        chatUser.setBotState(BotState.REG);
        chatUser.setUserState(UserState.SELECT_ROLE);
        return List.of(HelperUtils.selectRole(userRepository.save(chatUser)));
    }

    private List<PartialBotApiMethod<? extends Serializable>> welcomeMessage(ChatUser chatUser) {
        chatUser.setUserState(UserState.START);
        userRepository.save(chatUser);
        log.info("Set chatUser({}): user_state - {}", chatUser.getId(), UserState.START);

        return List.of(TelegramUtil.createMessageTemplate(chatUser)
                .text(String.format("Привет <b>%s</b>!%nМеня зовут @%s :D%n" +
                        "Я был создан для работы со студентами и педагогами ЧТОТиБ.%n" +
                        "Давай создадим твою анкету?!", chatUser.getUserName(), botUsername))
                .replyMarkup(TelegramUtil.createReplyKeyboardMarkup()
                        .keyboardRow(TelegramUtil.createKeyboardRow(List.of(Const.CREATE).toArray()))
                        .build())
                .build());
    }

    @Override
    public BotState operatedBotState() {
        return BotState.START;
    }

    @Override
    public List<Role> operatedUserRoles() {
        return List.of(Role.NONE);
    }

    @Override
    public List<UserState> operatedUserSate() {
        return List.of(
                UserState.NONE,
                UserState.START
        );
    }
}
