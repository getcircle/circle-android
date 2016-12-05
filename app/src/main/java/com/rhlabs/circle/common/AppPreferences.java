package com.rhlabs.circle.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.rhlabs.circle.models.CircleObjectCache;
import com.rhlabs.circle.models.OrgStats;
import com.rhlabs.protobufs.services.organization.containers.OrganizationV1;
import com.rhlabs.protobufs.services.organization.containers.integration.IntegrationTypeV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.user.containers.UserV1;
import com.squareup.wire.Wire;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by anju on 4/28/15.
 */
public class AppPreferences {

    private final static String PREF_FILE = AppPreferences.class.getSimpleName();
    private final static String AUTH_TOKEN = "Token";
    private final static String USER_ID = "UserID";
    private final static String PROFILE_ID = "ProfileID";
    private final static String ORGANIZATION_ID = "OrganizationID";
    private final static String REQUESTED_ACCESS_ID = "RequestedAccessID";

    // Org Stats
    private final static String PROFILES_COUNT = "ProfilesCount";
    private final static String SKILLS_COUNT = "SkillsCount";
    private final static String LOCATIONS_COUNT = "LocationsCount";
    private final static String TEAMS_COUNT = "TeamsCount";

    // Service Integration Status
    private final static String INTEGRATION_STATUS = "Integration_%1$d";

    // Flags
    private final static String SENT_TOKEN_TO_SERVER = "SentTokenToServer";

    // This one is public because there is Google provided code that accepts a
    // key name to store the preference directly
    public final static String REGISTRATION_COMPLETE = "RegistrationComplete";

    private final static String REALM_CACHE = "circlecache.realm";

    private final static Wire wireInstance = new Wire();

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public static AppPreferences sharedAppPreferencesManager;

    private AppPreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    private static AppPreferences getSharedAppPreferencesManager() {
        if (sharedAppPreferencesManager == null) {
            sharedAppPreferencesManager = new AppPreferences(CircleApp.getContext());
        }
        return sharedAppPreferencesManager;
    }

    public static void clear() {
        // Apply is asynchronous and should work..
        // But to be sure, we need to use commit and call it in a background thread
        getSharedAppPreferencesManager().mEditor.clear();
        getSharedAppPreferencesManager().mEditor.apply();

        // Clear all objects from realm
        Realm realm = Realm.getInstance(CircleApp.getContext(), REALM_CACHE);
        realm.beginTransaction();
        RealmResults<CircleObjectCache> allObjects = realm.allObjects(CircleObjectCache.class);
        allObjects.clear();
        realm.commitTransaction();
        realm.close();
    }

    public static void putAuthToken(String token) {
        getSharedAppPreferencesManager().mEditor.putString(AUTH_TOKEN, token);
        getSharedAppPreferencesManager().mEditor.apply();
    }

    public static String getAuthToken() {
        return getSharedAppPreferencesManager().mSharedPreferences.getString(AUTH_TOKEN, null);
    }

    public static void putLoggedInUserId(String userId) {
        getSharedAppPreferencesManager().mEditor.putString(USER_ID, userId);
        getSharedAppPreferencesManager().mEditor.apply();
    }

    public static String getLoggedInUserId() {
        return getSharedAppPreferencesManager().mSharedPreferences.getString(USER_ID, null);
    }

    public static void putLoggedInUserProfileId(String profileId) {
        getSharedAppPreferencesManager().mEditor.putString(PROFILE_ID, profileId);
        getSharedAppPreferencesManager().mEditor.apply();
    }

    public static String getLoggedInUserProfileId() {
        return getSharedAppPreferencesManager().mSharedPreferences.getString(PROFILE_ID, null);
    }

    public static void putLoggedInUserProfile(final ProfileV1 profileV1) {

        // Cache just the ID first in app preferences
        putLoggedInUserProfileId(profileV1.id);

        // Put the entire object in persistent cache
        Realm realm = Realm.getInstance(CircleApp.getContext(), REALM_CACHE);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<CircleObjectCache> query = realm
                        .where(CircleObjectCache.class)
                        .equalTo("id", profileV1.id);

                CircleObjectCache cachedUser = query.findFirst();
                if (cachedUser != null) {
                    cachedUser.setData(profileV1.toByteArray());
                } else {
                    CircleObjectCache circleObjectCache = realm.createObject(CircleObjectCache.class);
                    circleObjectCache.setId(profileV1.id);
                    circleObjectCache.setData(profileV1.toByteArray());
                }
            }
        });

        realm.close();
    }

    public static ProfileV1 getLoggedInUserProfile() {
        ProfileV1 loggedInUserProfile = null;
        String loggedInUserProfileId = getLoggedInUserProfileId();

        if (loggedInUserProfileId != null) {
            Realm realm = Realm.getInstance(CircleApp.getContext(), REALM_CACHE);
            // Build the query looking at all users:
            RealmQuery<CircleObjectCache> query = realm
                    .where(CircleObjectCache.class)
                    .equalTo("id", getLoggedInUserProfileId());

            CircleObjectCache cachedProfile = query.findFirst();
            if (cachedProfile != null) {
                try {
                    loggedInUserProfile = wireInstance.parseFrom(cachedProfile.getData(), ProfileV1.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            realm.close();
        }

        return loggedInUserProfile;
    }

    public static void putLoggedInUserOrganization(final OrganizationV1 organizationV1) {

        // Cache just the ID first in app preferences
        putOrganizationId(organizationV1.id);

        // Put the entire object in persistent cache
        Realm realm = Realm.getInstance(CircleApp.getContext(), REALM_CACHE);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<CircleObjectCache> query = realm
                        .where(CircleObjectCache.class)
                        .equalTo("id", organizationV1.id);

                CircleObjectCache cachedUser = query.findFirst();
                if (cachedUser != null) {
                    cachedUser.setData(organizationV1.toByteArray());
                } else {
                    CircleObjectCache circleObjectCache = realm.createObject(CircleObjectCache.class);
                    circleObjectCache.setId(organizationV1.id);
                    circleObjectCache.setData(organizationV1.toByteArray());
                }
            }
        });

        realm.close();
    }

    public static OrganizationV1 getLoggedInUserOrganization() {
        OrganizationV1 organizationV1 = null;
        String organizationId = getOrganizationId();

        if (organizationId != null) {
            Realm realm = Realm.getInstance(CircleApp.getContext(), REALM_CACHE);
            // Build the query looking at all users:
            RealmQuery<CircleObjectCache> query = realm
                    .where(CircleObjectCache.class)
                    .equalTo("id", organizationId);

            CircleObjectCache cachedOrganization = query.findFirst();
            if (cachedOrganization != null) {
                try {
                    organizationV1 = wireInstance.parseFrom(cachedOrganization.getData(), OrganizationV1.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            realm.close();
        }

        return organizationV1;
    }

    public static void putLoggedInUser(final UserV1 userV1) {

        // Cache just the ID first in app preferences
        putLoggedInUserId(userV1.id);

        // Put the entire object in persistent cache
        Realm realm = Realm.getInstance(CircleApp.getContext(), REALM_CACHE);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<CircleObjectCache> query = realm
                        .where(CircleObjectCache.class)
                        .equalTo("id", userV1.id);

                CircleObjectCache cachedUser = query.findFirst();
                if (cachedUser != null) {
                    cachedUser.setData(userV1.toByteArray());
                } else {
                    CircleObjectCache circleObjectCache = realm.createObject(CircleObjectCache.class);
                    circleObjectCache.setId(userV1.id);
                    circleObjectCache.setData(userV1.toByteArray());
                }
            }
        });

        realm.close();
    }

    public static UserV1 getLoggedInUser() {
        UserV1 loggedInUser = null;
        String loggedInUserId = getLoggedInUserId();

        if (loggedInUserId != null) {
            Realm realm = Realm.getInstance(CircleApp.getContext(), REALM_CACHE);
            // Build the query looking at all users:
            RealmQuery<CircleObjectCache> query = realm
                    .where(CircleObjectCache.class)
                    .equalTo("id", loggedInUserId);

            CircleObjectCache cachedProfile = query.findFirst();
            if (cachedProfile != null) {
                try {
                    loggedInUser = wireInstance.parseFrom(cachedProfile.getData(), UserV1.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            realm.close();
        }

        return loggedInUser;
    }

    public static void putOrganizationId(String organizationId) {
        getSharedAppPreferencesManager().mEditor.putString(ORGANIZATION_ID, organizationId);
        getSharedAppPreferencesManager().mEditor.apply();
    }

    public static String getOrganizationId() {
        return getSharedAppPreferencesManager().mSharedPreferences.getString(ORGANIZATION_ID, null);
    }


    public static void putRequestedAccessUserId(String requestingUserId) {
        getSharedAppPreferencesManager().mEditor.putString(REQUESTED_ACCESS_ID, requestingUserId);
        getSharedAppPreferencesManager().mEditor.apply();
    }

    public static String getRequestedAccessUserId() {
        return getSharedAppPreferencesManager().mSharedPreferences.getString(REQUESTED_ACCESS_ID, null);
    }

    public static void putRegistrationComplete(Boolean registrationComplete) {
        getSharedAppPreferencesManager().mEditor.putBoolean(REGISTRATION_COMPLETE, registrationComplete);
        getSharedAppPreferencesManager().mEditor.apply();
    }

    public static Boolean getRegistrationComplete() {
        return getSharedAppPreferencesManager().mSharedPreferences.getBoolean(REGISTRATION_COMPLETE, false);
    }

    public static void putSentTokenToServer(Boolean sentTokenToServer) {
        getSharedAppPreferencesManager().mEditor.putBoolean(SENT_TOKEN_TO_SERVER, sentTokenToServer);
        getSharedAppPreferencesManager().mEditor.apply();
    }

    public static Boolean getSentTokenToServer() {
        return getSharedAppPreferencesManager().mSharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false);
    }

    public static void putOrgStats(OrgStats orgStats) {
        getSharedAppPreferencesManager().mEditor.putInt(PROFILES_COUNT, orgStats.getProfilesCount());
        getSharedAppPreferencesManager().mEditor.putInt(LOCATIONS_COUNT, orgStats.getLocationsCount());
        getSharedAppPreferencesManager().mEditor.putInt(TEAMS_COUNT, orgStats.getTeamsCount());
        getSharedAppPreferencesManager().mEditor.apply();
    }

    public static OrgStats getOrgStats() {
        int profilesCount = getSharedAppPreferencesManager().mSharedPreferences.getInt(PROFILES_COUNT, 0);
        if (profilesCount == 0) {
            return null;
        }

        return new OrgStats(
                profilesCount,
                getSharedAppPreferencesManager().mSharedPreferences.getInt(LOCATIONS_COUNT, 0),
                getSharedAppPreferencesManager().mSharedPreferences.getInt(TEAMS_COUNT, 0)
        );
    }

    public static Boolean getIntegrationStatus(IntegrationTypeV1 integrationType) {
        String key = String.format(INTEGRATION_STATUS, integrationType.getValue());
        return getSharedAppPreferencesManager().mSharedPreferences.getBoolean(key, false);
    }

    public static void setIntegrationStatus(IntegrationTypeV1 integrationType, boolean status) {
        String key = String.format(INTEGRATION_STATUS, integrationType.getValue());
        getSharedAppPreferencesManager().mEditor.putBoolean(key, status);
        getSharedAppPreferencesManager().mEditor.apply();
    }
}
