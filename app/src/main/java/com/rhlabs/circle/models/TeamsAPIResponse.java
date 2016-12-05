package com.rhlabs.circle.models;

import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.List;

/**
 * Created by anju on 6/24/15.
 */
public class TeamsAPIResponse extends APIResponse {
    private List<TeamV1> mTeams;

    public TeamsAPIResponse(List<TeamV1> teams, ServiceRequestV1 nextRequest) {
        mTeams = teams;
        mNextRequest = nextRequest;
    }

    public List<TeamV1> getTeams() {
        return mTeams;
    }

    public void setTeams(List<TeamV1> teams) {
        mTeams = teams;
    }
}
