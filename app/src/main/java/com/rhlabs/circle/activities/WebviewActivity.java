package com.rhlabs.circle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.rhlabs.circle.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WebviewActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.webView)
    WebView mWebView;

    @InjectView(R.id.pbCircleProgress)
    ProgressBar mProgressBar;
    private String mPageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.inject(this);

        // Setup toolbar
        setSupportActionBar(mToolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Configure related browser settings
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                mToolbar.setTitle(view.getTitle());
            }
        });

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mPageUrl = intent.getStringExtra(INTENT_ARG_WEB_URL);

            if (mPageUrl != null && !mPageUrl.isEmpty()) {
                mProgressBar.setVisibility(View.VISIBLE);
                mWebView.loadUrl(mPageUrl);
            }
        }
    }
}
