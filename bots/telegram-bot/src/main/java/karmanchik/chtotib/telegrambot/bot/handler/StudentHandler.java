package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.entityservice.entity.*;
import karmanchik.chtotib.entityservice.enums.BotState;
import karmanchik.chtotib.entityservice.enums.Role;
import karmanchik.chtotib.entityservice.enums.UserState;
import karmanchik.chtotib.entityservice.enums.WeekType;
import karmanchik.chtotib.entityservice.repositories.JpaChatUserRepository;
import karmanchik.chtotib.entityservice.repositories.JpaGroupRepository;
import karmanchik.chtotib.entityservice.repositories.JpaLessonsRepository;
import karmanchik.chtotib.entityservice.repositories.JpaReplacementRepository;
import karmanchik.chtotib.telegrambot.bot.helper.DateHelper;
import karmanchik.chtotib.telegrambot.bot.helper.Helper;
import karmanchik.chtotib.telegrambot.util.TelegramUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.stream.Collectors;

import static karmanchik.chtotib.telegrambot.bot.Const.MESSAGE_SPLIT;

@Log4j2
@Component
@RequiredArgsConstructor
public class StudentHandler extends MainHandler {
    private final JpaLessonsRepository lessonsRepository;
    private final JpaReplacementRepository replacementRepository;
    private final JpaGroupRepository groupRepository;
    private final JpaChatUserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> getTimetableNextDay(ChatUser chatUser) {
        Group group = groupRepository.findByChatUser(chatUser)
                .orElseThrow();
        LocalDate date = DateHelper.getNextSchoolDate();
        WeekType weekType = DateHelper.getWeekType();
        String name = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Helper.getLocale());

        List<Lesson> lessons = lessonsRepository.findAllByGroup(group);
        List<Replacement> replacements = replacementRepository.findByGroupAndDateOrderByDateAscPairNumberAsc(group, date);

        StringBuilder message = new StringBuilder();
        if (!lessons.isEmpty()) {
            message.append(MESSAGE_SPLIT).append("\n")
                    .append("Расписание ").append("<b>").append(group.getName()).append("</b>")
                    .append(" на ").append("<b>").append(date.format(DateTimeFormatter.ofPattern("dd MMMM", Helper.getLocale()))).append("</b>")
                    .append(" (").append(name).append("):").append("\n")
                    .append(MESSAGE_SPLIT).append("\n");
            lessons.stream()
                    .filter(lesson -> lesson.getDay() == date.getDayOfWeek().getValue())
                    .filter(lesson -> lesson.getWeekType() == weekType || lesson.getWeekType() == WeekType.NONE)
                    .forEach(Helper.getLessonGroup(message));
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
                    .append("Замены нет.").append("\n")
                    .append(MESSAGE_SPLIT).append("\n");
        }
        return List.of(TelegramUtil.createMessageTemplate(chatUser)
                        .text(message.toString()).build(),
                Helper.mainMessage(chatUser)
        );
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> getTimetableFull(ChatUser chatUser) {

        Group group = groupRepository.findByChatUser(chatUser)
                .orElseThrow();
        WeekType weekType = DateHelper.getWeekType();
        StringBuilder message = new StringBuilder();

        List<Lesson> lessons = lessonsRepository.findAllByGroup(group);
        message.append("Расписание ").append("<b>").append(group.getName()).append("</b>:").append("\n");
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
                            .forEach(Helper.getLessonGroup(message));
                });
        return List.of(TelegramUtil.createMessageTemplate(chatUser)
                        .text(message.toString()).build(),
                Helper.mainMessage(chatUser)
        );
    }

    @Override
    protected List<PartialBotApiMethod<? extends Serializable>> getTimetableOther(ChatUser chatUser) {
        chatUser.setUserState(UserState.INPUT_TEXT);
        return TimetableTeacherHandler.start(userRepository.save(chatUser));
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> editProfile(ChatUser chatUser) {
        chatUser.setBotState(BotState.REG);
        chatUser.setUserState(UserState.SELECT_ROLE);
        return List.of(
                Helper.selectRole(userRepository.save(chatUser)));
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
