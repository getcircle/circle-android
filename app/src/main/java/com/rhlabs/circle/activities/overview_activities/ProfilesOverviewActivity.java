package com.rhlabs.circle.activities.overview_activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.rhlabs.circle.fragments.overview_fragments.ProfilesOverviewFragment;
import com.rhlabs.circle.fragments.overview_fragments.TeamsOverviewFragment;
import com.rhlabs.circle.utils.IntentDataHolder;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;

/**
 * Created by anju on 6/11/15.
 */
public class ProfilesOverviewActivity extends BaseOverviewActivity {

    @Override
    protected Fragment getFragmentLayout(Intent intent) {
        ArrayList<ProfileV1> profiles = (ArrayList<ProfileV1>) IntentDataHolder.getInstance().getData(INTENT_ARG_PROFILES);
        if (profiles != null) {
            ServiceRequestV1 profilesNextRequest = (ServiceRequestV1) IntentDataHolder.getInstance().getData(INTENT_ARG_NEXT_REQUEST);
            String cardTypeString = intent.getStringExtra(INTENT_ARG_SRC_CARD_TYPE);
            return ProfilesOverviewFragment.newInstance(profiles, profilesNextRequest, cardTypeString);
        }

        ArrayList<TeamV1> teams = (ArrayList<TeamV1>) IntentDataHolder.getInstance().getData(INTENT_ARG_TEAMS);
        if (teams != null) {
            ServiceRequestV1 teamsNextRequest = (ServiceRequestV1) IntentDataHolder.getInstance().getData(INTENT_ARG_NEXT_REQUEST);
            String cardTypeString = intent.getStringExtra(INTENT_ARG_SRC_CARD_TYPE);
            return TeamsOverviewFragment.newInstance(teams, teamsNextRequest, cardTypeString);
        }

        return null;
    }
}
