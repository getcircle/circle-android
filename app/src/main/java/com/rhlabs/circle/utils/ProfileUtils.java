package com.rhlabs.circle.utils;

import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

/**
 * Created by anju on 6/11/15.
 */
public class ProfileUtils {

    // Return a thumbnail URL if present else return regular one
    public static String getProfileImageURL(ProfileV1 profileV1) {
        if (profileV1.small_image_url != null && !profileV1.small_image_url.isEmpty()) {
            return profileV1.small_image_url;
        } else if (profileV1.image_url != null && !profileV1.image_url.isEmpty()) {
            return profileV1.image_url;
        }

        return null;
    }
}
