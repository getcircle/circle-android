package com.rhlabs.circle.utils;

import android.view.View;
import android.widget.ListView;

/**
 * Created by anju on 6/30/15.
 */
public abstract class ListViewUtils {

    public static void updateRowWithObject(ListView listView, Object object) {
        if (listView != null && object != null) {
            int start = listView.getFirstVisiblePosition();
            for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++) {
                if (object == listView.getItemAtPosition(i)) {
                    View view = listView.getChildAt(i - start);
                    listView.getAdapter().getView(i, view, listView);
                    break;
                }
            }
        }
    }
}
