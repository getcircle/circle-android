package com.rhlabs.circle.services;

import android.util.Log;

import com.rhlabs.protobufs.services.registry.requests.Ext_requests;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.services.user.actions.authenticate_user.AuthenticateUserRequestV1;
import com.rhlabs.protobufs.services.user.actions.authenticate_user.AuthenticateUserResponseV1;
import com.rhlabs.protobufs.services.user.actions.record_device.RecordDeviceRequestV1;
import com.rhlabs.protobufs.services.user.actions.record_device.RecordDeviceResponseV1;
import com.rhlabs.protobufs.services.user.actions.request_access.RequestAccessRequestV1;
import com.rhlabs.protobufs.services.user.actions.request_access.RequestAccessResponseV1;
import com.rhlabs.protobufs.services.user.actions.send_verification_code.SendVerificationCodeRequestV1;
import com.rhlabs.protobufs.services.user.actions.send_verification_code.SendVerificationCodeResponseV1;
import com.rhlabs.protobufs.services.user.actions.update_user.UpdateUserRequestV1;
import com.rhlabs.protobufs.services.user.actions.update_user.UpdateUserResponseV1;
import com.rhlabs.protobufs.services.user.actions.verify_verification_code.VerifyVerificationCodeRequestV1;
import com.rhlabs.protobufs.services.user.actions.verify_verification_code.VerifyVerificationCodeResponseV1;
import com.rhlabs.protobufs.services.user.containers.AccessRequestV1;
import com.rhlabs.protobufs.services.user.containers.DeviceV1;
import com.rhlabs.protobufs.services.user.containers.UserV1;
import com.rhlabs.protobufs.services.user.containers.token.ClientTypeV1;
import com.rhlabs.protobufs.soa.ActionResponseV1;

import java.util.Map;

import retrofit.RetrofitError;

/**
 * Created by anju on 4/26/15.
 */
public class UserService {

    private final static String LOGTAG = UserService.class.getSimpleName();

    public interface AuthenticateUserCallback {
        void success(AuthenticateUserResponseV1 response);

        void failure(RetrofitError error);
    }

    public interface RecordDeviceCallback {
        void success(DeviceV1 device, Map<String, String> errors);

        void failure(RetrofitError error);
    }

    public interface RequestAccessCallback {
        void success(AccessRequestV1 accessRequest, Map<String, String> errors);

        void failure(RetrofitError error);
    }

    public interface SendVerificationCodeCallback {
        void success(SendVerificationCodeResponseV1 response);

        void failure(RetrofitError error);
    }

    public interface UpdateUserCallback {
        void success(UserV1 user, Map<String, String> errors);

        void failure(RetrofitError error);
    }

    public interface VerifyCodeCallback {
        void success(VerifyVerificationCodeResponseV1 response);

        void failure(RetrofitError error);
    }

    public static void authenticateUser(AuthenticateUserRequestV1.AuthBackendV1 authBackendV1, AuthenticateUserRequestV1.CredentialsV1 credentialsV1, final AuthenticateUserCallback cb) {
        AuthenticateUserRequestV1 request = new AuthenticateUserRequestV1.Builder()
                .backend(authBackendV1)
                .credentials(credentialsV1)
                .client_type(ClientTypeV1.ANDROID)
                .build();

        ServiceClient serviceClient = new ServiceClient("user");
        serviceClient.callAction(
                Ext_requests.authenticate_user,
                Ext_responses.authenticate_user,
                request,
                null,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        ActionResponseV1 actionResponseV1 = wrappedResponse.getActionResponse();
                        if (actionResponseV1 != null) {
                            Log.d(LOGTAG, actionResponseV1.toString());
                        }

                        Log.d(LOGTAG, wrappedResponse.toString());
                        cb.success((AuthenticateUserResponseV1) wrappedResponse.getResponse());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                }
        );
    }

    public static void updateUser(UserV1 user, final UpdateUserCallback cb) {
        UpdateUserRequestV1 request = new UpdateUserRequestV1.Builder()
                .user(user)
                .build();

        ServiceClient serviceClient = new ServiceClient("user");
        serviceClient.callAction(
                Ext_requests.update_user,
                Ext_responses.update_user,
                request,
                new Callback<WrappedResponse>() {
                    @Override
                    public void success(WrappedResponse wrappedResponse) {
                        UpdateUserResponseV1 responseV1 = (UpdateUserResponseV1) wrappedResponse.getResponse();
                        UserV1 updatedUser = responseV1 != null ? responseV1.user : null;
                        cb.success(updatedUser, wrappedResponse.getErrors());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error);
                    }
                }
        );
    }

    public static void sendVerificationCode(UserV1 user, final SendVerificationCodeCallback cb) {
        SendVerificationCodeRequestV1 request = new SendVerificationCodeRequestV1.Builder()
                .user_id(user.id)
                .build();

        ServiceClient serviceClient = new ServiceClient("user");
        serviceClient.callAction(Ext_requests.send_verification_code, Ext_responses.send_verification_code, request, new Callback<WrappedResponse>() {
            @Override
            public void success(WrappedResponse wrappedResponse) {
                SendVerificationCodeResponseV1 response = (SendVerificationCodeResponseV1)wrappedResponse.getResponse();
                cb.success(response);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

    public static void verifyVerificationCode(UserV1 user, String code, final VerifyCodeCallback cb) {
        VerifyVerificationCodeRequestV1 request = new VerifyVerificationCodeRequestV1.Builder()
                .user_id(user.id)
                .code(code)
                .build();

        ServiceClient serviceClient = new ServiceClient("user");
        serviceClient.callAction(Ext_requests.verify_verification_code, Ext_responses.verify_verification_code, request, new Callback<WrappedResponse>() {
            @Override
            public void success(WrappedResponse wrappedResponse) {
                VerifyVerificationCodeResponseV1 response = (VerifyVerificationCodeResponseV1) wrappedResponse.getResponse();
                cb.success(response);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

    public static void requestAccess(UserV1 user, final RequestAccessCallback cb) {
        RequestAccessRequestV1 request = new RequestAccessRequestV1.Builder()
                .user_id(user.id)
                .build();

        ServiceClient serviceClient = new ServiceClient("user");
        serviceClient.callAction(Ext_requests.request_access, Ext_responses.request_access, request, new Callback<WrappedResponse>() {
            @Override
            public void success(WrappedResponse wrappedResponse) {
                RequestAccessResponseV1 response = (RequestAccessResponseV1) wrappedResponse.getResponse();
                cb.success(response.access_request, wrappedResponse.getErrors());
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

    public static void recordDevice(DeviceV1 device, final RecordDeviceCallback cb) {
        RecordDeviceRequestV1 request = new RecordDeviceRequestV1.Builder()
                .device(device)
                .build();

        ServiceClient serviceClient = new ServiceClient("user");
        serviceClient.callAction(Ext_requests.record_device, Ext_responses.record_device, request, new Callback<WrappedResponse>() {
            @Override
            public void success(WrappedResponse wrappedResponse) {
                RecordDeviceResponseV1 response = (RecordDeviceResponseV1) wrappedResponse.getResponse();
                cb.success(response.device, wrappedResponse.getErrors());
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }
}
