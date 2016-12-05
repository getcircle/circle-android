package com.rhlabs.circle.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.activities.OnboardingActivity;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.services.ProfileService;
import com.rhlabs.circle.utils.KeyboardUtils;
import com.rhlabs.protobufs.services.profile.containers.ProfileStatusV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class EditAboutInfoFragment extends Fragment {

    public static final String SHOW_DONE_BUTTON_1 = "show_done_button";
    public static final String SHOW_CANCEL_BUTTON_2 = "show_cancel_button";

    @InjectView(R.id.etBio)
    EditText mBioField;

    @InjectView(R.id.tilBio)
    TextInputLayout mBioTextInputLayout;

    @InjectView(R.id.etNickname)
    EditText mNicknameField;

    @InjectView(R.id.btnNext)
    Button mNextButton;

    @InjectView(R.id.btnCancel)
    Button mCancelButton;

    @InjectView(R.id.tvInstruction)
    TextView mInstructionTextView;

    private OnEditAboutInfoListener mListener;
    private ProfileV1 mProfile;

    private boolean mShowDoneButton;
    private boolean mShowCancelButton;

    public static EditAboutInfoFragment newInstance(Boolean showDoneButton, Boolean showCancelButton) {
        EditAboutInfoFragment fragment = new EditAboutInfoFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_DONE_BUTTON_1, showDoneButton);
        args.putBoolean(SHOW_CANCEL_BUTTON_2, showCancelButton);
        fragment.setArguments(args);
        return fragment;
    }

    public EditAboutInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mShowDoneButton = getArguments().getBoolean(SHOW_DONE_BUTTON_1, true);
            mShowCancelButton = getArguments().getBoolean(SHOW_CANCEL_BUTTON_2, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_about_info, container, false);
        ButterKnife.inject(this, rootView);
        initData();
        configureNextButton();
        configureCancelButton();
        configureTheme(rootView);
        addOnFocusListeners();
        return rootView;
    }

    private void configureCancelButton() {
        if (mShowCancelButton) {
            mCancelButton.setVisibility(View.VISIBLE);
        }
        else {
            mCancelButton.setVisibility(View.INVISIBLE);
        }

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onEditAboutInfoCanceled();
                }
            }
        });
    }

    private void configureNextButton() {
        if (mShowDoneButton) {
            mNextButton.setText(R.string.generic_done_button_title);
        }
        else {
            mNextButton.setText(R.string.generic_next_button_title);
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButtonClicked(view);
            }
        });
    }

    private void configureTheme(View rootView) {

        if (getActivity() instanceof OnboardingActivity) {
            rootView.setBackgroundColor(getResources().getColor(R.color.app_ui_background_color));
            mBioField.setTextColor(getResources().getColor(R.color.app_light_text_color));
            mNicknameField.setTextColor(getResources().getColor(R.color.app_light_text_color));
            mInstructionTextView.setTextColor(getResources().getColor(R.color.app_light_text_color));
        }
        else {
            rootView.setBackgroundColor(getResources().getColor(R.color.app_view_background_color));
            mBioField.setTextColor(getResources().getColor(R.color.app_dark_text_color));
            mNicknameField.setTextColor(getResources().getColor(R.color.app_dark_text_color));
            mInstructionTextView.setTextColor(getResources().getColor(R.color.app_dark_text_color));
        }
    }

    private void addOnFocusListeners() {
        mBioField.requestFocus();

        mBioField.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.showKeyboard(getActivity(), mBioField);
            }
        }, 50);

        mBioField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    KeyboardUtils.showKeyboard(getActivity(), mBioField);
                }
            }
        });

        mNicknameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    KeyboardUtils.showKeyboard(getActivity(), mNicknameField);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEditAboutInfoListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEditAboutInfoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEditAboutInfoListener {
        void onEditAboutInfoComplete();
        void onEditAboutInfoCanceled();
    }

    private void initData() {

        ProfileV1 loggedInUserProfile = AppPreferences.getLoggedInUserProfile();
        if (loggedInUserProfile == null) {
            Toast.makeText(getActivity(), "Logged in user profile not found", Toast.LENGTH_SHORT).show();
            return;
        }

        mProfile = loggedInUserProfile;
        mBioField.setSingleLine(false);
        if (mProfile.status != null && !mProfile.status.value.isEmpty()) {
            mBioField.setText(mProfile.status.value);
        }

        if (mProfile.nickname != null && !mProfile.nickname.isEmpty()) {
            mNicknameField.setText(mProfile.nickname);
        }
    }

    public void nextButtonClicked(View view) {
        String bioText = mBioField.getText().toString().trim();

        if (bioText.equals("") || bioText.isEmpty()) {
            mBioTextInputLayout.setErrorEnabled(true);
            mBioTextInputLayout.setError("About text cannot be empty");
            return;
        }

        mBioTextInputLayout.setErrorEnabled(false);
        ProfileStatusV1 statusV1 = new ProfileStatusV1.Builder()
                .value(bioText)
                .build();

        ProfileV1 updatedProfile = new ProfileV1.Builder(mProfile)
                .status(statusV1)
                .nickname(mNicknameField.getText().toString())
                .verified(true)
                .build();

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "", true, false);
        ProfileService.updateProfile(updatedProfile, new ProfileService.UpdateProfileCallback() {
            @Override
            public void success(ProfileV1 updatedProfile, Map<String, String> errors) {
                progressDialog.dismiss();

                if (updatedProfile == null) {
                    Toast.makeText(getActivity(), "Error updating profile", Toast.LENGTH_SHORT).show();
                } else if (mListener != null) {
                    AppPreferences.putLoggedInUserProfile(updatedProfile);
                    mListener.onEditAboutInfoComplete();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error updating profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
