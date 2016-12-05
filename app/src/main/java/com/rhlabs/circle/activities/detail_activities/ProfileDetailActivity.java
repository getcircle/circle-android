package com.rhlabs.circle.activities.detail_activities;

import android.content.Intent;

import com.rhlabs.circle.fragments.BaseCardListFragment;
import com.rhlabs.circle.fragments.detail_fragments.ProfileDetailFragment;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

/**
 * Created by anju on 6/12/15.
 */
public class ProfileDetailActivity extends BaseDetailActivity {

    @Override
    protected BaseCardListFragment getFragmentLayout(Intent intent) {
        ProfileV1 profileV1 = (ProfileV1) intent.getSerializableExtra(INTENT_ARG_PROFILE);
        if (profileV1 != null) {
            return ProfileDetailFragment.newInstance(profileV1);
        }

        return null;
    }
}