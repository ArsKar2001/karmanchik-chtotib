package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.models.entity.*;
import karmanchik.chtotib.models.enums.BotState;
import karmanchik.chtotib.models.enums.Role;
import karmanchik.chtotib.models.enums.UserState;
import karmanchik.chtotib.models.enums.WeekType;
import karmanchik.chtotib.telegrambot.services.ChatUserService;
import karmanchik.chtotib.telegrambot.services.GroupService;
import karmanchik.chtotib.telegrambot.services.LessonsService;
import karmanchik.chtotib.telegrambot.services.ReplacementService;
import karmanchik.chtotib.telegrambot.util.DateHelperUtils;
import karmanchik.chtotib.telegrambot.util.HelperUtils;
import karmanchik.chtotib.telegrambot.util.TelegramUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.stream.Collectors;

import static karmanchik.chtotib.telegrambot.bot.Const.MESSAGE_SPLIT;

/**
 * Обработчик команд от пользователей - студентов.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class StudentHandler extends MainHandler {
    private final LessonsService lessonsService;
    private final ReplacementService replacementService;
    private final GroupService groupService;
    private final ChatUserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> getTimetableNextDay(ChatUser chatUser) {
        Group group = groupService.findGroup(chatUser);
        LocalDate date = DateHelperUtils.getNextSchoolDate(LocalDateTime.now());
        WeekType weekType = DateHelperUtils.getWeekType();
        String name = date.getDayOfWeek().getDisplayName(TextStyle.FULL, HelperUtils.getLocale());

        List<Lesson> lessons = lessonsService.findLessons(group);
        List<Replacement> replacements = replacementService.findReplacements(group, date);

        StringBuilder message = new StringBuilder();
        if (!lessons.isEmpty()) {
            message.append(MESSAGE_SPLIT).append("\n")
                    .append("Расписание ").append("<b>").append(group.getName()).append("</b>")
                    .append(" на ").append("<b>").append(date.format(DateTimeFormatter.ofPattern("dd MMMM", HelperUtils.getLocale()))).append("</b>")
                    .append(" (").append(name).append("):").append("\n")
                    .append(MESSAGE_SPLIT).append("\n");
            lessons.stream()
                    .filter(lesson -> lesson.getDay() == date.getDayOfWeek().getValue())
                    .filter(lesson -> lesson.getWeekType() == weekType || lesson.getWeekType() == WeekType.NONE)
                    .forEach(HelperUtils.getLessonGroup(message));
        } else {
            message.append("Пар на ").append("<b>").append(date).append("</b>").append(" нет.");
        }
        if (!replacements.isEmpty()) {
            message.append(MESSAGE_SPLIT).append("\n")
                    .append("Замена:").append("\n")
                    .append(MESSAGE_SPLIT).append("\n");
            replacements.forEach(replacement -> message.append("\t")
                    .append(replacement.getDate()).append("\t<b>|</b>\t")
                    .append(replacement.getPairNumber()).append("\t<b>|</b>\t")
                    .append(replacement.getDiscipline()).append("\t<b>|</b>\t")
                    .append(replacement.getAuditorium()).append("\t<b>|</b>\t")
                    .append(replacement.getTeachers().stream()
                            .map(Teacher::getName)
                            .collect(Collectors.joining(", ")))
                    .append("\n"));
        } else {
            message.append(MESSAGE_SPLIT).append("\n")
                    .append("Замены еще нет.").append("\n")
                    .append(MESSAGE_SPLIT).append("\n");
        }
        return List.of(TelegramUtil.createMessageTemplate(chatUser)
                        .text(message.toString()).build(),
                HelperUtils.mainMessage(chatUser)
        );
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> getTimetableFull(ChatUser chatUser) {
        Group group = groupService.findGroup(chatUser);
        WeekType weekType = DateHelperUtils.getWeekType();
        StringBuilder message = new StringBuilder();

        List<Lesson> lessons = lessonsService.findLessons(group);
        message.append("Расписание ").append("<b>").append(group.getName()).append("</b>:").append("\n");
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
                            .forEach(HelperUtils.getLessonGroup(message));
                });
        return List.of(TelegramUtil.createMessageTemplate(chatUser)
                        .text(message.toString()).build(),
                HelperUtils.mainMessage(chatUser)
        );
    }

    @Override
    protected List<PartialBotApiMethod<? extends Serializable>> getTimetableOther(ChatUser chatUser) {
        chatUser.setUserState(UserState.INPUT_TEXT);
        return TimetableTeacherHandler.start(userService.saveChatUser(chatUser));
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> editProfile(ChatUser chatUser) {
        chatUser.setBotState(BotState.REG);
        chatUser.setUserState(UserState.SELECT_ROLE);
        return List.of(
                HelperUtils.selectRole(userService.saveChatUser(chatUser)));
    }

    @Override
    public BotState operatedBotState() {
        return BotState.AUTHORIZED;
    }

    @Override
    public List<Role> operatedUserRoles() {
        return List.of(Role.STUDENT);
    }

    @Override
    public List<UserState> operatedUserSate() {
        return List.of(
                UserState.NONE
        );
    }
}
