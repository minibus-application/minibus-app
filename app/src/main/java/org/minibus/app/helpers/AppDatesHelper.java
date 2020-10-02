package org.minibus.app.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AppDatesHelper {

    public static List<Integer> getDaysOfWeek() {
        return IntStream.rangeClosed(1, DayOfWeek.values().length).boxed().collect(Collectors.toList());
    }

    public static int getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }

    public static ArrayList<LocalDate> getWeek() {
        ArrayList<LocalDate> weekdays = new ArrayList<>();

        IntStream.range(0, DayOfWeek.values().length).forEach(i -> {
            LocalDateTime date = LocalDate.now().plus(i, ChronoUnit.DAYS).atStartOfDay();
            weekdays.add(date.toLocalDate());
        });

        return weekdays;
    }

    public static LocalDate parseDate(String date, DatePattern datePattern) {
        return LocalDate.parse(date, getDateTimeFormatter(datePattern));
    }

    public static String formatDate(String date, DatePattern currentPattern, DatePattern newPattern) {
        return getDateTimeFormatter(newPattern).format(parseDate(date, currentPattern));
    }

    public static String formatDate(LocalDate date, DatePattern newPattern) {
        return getDateTimeFormatter(newPattern).format(date);
    }

    private static DateTimeFormatter getDateTimeFormatter(DatePattern datePattern) {
        return DateTimeFormatter.ofPattern(datePattern.toString());
    }

    public enum DatePattern {
        ISO("yyyy-MM-dd"),
        CALENDAR ("EE d"),
        BOOKING ("EEEE, d MMM"),
        SUMMARY ("EE, d MMM");

        private final String pattern;

        DatePattern(String pattern) {
            this.pattern = pattern;
        }

        public String toString() {
            return this.pattern;
        }
    }
}
