package com.rhlabs.circle.models;

import com.rhlabs.circle.R;
import com.rhlabs.protobufs.services.profile.containers.ContactMethodV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

/**
 * Created by anju on 7/9/15.
 */
public class ProfileContactMethod {

    public enum SubType {
        SMS,
        None
    }

    private ContactMethodV1.ContactMethodTypeV1 mContactMethodType;
    private ProfileV1 mProfile;
    private SubType mSubType;

    public ProfileContactMethod(ContactMethodV1.ContactMethodTypeV1 contactMethodType, ProfileV1 profile) {
        mContactMethodType = contactMethodType;
        mProfile = profile;
        mSubType = SubType.None;
    }

    public ProfileContactMethod(ContactMethodV1.ContactMethodTypeV1 contactMethodType, ProfileV1 profile, SubType subType) {
        mContactMethodType = contactMethodType;
        mProfile = profile;
        mSubType = subType;
    }

    public ProfileV1 getProfile() {
        return mProfile;
    }

    public ContactMethodV1.ContactMethodTypeV1 getContactMethodType() {
        return mContactMethodType;
    }

    public String getDataForContactMethodType(ContactMethodV1.ContactMethodTypeV1 contactMethodType) {
        for (ContactMethodV1 contactMethodV1 : mProfile.contact_methods) {
            if (contactMethodV1.contact_method_type == contactMethodType) {
                return contactMethodV1.value;
            }
        }

        return null;
    }

    public int getContactMethodImage() {
        if (mContactMethodType == null) {
            return R.drawable.ic_elipse;
        }

        switch (mContactMethodType) {
            case PHONE:
                return R.drawable.ic_call;

            case CELL_PHONE:
                if (mSubType == SubType.SMS) {
                    return R.drawable.ic_sms;
                } else {
                    return R.drawable.ic_call;
                }

            case EMAIL:
                return R.drawable.ic_email;

            case TWITTER:
            case FACEBOOK:
            case SLACK:
            case HIPCHAT:
                return R.drawable.ic_sms;

            case SKYPE:
                return R.drawable.ic_camera;
        }

        return R.drawable.ic_elipse;
    }

    public SubType getSubType() {
        return mSubType;
    }
}
