package com.rhlabs.circle.activities.detail_activities;

import android.content.Intent;

import com.rhlabs.circle.fragments.BaseCardListFragment;
import com.rhlabs.circle.fragments.detail_fragments.LocationDetailFragment;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;

/**
 * Created by anju on 6/11/15.
 */
public class LocationDetailActivity extends BaseDetailActivity {

    @Override
    protected BaseCardListFragment getFragmentLayout(Intent intent) {
        LocationV1 location = (LocationV1) intent.getSerializableExtra(INTENT_ARG_LOCATION);
        if (location != null) {
            return LocationDetailFragment.newInstance(location);
        }

        return null;
    }
}
