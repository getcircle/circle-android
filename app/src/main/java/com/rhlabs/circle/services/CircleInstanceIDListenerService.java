package com.rhlabs.circle.services;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by anju on 6/22/15.
 */
public class CircleInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Refresh tokens");
        Intent intent = new Intent(this, CircleRegistrationIntentService.class);
        startService(intent);
    }
}
