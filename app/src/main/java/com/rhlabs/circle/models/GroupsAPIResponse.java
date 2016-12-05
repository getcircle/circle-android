package com.rhlabs.circle.models;

import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.List;

/**
 * Created by anju on 6/29/15.
 */
public class GroupsAPIResponse extends APIResponse {

    private List<GroupV1> mGroups;

    public GroupsAPIResponse(List<GroupV1> groups, ServiceRequestV1 nextRequest) {
        mGroups = groups;
        mNextRequest = nextRequest;
    }

    public List<GroupV1> getGroups() {
        return mGroups;
    }
}
