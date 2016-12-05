package com.rhlabs.circle.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rhlabs.circle.fragments.SearchFragment;

public class SearchActivity extends BaseActivity {

    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setQuery(query);
            Log.d("LOGTAG", query);
        }

        if (savedInstanceState == null) {
            final SearchFragment fragmentLayout = SearchFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragmentLayout)
                    .commit();
        }
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery = query;
    }
}
