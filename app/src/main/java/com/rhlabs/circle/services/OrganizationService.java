package com.rhlabs.circle.services;

import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.circle.models.LocationsAPIResponse;
import com.rhlabs.circle.models.TeamReportingData;
import com.rhlabs.circle.models.TeamsAPIResponse;
import com.rhlabs.protobufs.services.organization.actions.get_addresses.GetAddressesResponseV1;
import com.rhlabs.protobufs.services.organization.actions.get_integration.GetIntegrationRequestV1;
import com.rhlabs.protobufs.services.organization.actions.get_integration.GetIntegrationResponseV1;
import com.rhlabs.protobufs.services.organization.actions.get_location.GetLocationRequestV1;
import com.rhlabs.protobufs.services.organization.actions.get_location.GetLocationResponseV1;
import com.rhlabs.protobufs.services.organization.actions.get_locations.GetLocationsRequestV1;
import com.rhlabs.protobufs.services.organization.actions.get_locations.GetLocationsResponseV1;
import com.rhlabs.protobufs.services.organization.actions.get_organization.GetOrganizationRequestV1;
import com.rhlabs.protobufs.services.organization.actions.get_organization.GetOrganizationResponseV1;
import com.rhlabs.protobufs.services.organization.actions.get_team.GetTeamRequestV1;
import com.rhlabs.protobufs.services.organization.actions.get_team.GetTeamResponseV1;
import com.rhlabs.protobufs.services.organization.actions.get_team_reporting_details.GetTeamReportingDetailsRequestV1;
import com.rhlabs.protobufs.services.organization.actions.get_team_reporting_details.GetTeamReportingDetailsResponseV1;
import com.rhlabs.protobufs.services.organization.actions.get_teams.GetTeamsRequestV1;
import com.rhlabs.protobufs.services.organization.actions.get_teams.GetTeamsResponseV1;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.organization.containers.integration.IntegrationTypeV1;
import com.rhlabs.protobufs.services.registry.requests.Ext_requests;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.soa.PaginatorV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import retrofit.RetrofitError;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by anju on 4/30/15.
 */
public class OrganizationService {

    public interface GetAddressesCallback {
        void success(GetAddressesResponseV1 response);

        void failure(RetrofitError error);
    }

    public interface GetOrganizationCallback {
        void success(GetOrganizationResponseV1 response);

        void failure(RetrofitError error);
    }

    public interface GetIntegrationStatusCallback {
        void success(Boolean status);

        void failure(RetrofitError error);
    }

    public interface GetLocationCallback {
        void success(LocationV1 locationV1);

        void failure(RetrofitError error);
    }

    public static void getOrganization(final GetOrganizationCallback cb) {
        GetOrganizationRequestV1 getOrganizationRequestV1 = new GetOrganizationRequestV1.Builder()
                .build();

        ServiceClient serviceClient = new ServiceClient("organization");
        serviceClient.callAction(
                Ext_requests.get_organization,
                Ext_responses.get_organization,
                getOrganizationRequestV1,
                null,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        cb.success((GetOrganizationResponseV1) wrappedResponse.getResponse());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                });
    }

    public static Observable<LocationV1> getLocation(LocationV1 locationV1) {
        GetLocationRequestV1 request = new GetLocationRequestV1.Builder()
                .location_id(locationV1.id)
                .build();

        ServiceClient serviceClient = new ServiceClient("organization");
        return serviceClient.callAction(
                Ext_requests.get_location,
                Ext_responses.get_location,
                request, (PaginatorV1) null
        ).map(new Func1<WrappedResponse, LocationV1>() {
            @Override
            public LocationV1 call(WrappedResponse wrappedResponse) {
                GetLocationResponseV1 response = (GetLocationResponseV1) wrappedResponse.getResponse();
                return response.location;
            }
        });
    }

    public static Observable<TeamV1> getTeam(String teamId) {

        final GetTeamRequestV1 getTeamRequestV1 = new GetTeamRequestV1.Builder()
                .team_id(teamId)
                .build();

        ServiceClient serviceClient = new ServiceClient("organization");
        return serviceClient.callAction(Ext_requests.get_team, Ext_responses.get_team, getTeamRequestV1, (PaginatorV1) null).map(new Func1<WrappedResponse, TeamV1>() {
            @Override
            public TeamV1 call(WrappedResponse wrappedResponse) {
                GetTeamResponseV1 getTeamResponseV1 = (GetTeamResponseV1) wrappedResponse.getResponse();
                return getTeamResponseV1 != null ? getTeamResponseV1.team : null;
            }
        });
    }

    public static Observable<TeamReportingData> getTeamReportingDetails(String teamId) {
        final GetTeamReportingDetailsRequestV1 getTeamReportingDetailsRequestV1 = new GetTeamReportingDetailsRequestV1.Builder()
                .team_id(teamId)
                .build();

        ServiceClient serviceClient = new ServiceClient("organization");
        return serviceClient.callAction(
                Ext_requests.get_team_reporting_details,
                Ext_responses.get_team_reporting_details,
                getTeamReportingDetailsRequestV1,
                (PaginatorV1) null
            ).map(
                new Func1<WrappedResponse, TeamReportingData>() {
                    @Override
                    public TeamReportingData call(WrappedResponse wrappedResponse) {
                        GetTeamReportingDetailsResponseV1 getTeamReportingDetailsResponseV1 = (GetTeamReportingDetailsResponseV1) wrappedResponse.getResponse();
                        if (getTeamReportingDetailsResponseV1 == null) {
                            return new TeamReportingData(null, null, null);
                        }

                        return new TeamReportingData(
                                getTeamReportingDetailsResponseV1.members,
                                getTeamReportingDetailsResponseV1.manager,
                                getTeamReportingDetailsResponseV1.child_teams
                        );
                    }
                }
        );
    }

    public static ServiceRequestV1 getTeamsServiceRequest() {
        final GetTeamsRequestV1 request = new GetTeamsRequestV1.Builder()
                .build();

        ServiceClient serviceClient = new ServiceClient("organization");
        return serviceClient.buildRequest(
                Ext_requests.get_teams,
                Ext_responses.get_teams,
                request,
                (PaginatorV1) null
        );
    }

    public static Observable<TeamsAPIResponse> getTeams() {

        final GetTeamsRequestV1 getTeamsRequestV1 = new GetTeamsRequestV1.Builder()
                .build();

        return getTeams(getTeamsRequestV1);
    }

    private static Observable<TeamsAPIResponse> getTeams(final GetTeamsRequestV1 getTeamsRequestV1) {
        ServiceClient serviceClient = new ServiceClient("organization");
        return serviceClient.callAction(
                Ext_requests.get_teams,
                Ext_responses.get_teams,
                getTeamsRequestV1,
                (PaginatorV1) null).map(
                new Func1<WrappedResponse, TeamsAPIResponse>() {
                    @Override
                    public TeamsAPIResponse call(WrappedResponse wrappedResponse) {
                        GetTeamsResponseV1 getTeamsResponseV1 = (GetTeamsResponseV1) wrappedResponse.getResponse();
                        TeamsAPIResponse teamsAPIResponse = new TeamsAPIResponse(getTeamsResponseV1.teams, wrappedResponse.getNextRequest());
                        return teamsAPIResponse;
                    }
                }
        );
    }

    public static ServiceRequestV1 getLocationsServiceRequest() {
        final GetLocationsRequestV1 request = new GetLocationsRequestV1.Builder()
                .build();

        ServiceClient serviceClient = new ServiceClient("organization");
        return serviceClient.buildRequest(
                Ext_requests.get_locations,
                Ext_responses.get_locations,
                request,
                (PaginatorV1) null
        );
    }

    public static Observable<LocationsAPIResponse> getLocations() {
        final GetLocationsRequestV1 request = new GetLocationsRequestV1.Builder()
                .build();

        ServiceClient serviceClient = new ServiceClient("organization");
        return serviceClient.callAction(
                Ext_requests.get_locations,
                Ext_responses.get_locations,
                request,
                (PaginatorV1) null).map(
                new Func1<WrappedResponse, LocationsAPIResponse>() {
                    @Override
                    public LocationsAPIResponse call(WrappedResponse wrappedResponse) {
                        GetLocationsResponseV1 response = (GetLocationsResponseV1) wrappedResponse.getResponse();
                        LocationsAPIResponse locationsAPIResponse = new LocationsAPIResponse(response.locations, wrappedResponse.getNextRequest());
                        return locationsAPIResponse;
                    }
                }
        );
    }

    public static void getIntegrationStatus(final IntegrationTypeV1 integrationType, boolean forceRefresh, final GetIntegrationStatusCallback cb) {

        if (!forceRefresh) {
            Boolean status = AppPreferences.getIntegrationStatus(integrationType);
            if (status != null) {
                cb.success(status);
                return;
            }
        }

        GetIntegrationRequestV1 request = new GetIntegrationRequestV1.Builder()
                .integration_type(integrationType)
                .build();

        ServiceClient serviceClient = new ServiceClient("organization");
        serviceClient.callAction(Ext_requests.get_integration, Ext_responses.get_integration, request, new Callback<WrappedResponse>() {
            @Override
            public void success(WrappedResponse wrappedResponse) {
                GetIntegrationResponseV1 response = (GetIntegrationResponseV1) wrappedResponse.getResponse();
                boolean status = false;
                if (response != null && response.integration != null) {
                    status = true;
                }

                AppPreferences.setIntegrationStatus(integrationType, status);
                cb.success(status);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });

    }

}
