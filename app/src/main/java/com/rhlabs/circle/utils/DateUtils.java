package com.rhlabs.circle.utils;

import android.support.annotation.Nullable;

import com.rhlabs.circle.common.CircleApp;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anju on 6/21/15.
 */
public class DateUtils {

    public static String getFormattedHireDate(String hireDate) {
        return getFormattedDate(hireDate, "MMMM yyyy");
    }

    public static String getFormattedBirthDate(String birthDate) {
        return getFormattedDate(birthDate, "MMMM d");
    }

    public static String getFormattedNewHireDate(String hireDate) {
        return getFormattedDate(hireDate, "MMMM d");
    }

    @Nullable
    public static String getFormattedWorkAnniversaryDate(String hireDate) {
        String actualDate = getFormattedDate(hireDate, "MMMM yyyy");

        Locale currentLocale = CircleApp.getContext().getResources().getConfiguration().locale;
        SimpleDateFormat backendFormat = new SimpleDateFormat("yyyy-MM-dd", currentLocale);
        Date date = null;
        try {
            date = backendFormat.parse(hireDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        String yearsBetween = "";
        if (date != null) {
            DateTime currentDate = new DateTime();
            Period period = new Period(new DateTime(date), currentDate);
            int diffYears = period.getYears();
            if (period.getMonths() >= 11) {
                diffYears += 1;
            }

            if (diffYears > 0) {
                yearsBetween = diffYears + " " + (diffYears == 1 ? "year" : "years");
            }
        }

        return yearsBetween + (actualDate != null ? " - " + actualDate : "");
    }

    @Nullable
    private static String getFormattedDate(String stringDate, String template) {
        Locale currentLocale = CircleApp.getContext().getResources().getConfiguration().locale;
        SimpleDateFormat backendFormat = new SimpleDateFormat("yyyy-MM-dd", currentLocale);
        Date date = null;
        try {
            date = backendFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat expectedFormat = new SimpleDateFormat(template, currentLocale);
        return expectedFormat.format(date);
    }


}
