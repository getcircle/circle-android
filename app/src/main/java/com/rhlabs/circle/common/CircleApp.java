package com.rhlabs.circle.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by anju on 4/29/15.
 */
public class CircleApp extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context;
    }
}
