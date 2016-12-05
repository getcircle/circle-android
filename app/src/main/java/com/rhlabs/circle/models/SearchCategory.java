package com.rhlabs.circle.models;

import com.rhlabs.circle.R;

/**
 * Created by anju on 6/28/15.
 */
public class SearchCategory {

    public enum SearchCategoryType {
        LOCATIONS("Locations"),
        PROFILES("People"),
        TEAMS("Teams");

        private String mValue;
        SearchCategoryType(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    private SearchCategoryType mType;
    private int mCount;

    public SearchCategory(SearchCategoryType type, int count) {
        mType = type;
        mCount = count;
    }

    public SearchCategoryType getType() {
        return mType;
    }

    public int getCount() {
        return mCount;
    }

    @Override
    public String toString() {
        return getType().toString() + ": " + getCount();
    }

    public int getImageResource() {

        switch (mType) {
            case LOCATIONS:
                return R.drawable.ic_feedlocation;

            case PROFILES:
                return R.drawable.ic_feedpeers;

            case TEAMS:
                return R.drawable.ic_feedreports;
        }

        return 0;
    }
}
