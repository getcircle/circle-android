package com.rhlabs.circle.models;

import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.List;

/**
 * Created by anju on 6/24/15.
 */
public class ProfilesAPIResponse extends APIResponse {
    private List<ProfileV1> mProfiles;

    public ProfilesAPIResponse(List<ProfileV1> profiles, ServiceRequestV1 nextRequest) {
        mProfiles = profiles;
        mNextRequest = nextRequest;
    }

    public List<ProfileV1> getProfiles() {
        return mProfiles;
    }

    public void setProfiles(List<ProfileV1> profiles) {
        mProfiles = profiles;
    }
}
