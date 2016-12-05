package com.rhlabs.circle.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.rhlabs.circle.R;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.services.ProfileService;
import com.rhlabs.circle.services.UserService;
import com.rhlabs.protobufs.services.profile.actions.get_profile.GetProfileResponseV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.user.actions.authenticate_user.AuthenticateUserRequestV1;
import com.rhlabs.protobufs.services.user.actions.authenticate_user.AuthenticateUserResponseV1;
import com.rhlabs.protobufs.services.user.containers.UserV1;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class AuthActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ServerAuthCodeCallbacks,
        View.OnClickListener {

    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 1001;
    private static final int RC_HOMELESS_VIEW = 1002;
    private static final String LOG_TAG = AuthActivity.class.getSimpleName();
    private static final String SERVER_KEY = "1077014421904-1a697ks3qvtt6975qfqhmed8529en8s2.apps.googleusercontent.com";

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked = false;
    private boolean mShouldSignOut;
    private boolean mShouldDisconnect;

    @InjectView(R.id.login_progress)
    View mProgressView;

    @InjectView(R.id.sign_in_button)
    Button mSignInButton;

    @InjectView(R.id.login_form)
    View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .requestServerAuthCode(SERVER_KEY, this)
                .build();

        mShouldSignOut = intent.getBooleanExtra(BaseActivity.INTENT_ARG_SHOULD_SIGN_OUT, false);
        mShouldDisconnect = intent.getBooleanExtra(BaseActivity.INTENT_ARG_SHOULD_DISCONNECT, false);
        if (supportsGooglePlayServices()) {
            mSignInButton.setOnClickListener(this);
        } else {
            mSignInButton.setVisibility(View.GONE);
            return;
        }

        if (intent.getBooleanExtra(BaseActivity.INTENT_ARG_SHOULD_SHOW_HOMELESS_VIEW, false)) {
            showHomelessView();
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View rootView = super.onCreateView(parent, name, context, attrs);
        return rootView;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View view) {
        // Call sign in from here
        if (view.getId() == R.id.sign_in_button && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            mGoogleApiClient.connect();
            showProgress(true);
        }
    }

    @Override
    public void onBackPressed() {
        // Do not allow going back from this view using the hardware button
    }

    public void signOut() {

        Log.d(LOG_TAG, "Signing out user");
        clearCaches();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
            mGoogleApiClient.disconnect();
        }
        showProgress(false);
        mShouldSignOut = false;
    }

    public void revokeAccessAndDisconnect() {

        Log.d(LOG_TAG, "Revoke access and disconnect");
        clearCaches();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        showProgress(false);
        mShouldDisconnect = false;
    }

    private void clearCaches() {
        // Clear everything from the cache
        AppPreferences.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.reconnect();
            }
        } else if (requestCode == RC_HOMELESS_VIEW) {
            if (responseCode == RESULT_OK || responseCode == RESULT_CANCELED) {
                mShouldSignOut = true;
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            if (mSignInClicked && result.hasResolution()) {
                try {
                    mIntentInProgress = true;
                    result.startResolutionForResult(this, RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Show dialog using GooglePlayServicesUtil.getErrorDialog()
                if (result.getErrorCode() != 4) {
                    // 4 is sign in required...it simply indicates that the user is not signed in.
                    Log.d(LOG_TAG, result.toString());
                    showErrorDialog(result.getErrorCode());
                } else {
                    showProgress(false);
                }
            }
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mIntentInProgress = false;
        showProgress(false);
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), RC_SIGN_IN);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((AuthActivity) getActivity()).onDialogDismissed();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors. mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        Log.d(LOG_TAG, "Google API client connected");
        if (!mSignInClicked) {
            if (mShouldSignOut) {
                // If sign out was requested and sign in button wasn't explicitly clicked, sign out user
                signOut();
            } else if (mShouldDisconnect) {
                revokeAccessAndDisconnect();
            }
        }

        mSignInClicked = false;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    protected void dismissActivity() {
        boolean connected = mGoogleApiClient.isConnected();
        if (connected) {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * Check if the device supports Google Play Services.  It's best
     * practice to check first rather than handling this as an error case.
     *
     * @return whether the device supports Google Play Services
     */
    private boolean supportsGooglePlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    }

    @Override
    public CheckResult onCheckServerAuthorization(String s, Set<Scope> set) {
        if (mSignInClicked || AppPreferences.getLoggedInUserId() == null) {
            return CheckResult.newAuthRequiredResult(set);
        } else {
            return CheckResult.newAuthNotRequiredResult();
        }
    }

    @Override
    public boolean onUploadServerAuthCode(String idToken, String serveAuthCode) {
        Log.d("AuthActivity", idToken);
        authenticateUser(idToken, serveAuthCode);
        return true;
    }

    private void authenticateUser(String idToken, String authCode) {
        AuthenticateUserRequestV1.CredentialsV1 credentialsV1 = new AuthenticateUserRequestV1.CredentialsV1.Builder()
                .key(authCode)
                .secret(idToken)
                .build();

        UserService.authenticateUser(AuthenticateUserRequestV1.AuthBackendV1.GOOGLE, credentialsV1, new UserService.AuthenticateUserCallback() {
            @Override
            public void success(AuthenticateUserResponseV1 response) {
                if (response != null) {
                    cacheUserAndToken(response.token, response.user);
                    fetchProfile();
                } else {
                    showProgress(false);
                    Toast.makeText(AuthActivity.this, "Error authenticating user", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Toast.makeText(AuthActivity.this, "Error authenticating user", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cacheUserAndToken(String token, UserV1 userV1) {
        AppPreferences.putAuthToken(token);
        AppPreferences.putLoggedInUser(userV1);
    }

    private void cacheProfile(ProfileV1 profileV1) {
        AppPreferences.putLoggedInUserProfile(profileV1);
        AppPreferences.putOrganizationId(profileV1.organization_id);
    }

    private void fetchProfile() {
        ProfileService.getProfile(new ProfileService.GetProfileCallback() {
            @Override
            public void success(GetProfileResponseV1 response) {
                if (response != null) {
                    cacheProfile(response.profile);
                    dismissActivity();
                } else {
                    showProgress(false);
                    showHomelessView();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Toast.makeText(AuthActivity.this, "Error fetching profile", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showHomelessView() {
        Intent intent = new Intent(AuthActivity.this, HomelessActivity.class);
        startActivityForResult(intent, RC_HOMELESS_VIEW);
    }
}

