package com.rhlabs.circle.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.rhlabs.circle.BuildConfig;
import com.rhlabs.circle.R;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.protobufs.services.user.containers.DeviceV1;

import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;

/**
 * Created by anju on 6/22/15.
 */
public class CircleRegistrationIntentService extends IntentService {
    private static final String TAG = "CircleRIS";

    public CircleRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);
                sendRegistrationToServer(token);
                AppPreferences.putSentTokenToServer(true);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            AppPreferences.putSentTokenToServer(false);
        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(AppPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {

        String loggedInUserID = AppPreferences.getLoggedInUserId();

        if (loggedInUserID == null) {
            // Reset flags if user is not logged in
            AppPreferences.putSentTokenToServer(false);
            return;
        }

        Log.d(TAG, "Recording user's device");
        // Add custom implementation, as needed.
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();
        DeviceV1 userDevice = new DeviceV1.Builder()
                .platform(Build.MANUFACTURER + " " + Build.MODEL)
                .os_version(String.valueOf(Build.VERSION.SDK_INT))
                .app_version(BuildConfig.VERSION_NAME)
                .device_uuid(uuid)
                .provider(DeviceV1.ProviderV1.GOOGLE)
                .user_id(loggedInUserID)
                .notification_token(token)
                .language_preference(Locale.getDefault().getDisplayLanguage())
                .build();

        UserService.recordDevice(userDevice, new UserService.RecordDeviceCallback() {
            @Override
            public void success(DeviceV1 device, Map<String, String> errors) {
                if (device == null) {
                    // Reset flags if there was an error in registering the notification token
                    AppPreferences.putSentTokenToServer(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // Reset flags if there was an error in registering the notification token
                AppPreferences.putSentTokenToServer(false);
            }
        });

    }
}
