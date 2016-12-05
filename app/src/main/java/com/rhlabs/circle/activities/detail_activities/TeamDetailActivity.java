package com.rhlabs.circle.activities.detail_activities;

import android.content.Intent;

import com.rhlabs.circle.fragments.BaseCardListFragment;
import com.rhlabs.circle.fragments.detail_fragments.TeamDetailFragment;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;

/**
 * Created by anju on 6/11/15.
 */
public class TeamDetailActivity extends BaseDetailActivity {

    @Override
    protected BaseCardListFragment getFragmentLayout(Intent intent) {
        TeamV1 team = (TeamV1) intent.getSerializableExtra(INTENT_ARG_TEAM);
        if (team != null) {
            return TeamDetailFragment.newInstance(team);
        }

        return null;
    }
}
