package com.rhlabs.circle.activities.detail_activities;

import android.content.Intent;
import android.os.Bundle;

import com.rhlabs.circle.fragments.BaseCardListFragment;
import com.rhlabs.circle.fragments.detail_fragments.GroupDetailFragment;
import com.rhlabs.circle.models.GroupActionEvent;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.squareup.otto.Subscribe;

/**
 * Created by anju on 6/29/15.
 */
public class GroupDetailActivity extends BaseDetailActivity {

    @Override
    protected BaseCardListFragment getFragmentLayout(Intent intent) {
        GroupV1 group = (GroupV1) intent.getSerializableExtra(INTENT_ARG_GROUP);
        if (group != null) {
            return GroupDetailFragment.newInstance(group);
        }

        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getMainBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getMainBus().unregister(this);
    }

    @Subscribe
    public void onGroupActionPerformed(GroupActionEvent groupActionEvent) {
        GroupV1 group = groupActionEvent.getGroup();
        if (group != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(INTENT_ARG_GROUP, group);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
    }
}



