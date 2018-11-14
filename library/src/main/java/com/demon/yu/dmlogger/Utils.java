package com.demon.yu.dmlogger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class Utils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
    private static SimpleDateFormat dailyDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String stringFormat(String message, Object... args) {
        return String.format(message, args);
    }


    @NonNull
    static <T> T checkNotNull(@Nullable final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim());
    }

    static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }


    static void closeWriter(Writer writer) {
        if (writer == null) {
            return;
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean isSameDay(Date first, Date second) {
        return first.getDay() == second.getDay() && first.getYear() == second.getYear();
    }


    static String createBakFileName(Date date) {
        return simpleDateFormat.format(date);
    }

    static String createDailyFileName(Date date) {
        return dailyDateFormat.format(date);
    }

}
