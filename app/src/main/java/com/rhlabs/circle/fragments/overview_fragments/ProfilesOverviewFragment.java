package com.rhlabs.circle.fragments.overview_fragments;

import android.os.Bundle;

import com.rhlabs.protobufs.services.profile.actions.get_profiles.GetProfilesResponseV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnProfileClickedListener}
 * interface.
 */
public class ProfilesOverviewFragment extends BaseOverviewFragment {

    private ArrayList<ProfileV1> mProfiles;

    public static ProfilesOverviewFragment newInstance(ArrayList<ProfileV1> profiles, ServiceRequestV1 profilesNextRequest, String cardTypeString) {
        ProfilesOverviewFragment fragment = new ProfilesOverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_ITEMS_1, profiles);
        args.putSerializable(ARG_NEXT_REQUEST_2, profilesNextRequest);
        args.putSerializable(ARG_CARD_TYPE_3, cardTypeString);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfilesOverviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mProfiles = (ArrayList<ProfileV1>) getArguments().getSerializable(ARG_LIST_ITEMS_1);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected List getListItems() {
        return mProfiles;
    }

    @Override
    protected Object getNextRequestResponseExtension() {
        return Ext_responses.get_profiles;
    }

    @Override
    protected List getListItemsFromResponse(Object response) {
        if (response != null && response instanceof GetProfilesResponseV1) {
            return ((GetProfilesResponseV1) response).profiles;
        }

        return null;
    }
}
