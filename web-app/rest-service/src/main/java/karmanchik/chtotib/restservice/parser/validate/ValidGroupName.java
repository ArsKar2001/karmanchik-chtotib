package karmanchik.chtotib.restservice.parser.validate;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Проверяет корректность названия групп
 */
public class ValidGroupName {
    private static final Pattern GROUP_NAME =
            Pattern.compile("[А-Яа-я]+(\\s?+-\\s?+|\\s?+)\\d{2}(\\s?+-\\s?+)\\d([а-я]?|)");
    private static final Pattern ITEM_GROUP_NAME = Pattern.compile("((\\d+([а-я]|))|([А-Я]|[а-я])+)");

    private ValidGroupName() {
    }

    public static Matcher getMatcher(String s) {
        return GROUP_NAME.matcher(s);
    }

    public static Pattern getPatternGroupName() {
        return GROUP_NAME;
    }

    public static Pattern getPatternItemGroupName() {
        return ITEM_GROUP_NAME;
    }

    public static boolean isGroupName(String s) {
        return GROUP_NAME.matcher(s).find();
    }

    public static String getValidGroupName(String s) {
        List<String> list = new LinkedList<>();
        String s1 = s.replace('-', ' ');
        Matcher mt = ITEM_GROUP_NAME.matcher(s1);
        while (mt.find()) {
            String s2 = s1.substring(mt.start(), mt.end());
            list.add(s2);
        }
        return String.join("-", list);
    }
}
