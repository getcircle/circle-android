package com.rhlabs.circle.utils;

import com.rhlabs.circle.common.CircleApp;

import java.util.Locale;

/**
 * Created by anju on 6/29/15.
 */
public class LocaleUtils {

    public static Locale currentLocale() {
        return CircleApp.getContext().getResources().getConfiguration().locale;
    }
}
