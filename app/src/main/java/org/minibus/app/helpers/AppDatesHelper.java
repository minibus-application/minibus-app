package org.minibus.app.helpers;

import org.minibus.app.AppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppDatesHelper {

    public enum DatePattern {
        API_SCHEDULE_REQUEST("yyyy-MM-dd"),
        API_BOOKING_RESPONSE ("yyyy-MM-dd"),
        CALENDAR ("EE d"),
        BOOKING ("EEEE, d MMM"),
        SUMMARY ("yyyy-MM-dd");

        private final String pattern;

        DatePattern(String pattern) {
            this.pattern = pattern;
        }

        public String toString() {
            return this.pattern;
        }
    }

    private static final String DEFAULT_APP_LOCALE = AppConstants.DEFAULT_APP_LOCALE;

    private static SimpleDateFormat createDateFormat(DatePattern datePattern) {
        return new SimpleDateFormat(datePattern.toString(), new Locale(DEFAULT_APP_LOCALE));
    }

    private static Date parseDate(String date, DatePattern datePattern) {
        Date parsed = new Date();
        try {
            parsed = createDateFormat(datePattern).parse(date);
        } catch (ParseException e) {}
        return parsed;
    }

    public static String getTimestamp(DatePattern datePattern) {
        return createDateFormat(datePattern).format(new Date());
    }

    public static String getTimestamp() {
        return createDateFormat(DatePattern.API_SCHEDULE_REQUEST).format(new Date());
    }

    public static String formatDate(String date, DatePattern currentPattern, DatePattern newPattern) {
        return createDateFormat(newPattern).format(parseDate(date, currentPattern));
    }

    public static ArrayList<String> createWeekdays(int amount, DatePattern datePattern) {
        ArrayList<String> weekdays = new ArrayList<>();
        DateFormat dateFormat = createDateFormat(datePattern);

        int end = amount < 0 ? amount * -1 : amount;

        for (int i = 0; i < end; i++) {
            Calendar calendar = Calendar.getInstance();

            if (i == 0) {
                calendar.setTime(new Date());
            } else {
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
            }

            calendar.add(Calendar.DATE, i);
            weekdays.add(dateFormat.format(calendar.getTime()));
        }

        return weekdays;
    }

    public static int getDaysDifference(String d1, String d2, DatePattern datePattern) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(parseDate(d1, datePattern));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(parseDate(d2, datePattern));

        return Math.abs(calendar1.get(Calendar.DAY_OF_MONTH) - calendar2.get(Calendar.DAY_OF_MONTH));
    }
}
