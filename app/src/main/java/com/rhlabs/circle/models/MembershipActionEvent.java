package com.rhlabs.circle.models;

import com.rhlabs.protobufs.services.group.actions.respond_to_membership_request.RespondToMembershipRequestRequestV1;
import com.rhlabs.protobufs.services.group.containers.MembershipRequestV1;

/**
 * Created by anju on 7/1/15.
 */
public class MembershipActionEvent {

    MembershipRequestV1 mRequestActedUpon;
    RespondToMembershipRequestRequestV1.ResponseActionV1 mResponseAction;
    int numberOfRequestsLeft = -1;

    public MembershipActionEvent(
            MembershipRequestV1 requestActedUpon,
            RespondToMembershipRequestRequestV1.ResponseActionV1 responseAction,
            int numberOfRequestsLeft
    ) {
        mRequestActedUpon = requestActedUpon;
        mResponseAction = responseAction;
        this.numberOfRequestsLeft = numberOfRequestsLeft;
    }

    public MembershipRequestV1 getRequestActedUpon() {
        return mRequestActedUpon;
    }

    public RespondToMembershipRequestRequestV1.ResponseActionV1 getResponseAction() {
        return mResponseAction;
    }

    public int getNumberOfRequestsLeft() {
        return numberOfRequestsLeft;
    }
}
