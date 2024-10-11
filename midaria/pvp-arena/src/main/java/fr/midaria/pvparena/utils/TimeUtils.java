package fr.midaria.pvparena.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String getTimeString(int seconds, String format) {
        Date d = new Date(seconds * 1000L);
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }
}
