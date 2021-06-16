package karmanchik.chtotib.telegrambot.bot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    @Test
    void test_course_1() {
        System.out.println(Course.isCourse("I"));
        System.out.println(Course.isCourse(1));
        System.out.println(Course.isCourse(Course.I));
    }

    @Test
    void test_course_2() {

    }
}