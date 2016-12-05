package com.rhlabs.circle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rhlabs.circle.BuildConfig;
import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.SettingsListAdapter;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.models.SettingsListItem;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.lvSettings)
    ListView mSettingsListView;

    private SettingsListAdapter mSettingsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);

        // Setup toolbar
        setSupportActionBar(mToolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitle(getString(R.string.nav_item_title_settings));

        // Footer
        addFooterView();

        // Adapter
        ArrayList<SettingsListItem> settingsListItems = getData();
        mSettingsListAdapter = new SettingsListAdapter(this, settingsListItems);
        mSettingsListView.setAdapter(mSettingsListAdapter);

        // Item click listener
        mSettingsListView.setOnItemClickListener(this);
    }

    @NonNull
    private ArrayList<SettingsListItem> getData() {
        ArrayList<SettingsListItem> settingsListItems = new ArrayList<>();

        ProfileV1 loggedInProfile = AppPreferences.getLoggedInUserProfile();

        // Legal
        settingsListItems.add(new SettingsListItem(
                getString(R.string.legal_section_title),
                SettingsListItem.ViewType.SectionHeader
        ));
        settingsListItems.add(new SettingsListItem(
                getString(R.string.attributions_button_title),
                SettingsListItem.DataType.Attributions
        ));
        settingsListItems.add(new SettingsListItem(
                getString(R.string.tos_button_title),
                SettingsListItem.DataType.Terms
        ));
        settingsListItems.add(new SettingsListItem(
                getString(R.string.privacy_button_title),
                SettingsListItem.DataType.Privacy
        ));

        // Account
        settingsListItems.add(new SettingsListItem(
                getString(R.string.account_section_title),
                SettingsListItem.ViewType.SectionHeader
        ));
        settingsListItems.add(new SettingsListItem(
                loggedInProfile.email,
                SettingsListItem.DataType.AccountEmail
        ));
        settingsListItems.add(new SettingsListItem(
                getString(R.string.disconnect_account),
                SettingsListItem.DataType.DisconnectAccount
        ));
        settingsListItems.add(new SettingsListItem(
                getString(R.string.sign_out),
                SettingsListItem.DataType.SignOut
        ));
        return settingsListItems;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        SettingsListItem settingsListItem = mSettingsListAdapter.getItem(position);
        Intent intent;
        switch (settingsListItem.getDataType()) {
            case Attributions:
                loadURL("http://www.circlehq.co/attributions.html");
                break;

            case Privacy:
                loadURL("http://www.circlehq.co/privacy.html");
                break;

            case Terms:
                loadURL("http://www.circlehq.co/terms.html");
                break;

            case SignOut:
                intent = new Intent();
                intent.putExtra(INTENT_ARG_SHOULD_SIGN_OUT, true);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case DisconnectAccount:
                intent = new Intent();
                intent.putExtra(INTENT_ARG_SHOULD_DISCONNECT, true);
                setResult(RESULT_OK, intent);
                finish();
                break;

            default:
                break;
        }
    }

    private void loadURL(String pageUrl) {
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra(INTENT_ARG_WEB_URL, pageUrl);
        startActivity(intent);
    }

    private void addFooterView() {

        TextView versionTextView = new TextView(this);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        versionTextView.setLayoutParams(layoutParams);
        versionTextView.setGravity(Gravity.CENTER);
        versionTextView.setPadding(14, 44, 14, 14);
        versionTextView.setTextColor(getResources().getColor(R.color.app_dark_text_color_secondary_info));
        versionTextView.setText("Version " + BuildConfig.VERSION_NAME);
        mSettingsListView.addFooterView(versionTextView);
    }
}
