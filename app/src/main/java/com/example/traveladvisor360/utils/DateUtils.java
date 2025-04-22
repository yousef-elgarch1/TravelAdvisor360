package com.example.traveladvisor360.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault());

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }

    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    public static int getDaysBetween(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24)) + 1;
    }

    public static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static boolean isDateInRange(Date date, Date startDate, Date endDate) {
        return !date.before(startDate) && !date.after(endDate);
    }

    public static String getRelativeTimeSpan(Date date) {
        long diff = System.currentTimeMillis() - date.getTime();

        if (diff < 60 * 1000) {
            return "Just now";
        } else if (diff < 60 * 60 * 1000) {
            return (diff / (60 * 1000)) + " minutes ago";
        } else if (diff < 24 * 60 * 60 * 1000) {
            return (diff / (60 * 60 * 1000)) + " hours ago";
        } else if (diff < 7 * 24 * 60 * 60 * 1000) {
            return (diff / (24 * 60 * 60 * 1000)) + " days ago";
        } else {
            return DATE_FORMAT.format(date);
        }
    }
}
