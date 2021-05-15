package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.entityservice.entity.ChatUser;
import karmanchik.chtotib.entityservice.exception.ResourceNotFoundException;
import karmanchik.chtotib.telegrambot.bot.command.MainCommand;
import karmanchik.chtotib.telegrambot.bot.helper.Helper;
import karmanchik.chtotib.telegrambot.util.TelegramUtil;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class MainHandler implements Handler {
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(ChatUser chatUser, String message) throws ResourceNotFoundException {
        if (MainCommand.isCommand(message)) {
            switch (Objects.requireNonNull(MainCommand.get(message))) {
                case COMMAND_1:
                    return getTimetableNextDay(chatUser);
                case COMMAND_2:
                    return getTimetableFull(chatUser);
                case COMMAND_3:
                    return getTimetableOther(chatUser);
                case COMMAND_4:
                    return editProfile(chatUser);
                default:
                    throw new IllegalArgumentException();
            }
        }
        return List.of(
                Helper.mainMessage(chatUser)
        );
    }

    protected abstract List<PartialBotApiMethod<? extends Serializable>> editProfile(ChatUser chatUser);

    protected abstract List<PartialBotApiMethod<? extends Serializable>> getTimetableOther(ChatUser chatUser);

    protected abstract List<PartialBotApiMethod<? extends Serializable>> getTimetableFull(ChatUser chatUser);

    protected abstract List<PartialBotApiMethod<? extends Serializable>> getTimetableNextDay(ChatUser chatUser);
}
