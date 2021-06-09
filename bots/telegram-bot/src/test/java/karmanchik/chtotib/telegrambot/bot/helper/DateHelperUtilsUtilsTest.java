package karmanchik.chtotib.telegrambot.bot.helper;

import karmanchik.chtotib.telegrambot.bot.Course;
import karmanchik.chtotib.telegrambot.util.DateHelperUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

class DateHelperUtilsUtilsTest {
    @Test
    void testDate() {
        System.out.println(Arrays.stream(Course.values())
                .collect(Collectors.toMap(course -> course, DateHelperUtils::getAcademicYearByCourse)));
    }

    @Test
    void testName() {
        System.out.println(DateHelperUtils.getNextSchoolDate(LocalDateTime.now()));
    }

    @Test
    void getNextSchoolDate() {
        System.out.println(DateHelperUtils.getNextSchoolDate(LocalDateTime.of(2021, 6, 11, 14, 0)));
    }

    @Test
    void getAcademicYearByCourse() {
    }

    @Test
    void getWeekType() {
    }
}