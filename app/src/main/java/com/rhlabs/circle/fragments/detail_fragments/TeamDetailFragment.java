package com.rhlabs.circle.fragments.detail_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.models.TeamReportingData;
import com.rhlabs.circle.services.OrganizationService;
import com.rhlabs.circle.utils.CircleColor;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by anju on 6/6/15.
 */
public class TeamDetailFragment extends BaseDetailFragment {

    private TeamV1 mTeam;
    private TeamReportingData mTeamData;
    private static final String ARG_TEAM_1 = "team";
    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static TeamDetailFragment newInstance(TeamV1 team) {
        TeamDetailFragment fragment = new TeamDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEAM_1, team);
        fragment.setArguments(args);
        return fragment;
    }

    public TeamDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTeam = (TeamV1) getArguments().getSerializable(ARG_TEAM_1);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        final int teamColor = CircleColor.appTeamBackgroundColor(mTeam);
        mToolbar.setBackgroundColor(teamColor);
        mCollapsingToolbarLayout.setContentScrimColor(teamColor);
        mDetailViewBackdropImageView.setBackgroundColor(teamColor);
        return rootView;
    }

    @Override
    protected String getViewTitle() {
        return mTeam.name;
    }

    @Override
    public void onDestroyView() {
        mCompositeSubscription.unsubscribe();
        super.onDestroyView();
    }

    @Override
    protected void loadData() {
        final Observable<TeamReportingData> teamDetailObservable = Observable.zip(
                OrganizationService.getTeam(mTeam.id),
                OrganizationService.getTeamReportingDetails(mTeam.id),
                new Func2<TeamV1, TeamReportingData, TeamReportingData>() {
                    @Override
                    public TeamReportingData call(TeamV1 teamV1, TeamReportingData teamReportingData) {
                        mTeam = teamV1;
                        mTeamData = teamReportingData;
                        return teamReportingData;
                    }
                }
        );

        mCompositeSubscription.add(
                teamDetailObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<TeamReportingData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(TeamReportingData teamReportingData) {
                                loadCards();
                            }
                        })
        );
    }

    private void loadCards() {
        mCards.clear();
        showProgress(false);

        if (mTeam != null && mTeamData != null) {
            Log.d("LOG", mTeamData.toString());
            if (mTeamData.getManagers() != null && mTeamData.getManagers().size() > 0) {
                Card managerCard = new Card(Card.CardType.Profiles, getResources().getString(R.string.manager_section_title));
                managerCard.addContent(mTeamData.getManagers());
                managerCard.setCardFullScreenWidth(true);
                mCards.add(managerCard);
            }

            if (mTeamData.getMembers() != null && mTeamData.getMembers().size() > 0) {
                Card membersCard = new Card(Card.CardType.Profiles, getResources().getString(R.string.group_members_section_title));
                membersCard.showContentCount = false;
                membersCard.addContent(mTeamData.getMembers());
                membersCard.setCardFullScreenWidth(true);
                mCards.add(membersCard);
            }

            if (mTeamData.getSubTeams() != null && mTeamData.getSubTeams().size() > 0) {
                Card subTeamsCard = new Card(Card.CardType.Profiles, getResources().getString(R.string.teams_section_title));
                subTeamsCard.showContentCount = false;
                subTeamsCard.addContent(mTeamData.getSubTeams(), 6);
                subTeamsCard.setCardFullScreenWidth(true);
                mCards.add(subTeamsCard);
            }

            mAdapter.setCards(mCards);
            mAdapter.notifyDataSetChanged();
        }
    }
}
