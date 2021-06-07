package karmanchik.chtotib.restservice.parser.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidTeacherName {
    private static final Pattern PATTERN = Pattern.compile("[А-Я][а-я]+\\s[А-Я][а-я]?+\\.[А-Я][а-я]?+\\.");

    private ValidTeacherName() {
    }

    public static Pattern getPattern() {
        return PATTERN;
    }

    public static boolean isTeacher(String s) {
        return PATTERN.matcher(s).matches();
    }

    public static String getValidTeacherName(String teacher, Matcher matcher) {
        return teacher.substring(matcher.start(), matcher.end());
    }
}
