package com.rhlabs.circle.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.services.UserService;
import com.rhlabs.circle.utils.KeyboardUtils;
import com.rhlabs.protobufs.services.user.actions.send_verification_code.SendVerificationCodeResponseV1;
import com.rhlabs.protobufs.services.user.actions.verify_verification_code.VerifyVerificationCodeResponseV1;
import com.rhlabs.protobufs.services.user.containers.UserV1;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyPhoneFragment extends Fragment {

    @InjectView(R.id.tvInstructions)
    TextView mInstructionsTextView;

    @InjectView(R.id.tilPhoneNumber)
    TextInputLayout mPhoneNumberContainerLayout;

    @InjectView(R.id.etPhoneNumber)
    EditText mPhoneNumberField;

    @InjectView(R.id.pbProgressBar)
    ProgressBar mProgressBar;

    @InjectView(R.id.btnResendCode)
    Button mResendCodeButton;

    @InjectView(R.id.btnSendVerifyCode)
    Button mSendVerifyCodeButton;

    @InjectView(R.id.tilVerifyCode)
    TextInputLayout mVerificationCodeContainerLayout;

    @InjectView(R.id.etVerifyCode)
    EditText mVerificationCodeField;

    private VerifyPhoneState mVerifyPhoneState = VerifyPhoneState.INIT;
    private String mErrorString = "";
    private OnVerifyPhoneListener mListener;

    enum VerifyPhoneState {
        INIT,
        PHONE_NUMBER_ERROR,
        VALID_PHONE_NUMBER_ENTERED,
        VERIFICATION_CODE_REQUESTED,
        VERIFICATION_CODE_SENT,
        RESEND_CODE_REQUESTED,
        VALID_VERIFICATION_CODE_ENTERED,
        VERIFY_VERIFICATION_CODE_REQUESTED,
        CODE_VERIFICATION_COMPLETE
    }

    public static VerifyPhoneFragment newInstance() {
        VerifyPhoneFragment fragment = new VerifyPhoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public VerifyPhoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_verify_phone, container, false);
        ButterKnife.inject(this, rootView);

        addOnClickListeners();
        addTextChangedListener();
        addOnFocusListeners();
        updateForState(VerifyPhoneState.INIT);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnVerifyPhoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVerifyPhoneListener");
        }
    }

    private void updateForState(VerifyPhoneState state) {
        Log.d("TAG", state.toString());
        mVerifyPhoneState = state;
        switch (state) {
            case INIT:
                mPhoneNumberContainerLayout.setErrorEnabled(false);
                mPhoneNumberField.requestFocus();
                mProgressBar.setVisibility(View.INVISIBLE);
                mResendCodeButton.setVisibility(View.INVISIBLE);
                mSendVerifyCodeButton.setEnabled(false);
                mSendVerifyCodeButton.setText(getResources().getString(R.string.verify_phone_send_code_button_title));
                mVerificationCodeField.setVisibility(View.GONE);
                break;

            case VALID_PHONE_NUMBER_ENTERED:
                mPhoneNumberContainerLayout.setErrorEnabled(false);
                mSendVerifyCodeButton.setText(getResources().getString(R.string.verify_phone_send_code_button_title));
                mSendVerifyCodeButton.setEnabled(true);
                mVerificationCodeField.setVisibility(View.GONE);
                break;

            case VERIFICATION_CODE_REQUESTED:
                mErrorString = "";
                mVerificationCodeField.setText("");
                mPhoneNumberContainerLayout.setErrorEnabled(false);
                mProgressBar.setVisibility(View.VISIBLE);
                mSendVerifyCodeButton.setEnabled(false);
                break;

            case VERIFICATION_CODE_SENT:
                mProgressBar.setVisibility(View.INVISIBLE);
                mResendCodeButton.setVisibility(View.VISIBLE);
                mSendVerifyCodeButton.setText(getResources().getString(R.string.verify_phone_verify_code_button_title));
                mVerificationCodeField.setVisibility(View.VISIBLE);
                mVerificationCodeField.requestFocus();
                break;

            case PHONE_NUMBER_ERROR:
                mPhoneNumberContainerLayout.setErrorEnabled(true);
                mPhoneNumberContainerLayout.setError(mErrorString);
                mProgressBar.setVisibility(View.INVISIBLE);
                mSendVerifyCodeButton.setEnabled(true);
                break;

            case RESEND_CODE_REQUESTED:
                mProgressBar.setVisibility(View.VISIBLE);
                break;

            case VALID_VERIFICATION_CODE_ENTERED:
                mVerificationCodeContainerLayout.setErrorEnabled(false);
                mSendVerifyCodeButton.setEnabled(true);
                break;

            case VERIFY_VERIFICATION_CODE_REQUESTED:
                mProgressBar.setVisibility(View.VISIBLE);
                mResendCodeButton.setVisibility(View.INVISIBLE);
                mSendVerifyCodeButton.setEnabled(false);
                break;

            case CODE_VERIFICATION_COMPLETE:
                mProgressBar.setVisibility(View.INVISIBLE);
                mSendVerifyCodeButton.setEnabled(true);
                break;

            default:
                break;
        }
    }

    public interface OnVerifyPhoneListener {
        void onVeriyPhoneComplete();
    }

    private class PhoneNumberWatcher extends PhoneNumberFormattingTextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);

            String phoneNumber = s.toString();
            String normalizedPhoneNumber = phoneNumber.replaceAll("\\D+", "");
            if (normalizedPhoneNumber != null && normalizedPhoneNumber.length() > 9) {
                updateForState(VerifyPhoneState.VALID_PHONE_NUMBER_ENTERED);
            } else {
                updateForState(VerifyPhoneState.INIT);
            }
        }
    }

    private void addTextChangedListener() {
        mPhoneNumberField.addTextChangedListener(new PhoneNumberWatcher());
        mVerificationCodeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Todo: validate code and enable button if a valid value has been entered.
                String verificationCode = charSequence.toString();
                if (verificationCode.length() > 0) {
                    try {
                        final int numericCode = Integer.parseInt(verificationCode);
                    } catch (NumberFormatException e) {
                        showToast("Verification code has to be numbers only");
                        return;
                    }

                    if (verificationCode.length() == 6) {
                        updateForState(VerifyPhoneState.VALID_VERIFICATION_CODE_ENTERED);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addOnFocusListeners() {
        mPhoneNumberField.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.showKeyboard(getActivity(), mPhoneNumberField);
            }
        }, 50);

        mPhoneNumberField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    KeyboardUtils.showKeyboard(getActivity(), mPhoneNumberField);
                }
            }
        });

        mVerificationCodeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    KeyboardUtils.showKeyboard(getActivity(), mVerificationCodeField);
                }
            }
        });
    }

    private void addOnClickListeners() {
        mSendVerifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserV1 userV1 = AppPreferences.getLoggedInUser();
                if (userV1 == null) {
                    showToast("Could not find logged in user");
                    return;
                }


                if (mVerifyPhoneState == VerifyPhoneState.VERIFICATION_CODE_SENT || mVerifyPhoneState == VerifyPhoneState.VALID_VERIFICATION_CODE_ENTERED) {
                    // Verify number
                    updateForState(VerifyPhoneState.VERIFY_VERIFICATION_CODE_REQUESTED);
                    verifyVerificationCode(userV1);
                } else {
                    // Send verification code
                    updateForState(VerifyPhoneState.VERIFICATION_CODE_REQUESTED);
                    updateUserPhoneNumber(userV1);
                }
            }
        });

        mResendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserV1 userV1 = AppPreferences.getLoggedInUser();
                if (userV1 == null) {
                    showToast("Could not find logged in user");
                    return;
                }

                // Send verification code
                updateForState(VerifyPhoneState.VERIFICATION_CODE_REQUESTED);
                updateUserPhoneNumber(userV1);
            }
        });
    }

    private void updateUserPhoneNumber(UserV1 userV1) {
        UserV1 updatedUser = new UserV1.Builder(userV1)
                .phone_number(mPhoneNumberField.getText().toString())
                .build();
        UserService.updateUser(updatedUser, new UserService.UpdateUserCallback() {
            @Override
            public void success(UserV1 user, Map<String, String> errors) {
                if (errors != null && errors.size() > 0) {
                    for (String error : errors.values()) {
                        switch (error) {
                            case "DUPLICATE":
                                mErrorString = "This number is registered by another user";
                                break;

                            case "INVALID":
                                mErrorString = "Invalid phone number";
                                break;
                        }
                    }

                    updateForState(VerifyPhoneState.PHONE_NUMBER_ERROR);
                } else if (user != null) {
                    // Put updated user in the cache
                    AppPreferences.putLoggedInUser(user);
                    sendVerificationCode(user);
                } else {
                    showToast("Error updating user");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showToast("Error updating user");
            }
        });
    }

    private void sendVerificationCode(UserV1 user) {
        UserService.sendVerificationCode(user, new UserService.SendVerificationCodeCallback() {
            @Override
            public void success(SendVerificationCodeResponseV1 response) {
                if (response == null) {
                    showToast("Error sending verification code");
                } else {
                    updateForState(VerifyPhoneState.VERIFICATION_CODE_SENT);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showToast("Error sending verification code");
            }
        });
    }

    private void verifyVerificationCode(final UserV1 userV1) {
        final String verificationCode = mVerificationCodeField.getText().toString();
        if (verificationCode.isEmpty()) {
            showToast("Verification code cannot be empty");
            return;
        }

        if (verificationCode.length() != 6) {
            showToast("Verification code can be six digits only");
            return;
        }

        UserService.verifyVerificationCode(userV1, verificationCode, new UserService.VerifyCodeCallback() {
            @Override
            public void success(VerifyVerificationCodeResponseV1 response) {
                updateForState(VerifyPhoneState.CODE_VERIFICATION_COMPLETE);
                if (response != null && response.verified) {
                    UserV1 updatedUser = new UserV1.Builder(userV1)
                            .phone_number_verified(true)
                            .build();

                    // Put updated user in the cache
                    AppPreferences.putLoggedInUser(updatedUser);
                    KeyboardUtils.hideKeyboard(getActivity(), mVerificationCodeField);
                    if (mListener != null) {
                        mListener.onVeriyPhoneComplete();
                    }
                } else {
                    mVerificationCodeContainerLayout.setError("Invalid code");
                    mVerificationCodeContainerLayout.setErrorEnabled(true);
                    showToast("Incorrect code entered");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showToast("Error verifying code");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
