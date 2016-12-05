package com.rhlabs.circle.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.services.UserService;
import com.rhlabs.protobufs.services.user.containers.AccessRequestV1;
import com.rhlabs.protobufs.services.user.containers.UserV1;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class HomelessActivity extends AppCompatActivity {

    @InjectView(R.id.btnRequestAccess)
    Button mRequestAccessButton;

    @InjectView(R.id.btnTryAgain)
    Button mTryAgainButton;

    @InjectView(R.id.pbProgressBar)
    ProgressBar mProgressView;

    @InjectView(R.id.tvBetaText)
    TextView mBetaTextView;

    @InjectView(R.id.tvConfirmationText)
    TextView mConfirmationTextView;

    private UserV1 mLoggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeless);
        ButterKnife.inject(this);

        mLoggedInUser = AppPreferences.getLoggedInUser();
        if (mLoggedInUser == null) {
            Toast.makeText(this, "Logged in user not found", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        configureViews();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homeless, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Do not allow going back from this view using the hardware button
    }

    private void configureViews() {
        mConfirmationTextView.setVisibility(View.INVISIBLE);
        mProgressView.setVisibility(View.INVISIBLE);

        String requestedAccessUserId = AppPreferences.getRequestedAccessUserId();
        if (requestedAccessUserId == null) {
            mBetaTextView.setText(R.string.private_beta_info_text);
            mTryAgainButton.setText(R.string.try_again_button_title);
        }
        else if (requestedAccessUserId.equals(mLoggedInUser.id)) {
            mBetaTextView.setText(R.string.waitlist_info_text);
            mRequestAccessButton.setVisibility(View.INVISIBLE);
            mTryAgainButton.setText(R.string.sign_out);
        }
    }

    public void onTryAgainButtonClicked(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void onRequestAccessButtonClicked(View view) {
        mRequestAccessButton.setVisibility(View.INVISIBLE);
        mProgressView.setVisibility(View.VISIBLE);

        UserService.requestAccess(mLoggedInUser, new UserService.RequestAccessCallback() {
            @Override
            public void success(AccessRequestV1 accessRequest, Map<String, String> errors) {
                mProgressView.setVisibility(View.INVISIBLE);
                if (accessRequest != null) {
                    AppPreferences.putRequestedAccessUserId(accessRequest.user_id);
                    mConfirmationTextView.setVisibility(View.VISIBLE);
                }
                else {
                    mRequestAccessButton.setVisibility(View.VISIBLE);
                    Toast.makeText(HomelessActivity.this, "Error requesting access to circle", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressView.setVisibility(View.INVISIBLE);
                Toast.makeText(HomelessActivity.this, "Error requesting access to circle", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
