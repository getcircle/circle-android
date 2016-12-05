package com.rhlabs.circle.models;

/**
 * Created by anju on 6/27/15.
 */
public class OrgStats {
    int mProfilesCount;
    int mLocationsCount;
    int mTeamsCount;

    public OrgStats(int profilesCount, int locationsCount, int teamsCount) {
        mProfilesCount = profilesCount;
        mLocationsCount = locationsCount;
        mTeamsCount = teamsCount;
    }

    public int getProfilesCount() {
        return mProfilesCount;
    }

    public int getLocationsCount() {
        return mLocationsCount;
    }

    public int getTeamsCount() {
        return mTeamsCount;
    }

    @Override
    public String toString() {
        return "Profiles = " + getProfilesCount() +
                "Locations = " + getLocationsCount() +
                "Teams = " + getTeamsCount();
    }
}
