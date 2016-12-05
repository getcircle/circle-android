package com.rhlabs.circle.fragments.overview_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.GroupListAdapter;
import com.rhlabs.circle.services.Callback;
import com.rhlabs.circle.services.ServiceClient;
import com.rhlabs.circle.services.WrappedResponse;
import com.rhlabs.circle.utils.EndlessScrollListener;
import com.rhlabs.circle.utils.ListViewUtils;
import com.rhlabs.protobufs.services.group.actions.get_groups.GetGroupsResponseV1;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * Created by anju on 6/29/15.
 */
public class GroupsOverviewFragment extends android.support.v4.app.ListFragment {

    @InjectView(R.id.pbProgressBar)
    ProgressBar mProgressBar;

    @InjectView(R.id.tvNoDataFound)
    TextView mNoDataFoundTextView;

    private static final String ARG_GROUPS_1 = "groups";
    private static final String ARG_GROUPS_NEXT_REQUEST_2 = "groupsNextRequest";

    private ArrayList<GroupV1> mGroups;
    private OnGroupClickedListener mListener;
    private ServiceRequestV1 mGroupsNextRequest;
    private ServiceRequestV1 mFirstGroupRequest;
    private GroupListAdapter mGroupListAdapter;

    public static GroupsOverviewFragment newInstance(ArrayList<GroupV1> groups, ServiceRequestV1 groupsNextRequest) {
        GroupsOverviewFragment fragment = new GroupsOverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUPS_1, groups);
        args.putSerializable(ARG_GROUPS_NEXT_REQUEST_2, groupsNextRequest);
        fragment.setArguments(args);
        return fragment;
    }

    public GroupsOverviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mGroups = (ArrayList<GroupV1>) getArguments().getSerializable(ARG_GROUPS_1);
            mGroupsNextRequest = (ServiceRequestV1) getArguments().getSerializable(ARG_GROUPS_NEXT_REQUEST_2);
            mFirstGroupRequest = mGroupsNextRequest;
        }

        mGroupListAdapter = new GroupListAdapter(getActivity(), mGroups);
        setListAdapter(mGroupListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groups_overview, container, false);
        ButterKnife.inject(this, rootView);

        if (rootView != null) {
            final ListView listView = (ListView) rootView.findViewById(android.R.id.list);

            if (mGroupsNextRequest != null) {
                int pageSize = mGroups.size() > 0 ? Math.min(mGroups.size(), 20) : 20;
                listView.setOnScrollListener(new EndlessScrollListener(pageSize) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        loadMoreData();
                    }
                });

                if (mGroups.size() == 0) {
                    // Call next request explicitly once if there are no groups loaded
                    loadMoreData();
                }
            } else {
                mProgressBar.setVisibility(View.GONE);
                showNoDataMessageView();
            }
        }
        return rootView;
    }

    private void showNoDataMessageView() {
        if (mGroups.size() == 0) {
            mNoDataFoundTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGroupClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnGroupClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onGroupClicked(mGroups.get(position));
        }
    }

    public interface OnGroupClickedListener {
        void onGroupClicked(GroupV1 group);
    }

    private void loadMoreData() {
        if (mGroupsNextRequest != null && mGroupListAdapter != null) {
            ServiceClient.sendRequest(mGroupsNextRequest, Ext_responses.get_groups, new Callback<WrappedResponse>() {
                @Override
                public void success(WrappedResponse wrappedResponse) {
                    GetGroupsResponseV1 response = (GetGroupsResponseV1) wrappedResponse.getResponse();
                    if (response != null && response.groups.size() > 0) {
                        mGroupListAdapter.addAll(response.groups);
                        mGroupListAdapter.notifyDataSetChanged();
                    }

                    mGroupsNextRequest = wrappedResponse.getNextRequest();
                    if (mGroupsNextRequest == null) {
                        mProgressBar.setVisibility(View.GONE);
                        showNoDataMessageView();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Error loading more data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void updateRowWithGroup(GroupV1 updatedGroup) {
        int position = -1;
        GroupV1 group;
        for (int i = 0; i < mGroupListAdapter.getCount(); i++) {
            group = mGroupListAdapter.getItem(i);
            if (group != null && group.id.equals(updatedGroup.id)) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            mGroupListAdapter.remove(mGroupListAdapter.getItem(position));
            mGroupListAdapter.insert(updatedGroup, position);
            ListViewUtils.updateRowWithObject(getListView(), position);
        }
    }
}
