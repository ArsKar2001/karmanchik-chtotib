package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.entity.Lesson;
import karmanchik.chtotib.models.entity.Teacher;
import karmanchik.chtotib.models.enums.BotState;
import karmanchik.chtotib.models.enums.Role;
import karmanchik.chtotib.models.enums.UserState;
import karmanchik.chtotib.models.enums.WeekType;
import karmanchik.chtotib.models.exception.ResourceNotFoundException;
import karmanchik.chtotib.telegrambot.services.ChatUserService;
import karmanchik.chtotib.telegrambot.services.LessonsService;
import karmanchik.chtotib.telegrambot.services.TeacherService;
import karmanchik.chtotib.telegrambot.util.DateHelperUtils;
import karmanchik.chtotib.telegrambot.util.HelperUtils;
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
    private final TeacherService teacherService;
    private final ChatUserService userService;
    private final LessonsService lessonsService;

    public List<PartialBotApiMethod<? extends Serializable>> handle(ChatUser chatUser, String message) throws ResourceNotFoundException {
        switch (chatUser.getUserState()) {
            case SELECT_TEACHER:
                return selectTeacherOrAccept(chatUser, message);
            case INPUT_TEXT:
                return selectTeacher(chatUser, message);
            default:
                return List.of(
                        cancel(chatUser)
                );
        }
    }

    public static List<PartialBotApiMethod<? extends Serializable>> start(ChatUser chatUser) {
        return List.of(
                TelegramUtil.createMessageTemplate(chatUser)
                        .text("Введите фамилию...").build()
        );
    }

    private List<PartialBotApiMethod<? extends Serializable>> selectTeacher(ChatUser chatUser, String message) {
        List<Teacher> teacherNames = teacherService.findTeachersByNameLike(message.toLowerCase());
        if (!teacherNames.isEmpty()) {
            chatUser.setUserState(UserState.SELECT_TEACHER);
            ChatUser save = userService.saveChatUser(chatUser);

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
            return start(userService.saveChatUser(chatUser));
        } else if (HelperUtils.isNumeric(message)) {
            int id = Integer.parseInt(message);
            Teacher teacher = teacherService.findTeacher(id);
            return List.of(
                    createMessage(teacher, chatUser),
                    cancel(chatUser)
            );
        }
        return Collections.emptyList();
    }

    private PartialBotApiMethod<? extends Serializable> createMessage(Teacher teacher, ChatUser chatUser) {
        WeekType weekType = DateHelperUtils.getWeekType();
        StringBuilder message = new StringBuilder();

        List<Lesson> lessons = lessonsService.findLessons(teacher);
        message.append("Расписание ").append("<b>").append(teacher.getName()).append("</b>:").append("\n");
        lessons.stream()
                .map(Lesson::getDay)
                .distinct()
                .sorted()
                .forEach(day -> {
                    String displayName = DayOfWeek.of(day).getDisplayName(TextStyle.FULL, HelperUtils.getLocale());
                    message.append(MESSAGE_SPLIT).append("\n")
                            .append("<b>").append(displayName).append("</b>:").append("\n");
                    lessons.stream()
                            .filter(lesson -> lesson.getDay().equals(day))
                            .filter(lesson -> lesson.getWeekType() == weekType || lesson.getWeekType() == WeekType.NONE)
                            .forEach(HelperUtils.getLessonTeacher(message));
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
        return HelperUtils.mainMessage(userService.saveChatUser(chatUser));
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
