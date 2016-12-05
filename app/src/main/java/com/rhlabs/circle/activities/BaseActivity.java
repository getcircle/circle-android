package com.rhlabs.circle.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.rhlabs.circle.activities.detail_activities.GroupDetailActivity;
import com.rhlabs.circle.activities.detail_activities.LocationDetailActivity;
import com.rhlabs.circle.activities.detail_activities.ProfileDetailActivity;
import com.rhlabs.circle.activities.detail_activities.TeamDetailActivity;
import com.rhlabs.circle.activities.overview_activities.GroupsOverviewActivity;
import com.rhlabs.circle.activities.overview_activities.LocationsOverviewActivity;
import com.rhlabs.circle.activities.overview_activities.ProfilesOverviewActivity;
import com.rhlabs.circle.fragments.ContactMethodsFragment;
import com.rhlabs.circle.fragments.overview_fragments.GroupsOverviewFragment;
import com.rhlabs.circle.fragments.overview_fragments.LocationsOverviewFragment;
import com.rhlabs.circle.fragments.overview_fragments.ProfilesOverviewFragment;
import com.rhlabs.circle.interfaces.OnCardHeaderClickedListener;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.models.ProfileContactMethod;
import com.rhlabs.circle.models.ProfileImagesData;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.circle.utils.IntentDataHolder;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.containers.ContactMethodV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.squareup.otto.Subscribe;

/**
 * Created by anju on 6/11/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        OnCardHeaderClickedListener,
        LocationsOverviewFragment.OnLocationClickedListener,
        ProfilesOverviewFragment.OnProfileClickedListener,
        GroupsOverviewFragment.OnGroupClickedListener {

    public final static String INTENT_ARG_LOCATION = "com.rhlabs.circle.LOCATION";
    public final static String INTENT_ARG_LOCATIONS = "com.rhlabs.circle.LOCATIONS";
    public final static String INTENT_ARG_PROFILE = "com.rhlabs.circle.PROFILE";
    public final static String INTENT_ARG_PROFILES = "com.rhlabs.circle.PROFILES";
    public final static String INTENT_ARG_TEAM = "com.rhlabs.circle.TEAM";
    public final static String INTENT_ARG_TEAMS = "com.rhlabs.circle.TEAMS";
    public final static String INTENT_ARG_GROUP = "com.rhlabs.circle.GROUP";
    public final static String INTENT_ARG_GROUPS = "com.rhlabs.circle.GROUPS";

    public final static String INTENT_ARG_NEXT_REQUEST = "com.rhlabs.circle.NEXT_REQUEST";

    public final static String INTENT_ARG_SRC_CARD_TYPE = "com.rhlabs.circle.CARD_TYPE";

    public final static String INTENT_ARG_VIEW_TITLE = "com.rhlabs.circle.VIEW_TITLE";
    public final static String INTENT_ARG_SHOULD_SIGN_OUT = "com.rhlabs.circle.SIGN_OUT";
    public final static String INTENT_ARG_SHOULD_DISCONNECT = "com.rhlabs.circle.DISCONNECT";
    public final static String INTENT_ARG_SHOULD_SHOW_HOMELESS_VIEW = "com.rhlabs.circle.SHOW_HOMELESS_VIEW";

    public final static String INTENT_ARG_WEB_URL = "com.rhlabs.circle.WEB_URL";

    public final static int REQUEST_CODE_DETAIL_ACTIVITY = 1300;

    protected Object busEventListener;

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getMainBus().unregister(busEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (busEventListener == null) {
            initBusEventListener();
        }
        BusProvider.getMainBus().register(busEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Finish activity on back button
        if (id == android.R.id.home && !(this instanceof MainActivity)) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardHeaderClicked(Card card, View view) {

        String intentDataKey = null;
        Class<?> overviewActivityClass = null;
        switch (card.getCardType()) {
            case Anniversaries:
            case Birthdays:
            case ProfileImages:
            case NewHires:
            case Profiles:
                intentDataKey = INTENT_ARG_PROFILES;
                overviewActivityClass = ProfilesOverviewActivity.class;
                break;

            case Groups:
                intentDataKey = INTENT_ARG_GROUPS;
                overviewActivityClass = GroupsOverviewActivity.class;
                break;

            case TeamsGrid:
                intentDataKey = INTENT_ARG_TEAMS;
                overviewActivityClass = ProfilesOverviewActivity.class;
                break;

            case Offices:
                intentDataKey = INTENT_ARG_LOCATIONS;
                overviewActivityClass = LocationsOverviewActivity.class;
                break;

            default:
                break;
        }

        if (overviewActivityClass != null) {
            IntentDataHolder.getInstance().setData(INTENT_ARG_NEXT_REQUEST, card.getContentNextRequest());
            IntentDataHolder.getInstance().setData(intentDataKey, card.getAllContent());
            Intent overviewActivityIntent = new Intent(this, overviewActivityClass);
            overviewActivityIntent.putExtra(INTENT_ARG_SRC_CARD_TYPE, card.getCardType().toString());
            overviewActivityIntent.putExtra(INTENT_ARG_VIEW_TITLE, card.getTitle());
            startActivity(overviewActivityIntent);
        }
    }

    protected void setToolbarAndTitle(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);

            if (title != null) {
                supportActionBar.setTitle(title);
            }
        }
    }

    private void initBusEventListener() {

        // Event bus does not traverse the class hierarchy looking for listeners
        // The only way to make the event bus handlers be shared is by defining a common
        // object which forces the calls to go to the parent and thus allows
        // these to be shared as well as override as needed.
        busEventListener = new Object() {
            @Subscribe
            public void onLocationClicked(LocationV1 location) {
                BaseActivity.this.onLocationClicked(location);
            }

            @Subscribe
            public void onProfileClicked(ProfileV1 profile) {
                BaseActivity.this.onProfileClicked(profile);
            }

            @Subscribe
            public void onTeamClicked(TeamV1 team) {
                BaseActivity.this.onTeamClicked(team);
            }

            @Subscribe
            public void onGroupClicked(GroupV1 group) {
                BaseActivity.this.onGroupClicked(group);
            }

            @Subscribe
            public void onProfileImagesClicked(ProfileImagesData profileImagesData) {
                BaseActivity.this.onProfileImagesClicked(profileImagesData);
            }

            @Subscribe
            public void onProfileContactMethodClicked(ProfileContactMethod profileContactMethod) {
                BaseActivity.this.onProfileContactMethodClicked(profileContactMethod);
            }
        };
    }

    @Override
    public void onLocationClicked(LocationV1 location) {
        Intent intent = new Intent(this, LocationDetailActivity.class);
        intent.putExtra(INTENT_ARG_LOCATION, location);
        startActivity(intent);
    }

    @Override
    public void onProfileClicked(ProfileV1 profile) {
        Intent intent = new Intent(this, ProfileDetailActivity.class);
        intent.putExtra(INTENT_ARG_PROFILE, profile);
        startActivity(intent);
    }

    @Override
    public void onTeamClicked(TeamV1 team) {
        Intent intent = new Intent(this, TeamDetailActivity.class);
        intent.putExtra(INTENT_ARG_TEAM, team);
        startActivity(intent);
    }

    @Override
    public void onGroupClicked(GroupV1 group) {
        Intent intent = new Intent(this, GroupDetailActivity.class);
        intent.putExtra(INTENT_ARG_GROUP, group);
        startActivityForResult(intent, REQUEST_CODE_DETAIL_ACTIVITY);
    }

    private void onProfileImagesClicked(ProfileImagesData profileImagesData) {
        if (profileImagesData != null && profileImagesData.getProfiles().size() > 0) {
            // Simulate the click as if the header was clicked
            Card profileImagesCard = new Card(profileImagesData.getType(), profileImagesData.getTitle());
            profileImagesCard.addContent(profileImagesData.getProfiles());
            onCardHeaderClicked(profileImagesCard, null);
        }
    }

    private void onProfileContactMethodClicked(ProfileContactMethod profileContactMethod) {
        String contactMethodValue;
        ContactMethodV1.ContactMethodTypeV1 contactMethodType = profileContactMethod.getContactMethodType();
        if (contactMethodType == null) {
            // Show more/all contact info
            ContactMethodsFragment contactMethodsFragment = new ContactMethodsFragment();
            Bundle args = new Bundle();
            args.putSerializable(INTENT_ARG_PROFILE, profileContactMethod.getProfile());
            contactMethodsFragment.setArguments(args);
            contactMethodsFragment.show(getSupportFragmentManager(), "contacts_dialog");
            return;
        }

        switch (contactMethodType) {
            case CELL_PHONE:
                if (profileContactMethod.getSubType() != null && profileContactMethod.getSubType() == ProfileContactMethod.SubType.SMS) {
                    contactMethodValue = profileContactMethod.getDataForContactMethodType(contactMethodType);
                    if (contactMethodValue != null && !contactMethodValue.isEmpty()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.fromParts("sms", contactMethodValue, null));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                    break;
                }
                // Continue to Dial if sub type is not SMS
            case PHONE:
                contactMethodValue = profileContactMethod.getDataForContactMethodType(contactMethodType);
                if (contactMethodValue != null && !contactMethodValue.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.fromParts("tel", contactMethodValue, null));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;

            case EMAIL:
                if (profileContactMethod.getProfile().email != null && !profileContactMethod.getProfile().email.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{profileContactMethod.getProfile().email});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Hello");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;

            case TWITTER:
                contactMethodValue = profileContactMethod.getDataForContactMethodType(contactMethodType);
                if (contactMethodValue != null && !contactMethodValue.isEmpty()) {
                    Intent intent;
                    try {
                        // Get the Twitter app if possible
                        getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + contactMethodValue));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // No Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + contactMethodValue));
                    }
                    startActivity(intent);
                }
                break;

            case FACEBOOK:
                contactMethodValue = profileContactMethod.getDataForContactMethodType(contactMethodType);
                if (contactMethodValue != null && !contactMethodValue.isEmpty()) {
                    Intent intent;
                    try {
                        // Get the Twitter app if possible
                        getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // No Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                    }
                    startActivity(intent);
                }
                break;

            case SLACK:
                break;

            case HIPCHAT:
                contactMethodValue = profileContactMethod.getDataForContactMethodType(contactMethodType);
                if (contactMethodValue != null && !contactMethodValue.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("hipchat://www.hipchat.com/user/" + contactMethodValue));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;

            case SKYPE:
                contactMethodValue = profileContactMethod.getDataForContactMethodType(contactMethodType);
                if (contactMethodValue != null && !contactMethodValue.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("skype:" + contactMethodValue));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;

            default:
                break;
        }
    }
}
