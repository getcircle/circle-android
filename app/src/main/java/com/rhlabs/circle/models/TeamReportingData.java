package com.rhlabs.circle.models;

import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anju on 8/17/15.
 */
public class TeamReportingData {

    List<ProfileV1> members;
    List<ProfileV1> managers = new ArrayList<>();
    List<TeamV1> subTeams;

    public TeamReportingData(List<ProfileV1> members, ProfileV1 manager, List<TeamV1> subTeams) {
        this.members = members;
        if (manager != null) {
            this.managers.add(manager);
        }
        this.subTeams = subTeams;
    }

    public List<ProfileV1> getMembers() {
        return members;
    }

    public List<ProfileV1> getManagers() {
        return managers;
    }

    public List<TeamV1> getSubTeams() {
        return subTeams;
    }
}
