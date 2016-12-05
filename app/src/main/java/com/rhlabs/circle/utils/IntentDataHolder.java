package com.rhlabs.circle.utils;

import java.util.HashMap;

/**
 * Created by anju on 6/11/15.
 */
public class IntentDataHolder {

    private HashMap<String, Object> data = new HashMap<>();

    final static IntentDataHolder intentDataHolder = new IntentDataHolder();

    private IntentDataHolder() {

    }

    public void clearData() {
        data.clear();
    }

    public static IntentDataHolder getInstance() {
        return intentDataHolder;
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public void removeData(String key) {
        data.remove(key);
    }

}

