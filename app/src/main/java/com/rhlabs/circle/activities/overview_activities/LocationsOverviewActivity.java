package com.rhlabs.circle.activities.overview_activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.rhlabs.circle.fragments.overview_fragments.LocationsOverviewFragment;
import com.rhlabs.circle.utils.IntentDataHolder;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;

/**
 * Created by anju on 6/11/15.
 */
public class LocationsOverviewActivity extends BaseOverviewActivity {

    @Override
    protected Fragment getFragmentLayout(Intent intent) {
        ArrayList<LocationV1> locations = (ArrayList<LocationV1>) IntentDataHolder.getInstance().getData(INTENT_ARG_LOCATIONS);
        if (locations != null) {
            ServiceRequestV1 nextRequest = (ServiceRequestV1) IntentDataHolder.getInstance().getData(INTENT_ARG_NEXT_REQUEST);
            return LocationsOverviewFragment.newInstance(locations, nextRequest);
        }

        return null;
    }
}
