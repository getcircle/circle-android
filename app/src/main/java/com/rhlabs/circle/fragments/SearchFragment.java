package com.rhlabs.circle.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.activities.BaseActivity;
import com.rhlabs.circle.adapters.CardAdapter;
import com.rhlabs.circle.adapters.SearchAdapter;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.models.SearchCategory;
import com.rhlabs.circle.services.OrganizationService;
import com.rhlabs.circle.services.ProfileService;
import com.rhlabs.circle.services.SearchService;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.organization.containers.OrganizationV1;
import com.rhlabs.protobufs.services.search.containers.SearchResultV1;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;

public class SearchFragment extends BaseCardListFragment {

    private String mQuery;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
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
    protected CardAdapter getCardAdapter() {
        return new SearchAdapter(getActivity(), mCards, mCallback);
    }

    @Override
    protected void loadData() {
        if (mQuery != null && !mQuery.isEmpty()) {
            SearchService.search(mQuery, new SearchService.SearchResultsCallback() {
                @Override
                public void success(List<SearchResultV1> results, Map<String, String> errors) {
                    loadSearchCards(results);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Error loading search", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            });
        } else {
            loadDefaultSearchSuggestions();
        }
    }

    @Override
    protected void configureToolbar() {

        // Search container
        LinearLayout searchTextFieldContainer = new LinearLayout(getActivity());
        LinearLayout.LayoutParams searchTextFieldLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        searchTextFieldLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        searchTextFieldContainer.setGravity(Gravity.CENTER_VERTICAL);
        searchTextFieldContainer.setLayoutParams(searchTextFieldLayoutParams);
        searchTextFieldContainer.setOrientation(LinearLayout.HORIZONTAL);
        searchTextFieldContainer.setGravity(Gravity.CENTER_VERTICAL);
        mToolbar.addView(searchTextFieldContainer);

        // Search text field
        final EditText searchTextField = new EditText(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1
        );
        searchTextField.setLayoutParams(layoutParams);
        searchTextField.setSingleLine(true);
        searchTextField.setPadding(0, 1, 0, 0);
        searchTextField.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchTextField.setGravity(Gravity.CENTER_VERTICAL);
        searchTextField.setEllipsize(TextUtils.TruncateAt.END);
        searchTextField.setHint(R.string.search_hint_text);
        searchTextField.setTextColor(getResources().getColor(R.color.app_light_text_color));
        searchTextField.setHintTextColor(Color.parseColor("#ccffffff"));
        searchTextField.setBackgroundColor(Color.TRANSPARENT);
        searchTextFieldContainer.addView(searchTextField);
        searchTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mQuery = searchTextField.getText().toString();
                loadData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Clear button
        ImageButton clearButton = new ImageButton(getActivity());
        clearButton.setImageResource(R.drawable.abc_ic_clear_mtrl_alpha);
        clearButton.setBackgroundColor(getResources().getColor(R.color.app_ui_background_color));
        ViewGroup.LayoutParams buttonLayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        clearButton.setLayoutParams(buttonLayoutParams);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTextField.setText("");
            }
        });
        searchTextFieldContainer.addView(clearButton);
    }

    @Override
    protected void configureListView() {
        if (mRecyclerView != null) {
            mRecyclerView.setPadding(0, mRecyclerView.getPaddingTop(), 0, mRecyclerView.getPaddingBottom());
            mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
                    View currentFocussedView = getActivity().getCurrentFocus();
                    if (currentFocussedView != null) {
                        imm.hideSoftInputFromWindow(currentFocussedView.getWindowToken(), 0);
                    }
                    return false;
                }
            });
        }
    }

    private void loadDefaultSearchSuggestions() {
        OrganizationV1 organizationV1 = AppPreferences.getLoggedInUserOrganization();
        if (organizationV1 != null) {
            int profilesCount = organizationV1.profile_count == null ? 0 : organizationV1.profile_count;
            int locationsCount = organizationV1.location_count == null ? 0 : organizationV1.location_count;
            int teamsCount = organizationV1.team_count == null ? 0 : organizationV1.team_count;
            List<SearchCategory> searchCategories = new ArrayList<>();
            searchCategories.add(new SearchCategory(SearchCategory.SearchCategoryType.PROFILES, profilesCount));
            searchCategories.add(new SearchCategory(SearchCategory.SearchCategoryType.LOCATIONS, locationsCount));
            searchCategories.add(new SearchCategory(SearchCategory.SearchCategoryType.TEAMS, teamsCount));

            mCards.clear();
            Card searchCategoriesCard = new Card(Card.CardType.SearchCategories, "Explore".toUpperCase());
            searchCategoriesCard.setCardFullScreenWidth(true);
            searchCategoriesCard.addContent(searchCategories);
            searchCategoriesCard.addHeader = true;
            mCards.add(searchCategoriesCard);
            mAdapter.setCards(mCards);
            mAdapter.notifyDataSetChanged();
            showProgress(false);
        }
    }

    private void loadSearchCards(List<SearchResultV1> results) {

        mCards.clear();

        if (results != null) {
            ArrayList<Object> content = new ArrayList<>();
            for (SearchResultV1 result : results) {

                switch (result.result_object) {

                    case PROFILE:
                        content.add(result.profile);
                        break;

                    case TEAM:
                        content.add(result.team);
                        break;

                    case LOCATION:
                        content.add(result.location);
                        break;

                    default:
                        break;
                }
            }

            if (content.size() > 0) {
                Card card = new Card(Card.CardType.Profiles, "People");
                card.addContent(content);
                card.addHeader = false;
                card.setCardFullScreenWidth(true);
                mCards.add(card);
            }
        }

        mAdapter.setCards(mCards);
        mAdapter.notifyDataSetChanged();
        showProgress(false);
    }

    @Subscribe
    public void onStatsTileClicked(SearchCategory searchCategory) {

        String orgId = AppPreferences.getOrganizationId();
        if (orgId == null) {
            return;
        }

        OrganizationV1 currentOrganization = new OrganizationV1.Builder()
                .id(orgId)
                .build();

        Card dummyCard = null;
        switch (searchCategory.getType()) {
            case PROFILES:
                dummyCard = new Card(Card.CardType.Profiles, "People");
                dummyCard.setContentNextRequest(
                        ProfileService.getProfilesServiceRequest(currentOrganization)
                );
                break;

            case TEAMS:
                dummyCard = new Card(Card.CardType.TeamsGrid, "Teams");
                dummyCard.setContentNextRequest(
                        OrganizationService.getTeamsServiceRequest()
                );
                break;

            case LOCATIONS:
                dummyCard = new Card(Card.CardType.Offices, "Offices");
                dummyCard.setContentNextRequest(
                        OrganizationService.getLocationsServiceRequest()
                );
                break;
        }

        if (dummyCard != null) {
            ((BaseActivity) getActivity()).onCardHeaderClicked(dummyCard, null);
        }

    }
}
