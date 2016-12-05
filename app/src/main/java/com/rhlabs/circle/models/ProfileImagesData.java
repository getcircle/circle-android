package com.rhlabs.circle.models;

import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.List;

/**
 * Created by anju on 7/6/15.
 */
public class ProfileImagesData {

    private String mTitle;
    private Card.CardType mType;
    private List<ProfileV1> mProfiles;

    public ProfileImagesData(Card.CardType type, String title, List<ProfileV1> profiles) {
        mType = type;
        mTitle = title;
        mProfiles = profiles;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Card.CardType getType() {
        return mType;
    }

    public void setType(Card.CardType type) {
        mType = type;
    }

    public List<ProfileV1> getProfiles() {
        return mProfiles;
    }

    public void setProfiles(List<ProfileV1> profiles) {
        mProfiles = profiles;
    }
}
