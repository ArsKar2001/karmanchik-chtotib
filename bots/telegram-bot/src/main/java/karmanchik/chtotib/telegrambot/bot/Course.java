package karmanchik.chtotib.telegrambot.bot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Course {
    I(1),
    II(2),
    III(3),
    IV(4);

    private final int value;

    Course(int value) {
        this.value = value;
    }

    public static boolean isCourse(Object o) {
        return Arrays.stream(values())
                .anyMatch(course -> course.equals(o) || course.name().equals(o) || ((Integer) course.getValue()).equals(o));
    }

    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public int getValue() {
        return value;
    }
}
