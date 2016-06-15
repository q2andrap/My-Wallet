package com.dragoninc.mywallet.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by quan.le on 5/31/2016.
 */
public class Utils {
    public static String formatInt(int value) {
        return String.format("%,d", value);
    }

    public static String formatString(String value) {
        return value.replace(",", "");
    }

    public static int getDaysOfMonth(int month) {
        GregorianCalendar g = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), month,1);
        return g.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getScreenWidth(Context context){
        Activity activity = (Activity)context;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT < 17)
            activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        else
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        return width;
    }
}
