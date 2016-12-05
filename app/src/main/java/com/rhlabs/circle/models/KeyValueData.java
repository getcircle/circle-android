package com.rhlabs.circle.models;

/**
 * Created by anju on 6/12/15.
 */
public class KeyValueData {

    public enum KeyValueDataType {
        Birthday,
        HireDate,
        LocationProfileCount,
        Email, LeaveGroup, RequestToJoinGroup, JoinGroup, GroupsPlaceholder
    }

    private String mKey;
    private String mKeyLabel;
    private String mValue;
    private KeyValueDataType mKeyValueDataType;

    private int valueLabelGravity = -1;
    private int valueLabelTextColor = -1;

    public KeyValueData() {
    }

    public KeyValueData(String key, String keyLabel, String value, KeyValueDataType keyValueDataType) {
        mKey = key;
        mKeyLabel = keyLabel;
        mValue = value;
        mKeyValueDataType = keyValueDataType;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getKeyLabel() {
        return mKeyLabel;
    }

    public void setKeyLabel(String keyLabel) {
        mKeyLabel = keyLabel;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public KeyValueDataType getKeyValueDataType() {
        return mKeyValueDataType;
    }

    public void setKeyValueDataType(KeyValueDataType keyValueDataType) {
        mKeyValueDataType = keyValueDataType;
    }

    public int getValueLabelGravity() {
        return valueLabelGravity;
    }

    public void setValueLabelGravity(int valueLabelGravity) {
        this.valueLabelGravity = valueLabelGravity;
    }

    public int getValueLabelTextColor() {
        return valueLabelTextColor;
    }

    public void setValueLabelTextColor(int valueLabelTextColor) {
        this.valueLabelTextColor = valueLabelTextColor;
    }
}
