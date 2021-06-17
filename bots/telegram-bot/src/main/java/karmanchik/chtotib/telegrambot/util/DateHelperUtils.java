package karmanchik.chtotib.telegrambot.util;

import karmanchik.chtotib.models.enums.WeekType;
import karmanchik.chtotib.telegrambot.bot.Course;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * Utils-класс статических фабрик для обработки академических дат.
 */
public class DateHelperUtils {
    private DateHelperUtils() {
    }

    /**
     * Статическая фабрика. Возвращает дату следующего учебного дня относительно входной даты
     * @param date объект {@link LocalDateTime}
     * @return Дата следующего учебного дня {@link LocalDate}
     */
    public static LocalDate getNextSchoolDate(LocalDateTime date) {
        if (date.getDayOfWeek().getValue() >= 5) {
            do date = date.plusDays(1);
            while (date.getDayOfWeek() != DayOfWeek.MONDAY);
        } else if (date.getHour() > 14) {
            date = date.plusDays(1);
        }
        return date.toLocalDate();
    }

    /**
     * Возвращает академический год поступления курса.
     * @param course объект {@link Course}
     * @return Возвращает академический год поступления курса.
     */
    public static Year getAcademicYearByCourse(Course course) {
        LocalDate now = LocalDate.now();
        Month nowMonth = now.getMonth();
        int value = course.getValue();
        int diffYear = Year.now().getValue() - value;
        return nowMonth.getValue() > Month.SEPTEMBER.getValue() ?
                Year.of(diffYear + 1) :
                Year.of(diffYear);
    }

    /**
     * Статическая фабрика для обработки текущей учебной недели (верхняя/нижняя)
     * @return Возвращает {@link WeekType} относительно чености/нечетности номера недели с начала года.
     */
    public static WeekType getWeekType() {
        LocalDate now = LocalDate.now();
        int weekNumber = now.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        return weekNumber % 2 == 0 ? WeekType.DOWN : WeekType.UP;
    }
}
