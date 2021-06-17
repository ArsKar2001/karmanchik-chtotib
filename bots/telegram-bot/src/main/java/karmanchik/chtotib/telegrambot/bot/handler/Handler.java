package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.enums.BotState;
import karmanchik.chtotib.models.enums.Role;
import karmanchik.chtotib.models.enums.UserState;
import karmanchik.chtotib.models.exception.ResourceNotFoundException;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

/**
 * Интерфейис обработки входящих сообщений пользователя
 */
public interface Handler {
    /**
     * Основной метод обработки входящих сообщений от пользователей
     * @param chatUser Пользователь
     * @param message Сообщение пользователя
     * @return Список ответных сообщений чат-бота пользователю
     * @throws ResourceNotFoundException Ошибка в случае, если не найдены данные в БД по запросу пользователя
     */
    List<PartialBotApiMethod<? extends Serializable>> handle(ChatUser chatUser, String message) throws ResourceNotFoundException;

    /**
     * Определение статуса чат-бота для обработчика
     * @return Статус час-бота
     */
    BotState operatedBotState();

    /**
     * Определение коллекции Role пользователя для обработчика
     * @return Коллекция Role
     */
    List<Role> operatedUserRoles();

    /**
     * Определение коллекции статусов пользователя пользователя для обработчика
     * @return Коллекция статусов пользователя
     */
    List<UserState> operatedUserSate();
}
