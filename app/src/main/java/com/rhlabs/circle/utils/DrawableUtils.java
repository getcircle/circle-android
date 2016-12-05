package com.rhlabs.circle.utils;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by anju on 6/12/15.
 */
public class DrawableUtils {

    public static void setBackgroudDrawable(View view, Drawable drawable) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }
}
