package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.entity.Group;
import karmanchik.chtotib.models.entity.Teacher;
import karmanchik.chtotib.models.enums.BotState;
import karmanchik.chtotib.models.enums.Role;
import karmanchik.chtotib.models.enums.UserState;
import karmanchik.chtotib.models.exception.ResourceNotFoundException;
import karmanchik.chtotib.models.repositories.JpaChatUserRepository;
import karmanchik.chtotib.models.repositories.JpaGroupRepository;
import karmanchik.chtotib.models.repositories.JpaTeacherRepository;
import karmanchik.chtotib.telegrambot.bot.Course;
import karmanchik.chtotib.telegrambot.util.HelperUtils;
import karmanchik.chtotib.telegrambot.util.TelegramUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static karmanchik.chtotib.telegrambot.bot.Const.*;

/**
 * Обработчик создания анкеты пользователя чат-бота
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class RegistrationHandler implements Handler {
    private final JpaChatUserRepository userRepository;
    private final JpaGroupRepository groupRepository;
    private final JpaTeacherRepository teacherRepository;

    private final HelperUtils helperUtils;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(ChatUser chatUser, String message) {
        try {
            UserState state = chatUser.getUserState();
            switch (state) {
                case SELECT_COURSE:
                    return helperUtils.selectGroup(chatUser, message);
                case SELECT_GROUP:
                    return selectGroupOrAccept(chatUser, message);
                case SELECT_ROLE:
                    return switchRole(chatUser, message);
                case INPUT_TEXT:
                    return selectTeacher(chatUser, message);
                case SELECT_TEACHER:
                    return selectTeacherOrAccept(chatUser, message);
                default:
                    return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return List.of(
                    TelegramUtil.createMessageTemplate(chatUser)
                            .text("<b>Ошибка</b>: " + e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Метод для обработки роли, выбранной пользователем
     * @param chatUser Пользователь
     * @param message Сообщение пользователя
     * @return Список ответных сообщений чат-бота
     */
    private List<PartialBotApiMethod<? extends Serializable>> switchRole(ChatUser chatUser, String message) {
        if (message.equalsIgnoreCase(ROLE_STUDENT)) {
            chatUser.setUserState(UserState.SELECT_COURSE);
            chatUser.setRole(Role.STUDENT);
            ChatUser save = userRepository.save(chatUser);
            return HelperUtils.createSelectCourseButtonPanel(save);
        } else if (message.equalsIgnoreCase(ROLE_TEACHER)) {
            chatUser.setRole(Role.TEACHER);
            return inputTeacherName(chatUser);
        }
        return Collections.emptyList();
    }

    /**
     *
     * @param chatUser Пользователь
     * @return Список ответных сообщений чат-бота
     */
    private List<PartialBotApiMethod<? extends Serializable>> inputTeacherName(ChatUser chatUser) {
        chatUser.setUserState(UserState.INPUT_TEXT);
        return List.of(HelperUtils.inputMessage(userRepository.save(chatUser),
                "Введите фамилию..."));
    }

    /**
     * @param chatUser Пользователь
     * @param message Сообщение пользователя
     * @return Список ответных сообщений чат-бота
     * @throws ResourceNotFoundException Ошибка, если выбранная группа не найдена в БД
     */
    private List<PartialBotApiMethod<? extends Serializable>> selectGroupOrAccept(ChatUser chatUser, String message) throws ResourceNotFoundException {
        if (Course.isCourse(message)) {
            return helperUtils.selectGroup(chatUser, message);
        } else if (HelperUtils.isNumeric(message)) {
            int id = Integer.parseInt(message);
            log.info("Find group by id: {} ...", id);
            chatUser.setGroup(groupRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Group.class)));
            return List.of(
                    accept(chatUser)
            );
        }
        return Collections.emptyList();
    }

    /**
     * @param chatUser Пользователь
     * @param message Сообщение пользователя
     * @return Список ответных сообщений чат-бота
     * @throws ResourceNotFoundException Ошибка, если выбранный педагог не найдена в БД
     */
    private List<PartialBotApiMethod<? extends Serializable>> selectTeacherOrAccept(ChatUser chatUser, String message) throws ResourceNotFoundException {
        if (message.equalsIgnoreCase(CANCEL)) {
            return inputTeacherName(chatUser);
        } else if (HelperUtils.isNumeric(message)) {
            int id = Integer.parseInt(message);
            chatUser.setTeacher(teacherRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class)));
            return List.of(
                    accept(chatUser)
            );
        }
        return Collections.emptyList();
    }

    private List<PartialBotApiMethod<? extends Serializable>> selectTeacher(ChatUser chatUser, String message) {
        List<Teacher> teacherNames = teacherRepository.findAllByName(message.toLowerCase());
        if (!teacherNames.isEmpty()) {
            chatUser.setUserState(UserState.SELECT_TEACHER);

            return sendMessageItIsYou(userRepository.save(chatUser),
                    InlineKeyboardMarkup.builder()
                            .keyboard(TelegramUtil.createInlineKeyboardButtons(teacherNames, 2))
                            .build(),
                    TelegramUtil.createReplyKeyboardMarkup()
                            .keyboardRow(TelegramUtil.createKeyboardRow(new Object[]{CANCEL}))
                            .oneTimeKeyboard(true)
                            .build()
            );
        }
        return sendMessageNotFound(chatUser);
    }


    private List<PartialBotApiMethod<? extends Serializable>> sendMessageItIsYou(ChatUser chatUser,
                                                                                 InlineKeyboardMarkup markup1,
                                                                                 ReplyKeyboardMarkup markup2) {
        return List.of(
                TelegramUtil.createMessageTemplate(chatUser)
                        .text("Выберите педагога:")
                        .replyMarkup(markup1)
                        .build(),
                TelegramUtil.createMessageTemplate(chatUser)
                        .text("...")
                        .replyMarkup(markup2)
                        .build());
    }

    private List<PartialBotApiMethod<? extends Serializable>> sendMessageNotFound(ChatUser chatUser) {
        String outMessage = "Никого не нашел :(";
        return List.of(
                TelegramUtil.createMessageTemplate(chatUser)
                        .text(outMessage)
                        .build(),
                cancel(chatUser)
        );
    }

    private PartialBotApiMethod<? extends Serializable> accept(ChatUser chatUser) {
        chatUser.setUserState(UserState.NONE);
        chatUser.setBotState(BotState.AUTHORIZED);
        return HelperUtils.mainMessage(userRepository.save(chatUser));
    }

    private PartialBotApiMethod<? extends Serializable> cancel(ChatUser chatUser) {
        chatUser.setUserState(UserState.SELECT_ROLE);
        chatUser.setBotState(BotState.REG);
        return HelperUtils.selectRole(userRepository.save(chatUser));
    }

    @Override
    public BotState operatedBotState() {
        return BotState.REG;
    }

    @Override
    public List<Role> operatedUserRoles() {
        return List.of(
                Role.NONE,
                Role.STUDENT,
                Role.TEACHER
        );
    }

    @Override
    public List<UserState> operatedUserSate() {
        return List.of(
                UserState.SELECT_ROLE,
                UserState.SELECT_COURSE,
                UserState.SELECT_TEACHER,
                UserState.SELECT_GROUP,
                UserState.INPUT_TEXT
        );
    }
}
