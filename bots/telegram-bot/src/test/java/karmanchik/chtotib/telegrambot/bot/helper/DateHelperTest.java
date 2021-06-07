package karmanchik.chtotib.telegrambot.bot.helper;

import karmanchik.chtotib.telegrambot.bot.Course;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

class DateHelperTest {
    @Test
    void testDate() {
        System.out.println(Arrays.stream(Course.values())
                .collect(Collectors.toMap(course -> course, DateHelper::getAcademicYearByCourse)));
    }

    @Test
    void testName() {
        System.out.println(DateHelper.getNextSchoolDate());
    }
}