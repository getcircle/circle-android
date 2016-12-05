package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by anju on 7/10/15.
 */
public class ContactMethodsListView extends ListView {
    public ContactMethodsListView(Context context) {
        super(context);
    }

    public ContactMethodsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactMethodsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContactMethodsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
