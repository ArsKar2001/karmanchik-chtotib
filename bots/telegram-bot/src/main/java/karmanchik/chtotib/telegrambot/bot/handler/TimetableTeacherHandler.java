package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.entityservice.entity.ChatUser;
import karmanchik.chtotib.entityservice.entity.Lesson;
import karmanchik.chtotib.entityservice.entity.Teacher;
import karmanchik.chtotib.entityservice.enums.BotState;
import karmanchik.chtotib.entityservice.enums.Role;
import karmanchik.chtotib.entityservice.enums.UserState;
import karmanchik.chtotib.entityservice.enums.WeekType;
import karmanchik.chtotib.entityservice.exception.ResourceNotFoundException;
import karmanchik.chtotib.entityservice.repositories.JpaChatUserRepository;
import karmanchik.chtotib.entityservice.repositories.JpaLessonsRepository;
import karmanchik.chtotib.entityservice.repositories.JpaTeacherRepository;
import karmanchik.chtotib.telegrambot.bot.helper.DateHelper;
import karmanchik.chtotib.telegrambot.bot.helper.Helper;
import karmanchik.chtotib.telegrambot.util.TelegramUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;

import static karmanchik.chtotib.telegrambot.bot.Const.CANCEL;
import static karmanchik.chtotib.telegrambot.bot.Const.MESSAGE_SPLIT;

@Log4j2
@Component
@RequiredArgsConstructor
public class TimetableTeacherHandler implements Handler {
    private final JpaTeacherRepository teacherRepository;
    private final JpaChatUserRepository userRepository;
    private final JpaLessonsRepository lessonsRepository;

    public List<PartialBotApiMethod<? extends Serializable>> handle(ChatUser chatUser, String message) throws ResourceNotFoundException {
        return switch (chatUser.getUserState()) {
            case SELECT_TEACHER -> selectTeacherOrAccept(chatUser, message);
            case INPUT_TEXT -> selectTeacher(chatUser, message);
            default -> List.of(
                    cancel(chatUser)
            );
        };
    }

    public static List<PartialBotApiMethod<? extends Serializable>> start(ChatUser chatUser) {
        return List.of(
                TelegramUtil.createMessageTemplate(chatUser)
                        .text("Введите фамилию...").build()
        );
    }

    private List<PartialBotApiMethod<? extends Serializable>> selectTeacher(ChatUser chatUser, String message) {
        List<Teacher> teacherNames = teacherRepository.findAllByName(message.toLowerCase());
        if (!teacherNames.isEmpty()) {
            chatUser.setUserState(UserState.SELECT_TEACHER);
            ChatUser save = userRepository.save(chatUser);

            return sendMessageItIsYou(save,
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

    private List<PartialBotApiMethod<? extends Serializable>> selectTeacherOrAccept(ChatUser chatUser, String message) throws ResourceNotFoundException {
        if (message.equalsIgnoreCase(CANCEL)) {
            chatUser.setUserState(UserState.INPUT_TEXT);
            return start(userRepository.save(chatUser));
        } else if (Helper.isNumeric(message)) {
            int id = Integer.parseInt(message);
            Teacher teacher = teacherRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Teacher.class));
            return List.of(
                    createMessage(teacher, chatUser),
                    cancel(chatUser)
            );
        }
        return Collections.emptyList();
    }

    private PartialBotApiMethod<? extends Serializable> createMessage(Teacher teacher, ChatUser chatUser) {
        WeekType weekType = DateHelper.getWeekType();
        StringBuilder message = new StringBuilder();

        List<Lesson> lessons = lessonsRepository.findByTeacherOrderByPairNumberAsc(teacher);
        message.append("Расписание ").append("<b>").append(teacher.getName()).append("</b>:").append("\n");
        lessons.stream()
                .map(Lesson::getDay)
                .distinct()
                .sorted()
                .forEach(day -> {
                    String displayName = DayOfWeek.of(day).getDisplayName(TextStyle.FULL, Helper.getLocale());
                    message.append(MESSAGE_SPLIT).append("\n")
                            .append("<b>").append(displayName).append("</b>:").append("\n");
                    lessons.stream()
                            .filter(lesson -> lesson.getDay().equals(day))
                            .filter(lesson -> lesson.getWeekType() == weekType || lesson.getWeekType() == WeekType.NONE)
                            .forEach(Helper.getLessonTeacher(message));
                });
        return TelegramUtil.createMessageTemplate(chatUser)
                .text(message.toString()).build();
    }

    private List<PartialBotApiMethod<? extends Serializable>> sendMessageItIsYou(ChatUser chatUser,
                                                                                 InlineKeyboardMarkup markup1,
                                                                                 ReplyKeyboardMarkup markup2) {
        return List.of(
                TelegramUtil.createMessageTemplate(chatUser)
                        .text("Выберите педагога")
                        .replyMarkup(markup1).build(),
                TelegramUtil.createMessageTemplate(chatUser)
                        .text("...")
                        .replyMarkup(markup2).build());
    }

    private List<PartialBotApiMethod<? extends Serializable>> sendMessageNotFound(ChatUser chatUser) {
        String outMessage = "Педагог не найден";
        return List.of(
                TelegramUtil.createMessageTemplate(chatUser)
                        .text(outMessage).build(),
                cancel(chatUser)
        );
    }

    private PartialBotApiMethod<? extends Serializable> cancel(ChatUser chatUser) {
        chatUser.setUserState(UserState.NONE);
        return Helper.mainMessage(userRepository.save(chatUser));
    }

    public BotState operatedBotState() {
        return BotState.AUTHORIZED;
    }

    public List<Role> operatedUserRoles() {
        return List.of(Role.STUDENT);
    }

    public List<UserState> operatedUserSate() {
        return List.of(
                UserState.INPUT_TEXT,
                UserState.SELECT_TEACHER
        );
    }
}
