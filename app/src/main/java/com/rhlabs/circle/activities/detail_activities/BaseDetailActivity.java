package com.rhlabs.circle.activities.detail_activities;

import android.content.Intent;
import android.os.Bundle;

import com.rhlabs.circle.activities.BaseActivity;
import com.rhlabs.circle.fragments.BaseCardListFragment;

public abstract class BaseDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Attach the fragment
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            final BaseCardListFragment fragmentLayout = getFragmentLayout(intent);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragmentLayout)
                    .commit();
        }
    }

    // Return new instance of a detail fragment
    protected abstract BaseCardListFragment getFragmentLayout(Intent intent);
}
