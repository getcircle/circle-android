package com.rhlabs.circle.models;

import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.List;

/**
 * Created by anju on 6/27/15.
 */
public class LocationsAPIResponse extends APIResponse {

    private List<LocationV1> mLocations;

    public LocationsAPIResponse(List<LocationV1> locations, ServiceRequestV1 nextRequest) {
        mLocations = locations;
        mNextRequest = nextRequest;
    }

    public List<LocationV1> getLocations() {
        return mLocations;
    }

    public void setLocations(List<LocationV1> locations) {
        mLocations = locations;
    }
}
