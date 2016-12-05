package com.rhlabs.circle.fragments.overview_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.ProfileListAdapter;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.services.Callback;
import com.rhlabs.circle.services.ServiceClient;
import com.rhlabs.circle.services.WrappedResponse;
import com.rhlabs.circle.utils.EndlessScrollListener;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * Created by anju on 8/17/15.
 */
public abstract class BaseOverviewFragment  extends android.support.v4.app.ListFragment {

    @InjectView(R.id.pbProgressBar)
    ProgressBar mProgressBar;

    protected static final String ARG_LIST_ITEMS_1 = "listItems";
    protected static final String ARG_NEXT_REQUEST_2 = "nextRequest";
    protected static final String ARG_CARD_TYPE_3 = "cardType";

    protected Card.CardType mCardType;
    protected OnProfileClickedListener mListener;
    protected ServiceRequestV1 mNextRequest;
    protected ProfileListAdapter mProfileListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNextRequest = (ServiceRequestV1) getArguments().getSerializable(ARG_NEXT_REQUEST_2);
            mCardType = Card.CardType.fromString(getArguments().getString(ARG_CARD_TYPE_3));
        }

        mProfileListAdapter = new ProfileListAdapter(getActivity(), new ArrayList<>(getListItems()));
        setListAdapter(mProfileListAdapter);

        if (mCardType != null) {
            mProfileListAdapter.setCardType(mCardType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profiles_overview, container, false);
        ButterKnife.inject(this, rootView);

        if (rootView != null) {
            final ListView listView = (ListView) rootView.findViewById(android.R.id.list);

            if (mNextRequest != null) {
                int pageSize = getListItems().size() > 0 ? Math.min(getListItems().size(), 20) : 20;
                listView.setOnScrollListener(new EndlessScrollListener(pageSize) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        loadMoreData();
                    }
                });

                if (getListItems().size() == 0) {
                    // Call next request explicitly once if there are no profiles loaded
                    loadMoreData();
                }
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnProfileClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProfileClickedListener");
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
            Object selectedObject = mProfileListAdapter.getItem(position);
            if (selectedObject instanceof ProfileV1) {
                mListener.onProfileClicked((ProfileV1) selectedObject);
            }
            else if (selectedObject instanceof TeamV1) {
                mListener.onTeamClicked((TeamV1) selectedObject);
            }
        }
    }

    public interface OnProfileClickedListener {
        void onProfileClicked(ProfileV1 profile);
        void onTeamClicked(TeamV1 team);
    }

    private void loadMoreData() {
        if (mNextRequest != null && mProfileListAdapter != null) {
            ServiceClient.sendRequest(mNextRequest, getNextRequestResponseExtension(), new Callback<WrappedResponse>() {
                @Override
                public void success(WrappedResponse wrappedResponse) {
                    Object response = wrappedResponse.getResponse();
                    List listItems = getListItemsFromResponse(response);
                    if (response != null && listItems.size() > 0) {
                        mProfileListAdapter.addAll(listItems);
                        mProfileListAdapter.notifyDataSetChanged();
                    }

                    mNextRequest = wrappedResponse.getNextRequest();
                    if (mNextRequest == null) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Error loading more data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected abstract List getListItems();
    protected abstract Object getNextRequestResponseExtension();
    protected abstract List getListItemsFromResponse(Object response);
}
