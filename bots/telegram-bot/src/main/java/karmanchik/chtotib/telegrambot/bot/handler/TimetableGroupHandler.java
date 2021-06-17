package karmanchik.chtotib.telegrambot.bot.handler;

import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.entity.Group;
import karmanchik.chtotib.models.entity.Lesson;
import karmanchik.chtotib.models.enums.BotState;
import karmanchik.chtotib.models.enums.Role;
import karmanchik.chtotib.models.enums.UserState;
import karmanchik.chtotib.models.enums.WeekType;
import karmanchik.chtotib.models.exception.ResourceNotFoundException;
import karmanchik.chtotib.models.repositories.JpaChatUserRepository;
import karmanchik.chtotib.models.repositories.JpaGroupRepository;
import karmanchik.chtotib.models.repositories.JpaLessonsRepository;
import karmanchik.chtotib.telegrambot.bot.Course;
import karmanchik.chtotib.telegrambot.util.DateHelperUtils;
import karmanchik.chtotib.telegrambot.util.HelperUtils;
import karmanchik.chtotib.telegrambot.util.TelegramUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;

import static karmanchik.chtotib.telegrambot.bot.Const.MESSAGE_SPLIT;

@Log4j2
@Component
@RequiredArgsConstructor
public class TimetableGroupHandler implements Handler {
    private final JpaChatUserRepository userRepository;
    private final JpaGroupRepository groupRepository;
    private final JpaLessonsRepository lessonsRepository;
    private final HelperUtils helperUtils;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(ChatUser chatUser, String message) throws ResourceNotFoundException {
        switch (chatUser.getUserState()) {
            case SELECT_COURSE:
                return helperUtils.selectGroup(chatUser, message);
            case SELECT_GROUP:
                return selectGroupOrAccept(chatUser, message);
            default:
                return List.of(
                        cancel(chatUser)
                );
        }
    }

    public static List<PartialBotApiMethod<? extends Serializable>> start(ChatUser chatUser) {
        return HelperUtils.createSelectCourseButtonPanel(chatUser);
    }

    private List<PartialBotApiMethod<? extends Serializable>> selectGroupOrAccept(ChatUser chatUser, String message) throws ResourceNotFoundException {

        if (Course.isCourse(message)) {
            return helperUtils.selectGroup(chatUser, message);
        } else if (HelperUtils.isNumeric(message)) {
            int id = Integer.parseInt(message);
            log.info("Find group by id: {} ...", id);
            Group group = groupRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, Group.class));
            chatUser.setGroup(group);
            return List.of(
                    createMessage(chatUser, group),
                    cancel(chatUser)
            );
        }
        return Collections.emptyList();
    }

    private PartialBotApiMethod<? extends Serializable> createMessage(ChatUser chatUser, Group group) {
        WeekType weekType = DateHelperUtils.getWeekType();
        StringBuilder message = new StringBuilder();

        List<Lesson> lessons = lessonsRepository.findByGroup(group);
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
        return TelegramUtil.createMessageTemplate(chatUser)
                .text(message.toString()).build();
    }

    private PartialBotApiMethod<? extends Serializable> cancel(ChatUser chatUser) {
        chatUser.setUserState(UserState.NONE);
        return HelperUtils.mainMessage(userRepository.save(chatUser));
    }

    @Override
    public BotState operatedBotState() {
        return BotState.AUTHORIZED;
    }

    @Override
    public List<Role> operatedUserRoles() {
        return List.of(Role.TEACHER);
    }

    @Override
    public List<UserState> operatedUserSate() {
        return List.of(
                UserState.SELECT_COURSE,
                UserState.SELECT_GROUP
        );
    }
}
