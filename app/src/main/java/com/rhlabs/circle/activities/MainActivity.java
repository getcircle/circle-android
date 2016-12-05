package com.rhlabs.circle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.rhlabs.circle.R;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.common.CircleApp;
import com.rhlabs.circle.services.CircleRegistrationIntentService;
import com.rhlabs.circle.services.OrganizationService;
import com.rhlabs.protobufs.services.organization.actions.get_organization.GetOrganizationResponseV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.user.containers.UserV1;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.fabric.sdk.android.Fabric;
import retrofit.RetrofitError;

public class MainActivity extends BaseActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CODE_AUTH_ACTION = 1002;
    private static final int REQUEST_CODE_SETTINGS = 1003;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.ivOrganizationImage)
    ImageView mOrganizationImageView;

    @InjectView(R.id.btnSearchTrigger)
    Button mSearchTriggerButton;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        CircleApp.setContext(getApplicationContext());
        ButterKnife.inject(this);
        showAuthActivity(false, false);
        setSupportActionBar(mToolbar);
        configureSearchField();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_AUTH_ACTION && resultCode == RESULT_OK) {

            // User authenticated successfully
            checkAndPresentOnboardingIfNeeded();
            fetchAndCacheOrganizationInfo();

            // Record device and push notification token
            checkAndRegisterForNotifications(true);
        } else if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra(INTENT_ARG_SHOULD_SIGN_OUT, false)) {
                showAuthActivity(true, false);
            }
            else if (data != null && data.getBooleanExtra(INTENT_ARG_SHOULD_DISCONNECT, false)) {
                showAuthActivity(true, true);
            }
        }
    }

    private void configureSearchField() {
        mSearchTriggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showAuthActivity(boolean forced, boolean disconnect) {
        String loggedInProfileId = AppPreferences.getLoggedInUserProfileId();
        if (AppPreferences.getAuthToken() == null || loggedInProfileId == null || forced) {
            Intent intent = new Intent(this, AuthActivity.class);
            if (forced) {
                if (disconnect) {
                    intent.putExtra(INTENT_ARG_SHOULD_DISCONNECT, true);
                } else {
                    intent.putExtra(INTENT_ARG_SHOULD_SIGN_OUT, true);
                }
            } else if (loggedInProfileId == null) {
                intent.putExtra(INTENT_ARG_SHOULD_SHOW_HOMELESS_VIEW, true);
            }
            startActivityForResult(intent, REQUEST_CODE_AUTH_ACTION);
        } else {
            checkAndPresentOnboardingIfNeeded();
            checkAndRegisterForNotifications(false);
            fetchAndCacheOrganizationInfo();
        }
    }

    private void checkAndRegisterForNotifications(boolean forced) {
        if ((!AppPreferences.getSentTokenToServer() || forced) && checkPlayServices()) {
            Intent intent = new Intent(this, CircleRegistrationIntentService.class);
            startService(intent);
        }
    }

    private void checkAndPresentOnboardingIfNeeded() {
        UserV1 loggedInUser = AppPreferences.getLoggedInUser();
        ProfileV1 loggedInUserProfile = AppPreferences.getLoggedInUserProfile();
        if (loggedInUser != null &&
                loggedInUserProfile != null &&
                (!loggedInUser.phone_number_verified || !loggedInUserProfile.verified)) {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;

            case R.id.menu_profile:
                ProfileV1 profileV1 = AppPreferences.getLoggedInUserProfile();
                if (profileV1 != null) {
                    onProfileClicked(profileV1);
                } else {
                    Log.d(LOG_TAG, "Logged in user profile not found!");
                }
                return true;

            case R.id.menu_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SETTINGS);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void fetchAndCacheOrganizationInfo() {
        String orgId = AppPreferences.getOrganizationId();
        if (orgId != null && !orgId.isEmpty()) {

            // Get org details
            OrganizationService.getOrganization(new OrganizationService.GetOrganizationCallback() {
                @Override
                public void success(GetOrganizationResponseV1 response) {
                    if (response != null && response.organization != null) {
                        Picasso.with(MainActivity.this)
                                .load(response.organization.image_url)
                                .fit()
                                .centerInside()
                                .into(mOrganizationImageView);
                        AppPreferences.putLoggedInUserOrganization(response.organization);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }
}
