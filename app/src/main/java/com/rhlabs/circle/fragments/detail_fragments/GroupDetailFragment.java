package com.rhlabs.circle.fragments.detail_fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.models.GroupActionEvent;
import com.rhlabs.circle.models.KeyValueData;
import com.rhlabs.circle.models.ProfilesAPIResponse;
import com.rhlabs.circle.services.GroupService;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.circle.utils.CircleColor;
import com.rhlabs.circle.utils.ServiceRequestUtils;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.services.group.containers.MembershipRequestStatusV1;
import com.rhlabs.protobufs.services.group.containers.MembershipRequestV1;
import com.rhlabs.protobufs.services.group.containers.RoleV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by anju on 6/29/15.
 */
public class GroupDetailFragment extends BaseDetailFragment {

    private class GroupData {
        List<ProfileV1> members = new ArrayList<>();
        ServiceRequestV1 membersNextRequest;

        List<ProfileV1> owners = new ArrayList<>();
        ServiceRequestV1 ownersNextRequest;

        List<ProfileV1> managers = new ArrayList<>();
        ServiceRequestV1 managersNextRequest;

        List<GroupV1> subGroups;
    }

    private GroupV1 mGroup;
    private GroupData mGroupData;
    private static final String ARG_GROUP_1 = "group";
    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static GroupDetailFragment newInstance(GroupV1 group) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP_1, group);
        fragment.setArguments(args);
        return fragment;
    }

    public GroupDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mGroup = (GroupV1) getArguments().getSerializable(ARG_GROUP_1);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        final int groupColor = CircleColor.appGroupBackgroundColor(mGroup);
        mToolbar.setBackgroundColor(groupColor);
        mCollapsingToolbarLayout.setContentScrimColor(groupColor);
        mDetailViewBackdropImageView.setBackgroundColor(groupColor);
        return rootView;
    }

    @Override
    protected String getViewTitle() {
        return mGroup.name;
    }

    @Override
    public void onDestroyView() {
        mCompositeSubscription.unsubscribe();
        super.onDestroyView();
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

    @Override
    protected void loadData() {
        final Observable<GroupData> groupDetailObservable = Observable.zip(
                GroupService.getMembers(mGroup, RoleV1.OWNER),
                GroupService.getMembers(mGroup, RoleV1.MANAGER),
                GroupService.getMembers(mGroup, RoleV1.MEMBER),
                new Func3<ProfilesAPIResponse, ProfilesAPIResponse, ProfilesAPIResponse, GroupData>() {
                    @Override
                    public GroupData call(final ProfilesAPIResponse ownersAPIResponse,
                                          final ProfilesAPIResponse managersAPIResponse,
                                          final ProfilesAPIResponse membersAPIResponse) {
                        GroupData groupData = new GroupData();
                        groupData.owners = ownersAPIResponse.getProfiles();
                        groupData.ownersNextRequest = ownersAPIResponse.getNextRequest();

                        groupData.managers = managersAPIResponse.getProfiles();
                        groupData.managersNextRequest = managersAPIResponse.getNextRequest();

                        groupData.members = membersAPIResponse.getProfiles();
                        groupData.membersNextRequest = membersAPIResponse.getNextRequest();
                        return groupData;
                    }
                }

        );

        mCompositeSubscription.add(
                groupDetailObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<GroupData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(GroupData groupData) {
                                mGroupData = groupData;
                                Log.d("TAG", groupData.toString());
                                loadCards();
                            }
                        })
        );
    }

    private void loadCards() {
        mCards.clear();
        showProgress(false);

        if (mGroup != null && mGroupData != null) {

            // Group Email
            Card emailCard = new Card(Card.CardType.KeyValue, getResources().getString(R.string.generic_email_label));
            emailCard.setCardFullScreenWidth(true);
            emailCard.addHeader = false;
            List<KeyValueData> keyValueDataList = new ArrayList<>();
            KeyValueData hireDateKeyValueData = new KeyValueData(
                    "group_email",
                    getResources().getString(R.string.generic_email_label),
                    mGroup.email,
                    KeyValueData.KeyValueDataType.Email
            );
            keyValueDataList.add(hireDateKeyValueData);
            emailCard.addContent(keyValueDataList);
            mCards.add(emailCard);

            // Group description
            if (mGroup.group_description != null && !mGroup.group_description.isEmpty()) {
                Card descriptionCard = new Card(Card.CardType.TextValue, getResources().getString(R.string.group_description_title));
                descriptionCard.setCardFullScreenWidth(true);
                descriptionCard.showContentCount = false;
                List<String> textValues = new ArrayList<>();
                textValues.add(mGroup.group_description);
                descriptionCard.addContent(textValues);
                mCards.add(descriptionCard);
            }

            // Group Owners
            if (mGroupData.owners != null && mGroupData.owners.size() > 0) {
                Card ownersCard = new Card(Card.CardType.Profiles, getResources().getString(R.string.group_owners_section_title));
                ownersCard.showContentCount = false;
                ownersCard.addContent(mGroupData.owners, 5);
                ownersCard.setContentNextRequest(mGroupData.ownersNextRequest);
                ownersCard.setCardFullScreenWidth(true);
                mCards.add(ownersCard);
            }

            // Group Managers
            if (mGroupData.managers != null && mGroupData.managers.size() > 0) {
                Card managersCard = new Card(Card.CardType.Profiles, getResources().getString(R.string.manager_section_title));
                managersCard.showContentCount = false;
                managersCard.addContent(mGroupData.managers, 5);
                managersCard.setContentNextRequest(mGroupData.managersNextRequest);
                managersCard.setCardFullScreenWidth(true);
                mCards.add(managersCard);
            }

            // Group Members
            if (mGroupData.members != null && mGroupData.members.size() > 0) {
                Card membersCard = new Card(Card.CardType.Profiles, getResources().getString(R.string.group_members_section_title));
                membersCard.addContent(mGroupData.members, 10);
                membersCard.setContentNextRequest(mGroupData.membersNextRequest);
                int contentCount = mGroupData.members.size();
                if (mGroupData.membersNextRequest != null) {
                    contentCount = ServiceRequestUtils.getTotalItemsCount(mGroupData.membersNextRequest);
                }
                membersCard.setContentCount(contentCount);
                membersCard.setCardFullScreenWidth(true);
                mCards.add(membersCard);
            }

            // Group Action
            Card groupActionCard = new Card(Card.CardType.KeyValue, null);
            groupActionCard.setCardFullScreenWidth(true);
            groupActionCard.addHeader = false;
            List<KeyValueData> keyValueActionDataList = new ArrayList<>();
            KeyValueData groupActionKeyValueData = null;

            if (mGroup.is_member) {
                groupActionKeyValueData = new KeyValueData(
                        "group_action",
                        "",
                        "Leave group",
                        KeyValueData.KeyValueDataType.LeaveGroup
                );

                groupActionKeyValueData.setValueLabelGravity(Gravity.CENTER_HORIZONTAL);
                groupActionKeyValueData.setValueLabelTextColor(Color.RED);
            } else if (!mGroup.has_pending_request) {
                if (mGroup.can_join) {
                    groupActionKeyValueData = new KeyValueData(
                            "group_action",
                            "",
                            getResources().getString(R.string.join_group_button_title),
                            KeyValueData.KeyValueDataType.JoinGroup
                    );

                    groupActionKeyValueData.setValueLabelGravity(Gravity.CENTER_HORIZONTAL);
                    groupActionKeyValueData.setValueLabelTextColor(R.color.app_tint_color);
                } else if (mGroup.can_request) {
                    groupActionKeyValueData = new KeyValueData(
                            "group_action",
                            "",
                            getResources().getString(R.string.request_to_join_group_button_title),
                            KeyValueData.KeyValueDataType.RequestToJoinGroup
                    );

                    groupActionKeyValueData.setValueLabelGravity(Gravity.CENTER_HORIZONTAL);
                    groupActionKeyValueData.setValueLabelTextColor(getResources().getColor(R.color.app_tint_color));
                }
            }

            if (groupActionKeyValueData != null) {
                keyValueActionDataList.add(groupActionKeyValueData);
                groupActionCard.addContent(keyValueActionDataList);
                mCards.add(groupActionCard);
            }

            mAdapter.setCards(mCards);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onKeyValueRowSelected(KeyValueData keyValueData) {
        final GroupV1.Builder updatedGroupBuilder = new GroupV1.Builder(mGroup);
        switch (keyValueData.getKeyValueDataType()) {
            case JoinGroup:
            case RequestToJoinGroup:
                showProgress(true);
                GroupService.joinGroup(mGroup, new GroupService.JoinGroupCallback() {
                    @Override
                    public void success(MembershipRequestV1 request, Map<String, String> errors) {
                        if (request != null) {
                            if (request.status == MembershipRequestStatusV1.APPROVED) {
                                Snackbar.make(GroupDetailFragment.this.getView(), getResources().getString(R.string.group_join_confirmation), Snackbar.LENGTH_SHORT).show();
                                updatedGroupBuilder.is_member(true);
                                updatedGroupBuilder.members_count(updatedGroupBuilder.members_count + 1);
                                mGroup = updatedGroupBuilder.build();
                                BusProvider.getMainBus().post(new GroupActionEvent(GroupActionEvent.Type.Join, mGroup));
                                loadData();
                            } else if (request.status == MembershipRequestStatusV1.PENDING) {
                                Snackbar.make(GroupDetailFragment.this.getView(), getResources().getString(R.string.group_access_request_submitted), Snackbar.LENGTH_SHORT).show();
                                updatedGroupBuilder.has_pending_request(true);
                                mGroup = updatedGroupBuilder.build();
                                BusProvider.getMainBus().post(new GroupActionEvent(GroupActionEvent.Type.RequestToJoin, mGroup));
                                loadData();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error joining group.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Error joining group.", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case LeaveGroup:
                showProgress(true);
                GroupService.leaveGroup(mGroup, new GroupService.LeaveGroupCallback() {
                    @Override
                    public void success(Map<String, String> errors) {
                        if (errors == null || errors.size() == 0) {
                            GroupService.getGroup(mGroup.id, new GroupService.GetGroupCallback() {
                                @Override
                                public void success(GroupV1 group, Map<String, String> errors) {
                                    if (group != null) {
                                        mGroup = group;
                                        BusProvider.getMainBus().post(new GroupActionEvent(GroupActionEvent.Type.Leave, mGroup));
                                        loadData();
                                    } else {
                                        Toast.makeText(getActivity(), "Error getting details for a group", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getActivity(), "Error getting details for a group", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Error leaving group", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Error leaving group", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case Email:
                composeEmail(mGroup.email, "");
                break;

            default:
                break;
        }
    }

    @Override
    public void showProgress(boolean show) {
        super.showProgress(show);

        if (show) {
            mRecyclerView.setAlpha(0.5f);
            mRecyclerView.setClickable(false);
        } else {
            mRecyclerView.setAlpha(1);
            mRecyclerView.setClickable(true);
        }
    }

    public void composeEmail(String to, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
