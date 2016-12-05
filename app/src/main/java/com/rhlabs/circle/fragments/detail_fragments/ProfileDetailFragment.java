package com.rhlabs.circle.fragments.detail_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rhlabs.circle.R;
import com.rhlabs.circle.activities.BaseActivity;
import com.rhlabs.circle.activities.EditInfoContainerActivity;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.models.EditCardEvent;
import com.rhlabs.circle.models.KeyValueData;
import com.rhlabs.circle.models.ProfileContactMethod;
import com.rhlabs.circle.services.GroupService;
import com.rhlabs.circle.services.ProfileService;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.circle.utils.DateUtils;
import com.rhlabs.circle.utils.LocationUtils;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.containers.ContactMethodV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by anju on 6/12/15.
 */
public class ProfileDetailFragment extends BaseDetailFragment {

    private class ProfileData {
        ProfileV1 manager;
        TeamV1 team;
        LocationV1 location;

        public ProfileData(ProfileV1 manager, TeamV1 team, List<LocationV1> locations) {
            this.manager = manager;
            this.team = team;
            this.location = locations.get(0);
        }
    }

    private ProfileV1 mProfileV1;
    private ProfileData mProfileData;
    private boolean isLoggedInUser = false;
    private static final String ARG_PROFILE_1 = "profile";
    private static final int REQUEST_CODE_EDIT_VIEWS = 1001;

    public static ProfileDetailFragment newInstance(ProfileV1 profileV1) {
        ProfileDetailFragment fragment = new ProfileDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE_1, profileV1);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mProfileV1 = (ProfileV1) getArguments().getSerializable(ARG_PROFILE_1);
            String loggedInUserProfileId = AppPreferences.getLoggedInUserProfileId();
            if (loggedInUserProfileId != null &&
                    !loggedInUserProfileId.isEmpty() &&
                    mProfileV1.id.equals(loggedInUserProfileId)
                    ) {
                // Hide custom view for logged in user for now
                isLoggedInUser = true;
            }
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_VIEWS && resultCode == BaseActivity.RESULT_OK) {
            loadData();
        }
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
        ProfileService.getExtendedProfile(mProfileV1, new ProfileService.GetExtendedProfileCallback() {
            @Override
            public void success(ProfileV1 profile, ProfileV1 manager, TeamV1 team, List<ProfileV1> peers, List<ProfileV1> direct_reports, List<LocationV1> locations) {
                mProfileV1 = profile;
                mProfileData = new ProfileData(manager, team, locations);
                loadCards();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    protected String getViewTitle() {
        StringBuilder titleBuilder = new StringBuilder();
        titleBuilder.append(mProfileV1.first_name);

        if (mProfileV1.nickname != null && !mProfileV1.nickname.isEmpty()) {
            titleBuilder.append(" (" + mProfileV1.nickname + ")");
        }

        titleBuilder.append(" " + mProfileV1.last_name);
        return titleBuilder.toString();
    }

    private void loadCards() {
        mCards.clear();
        showProgress(false);

        if (mProfileV1.image_url != null && !mProfileV1.image_url.isEmpty()) {
            Picasso
                    .with(getActivity())
                    .load(mProfileV1.image_url)
                    .fit()
                    .centerCrop()
                    .into(mDetailViewBackdropImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (mDetailViewBackdropImageOverlayView != null) {
                                mDetailViewBackdropImageOverlayView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
            addContactsCard();
            addAboutCard();
            addManagerTeamCard();
            addLocationCard();
            addInfoCard();
            addGroupsCard(true);
        }
    }

    private void addContactsCard() {

        List<ProfileContactMethod> profileContactMethods = new ArrayList<>();
        ProfileContactMethod phoneMethod = new ProfileContactMethod(
                ContactMethodV1.ContactMethodTypeV1.CELL_PHONE,
                mProfileV1
        );
        profileContactMethods.add(phoneMethod);

        ProfileContactMethod textMethod = new ProfileContactMethod(
                ContactMethodV1.ContactMethodTypeV1.CELL_PHONE,
                mProfileV1,
                ProfileContactMethod.SubType.SMS
        );
        profileContactMethods.add(textMethod);

        ProfileContactMethod emailMethod = new ProfileContactMethod(
                ContactMethodV1.ContactMethodTypeV1.EMAIL,
                mProfileV1
        );
        profileContactMethods.add(emailMethod);

        ProfileContactMethod miscMethod = new ProfileContactMethod(null, mProfileV1);
        profileContactMethods.add(miscMethod);

        Card contactsCard = new Card(Card.CardType.ContactsBar, null);
        contactsCard.setCardFullScreenWidth(true);
        contactsCard.addHeader = false;
        contactsCard.addBottomMargin = isLoggedInUser;
        contactsCard.addContent(profileContactMethods);
        mCards.add(contactsCard);
    }

    private void addAboutCard() {
        Card aboutCard = new Card(Card.CardType.TextValue, getResources().getString(R.string.about_card_title));
        aboutCard.setCardFullScreenWidth(true);
        aboutCard.addHeader = isLoggedInUser;
        aboutCard.isEditable = isLoggedInUser;
        String aboutText = mProfileV1.status.value;
        if (aboutText == null || aboutText.isEmpty()) {
            aboutText = "Hi! I'm " + mProfileV1.first_name + ".";
            if (mProfileData.team != null && mProfileData.location != null) {
                aboutText += " I work on the " + mProfileData.team.name + " team in " + LocationUtils.officeName(mProfileData.location) + ".";
            }
        }

        List<String> textValues = new ArrayList<>();
        textValues.add(aboutText);
        aboutCard.addContent(textValues);
        mCards.add(aboutCard);
    }

    private void addManagerTeamCard() {
        Card managerTeamCard = new Card(
                Card.CardType.Profiles,
                getResources().getString(R.string.profile_section_title_manager_team)
        );
        managerTeamCard.setCardFullScreenWidth(true);
        ArrayList profileObjects = new ArrayList();
        if (mProfileData != null) {
            if (mProfileData.manager != null) {
                profileObjects.add(mProfileData.manager);
            }

            if (mProfileData.team != null) {
                profileObjects.add(mProfileData.team);
            }
        }

        if (profileObjects.size() > 0) {
            managerTeamCard.addContent(profileObjects);
            mCards.add(managerTeamCard);
        }
    }

    private void addLocationCard() {
        Card locationCard = new Card(
                Card.CardType.Profiles,
                getResources().getString(R.string.profile_section_title_office)
        );
        locationCard.setCardFullScreenWidth(true);
        ArrayList profileObjects = new ArrayList();
        if (mProfileData != null) {
            if (mProfileData.location != null) {
                profileObjects.add(mProfileData.location);
            }
        }

        if (profileObjects.size() > 0) {
            locationCard.addContent(profileObjects);
            mCards.add(locationCard);
        }
    }

    private void addInfoCard() {
        Card infoCard = new Card(Card.CardType.KeyValue, getResources().getString(R.string.profile_section_title_info));
        infoCard.setCardFullScreenWidth(true);

        List<KeyValueData> keyValueDataList = new ArrayList<>();
        if (mProfileV1.hire_date != null && !mProfileV1.hire_date.isEmpty()) {
            String formattedHireDate = DateUtils.getFormattedHireDate(mProfileV1.hire_date);
            if (formattedHireDate != null) {
                KeyValueData hireDateKeyValueData = new KeyValueData(
                        "hire_date",
                        getResources().getString(R.string.hire_date_label),
                        formattedHireDate,
                        KeyValueData.KeyValueDataType.HireDate
                );
                keyValueDataList.add(hireDateKeyValueData);
            }
        }

        if (mProfileV1.birth_date != null && !mProfileV1.birth_date.isEmpty()) {
            String formattedBirthDate = DateUtils.getFormattedBirthDate(mProfileV1.birth_date);
            if (formattedBirthDate != null) {
                KeyValueData birthDateKeyValueData = new KeyValueData(
                        "birth_date",
                        getResources().getString(R.string.birth_date_label),
                        formattedBirthDate,
                        KeyValueData.KeyValueDataType.Birthday
                );
                keyValueDataList.add(birthDateKeyValueData);
            }
        }

        if (keyValueDataList.size() > 0) {
            infoCard.addContent(keyValueDataList);
            mCards.add(infoCard);
        }
    }

    @Subscribe
    public void onKeyValueRowSelected(KeyValueData keyValueData) {
        switch (keyValueData.getKeyValueDataType()) {
            case GroupsPlaceholder:
                Card card = new Card(Card.CardType.Groups, getResources().getString(R.string.groups_plural_title));
                card.setContentNextRequest(
                        GroupService.getGroupsRequest(mProfileV1)
                );

                ((BaseActivity) getActivity()).onCardHeaderClicked(card, null);
                break;

            default:
                break;
        }
    }

    @Subscribe
    public void onEditCardClicked(EditCardEvent editCardEvent) {
        final Card card = editCardEvent.getCard();
        switch (card.getCardType()) {
            case TextValue:
                if (card.getTitle() != null && card.getTitle().equals(getResources().getString(R.string.about_card_title))) {
                    Intent intent = new Intent(getActivity(), EditInfoContainerActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_EDIT_VIEWS);
                }
                break;

            default:
                break;
        }
    }
}
