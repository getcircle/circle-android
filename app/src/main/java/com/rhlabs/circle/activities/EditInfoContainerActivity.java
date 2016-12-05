package com.rhlabs.circle.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.rhlabs.circle.activities.BaseActivity;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.fragments.EditAboutInfoFragment;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.user.containers.UserV1;

/**
 * Created by anju on 7/16/15.
 */
public class EditInfoContainerActivity extends BaseActivity implements EditAboutInfoFragment.OnEditAboutInfoListener {
    private UserV1 mUser;
    private ProfileV1 mProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mUser = AppPreferences.getLoggedInUser();
            mProfile = AppPreferences.getLoggedInUserProfile();
            if (mUser != null && mProfile != null) {
                EditAboutInfoFragment editAboutInfoFragment = EditAboutInfoFragment.newInstance(true, true);
                presentFragment(editAboutInfoFragment);
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
    public void onEditAboutInfoComplete() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onEditAboutInfoCanceled() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
