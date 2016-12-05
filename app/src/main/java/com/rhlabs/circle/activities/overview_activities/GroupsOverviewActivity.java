package com.rhlabs.circle.activities.overview_activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.rhlabs.circle.R;
import com.rhlabs.circle.fragments.overview_fragments.GroupsOverviewFragment;
import com.rhlabs.circle.utils.IntentDataHolder;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;

/**
 * Created by anju on 6/29/15.
 */
public class GroupsOverviewActivity extends BaseOverviewActivity {

    @Override
    protected Fragment getFragmentLayout(Intent intent) {
        ArrayList<GroupV1> groups = (ArrayList<GroupV1>) IntentDataHolder.getInstance().getData(INTENT_ARG_GROUPS);
        if (groups != null) {
            ServiceRequestV1 nextRequest = (ServiceRequestV1) IntentDataHolder.getInstance().getData(INTENT_ARG_NEXT_REQUEST);
            return GroupsOverviewFragment.newInstance(groups, nextRequest);
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DETAIL_ACTIVITY) {
            if (resultCode == RESULT_OK && data != null) {
                GroupV1 group = (GroupV1) data.getSerializableExtra(INTENT_ARG_GROUP);
                GroupsOverviewFragment fragment = (GroupsOverviewFragment) getSupportFragmentManager().findFragmentById(R.id.container);
                if (fragment != null) {
                    fragment.updateRowWithGroup(group);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
