package com.rhlabs.circle.models;

import com.rhlabs.protobufs.soa.ServiceRequestV1;

/**
 * Created by anju on 6/27/15.
 */
public class APIResponse {
    protected ServiceRequestV1 mNextRequest;

    public ServiceRequestV1 getNextRequest() {
        return mNextRequest;
    }
    public void setNextRequest(ServiceRequestV1 nextRequest) {
        mNextRequest = nextRequest;
    }
}
