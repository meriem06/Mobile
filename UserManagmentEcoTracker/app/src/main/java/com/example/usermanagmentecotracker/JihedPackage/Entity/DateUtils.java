package com.example.gestion_limunisite.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static String formatToISO(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Assurez-vous que la date est en UTC
        return sdf.format(date);
    }
}