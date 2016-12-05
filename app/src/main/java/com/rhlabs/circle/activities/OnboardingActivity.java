package com.rhlabs.circle.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.fragments.EditAboutInfoFragment;
import com.rhlabs.circle.fragments.EditProfileImageFragment;
import com.rhlabs.circle.fragments.VerifyPhoneFragment;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.user.containers.UserV1;

/**
 * Created by anju on 6/16/15.
 */
public class OnboardingActivity extends BaseActivity implements
        VerifyPhoneFragment.OnVerifyPhoneListener,
        EditProfileImageFragment.OnEditProfileImageListener,
        EditAboutInfoFragment.OnEditAboutInfoListener {

    private UserV1 mUser;
    private ProfileV1 mProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mUser = AppPreferences.getLoggedInUser();
            mProfile = AppPreferences.getLoggedInUserProfile();
            if (mUser != null && mProfile != null) {
                if (!mUser.phone_number_verified) {
                    presentFragment(VerifyPhoneFragment.newInstance());
                } else if (!mProfile.verified) {
                    presentFragment(EditProfileImageFragment.newInstance());
                }
            } else {
                Toast.makeText(this, "Logged in user profile not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void presentFragment(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commit();
    }

    @Override
    public void onVeriyPhoneComplete() {
        presentFragment(EditProfileImageFragment.newInstance());
    }

    @Override
    public void onEditProfileImageCompleted() {
        presentFragment(EditAboutInfoFragment.newInstance(true, false));
    }

    @Override
    public void onEditAboutInfoComplete() {
        finish();
    }

    @Override
    public void onEditAboutInfoCanceled() {

    }

    @Override
    public void onBackPressed() {
        // Do not allow exiting from onboarding
    }
}
