package com.rhlabs.circle.fragments.overview_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.rhlabs.protobufs.services.organization.actions.get_teams.GetTeamsResponseV1;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TeamsOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamsOverviewFragment extends BaseOverviewFragment {

    private ArrayList<TeamV1> mTeams;

    public static TeamsOverviewFragment newInstance(ArrayList<TeamV1> teams, ServiceRequestV1 teamsNextRequest, String cardTypeString) {
        TeamsOverviewFragment fragment = new TeamsOverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_ITEMS_1, teams);
        args.putSerializable(ARG_NEXT_REQUEST_2, teamsNextRequest);
        args.putSerializable(ARG_CARD_TYPE_3, cardTypeString);
        fragment.setArguments(args);
        return fragment;
    }

    public TeamsOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTeams = (ArrayList<TeamV1>) getArguments().getSerializable(ARG_LIST_ITEMS_1);
        }
        
        super.onCreate(savedInstanceState);
    }


    @Override
    protected List getListItems() {
        return mTeams;
    }

    @Override
    protected Object getNextRequestResponseExtension() {
        return Ext_responses.get_teams;
    }

    @Override
    protected List getListItemsFromResponse(Object response) {
        if (response != null && response instanceof GetTeamsResponseV1) {
            return ((GetTeamsResponseV1) response).teams;

        }

        return null;
    }
}
