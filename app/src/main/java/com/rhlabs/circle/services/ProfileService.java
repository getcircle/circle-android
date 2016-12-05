package com.rhlabs.circle.services;

import com.rhlabs.circle.models.ProfilesAPIResponse;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.organization.containers.OrganizationV1;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.actions.get_extended_profile.GetExtendedProfileRequestV1;
import com.rhlabs.protobufs.services.profile.actions.get_extended_profile.GetExtendedProfileResponseV1;
import com.rhlabs.protobufs.services.profile.actions.get_profile.GetProfileRequestV1;
import com.rhlabs.protobufs.services.profile.actions.get_profile.GetProfileResponseV1;
import com.rhlabs.protobufs.services.profile.actions.get_profiles.GetProfilesRequestV1;
import com.rhlabs.protobufs.services.profile.actions.get_profiles.GetProfilesResponseV1;
import com.rhlabs.protobufs.services.profile.actions.update_profile.UpdateProfileRequestV1;
import com.rhlabs.protobufs.services.profile.actions.update_profile.UpdateProfileResponseV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.registry.requests.Ext_requests;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.soa.PaginatorV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by anju on 4/28/15.
 */
public class ProfileService {
    public static final String LOG_TAG = "ProfileService";

    public interface GetProfileCallback {
        void success(GetProfileResponseV1 response);

        void failure(RetrofitError error);
    }

    public interface GetTagProfilesCallback {
        void success(List<ProfileV1> profiles);

        void failure(RetrofitError error);
    }

    public interface GetExtendedProfileCallback {
        void success(
                ProfileV1 profile,
                ProfileV1 manager,
                TeamV1 team,
                List<ProfileV1> peers,
                List<ProfileV1> direct_reports,
                List<LocationV1> locations
        );

        void failure(RetrofitError error);
    }

    public interface UpdateProfileCallback {
        void success(ProfileV1 updatedProfile, Map<String, String> errors);
        void failure(RetrofitError error);
    }

    public static void getProfile(final GetProfileCallback cb) {
        GetProfileRequestV1 getProfileRequestV1 = new GetProfileRequestV1.Builder()
                .build();

        ServiceClient serviceClient = new ServiceClient("profile");
        serviceClient.callAction(
                Ext_requests.get_profile,
                Ext_responses.get_profile,
                getProfileRequestV1,
                null,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        cb.success((GetProfileResponseV1) wrappedResponse.getResponse());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                }
        );
    }

    public static void getExtendedProfile(ProfileV1 profileV1, final GetExtendedProfileCallback cb) {
        final GetExtendedProfileRequestV1 getExtendedProfileRequestV1 = new GetExtendedProfileRequestV1.Builder()
                .profile_id(profileV1.id)
                .build();

        ServiceClient serviceClient = new ServiceClient("profile");
        serviceClient.callAction(Ext_requests.get_extended_profile, Ext_responses.get_extended_profile, getExtendedProfileRequestV1, new Callback<WrappedResponse>() {
            @Override
            public void success(WrappedResponse wrappedResponse) {
                GetExtendedProfileResponseV1 getExtendedProfileResponseV1 = (GetExtendedProfileResponseV1) wrappedResponse.getResponse();
                cb.success(
                        getExtendedProfileResponseV1.profile,
                        getExtendedProfileResponseV1.manager,
                        getExtendedProfileResponseV1.team,
                        getExtendedProfileResponseV1.peers,
                        getExtendedProfileResponseV1.direct_reports,
                        getExtendedProfileResponseV1.locations
                );
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

    public static ServiceRequestV1 getProfilesServiceRequest(OrganizationV1 organizationV1) {
        final GetProfilesRequestV1 request = new GetProfilesRequestV1.Builder()
                .build();

        ServiceClient serviceClient = new ServiceClient("profile");
        return serviceClient.buildRequest(
                Ext_requests.get_profiles,
                Ext_responses.get_profiles,
                request,
                (PaginatorV1) null
        );
    }

    public static Observable<ProfilesAPIResponse> getProfiles(OrganizationV1 organizationV1) {
        final GetProfilesRequestV1 request = new GetProfilesRequestV1.Builder()
                .build();

        return getProfiles(request);
    }

    public static Observable<ProfilesAPIResponse> getProfiles(TeamV1 teamV1) {
        final GetProfilesRequestV1 request = new GetProfilesRequestV1.Builder()
                .team_id(teamV1.id)
                .build();

        return getProfiles(request);
    }

    public static Observable<ProfilesAPIResponse> getProfiles(LocationV1 locationV1) {
        final GetProfilesRequestV1 request = new GetProfilesRequestV1.Builder()
                .location_id(locationV1.id)
                .build();

        return getProfiles(request);
    }

    private static Observable<ProfilesAPIResponse> getProfiles(GetProfilesRequestV1 request) {
        ServiceClient serviceClient = new ServiceClient("profile");
        return serviceClient.callAction(
                Ext_requests.get_profiles,
                Ext_responses.get_profiles,
                request,
                (PaginatorV1) null
        ).map(
                new Func1<WrappedResponse, ProfilesAPIResponse>() {
                    @Override
                    public ProfilesAPIResponse call(WrappedResponse wrappedResponse) {
                        GetProfilesResponseV1 getProfilesResponseV1 = (GetProfilesResponseV1) wrappedResponse.getResponse();
                        ProfilesAPIResponse profilesAPIResponse = new ProfilesAPIResponse(getProfilesResponseV1.profiles, wrappedResponse.getNextRequest());
                        return profilesAPIResponse;
                    }
                }
        );
    }

    public static void updateProfile(ProfileV1 profile, final UpdateProfileCallback cb) {
        UpdateProfileRequestV1 request = new UpdateProfileRequestV1.Builder()
                .profile(profile)
                .build();

        ServiceClient serviceClient = new ServiceClient("profile");
        serviceClient.callAction(Ext_requests.update_profile, Ext_responses.update_profile, request, new Callback<WrappedResponse>() {
            @Override
            public void success(WrappedResponse wrappedResponse) {
                UpdateProfileResponseV1 response = (UpdateProfileResponseV1) wrappedResponse.getResponse();
                ProfileV1 updatedProfile = response != null ? response.profile : null;
                cb.success(updatedProfile, wrappedResponse.getErrors());
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }
}
