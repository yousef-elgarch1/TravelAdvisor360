package com.example.traveladvisor360.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for time-related operations
 */
public class TimeUtils {

    /**
     * Returns a user-friendly relative time string (e.g., "2 hours ago")
     * @param date The date to format
     * @return A relative time string
     */
    public static String getTimeAgo(Date date) {
        if (date == null) return "";

        long time = date.getTime();
        long now = System.currentTimeMillis();

        // Convert to seconds
        long diff = (now - time) / 1000;

        if (diff < 60) {
            return "just now";
        } else if (diff < 3600) {
            long minutes = diff / 60;
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else if (diff < 86400) {
            long hours = diff / 3600;
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (diff < 604800) {
            long days = diff / 86400;
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (diff < 2592000) {
            long weeks = diff / 604800;
            return weeks + (weeks == 1 ? " week ago" : " weeks ago");
        } else if (diff < 31536000) {
            long months = diff / 2592000;
            return months + (months == 1 ? " month ago" : " months ago");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            return sdf.format(date);
        }
    }

    /**
     * Formats a date as "Month Day, Year" (e.g., "Jan 1, 2025")
     * @param date The date to format
     * @return A formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}