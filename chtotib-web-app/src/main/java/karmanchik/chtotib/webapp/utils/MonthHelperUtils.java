package karmanchik.chtotib.webapp.utils;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class MonthHelperUtils {
//    private static final Pattern DAY_MONTHS = new

    public static String getRusMonthName(Month month, TextStyle style) {
        return month.getDisplayName(style, Locale.forLanguageTag("ru"));
    }
}
