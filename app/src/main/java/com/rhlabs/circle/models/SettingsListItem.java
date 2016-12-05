package com.rhlabs.circle.models;

/**
 * Created by anju on 7/19/15.
 */
public class SettingsListItem {

    public enum ViewType {
        Data,
        SectionHeader
    }

    public enum DataType {
        Attributions,
        DisconnectAccount,
        Privacy,
        Terms,
        AccountEmail,
        SignOut,
        None
    }

    private String mValue;
    private DataType mDataType;
    private ViewType mViewType;

    public SettingsListItem(String value, ViewType viewType) {
        mValue = value;
        mDataType = DataType.None;
        mViewType = viewType;
    }

    public SettingsListItem(String value, DataType dataType) {
        mValue = value;
        mDataType = dataType;
        mViewType = ViewType.Data;
    }

    public String getValue() {
        return mValue;
    }

    public DataType getDataType() {
        return mDataType;
    }

    public ViewType getViewType() {
        return mViewType;
    }
}
