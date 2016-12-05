package com.rhlabs.circle.services;

import com.rhlabs.circle.models.ProfilesAPIResponse;
import com.rhlabs.protobufs.services.group.actions.get_group.GetGroupRequestV1;
import com.rhlabs.protobufs.services.group.actions.get_group.GetGroupResponseV1;
import com.rhlabs.protobufs.services.group.actions.get_groups.GetGroupsRequestV1;
import com.rhlabs.protobufs.services.group.actions.get_members.GetMembersRequestV1;
import com.rhlabs.protobufs.services.group.actions.get_members.GetMembersResponseV1;
import com.rhlabs.protobufs.services.group.actions.join_group.JoinGroupRequestV1;
import com.rhlabs.protobufs.services.group.actions.join_group.JoinGroupResponseV1;
import com.rhlabs.protobufs.services.group.actions.leave_group.LeaveGroupRequestV1;
import com.rhlabs.protobufs.services.group.actions.respond_to_membership_request.RespondToMembershipRequestRequestV1;
import com.rhlabs.protobufs.services.group.containers.GroupProviderV1;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.services.group.containers.MemberV1;
import com.rhlabs.protobufs.services.group.containers.MembershipRequestV1;
import com.rhlabs.protobufs.services.group.containers.RoleV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.services.registry.requests.Ext_requests;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.soa.PaginatorV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by anju on 6/29/15.
 */
public class GroupService {

    public interface JoinGroupCallback {
        void success(MembershipRequestV1 request, Map<String, String> errors);

        void failure(RetrofitError error);
    }

    public interface LeaveGroupCallback {
        void success(Map<String, String> errors);

        void failure(RetrofitError error);
    }

    public interface RespondToMembershipRequestCallback {
        void success(Map<String, String> errors);

        void failure(RetrofitError error);
    }

    public interface GetGroupCallback {
        void success(GroupV1 group, Map<String, String> errors);

        void failure(RetrofitError error);
    }

    public static void getGroup(String groupId, final GetGroupCallback cb) {
        GetGroupRequestV1 request = new GetGroupRequestV1.Builder()
                .group_id(groupId)
                .provider(GroupProviderV1.GOOGLE)
                .build();

        ServiceClient serviceClient = new ServiceClient("group");
        serviceClient.callAction(
                Ext_requests.get_group,
                Ext_responses.get_group,
                request,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        GetGroupResponseV1 response = (GetGroupResponseV1) wrappedResponse.getResponse();
                        GroupV1 group = response != null ? response.group : null;
                        cb.success(group, wrappedResponse.getErrors());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                });
    }

    public static ServiceRequestV1 getGroupsRequest() {
        final GetGroupsRequestV1 request = new GetGroupsRequestV1.Builder()
                .provider(GroupProviderV1.GOOGLE)
                .build();

        return getGroupsRequest(request);
    }

    public static ServiceRequestV1 getGroupsRequest(ProfileV1 profileV1) {
        final GetGroupsRequestV1 request = new GetGroupsRequestV1.Builder()
                .profile_id(profileV1.id)
                .provider(GroupProviderV1.GOOGLE)
                .build();

        return getGroupsRequest(request);
    }

    private static ServiceRequestV1 getGroupsRequest(GetGroupsRequestV1 request) {
        ServiceClient serviceClient = new ServiceClient("group");
        return serviceClient.buildRequest(
                Ext_requests.get_groups,
                Ext_responses.get_groups,
                request,
                (PaginatorV1) null
        );
    }

    public static Observable<ProfilesAPIResponse> getMembers(GroupV1 group, RoleV1 role) {
        GetMembersRequestV1 request = new GetMembersRequestV1.Builder()
                .group_id(group.id)
                .role(role)
                .provider(GroupProviderV1.GOOGLE)
                .build();

        ServiceClient serviceClient = new ServiceClient("group");
        return serviceClient.callAction(
                Ext_requests.get_members,
                Ext_responses.get_members,
                request,
                (PaginatorV1) null
        ).map(
                new Func1<WrappedResponse, ProfilesAPIResponse>() {
                    @Override
                    public ProfilesAPIResponse call(WrappedResponse wrappedResponse) {
                        GetMembersResponseV1 response = (GetMembersResponseV1) wrappedResponse.getResponse();
                        List<ProfileV1> profiles = new ArrayList<>();
                        if (response != null && response.members != null) {
                            for (MemberV1 memberV1 : response.members) {
                                profiles.add(memberV1.profile);
                            }
                        }

                        return new ProfilesAPIResponse(profiles, wrappedResponse.getNextRequest());
                    }
                }
        );
    }

    public static void joinGroup(GroupV1 group, final JoinGroupCallback cb) {
        JoinGroupRequestV1 request = new JoinGroupRequestV1.Builder()
                .group_id(group.id)
                .provider(GroupProviderV1.GOOGLE)
                .build();

        ServiceClient serviceClient = new ServiceClient("group");
        serviceClient.callAction(
                Ext_requests.join_group,
                Ext_responses.join_group,
                request,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        JoinGroupResponseV1 response = (JoinGroupResponseV1) wrappedResponse.getResponse();
                        MembershipRequestV1 request = response != null ? response.request : null;
                        cb.success(request, wrappedResponse.getErrors());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                });
    }

    public static void leaveGroup(GroupV1 group, final LeaveGroupCallback cb) {
        LeaveGroupRequestV1 request = new LeaveGroupRequestV1.Builder()
                .group_id(group.id)
                .provider(GroupProviderV1.GOOGLE)
                .build();

        ServiceClient serviceClient = new ServiceClient("group");
        serviceClient.callAction(
                Ext_requests.leave_group,
                Ext_responses.leave_group,
                request,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        cb.success(wrappedResponse.getErrors());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                });
    }

    public static void respondToMembershipRequest(
            String requestId,
            RespondToMembershipRequestRequestV1.ResponseActionV1 responseAction,
            final RespondToMembershipRequestCallback cb) {

        RespondToMembershipRequestRequestV1 request = new RespondToMembershipRequestRequestV1.Builder()
                .request_id(requestId)
                .action(responseAction)
                .build();

        ServiceClient serviceClient = new ServiceClient("group");
        serviceClient.callAction(
                Ext_requests.respond_to_membership_request,
                Ext_responses.respond_to_membership_request,
                request,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        cb.success(wrappedResponse.getErrors());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                });
    }
}
