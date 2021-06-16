package karmanchik.chtotib.telegrambot.util;

import karmanchik.chtotib.telegrambot.bot.Course;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateHelperUtilsTest {
    @Test
    void test_1() {
        System.out.println(DateHelperUtils.getAcademicYearByCourse(Course.IV));
        System.out.println(DateHelperUtils.getNextSchoolDate(LocalDateTime.now()));
    }
}