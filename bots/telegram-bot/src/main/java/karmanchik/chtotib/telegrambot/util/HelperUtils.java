package karmanchik.chtotib.telegrambot.util;

import karmanchik.chtotib.entityservice.entity.ChatUser;
import karmanchik.chtotib.entityservice.entity.Group;
import karmanchik.chtotib.entityservice.entity.Lesson;
import karmanchik.chtotib.entityservice.entity.Teacher;
import karmanchik.chtotib.entityservice.enums.Role;
import karmanchik.chtotib.entityservice.enums.UserState;
import karmanchik.chtotib.entityservice.enums.WeekType;
import karmanchik.chtotib.entityservice.repositories.JpaChatUserRepository;
import karmanchik.chtotib.entityservice.repositories.JpaGroupRepository;
import karmanchik.chtotib.telegrambot.bot.Const;
import karmanchik.chtotib.telegrambot.bot.Course;
import karmanchik.chtotib.telegrambot.bot.NumberLesson;
import karmanchik.chtotib.telegrambot.bot.command.MainCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Вспомогательный util-класс, содержаший статические методы, которыми пользуются несколько обработчиков.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class HelperUtils {
    private final JpaGroupRepository groupRepository;
    private final JpaChatUserRepository userRepository;

    public static PartialBotApiMethod<? extends Serializable> selectRole(ChatUser chatUser) {
        return TelegramUtil.createMessageTemplate(chatUser)
                .text("Кто ты?")
                .replyMarkup(TelegramUtil.createReplyKeyboardMarkup()
                        .keyboardRow(TelegramUtil.createKeyboardRow(List.of(
                                Const.ROLE_STUDENT,
                                Const.ROLE_TEACHER).toArray()))
                        .oneTimeKeyboard(true)
                        .build())
                .build();
    }

    public static PartialBotApiMethod<? extends Serializable> inputMessage(ChatUser chatUser, String text) {
        return TelegramUtil.createMessageTemplate(chatUser)
                .text(text).build();
    }

    public static PartialBotApiMethod<? extends Serializable> mainMessage(ChatUser chatUser) {
        ReplyKeyboardMarkup markup = TelegramUtil.createReplyKeyboardMarkup().build();
        LocalDate nextSchoolDate = DateHelperUtils.getNextSchoolDate(LocalDateTime.now());
        String weekType = DateHelperUtils.getWeekType().equals(WeekType.DOWN) ? "нижняя" : "верхняя";
        String name = nextSchoolDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"));
        Role role = chatUser.getRole();


        markup.setKeyboard(List.of(TelegramUtil.createKeyboardRow(MainCommand.vals())));
        return role.equals(Role.STUDENT) ?
                TelegramUtil.createMessageTemplate(chatUser)
                        .text("1.\tРасписание на " + "<b>" + nextSchoolDate.format(DateTimeFormatter.ofPattern("dd MMMM", HelperUtils.getLocale())) + "</b>" + " (" + name + ")\n" +
                                "2.\tРасписание на эту неделю (" + weekType + ")\n" +
                                "3.\tУзнать расписание педагога\n" +
                                "4.\tИзменить анкету")
                        .replyMarkup(markup).build() :
                TelegramUtil.createMessageTemplate(chatUser)
                        .text("1.\tРасписание на " + "<b>" + nextSchoolDate.format(DateTimeFormatter.ofPattern("dd MMMM", HelperUtils.getLocale())) + "</b>" + " (" + name + ")\n" +
                                "2.\tРасписание на эту неделю (" + weekType + ")\n" +
                                "3.\tУзнать расписание группы\n" +
                                "4.\tИзменить анкету")
                        .replyMarkup(markup).build();
    }

    public static Locale getLocale() {
        return Locale.forLanguageTag("ru");
    }

    public static List<PartialBotApiMethod<? extends Serializable>> createSelectCourseButtonPanel(ChatUser chatUser) {
        List<String> names = Course.names();
        Collections.sort(names);
        KeyboardRow row = TelegramUtil.createKeyboardRow(names.toArray());

        return List.of(TelegramUtil.createMessageTemplate(chatUser)
                .text("Выбери курс:")
                .replyMarkup(TelegramUtil.createReplyKeyboardMarkup()
                        .keyboardRow(row)
                        .build())
                .build());
    }

    public static boolean isNumeric(String message) {
        if (message != null) try {
            Integer.parseInt(message);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
        else return false;
    }

    public static Consumer<Lesson> getLessonTeacher(StringBuilder message) {
        return lesson -> {
            String number = NumberLesson.get(lesson.getPairNumber());
            message.append("\t")
                    .append(number).append("\t<b>|</b>\t")
                    .append(lesson.getDiscipline()).append("\t<b>|</b>\t")
                    .append(lesson.getAuditorium()).append("\t<b>|</b>\t")
                    .append(lesson.getGroup().getName())
                    .append("\n");
        };
    }

    public static Consumer<Lesson> getLessonGroup(StringBuilder message) {
        return lesson -> {
            String number = NumberLesson.get(lesson.getPairNumber());
            message.append("\t")
                    .append(number).append("\t<b>|</b>\t")
                    .append(lesson.getDiscipline()).append("\t<b>|</b>\t")
                    .append(lesson.getAuditorium()).append("\t<b>|</b>\t")
                    .append(lesson.getTeachers().stream()
                            .map(Teacher::getName)
                            .collect(Collectors.joining(", ")))
                    .append("\n");
        };
    }
    public List<PartialBotApiMethod<? extends Serializable>> selectGroup(ChatUser chatUser, String message) {
        if (Course.isCourse(message)) {
            Course course = Course.valueOf(message);
            String academicYearSuffix = DateHelperUtils.getAcademicYearByCourse(course)
                    .format(DateTimeFormatter.ofPattern("yy"));
            List<Group> groups = groupRepository.findAllByYearSuffix(academicYearSuffix);


            chatUser.setUserState(UserState.SELECT_GROUP);
            return List.of(
                    TelegramUtil.createMessageTemplate(userRepository.save(chatUser))
                            .text("Выбери группу:")
                            .replyMarkup(InlineKeyboardMarkup.builder()
                                    .keyboard(TelegramUtil.createInlineKeyboardButtons(groups, 3))
                                    .build())
                            .build());
        }
        return Collections.emptyList();
    }
}
