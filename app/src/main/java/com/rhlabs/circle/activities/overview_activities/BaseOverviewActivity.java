package com.rhlabs.circle.activities.overview_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.rhlabs.circle.R;
import com.rhlabs.circle.activities.BaseActivity;
import com.rhlabs.circle.utils.IntentDataHolder;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 6/11/15.
 */
public abstract class BaseOverviewActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_base);
        ButterKnife.inject(this);

        // Attach the fragment
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            final Fragment fragmentLayout = getFragmentLayout(intent);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragmentLayout)
                    .commit();

            IntentDataHolder.getInstance().clearData();
        }

        setToolbarAndTitle(mToolbar, intent.getStringExtra(INTENT_ARG_VIEW_TITLE));
    }

    // Return new instance of an overview fragment
    protected abstract Fragment getFragmentLayout(Intent intent);
}
