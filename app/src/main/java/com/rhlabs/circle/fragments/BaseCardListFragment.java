package com.rhlabs.circle.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.activities.MainActivity;
import com.rhlabs.circle.adapters.CardAdapter;
import com.rhlabs.circle.interfaces.OnCardHeaderClickedListener;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.models.KeyValueData;
import com.rhlabs.circle.services.OrganizationService;
import com.rhlabs.circle.utils.LocaleUtils;
import com.rhlabs.protobufs.services.organization.containers.integration.IntegrationTypeV1;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import retrofit.RetrofitError;

/**
 * Created by anju on 6/6/15.
 */
public abstract class BaseCardListFragment extends Fragment {

    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;

    @InjectView(R.id.rvCardsList)
    protected RecyclerView mRecyclerView;

    @Optional
    @InjectView(R.id.pbCircleProgress)
    View mProgressView;

    protected CardAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Card> mCards;
    protected OnCardHeaderClickedListener mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCards = new ArrayList<>();
        mAdapter = getCardAdapter();
        loadData();
    }

    public void showProgress(final boolean show) {
        if (mProgressView != null) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getCardListLayout(), container, false);
        ButterKnife.inject(this, rootView);

        // Setup Toolbar and Back button
        final FragmentActivity fragmentActivity = getActivity();
        ((AppCompatActivity) fragmentActivity).setSupportActionBar(mToolbar);
        final ActionBar supportActionBar = ((AppCompatActivity) fragmentActivity).getSupportActionBar();
        if (supportActionBar != null) {
            if (fragmentActivity instanceof MainActivity) {
                supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            } else {
                supportActionBar.setHomeButtonEnabled(true);
                supportActionBar.setDisplayShowHomeEnabled(true);
            }
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup Recycler view
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(fragmentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        showProgress(!(mCards.size() > 0));
        configureToolbar();
        configureListView();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnCardHeaderClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCardHeaderClickedListener");
        }
    }

    protected void clearData() {
        mCards.clear();
        mAdapter.setCards(mCards);
        mAdapter.notifyDataSetChanged();
    }

    protected abstract void loadData();

    protected CardAdapter getCardAdapter() {
        return new CardAdapter(getActivity(), mCards, mCallback);
    }

    protected int getCardListLayout() {
        return R.layout.fragment_card_list_base;
    }

    protected void configureListView() {

    }

    protected void configureToolbar() {

    }

    final protected void addGroupsCard(final boolean fullScreen) {
        OrganizationService.getIntegrationStatus(IntegrationTypeV1.GOOGLE_GROUPS, false, new OrganizationService.GetIntegrationStatusCallback() {
            @Override
            public void success(Boolean status) {
                if (!status) {
                    return;
                }

                // GroupsCard
                Card groupsCard = new Card(Card.CardType.KeyValue, null);
                groupsCard.addHeader = false;
                if (fullScreen) {
                    groupsCard.setCardFullScreenWidth(true);
                }
                List<KeyValueData> keyValueDataList = new ArrayList<>();
                String keyValueTitle = getResources()
                        .getString(R.string.groups_plural_title)
                        .toUpperCase(LocaleUtils.currentLocale());

                KeyValueData groupsTriggerData = new KeyValueData(
                        "groups",
                        keyValueTitle,
                        "",
                        KeyValueData.KeyValueDataType.GroupsPlaceholder
                );
                keyValueDataList.add(groupsTriggerData);
                groupsCard.addContent(keyValueDataList);
                mCards.add(groupsCard);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Error fetching google groups integration status.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
